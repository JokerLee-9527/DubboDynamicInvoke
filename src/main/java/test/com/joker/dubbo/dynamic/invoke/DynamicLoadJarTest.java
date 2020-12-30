package test.com.joker.dubbo.dynamic.invoke;

import com.joker.dubbo.dynamic.invoke.DynamicLoadJar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author JokerLee
 * https://github.com/JokerLee-9527
 * @version V1.0
 * @Description: DynamicLoadJar测试
 * @date 2020/12/30 14:02
 */
public class DynamicLoadJarTest {
    private DynamicLoadJar dynamicLoadJar;

    @Before
    public void before() throws Exception {
        dynamicLoadJar = new DynamicLoadJar("D:\\temp\\JokerIntellijPlugin\\test_project\\dubbo-api\\target");
    }

    @After
    public void after() throws Exception {
        dynamicLoadJar._DynamicLoadJar();
    }

    /**
     * Method: getAllClass(String jarPath)
     */
    @Test
    public void testGetAllClass() throws Exception {
        final Map<String, Class<?>> allClassFromJar = dynamicLoadJar.getAllClass();
        allClassFromJar.entrySet().forEach(u-> {
            System.out.println("ClassName:" + u.getKey());
        });
    }

    /**
     * Method: getAllMethod(interfaceName)
     */
    @Test
    public void testGetAllMethod() throws Exception {
        final Map<String, Method> allMethod = dynamicLoadJar.getAllMethod("com.joker.plugin.dubbo.api.DemoServiceProvider");
        allMethod.entrySet().forEach(u -> {
            System.out.println("MethodName:" + u.getKey());
        });
    }

    /**
     * Method: getAllMethod(interfaceName)
     */
    @Test
    public void testGetAllMethodError() throws Exception {
        final Map<String, Method> allMethod = dynamicLoadJar.getAllMethod("com.joker.plugin.dubbo.api.DemoServiceProviderError");
        allMethod.entrySet().forEach(u -> {
            System.out.println("MethodName:" + u.getKey());
        });
    }

    /**
     * Method: getMethod(className,methodName)
     */
    @Test
    public void testGetMethod() throws Exception {
        final Method sayHello = dynamicLoadJar.getMethod("com.joker.plugin.dubbo.api.DemoServiceProvider", "sayHello");
        if (sayHello ==null) {
            System.out.println("Method is null");
        } else {
            System.out.println("Method name :" + sayHello.getName());
        }
    }

    /**
     * Method: getMethod(className,methodName)
     */
    @Test
    public void testGetMethodError() throws Exception {
        final Method sayHello = dynamicLoadJar.getMethod("com.joker.plugin.dubbo.api.DemoServiceProvider", "sayHelloError");
        if (sayHello ==null) {
            System.out.println("Method is null");
        } else {
            System.out.println("Method name :" + sayHello.getName());
        }
    }

    /**
     * Method: getMethod(className,methodName)
     */
    @Test
    public void testGetMethodError2() throws Exception {
        final Method sayHello = dynamicLoadJar.getMethod("com.joker.plugin.dubbo.api.DemoServiceProviderError", "sayHello");
        if (sayHello ==null) {
            System.out.println("Method is null");
        } else {
            System.out.println("Method name :" + sayHello.getName());
        }
    }





}
