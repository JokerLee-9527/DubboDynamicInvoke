package com.joker.dubbo.dynamic.invoke;

import com.joker.dubbo.dynamic.invoke.classload.DynamicInvokeClassLoader;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JokerLee
 * @version V1.0
 * @Description: 动态加载Jar, 并获取class和method
 * @date 2020/12/30 10:55
 */
public class DynamicLoadJar {

    private DynamicInvokeClassLoader dynamicInvokeClassLoader;

    /**
     * 构造函数
     *
     * @param jarPath Jar包的目录
     */
    public DynamicLoadJar(String jarPath) {
        dynamicInvokeClassLoader = new DynamicInvokeClassLoader(jarPath);
    }

    /**
     * 获取所有的class
     *
     * @return
     */
    @SneakyThrows
    public Map<String, Class<?>> getAllClass() {
        dynamicInvokeClassLoader.loadJars();
        return dynamicInvokeClassLoader.loadAllClass();
    }


    /**
     * 根据className获取所有的Method
     *
     * @param className
     * @return
     */
    @SneakyThrows
    public Map<String, Method> getAllMethod(String className) {
        dynamicInvokeClassLoader.loadJars();
        Class<?> clazz = dynamicInvokeClassLoader.loadClass(className);
        Method[] methods = clazz.getMethods();
        return Arrays.stream(methods).collect(Collectors.toMap(Method::getName, u -> u));
    }

    /**
     * 根据className和methodName获取Method
     *
     * @param className
     * @param methodName
     * @return
     */
    public Method getMethod(String className, String methodName) {
        final Map<String, Method> allMethod = getAllMethod(className);
        return allMethod.get(methodName);
    }


    /**
     * 析构函数
     */
    public void _DynamicLoadJar() {
        dynamicInvokeClassLoader.clearCache();
    }


}
