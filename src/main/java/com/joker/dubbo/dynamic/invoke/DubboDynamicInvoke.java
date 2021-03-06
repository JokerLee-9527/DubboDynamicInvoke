package com.joker.dubbo.dynamic.invoke;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.joker.dubbo.dynamic.invoke.client.DubboNettyClient;
import com.joker.dubbo.dynamic.invoke.client.channel.ResponseDispatcher;
import com.joker.dubbo.dynamic.invoke.exception.DynamicInvokeException;
import com.joker.dubbo.dynamic.invoke.model.DubboDynamicInvokeParam;
import com.joker.dubbo.dynamic.invoke.model.DubboDynamicInvokeWithDownloadJarParam;
import com.joker.dubbo.dynamic.invoke.model.UrlModel;
import com.joker.dubbo.dynamic.invoke.pom.PomUtil;
import com.joker.dubbo.dynamic.invoke.util.GsonUtils;
import com.joker.dubbo.dynamic.invoke.util.ParamUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author JokerLee
 * @version V1.0
 * @Description: 动态执行dubbo接口
 * @date 2020/12/25 15:00
 */
@Slf4j
public class DubboDynamicInvoke {


    /**
     * 1. 构建pom.xml
     * 2. 通过mvn下载jar
     * 3. 自定义classloader中加载class
     * 4. 序列化对象
     * 5. netty tcp实现远程rpc调用
     *
     * @param dubboDynamicInvokeWithDownloadJarParam
     * @return
     */
    public static String runWithDownLoadJar(DubboDynamicInvokeWithDownloadJarParam dubboDynamicInvokeWithDownloadJarParam) {

        // 1. 构建pom.xml
        // 2. 通过mvn下载jar
        PomUtil.startDownloadJar(dubboDynamicInvokeWithDownloadJarParam.getStartDownloadJarParam());

        // 3. 自定义classloader中加载class
        // 4. 序列化对象
        // 5. netty tcp实现远程rpc调用
        return run(dubboDynamicInvokeWithDownloadJarParam);
    }

    /**
     * 动态执行dubbo接口
     *
     * @param dubboDynamicInvokeParam
     * @return
     */
    @SneakyThrows
    public static String run(DubboDynamicInvokeParam dubboDynamicInvokeParam) {

//        // todo
//        BasicConfigurator.configure();

        dubboDynamicInvokeParam.checkParam();

        final String jarPath = dubboDynamicInvokeParam.getJarPath();
        final String interfaceName = dubboDynamicInvokeParam.getInterfaceName();
        final String methodName = dubboDynamicInvokeParam.getMethodName();
        final String jsonParam = dubboDynamicInvokeParam.getJsonParam();

        final String dubboProtocol = dubboDynamicInvokeParam.getDubboProtocol();
        final String dubboVersion = dubboDynamicInvokeParam.getDubboVersion();
        final String zkHost = dubboDynamicInvokeParam.getZkHost();
        final Integer zkPort = dubboDynamicInvokeParam.getZkPort();


        // 动态加载jar,生成方法入参的 Object[]
        Object[] params;
        Method runMethod;
        DynamicLoadJar dynamicLoadJar = null;
        try {
            dynamicLoadJar = new DynamicLoadJar(jarPath);
            runMethod = dynamicLoadJar.getMethod(interfaceName, methodName);
            if (runMethod == null) {
                throw new DynamicInvokeException("Jar中未找到相应的Method");
            }
            params = ParamUtil.checkAndPrepareJsonParam(jsonParam, runMethod);
            log.info("DubboDynamicInvoke run params:"+GsonUtils.toJsonString(params) );
        } finally {
            if (dynamicLoadJar != null) {
                dynamicLoadJar._DynamicLoadJar();
            }
        }

        // 从zk中获取providers
        DubboZk dubboZk = new DubboZk(dubboProtocol, zkHost, zkPort);
        List<UrlModel> providerUrls;
        try {
            dubboZk.open();
            providerUrls = dubboZk.getProviders(interfaceName,dubboVersion,null);
        } finally {
//            dubboZk.close();
        }


        for (UrlModel providerUrl : providerUrls) {
            log.info("DubboDynamicInvoke providerUrl:{}", GsonUtils.toJsonString(providerUrl));
        }

        URL url = providerUrls.get(0).getUrl();
        url = url.addParameter(Constants.CODEC_KEY, dubboProtocol); // 非常重要，必须要设置编码器协议类型
        HashMap<String, String> map = ParamUtil.getAttachmentFromUrl(url);

        DubboNettyClient client = new DubboNettyClient(url);
        client.doConnect();

        // create request.
        Request req = new Request();
        req.setVersion(dubboProtocol);
        req.setTwoWay(true);
        req.setData(new RpcInvocation(runMethod, params, map));

        client.send(req);

        CompletableFuture<RpcResult> future = ResponseDispatcher.getDispatcher().getFuture(req);
        RpcResult result = future.get(10, TimeUnit.SECONDS);
        return result.toString();
    }
}
