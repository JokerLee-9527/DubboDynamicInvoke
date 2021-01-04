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


import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.joker.dubbo.dynamic.invoke.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

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
public class DubboTelnetParam {

    /**
     * 通过telnet调用dubbo的命令类型
     */
    private DubboTelnetCommandTypeEnum commandType;

    /**
     * ip and port.
     */
    private String conn;
    /**
     * interface name;
     */
    private String serviceName;
    /**
     * method name.
     */
    private String methodName;
    /**
     * method params.
     */
    private List<String> params;
    /**
     * timeout of waiting for result.
     */
    private int timeout;

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public DubboTelnetCommandTypeEnum getCommandType() {
        return commandType;
    }

    public void setCommandType(DubboTelnetCommandTypeEnum commandType) {
        this.commandType = commandType;
    }

    public void addParam(String paramJson) {
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(paramJson);
    }

    private String jointAllParams() {
        StringBuilder allParams = new StringBuilder();

        if (CollectionUtils.isNotEmpty(params)) {
            for (String param : params) {
                allParams.append(param).append(",");
            }
        }
        return allParams.length() == 0 ? allParams.toString() : allParams.subSequence(0, allParams.length() -1).toString();
    }

    public String getCommand() {
        switch (commandType) {
            case INVOKE:
                return StringUtil.format("invoke {}.{}({})", serviceName, methodName, jointAllParams());
            case LS_INTERFACE:
                return StringUtil.format("ls ");
            case LS_METHOD:
                return StringUtil.format("ls {}", serviceName);
            default:
                return "";
        }
    }
}

