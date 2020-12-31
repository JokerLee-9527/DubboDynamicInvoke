/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.joker.dubbo.dynamic.invoke;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistry;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.curator.CuratorZookeeperTransporter;
import com.joker.dubbo.dynamic.invoke.exception.DynamicInvokeException;
import com.joker.dubbo.dynamic.invoke.model.ServiceModel;
import com.joker.dubbo.dynamic.invoke.model.UrlModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description: 从zk中获取provider等方法
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/30 16:10
* @version V1.0
*/
public class DubboZk {

    private final String protocol;
    private final String host;
    private final int port;
    private ZookeeperClient zkClient;
    private ZookeeperRegistry registry;
    private String root = "/dubbo";

    /**
     * 构造函数
     *
     * @param protocol
     * @param host
     * @param port
     */
    public DubboZk(String protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;

//        // todo
//        BasicConfigurator.configure();
    }

    /**
     * 创建zkClient
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void open() throws NoSuchFieldException, IllegalAccessException {

        CuratorZookeeperTransporter zookeeperTransporter = new CuratorZookeeperTransporter();
        URL url = new URL(protocol, host, port);

        registry = new ZookeeperRegistry(url, zookeeperTransporter);

        Field field = registry.getClass().getDeclaredField("zkClient");
        field.setAccessible(true);
        zkClient = (ZookeeperClient) field.get(registry);

    }

    /**
     * 获得所有接口
     *
     * @return
     */
    public List<ServiceModel> getAllInterfaces() {
        List<ServiceModel> ret = new ArrayList<>();
        List<String> list = zkClient.getChildren(root);
        for (int i = 0; i < list.size(); i++) {
            ServiceModel model = new ServiceModel();
            model.setServiceName(list.get(i));
            ret.add(model);
        }
        return ret;
    }

    /**
     * 从ZK获取interface
     *
     * @param interfaceName
     * @param version
     * @param group
     * @return
     */
    public List<UrlModel> getProviders(String interfaceName,String version, String group) {


        if (StringUtils.isEmpty(interfaceName)) {
            throw new DynamicInvokeException("interfaceName name can't be null.");
        }

        Map<String, String> map = new HashMap<>();
        map.put(Constants.INTERFACE_KEY, interfaceName);

        if (StringUtils.isNotEmpty(version)) {
            map.put(Constants.VERSION_KEY, version);
        }
        if (StringUtils.isNotEmpty(group)) {
            map.put(Constants.GROUP_KEY, group);
        }

        URL url = new URL(protocol, host, port, map);
        List<URL> list = registry.lookup(url);

        return UrlModel.converter2UrlModelList(interfaceName, list);
    }

//    /**
//     * 获得接口的方法
//     *
//     * @param interfaceName
//     * @return
//     * @throws ClassNotFoundException
//     */
//    public List<MethodModelParam> getMethods(String interfaceName) throws ClassNotFoundException {
//
//        Class<?> clazz = Class.forName(interfaceName);
//        Method[] methods = clazz.getMethods();
//
//        return converter2MethodModelDTOList(interfaceName, methods);
//
//    }

    /**
     * 断开
     */
    public void close() {
        registry.destroy();
    }

    /**
     * 判断是否可用
     *
     * @return
     */
    public boolean isAvailable() {
        return registry.isAvailable();
    }
}
