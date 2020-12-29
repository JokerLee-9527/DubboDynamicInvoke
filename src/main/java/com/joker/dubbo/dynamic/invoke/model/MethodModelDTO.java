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
 * @author Joey
 * @date 2018/6/18 21:49
 */
@Data
public class MethodModelDTO {

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

    public MethodModelDTO() {

    }

    public MethodModelDTO(MethodModel model) {

        this.methodKey = model.getKey();
        this.methodName = model.getMethod().getName();
        this.methodText = model.getMethodText();
    }

    public static MethodModelDTO converter2MethodModelDTO(final String interfaceName, final Method method) {
        String key = generateMethodKey(method, interfaceName);
        return new MethodModelDTO(new MethodModel(key, method));
    }

    public static List<MethodModelDTO> converter2MethodModelDTOList(final String interfaceName, Method[] methods) {
        return Arrays.stream(methods).map(method-> MethodModelDTO.converter2MethodModelDTO(interfaceName, method)).collect(Collectors.toList());
    }

    private static String generateMethodKey(Method method, String interfaceName) {
        return String.format("%s#%s", interfaceName, MD5Util.encrypt(method.toGenericString()));
    }

}
