package test.com.joker.dubbo.dynamic.invoke;

import com.joker.dubbo.dynamic.invoke.DubboDynamicInvoke;
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
