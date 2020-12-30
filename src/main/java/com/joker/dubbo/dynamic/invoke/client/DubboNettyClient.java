/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.joker.dubbo.dynamic.invoke.client;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.remoting.Codec2;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.buffer.DynamicChannelBuffer;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.transport.netty.NettyHandler;
import com.joker.dubbo.dynamic.invoke.client.channel.NettyChannel;
import com.joker.dubbo.dynamic.invoke.client.channel.SendReceiveHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author JokerLee
 * https://github.com/JokerLee-9527
 * @version V1.0
 * @Description: 请在此处输入方法描述信息
 * @date 2020/12/28 15:49
 */
public class DubboNettyClient {


    // 设置reactor 线程
    // 设置nio类型的channel
    protected static final ChannelFactory channelFactory = new NioClientSocketChannelFactory(
            Executors.newCachedThreadPool(new NamedThreadFactory("NettyClientBoss", true)),
            Executors.newCachedThreadPool(new NamedThreadFactory("NettyClientWorker", true)),
            Constants.DEFAULT_IO_THREADS);

    protected ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);

    protected int bufferSize = Constants.DEFAULT_BUFFER_SIZE;
    protected int timeout = Constants.DEFAULT_TIMEOUT;
    protected final Codec2 codec;
    protected final URL url;
    protected volatile Channel channel; // volatile, please copy reference to use
    protected com.alibaba.dubbo.remoting.ChannelHandler handler;

    // {@link com.alibaba.dubbo.remoting.transport.netty.NettyClient.doOpen}
    public DubboNettyClient(URL url) {

        this.url = url;
        this.handler = new SendReceiveHandler();
        this.codec = getChannelCodec(url);

        //设置通道选项
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("connectTimeoutMillis", timeout);

        // 装配流水线
        final NettyHandler nettyHandler = new NettyHandler(url, handler);
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline = Channels.pipeline();
            // 在创建DefaultChannelHandlerContext是判断是 Upstream 还是 Downstream
            pipeline.addLast("decoder", getDecoder());
            pipeline.addLast("encoder", getEncoder());

            // public class NettyHandler extends SimpleChannelHandler
            // public class SimpleChannelHandler implements ChannelUpstreamHandler, ChannelDownstreamHandler
            // UpstreamHandler是从前往后执行的，DownstreamHandler是从后往前执行的   ???
            /**
             * {@link NettyHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)}
             *  Invoked when a {@link Channel} is open, bound to a local address, and
             *  connected to a remote address.
             *
             *
             */
            pipeline.addLast("handler", nettyHandler);
            return pipeline;
        });
    }

    // {@link com.alibaba.dubbo.remoting.transport.netty.NettyClient.doConnect}
    public void doConnect() {
        // 设置监听端口
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        boolean ret = future.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
        if (ret && future.isSuccess()) {
            Channel newChannel = future.getChannel();
            newChannel.setInterestOps(Channel.OP_READ_WRITE);
            channel = future.getChannel();
        } else {
            throw new RuntimeException("can't not connect to server.");
        }
    }

    public void send(Request req) throws RemotingException {

        NettyChannel ch = NettyChannel.getOrAddChannel(this.channel, url, handler);

        ch.send(req);

    }


    protected static Codec2 getChannelCodec(URL url) {
        String codecName = url.getParameter(Constants.CODEC_KEY, "telnet");
        return ExtensionLoader.getExtensionLoader(Codec2.class).getExtension(codecName);
    }

    protected SocketAddress getConnectAddress() {
        return new InetSocketAddress(NetUtils.filterLocalHost(url.getHost()), url.getPort());
    }

    // {@link com.alibaba.dubbo.remoting.transport.netty.NettyCodecAdapter.getEncoder }
    private ChannelHandler getEncoder() {
        return new InternalEncoder();
    }

    // {@link com.alibaba.dubbo.remoting.transport.netty.NettyCodecAdapter.getDecoder }
    private ChannelHandler getDecoder() {
        return new InternalDecoder();
    }

    // {@link com.alibaba.dubbo.remoting.transport.netty.NettyCodecAdapter.InternalEncoder}
    private class InternalEncoder extends OneToOneEncoder {

        @Override
        protected Object encode(ChannelHandlerContext ctx, Channel ch, Object msg) throws Exception {
            com.alibaba.dubbo.remoting.buffer.ChannelBuffer buffer =
                    com.alibaba.dubbo.remoting.buffer.ChannelBuffers.dynamicBuffer(1024);
            NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
            try {
                codec.encode(channel, buffer, msg);
            } finally {
                NettyChannel.removeChannelIfDisconnected(ch);
            }
            return ChannelBuffers.wrappedBuffer(buffer.toByteBuffer());
        }
    }

    // {@link com.alibaba.dubbo.remoting.transport.netty.NettyCodecAdapter.InternalDecoder}
    private class InternalDecoder extends SimpleChannelUpstreamHandler {

        private com.alibaba.dubbo.remoting.buffer.ChannelBuffer buffer =
                com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
            Object o = event.getMessage();
            if (!(o instanceof ChannelBuffer)) {
                ctx.sendUpstream(event);
                return;
            }

            ChannelBuffer input = (ChannelBuffer) o;
            int readable = input.readableBytes();
            if (readable <= 0) {
                return;
            }

            com.alibaba.dubbo.remoting.buffer.ChannelBuffer message;
            if (buffer.readable()) {
                if (buffer instanceof DynamicChannelBuffer) {
                    buffer.writeBytes(input.toByteBuffer());
                    message = buffer;
                } else {
                    int size = buffer.readableBytes() + input.readableBytes();
                    message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.dynamicBuffer(
                            size > bufferSize ? size : bufferSize);
                    message.writeBytes(buffer, buffer.readableBytes());
                    message.writeBytes(input.toByteBuffer());
                }
            } else {
                message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.wrappedBuffer(
                        input.toByteBuffer());
            }

            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.getChannel(), url, handler);
            Object msg;
            int saveReaderIndex;

            try {
                // decode object.
                do {
                    saveReaderIndex = message.readerIndex();
                    try {
                        msg = codec.decode(channel, message);
                    } catch (IOException e) {
                        buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                        throw e;
                    }
                    if (msg == Codec2.DecodeResult.NEED_MORE_INPUT) {
                        message.readerIndex(saveReaderIndex);
                        break;
                    } else {
                        if (saveReaderIndex == message.readerIndex()) {
                            buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                            throw new IOException("Decode without read data.");
                        }
                        if (msg != null) {
                            Channels.fireMessageReceived(ctx, msg, event.getRemoteAddress());
                        }
                    }
                } while (message.readable());
            } finally {
                if (message.readable()) {
                    message.discardReadBytes();
                    buffer = message;
                } else {
                    buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                }
                NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            ctx.sendUpstream(e);
        }
    }
}
