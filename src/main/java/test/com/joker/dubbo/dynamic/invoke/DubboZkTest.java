package test.com.joker.dubbo.dynamic.invoke;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.joker.dubbo.dynamic.invoke.DubboZk;
import com.joker.dubbo.dynamic.invoke.model.ServiceModel;
import com.joker.dubbo.dynamic.invoke.model.UrlModel;
import com.joker.dubbo.dynamic.invoke.util.GsonUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author JokerLee
 * https://github.com/JokerLee-9527
 * @version V1.0
 * @Description: DubboZkTest
 * @date 2020/12/30 16:31
 */
public class DubboZkTest {

    private DubboZk dubboZk;

    @Before
    public void before() throws Exception {
        dubboZk = new DubboZk("dubbo", "172.19.28.232", 2181);
        dubboZk.open();
    }

    @After
    public void after() throws Exception {
        if (dubboZk != null) {
            dubboZk.close();
        }
    }


    /**
     * Method: getAllInterfaces()
     */
    @Test
    public void testGetAllInterfaces() throws Exception {
        final List<ServiceModel> allInterfaces = dubboZk.getAllInterfaces();
        allInterfaces.stream().forEach(u -> {
            System.out.println(u.getServiceName());
        });
    }

    /**
     * Method: getProviders(String interfaceName, String version, String group)
     */
    @Test
    public void testGetProviders() throws Exception {
        final List<UrlModel> providers = dubboZk.getProviders("com.joker.plugin.dubbo.api.DemoServiceProvider", "1.0", null);
        providers.forEach(u -> {
            System.out.println(GsonUtils.toJsonString(u));
        });
        /*
{
  "key": "com.joker.plugin.dubbo.api.DemoServiceProvider#172.19.3.31#20880#",
  "url": {
    "protocol": "dubbo",
    "host": "172.19.3.31",
    "port": 20880,
    "path": "com.joker.plugin.dubbo.api.DemoServiceProvider",
    "parameters": {
      "default.version": "1.0",
      "side": "provider",
      "methods": "sayHello,sayHello1,sayHello2,sayHello3,sayHello4",
      "dubbo": "2.5.6",
      "threads": "500",
      "pid": "16000",
      "interface": "com.joker.plugin.dubbo.api.DemoServiceProvider",
      "threadpool": "fixed",
      "generic": "false",
      "default.retries": "0",
      "application": "dubbo-provider",
      "default.connections": "5",
      "default.timeout": "10000",
      "anyhost": "true",
      "timestamp": "1609312378061"
    }
  }
}
         */
    }

    /**
     * Method: getProviders(String interfaceName, String version, String group)
     */
    @Test
    public void testGetProvidersVersionNull() throws Exception {
        final List<UrlModel> providers = dubboZk.getProviders("com.joker.plugin.dubbo.api.DemoServiceProvider", null, null);
        assert CollectionUtils.isEmpty(providers);

    }

} 
