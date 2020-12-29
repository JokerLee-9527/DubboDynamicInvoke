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

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.joker.dubbo.dynamic.invoke.client.channel.NettyChannel;
import com.joker.dubbo.dynamic.invoke.client.channel.SendReceiveHandler;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;


/**
* @Description: 请在此处输入方法描述信息
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/28 17:52
* @version V1.0
*/
public class DoeClient extends TransportClient {


    public DoeClient(URL url) {
        super(url, new SendReceiveHandler());
    }

    public void doConnect() {
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        boolean ret = future.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
        if (ret && future.isSuccess()) {
            Channel newChannel = future.getChannel();
            newChannel.setInterestOps(Channel.OP_READ_WRITE);
            DoeClient.this.channel = future.getChannel();
        } else {
            throw new RuntimeException("can't not connect to server.");
        }
    }

    public void send(Request req) throws RemotingException {

        NettyChannel ch = NettyChannel.getOrAddChannel(this.channel, url, handler);

        ch.send(req);

    }


}
