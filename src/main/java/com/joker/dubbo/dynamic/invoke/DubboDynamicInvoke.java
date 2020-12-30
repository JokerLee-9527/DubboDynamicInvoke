package com.joker.dubbo.dynamic.invoke;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.joker.dubbo.dynamic.invoke.client.DubboNettyClient;
import com.joker.dubbo.dynamic.invoke.client.channel.ResponseDispatcher;
import com.joker.dubbo.dynamic.invoke.exception.DynamicInvokeException;
import com.joker.dubbo.dynamic.invoke.model.ConnectParam;
import com.joker.dubbo.dynamic.invoke.model.DubboDynamicInvokeParam;
import com.joker.dubbo.dynamic.invoke.model.UrlModel;
import com.joker.dubbo.dynamic.invoke.util.GsonUtils;
import com.joker.dubbo.dynamic.invoke.util.ParamUtil;
import com.joker.dubbo.dynamic.invoke.zk.CuratorHandler;
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
     * 动态执行dubbo接口
     *
     * @param dubboDynamicInvokeParam
     * @return
     */
    @SneakyThrows
    public static String run(DubboDynamicInvokeParam dubboDynamicInvokeParam) {
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
        Method runMethod = null;
        DynamicLoadJar dynamicLoadJar = null;
        try {
            dynamicLoadJar = new DynamicLoadJar(jarPath);
            runMethod = dynamicLoadJar.getMethod(interfaceName, methodName);
        } finally {
//            if (dynamicLoadJar != null) {
//                dynamicLoadJar._DynamicLoadJar();
//            }
        }

        if (runMethod == null) {
            throw new DynamicInvokeException("Jar中未找到相应的Method");
        }
        Object[] params = ParamUtil.checkAndPrepareJsonParam(jsonParam, runMethod);
        log.info("DubboDynamicInvoke run params:"+GsonUtils.toJsonString(params) );


        // 从zk中获取providers
        CuratorHandler curatorHandler = new CuratorHandler(dubboProtocol, zkHost, zkPort);
        List<UrlModel> providerUrls;
        try {
            curatorHandler.doConnect();
            ConnectParam conn = new ConnectParam();
            conn.setServiceName(interfaceName);
            conn.setVersion(dubboVersion);
            providerUrls = curatorHandler.getProviders(conn);
        } finally {
            curatorHandler.close();
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
