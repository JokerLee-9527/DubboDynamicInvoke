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


import com.joker.dubbo.dynamic.invoke.util.MD5Util;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
* @Description: 请在此处输入方法描述信息
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/30 16:11
* @version V1.0
*/
@Data
public class MethodModelParam {

    /**
     * the name of interface which the method belong to.
     */
    private String interfaceName;
    /**
     * the cache key.
     */
    private String methodKey;
    /**
     * just only the method name.
     */
    private String methodName;
    /**
     * show on the web.
     */
    private String methodText;

//    public MethodModelParam() {
//
//    }

    public MethodModelParam(MethodModel model) {

        this.methodKey = model.getKey();
        this.methodName = model.getMethod().getName();
        this.methodText = model.getMethodText();
    }

    public static MethodModelParam converter2MethodModelDTO(final String interfaceName, final Method method) {
        String key = generateMethodKey(method, interfaceName);
        return new MethodModelParam(new MethodModel(key, method));
    }

    public static List<MethodModelParam> converter2MethodModelDTOList(final String interfaceName, Method[] methods) {
        return Arrays.stream(methods).map(method-> MethodModelParam.converter2MethodModelDTO(interfaceName, method)).collect(Collectors.toList());
    }

    private static String generateMethodKey(Method method, String interfaceName) {
        return String.format("%s#%s", interfaceName, MD5Util.encrypt(method.toGenericString()));
    }

}
