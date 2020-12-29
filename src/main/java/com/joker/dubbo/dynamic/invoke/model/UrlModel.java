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

import com.alibaba.dubbo.common.URL;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joey
 * @date 2018/6/15 17:56
 */
public class UrlModel {

    private final String key;
    private final URL url;

    public UrlModel(String key, URL url) {
        this.key = key;
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public URL getUrl() {
        return url;
    }

    public static UrlModel converter2UrlModel(String interfaceName, URL url) {
        String key = generateUrlKey(interfaceName, url.getHost(), url.getPort());
        return new UrlModel(key, url);
    }

    public static List<UrlModel> converter2UrlModelList(String interfaceName, List<URL> urls) {
        return urls.stream().map(u -> UrlModel.converter2UrlModel(interfaceName, u)).collect(Collectors.toList());
    }

    private static String generateUrlKey(String interfaceName, String host, int port) {
        return String.format("%s#%s#%d#", interfaceName, host, port);
    }
}
