/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.joker.dubbo.dynamic.invoke.exception;

/**
* @Description: 动态执行dubbo异常
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/25 11:37
* @version V1.0
*/
public class DynamicInvokeException extends RuntimeException {

    public DynamicInvokeException(String message) {
        super(message);
    }
}
