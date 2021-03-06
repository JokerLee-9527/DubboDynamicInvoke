/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.joker.dubbo.dynamic.invoke.model;

import lombok.Data;

/**
* @Description: 请在此处输入方法描述信息
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/30 16:10
* @version V1.0
*/
@Data
public class ConnectParam {

    /**
     * ip and port.
     */
    private String conn;
    /**
     * interface name;
     */
    private String serviceName;
    /**
     * the provider cache key.
     */
    private String providerKey;
    /**
     * method key.
     */
    private String methodKey;
    /**
     * method name.
     */
    private String methodName;
    /**
     * method params.
     */
    private String json;
    /**
     * timeout of waiting for result.
     */
    private int timeout;
    /**
     * interface version number, eg: 1.0.0
     */
    private String version;
    /**
     * the group of interface, eg: mmcgroup
     */
    private String group;
}
