package test.com.joker.dubbo.dynamic.invoke;

import com.joker.dubbo.dynamic.invoke.model.DubboTelnetCommandTypeEnum;
import com.joker.dubbo.dynamic.invoke.model.DubboTelnetParam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.joker.dubbo.dynamic.invoke.DubboTelnetUtils.executeCommand;

/**
 * @author JokerLee
 * https://github.com/JokerLee-9527
 * @version V1.0
 * @Description: DubboZkTest
 * @date 2020/12/30 16:31
 */
public class DubboTelnetTest {
    



    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testExecuteCommandInvokeDubbo() {
        DubboTelnetParam dubboTelnetParam = new DubboTelnetParam();

        dubboTelnetParam.setServiceName("com.joker.plugin.dubbo.api.DemoServiceProvider");
        dubboTelnetParam.setTimeout(1000);
        dubboTelnetParam.addParam("{\"birthday\":\"\",\"nickName\":\"码 昵称(微信昵称、手机号码)\",\"logo\":\"头像\",\"class\":\"com.joker.plugin.dubbo.api.demain.UserProfile\"}");
        dubboTelnetParam.addParam("\"111\"");
        dubboTelnetParam.setMethodName("sayHello");
        dubboTelnetParam.setConn("127.0.0.1:20880");
        System.out.println(dubboTelnetParam.getParams());
        dubboTelnetParam.setCommandType(DubboTelnetCommandTypeEnum.INVOKE);
        String strInvoke = executeCommand(dubboTelnetParam);
        System.out.println((strInvoke));
    }

    @Test
    public void testExecuteCommandLsProvider() {
        DubboTelnetParam dubboTelnetParam = new DubboTelnetParam();

        dubboTelnetParam.setTimeout(1000);
        dubboTelnetParam.setConn("127.0.0.1:20880");
        dubboTelnetParam.setCommandType(DubboTelnetCommandTypeEnum.LS_INTERFACE);
        String strLsInterface = executeCommand(dubboTelnetParam);
        System.out.println(strLsInterface);
    }

    @Test
    public void testExecuteCommandLsMethod() {
        DubboTelnetParam dubboTelnetParam = new DubboTelnetParam();

        dubboTelnetParam.setServiceName("com.joker.plugin.dubbo.api.DemoServiceProvider");
        dubboTelnetParam.setTimeout(1000);
        dubboTelnetParam.setConn("127.0.0.1:20880");
        dubboTelnetParam.setCommandType(DubboTelnetCommandTypeEnum.LS_METHOD);
        String strLsInterface = executeCommand(dubboTelnetParam);
        System.out.println(strLsInterface);
    }


} 
