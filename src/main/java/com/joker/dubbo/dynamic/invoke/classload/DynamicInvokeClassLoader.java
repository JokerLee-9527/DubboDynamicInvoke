/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.joker.dubbo.dynamic.invoke.classload;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.joker.dubbo.dynamic.invoke.exception.DynamicInvokeException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
* @Description: 自定义类加载器，做沙箱隔离.
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/25 14:23
* @version V1.0
*/
@Slf4j
public class DynamicInvokeClassLoader extends ClassLoader {


    private final String path;

    private static Map<String, byte[]> classMap = new ConcurrentHashMap<>();


    /**
     * destroy the Parental Entrustment.
     */
    public DynamicInvokeClassLoader(String path) {
        super(null);
        this.path = path;
    }


    private void scanJarFile(File file) throws Exception {

        JarFile jar = new JarFile(file);

        Enumeration<JarEntry> en = jar.entries();
        while (en.hasMoreElements()) {
            JarEntry je = en.nextElement();
            je.getName();
            String name = je.getName();
            if (name.endsWith(".class")) {

                String className = makeClassName(name);

                try (InputStream input = jar.getInputStream(je); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int bytesNumRead;
                    while ((bytesNumRead = input.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesNumRead);
                    }
                    addClass(className, baos.toByteArray());
                }
            }
        }
        jar.close();
    }

    private String makeClassName(String name) {
        String ret = name.replace("\\", ".")
                .replace("/", ".")
                .replace(".class", "");
        return ret;
    }

    /**
     * Add one class dynamically.
     */
    public static boolean addClass(String className, byte[] byteCode) {
        if (!classMap.containsKey(className)) {
            classMap.put(className, byteCode);
            return true;
        }
        return false;
    }

    /**
     * 从指定的目录加载
     */
    public void loadJars() throws Exception {

        if (StringUtils.isEmpty(path)) {
            throw new DynamicInvokeException(String.format("can't found the path %s", path));
        }

        File libPath = new File(path);
        if (!libPath.exists()) {
            throw new DynamicInvokeException(String.format("the path[%s] is not exists.", path));
        }

        File[] files = libPath.listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".zip"));

        if (files != null) {
            for (File file : files) {
                scanJarFile(file);
            }
        }
    }


    /**
     * 获取所有的Class
     *
     * @return
     * @throws ClassNotFoundException
     */
    public Map<String,Class<?>> loadAllClass() throws ClassNotFoundException {
        Map<String,Class<?>> res = new HashMap<>();
        classMap.keySet().forEach(u-> {
            try {
                res.put(u, loadClass(u,false));
            } catch (ClassNotFoundException e) {
                log.error("loadClass ex",e);
            }
        });

        return res;
    }



    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        name = makeClassName(name);

        byte[] stream = get(name);

        if (null != stream) {

            return defineClass(name, stream, 0, stream.length);
        }

        return super.loadClass(name, resolve);

    }

    /**
     * Get class in our classloader rather than system classloader.
     */
    public static Class<?> getClass(String name) throws ClassNotFoundException {
        return new DynamicInvokeClassLoader("").loadClass(name, false);
    }

    private static byte[] get(String className) {
        return classMap.getOrDefault(className, null);
    }

    /**
     * clear the class cache.
     */
    public void clearCache() {
        classMap.clear();
    }
}
