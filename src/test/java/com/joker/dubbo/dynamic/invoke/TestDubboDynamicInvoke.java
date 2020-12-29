package com.joker.dubbo.dynamic.invoke;

import com.joker.dubbo.dynamic.invoke.model.DubboDynamicInvokeParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
* @Description: 测试类
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/25 14:39
* @version V1.0
*/
@Slf4j
public class TestDubboDynamicInvoke {

    @SneakyThrows
    public static void main(String[] args) {

//        // 加载到自定义classload start
//        String interfaceName = "com.joker.plugin.dubbo.api.DemoServiceProvider";
//        String path = "D:\\temp\\JokerIntellijPlugin\\test_project\\dubbo-api\\target";
//        DynamicInvokeClassLoader dynamicInvokeClassLoader = new DynamicInvokeClassLoader(path);
//
//        dynamicInvokeClassLoader.loadJars();
//        dynamicInvokeClassLoader.loadClassFile();
//        Class<?> clazz = dynamicInvokeClassLoader.loadClass(interfaceName);
//        Method[] methods = clazz.getMethods();
//        Method method1 = null;
//        for (Method method : methods) {
//            if ("sayHello".equals(method.getName())) {
//                method1 = method;
//                break;
//            }
//        }
//        Method method1 = methods[0];
//        Object[] params = ParamUtil.checkAndPrepareJsonParam(jsonStr, method1);
        // 加载到自定义classload end


        final DubboDynamicInvokeParam param = DubboDynamicInvokeParam.builder()
                .interfaceName("com.joker.plugin.dubbo.api.DemoServiceProvider")    //接口名称
                .methodName("sayHello")    //方法名称
                .jsonParam("[{\"birthday\":\"\",\"nickName\":\"昵称(微信昵称、手机号码)\",\"logo\":\"头像\"},\"\"]")    //参数的json字符串
                .jarPath("D:\\temp\\JokerIntellijPlugin\\test_project\\dubbo-api\\target")    //jar包的路径
                .dubboProtocol("dubbo")
                .dubboVersion("1.0")
                .zkHost("172.19.28.232")
                .zkPort(2181)
                .build();


        final String run = DubboDynamicInvoke.run(param);
        System.out.println(run);
        log.info("run:" + run);


    }

}
