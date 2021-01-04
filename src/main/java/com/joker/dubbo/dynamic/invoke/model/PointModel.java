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

/**
* @Description:    java类作用描述
*
* @Author:         Joker
* @CreateDate:     2018/12/27 15:08
* @UpdateUser:     Joker
* @UpdateDate:     2018/12/27 15:08
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class PointModel {

    private String ip;
    private int port;

    public PointModel(String host, Integer port) {
        this.ip = host;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
