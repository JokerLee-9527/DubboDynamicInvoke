/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.joker.dubbo.dynamic.invoke.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @Description: 字符串工具类.
 * @Author: Joker
 * @CreateDate: 2018/12/27 14:48
 * @UpdateUser: Joker
 * @UpdateDate: 2018/12/27 14:48
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class StringUtil {

    /**
     * 获取系统的换行符
     *
     * @return
     */
    public static String getSystemLineSeparator() {
        return System.getProperty("line.separator", "\n");
    }

    /**
     * 把tab替换成4个空格
     *
     * @param param
     * @return
     */
    public static String replactTabWith4Space(String param) {
        return param.replace("\t", "    ");
    }

    /**
     * 简单格式化{}样式的字符串.<br>
     * String str = "aaa{} bbb{} ccc{}";<br>
     * System.out.println(StringUtil.format(str, "1", "2", "3"));
     *
     * @param src   源字符串
     * @param param 跟源字符串{}匹配的个数字符串
     * @return
     */
    public static String format(String src, Object... param) {
        int i = 0;
        int index = 0;
        StringBuffer sb = new StringBuffer(src);
        while (-1 != (index = sb.indexOf("{}"))) {
            sb.replace(index, index + 2, String.valueOf(param[i++]));
        }
        return sb.toString();
    }

    /**
     * 格式化json
     *
     * @param param
     * @return
     */
    public static String prettyFormatJson(String param) {
        return JSON.toJSONString(JSON.parse(param), SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

//    /**
//     * 判断string对象是否JSON格式
//     *
//     * @param jsonInString
//     * @return
//     */
//    public final static boolean isJSONValid(String jsonInString ) {
//        return true;
////        try {
////            final ObjectMapper mapper = new ObjectMapper();
////            mapper.readTree(jsonInString);
////            return true;
////        } catch (IOException e) {
////            return false;
////        }
//    }

//    public static void main(String[] args) {
//
//        System.out.println(StringUtil.isJSONValid("11"));
//        System.out.println(StringUtil.isJSONValid("[{\"param1\":{\"uuid\":null,\"cid\":\"C00000001012649\",\"clientIp\":\"172.19.64.57\",\"clientType\":\"CONN_VIEW\",\"partyAStore\":{\"storeId\":\"A859316\",\"storeType\":\"cloudShop\"},\"employee\":{\"employeeId\":\"172.19.64.57\",\"employeeName\":\"CONN_VIEW\",\"employeeUserId\":\"172.19.64.57\"},\"operationType\":\"PARTY_A_QUERY_BY_CID\",\"lockKeySuffix\":\"\"}},{\"param2\":{\"uuid\":null,\"cid\":\"C00000001012649\",\"clientIp\":\"172.19.64.57\",\"clientType\":\"CONN_VIEW\",\"partyAStore\":{\"storeId\":\"A859316\",\"storeType\":\"cloudShop\"},\"employee\":{\"employeeId\":\"172.19.64.57\",\"employeeName\":\"CONN_VIEW\",\"employeeUserId\":\"172.19.64.57\"},\"operationType\":\"PARTY_A_QUERY_BY_CID\",\"lockKeySuffix\":\"\"}}]"));
//        System.out.println(StringUtil.isJSONValid("[\"param1\":{\"uuid\":null,\"cid\":\"C00000001012649\",\"clientIp\":\"172.19.64.57\",\"clientType\":\"CONN_VIEW\",\"partyAStore\":{\"storeId\":\"A859316\",\"storeType\":\"cloudShop\"},\"employee\":{\"employeeId\":\"172.19.64.57\",\"employeeName\":\"CONN_VIEW\",\"employeeUserId\":\"172.19.64.57\"},\"operationType\":\"PARTY_A_QUERY_BY_CID\",\"lockKeySuffix\":\"\"}},{\"param2\":{\"uuid\":null,\"cid\":\"C00000001012649\",\"clientIp\":\"172.19.64.57\",\"clientType\":\"CONN_VIEW\",\"partyAStore\":{\"storeId\":\"A859316\",\"storeType\":\"cloudShop\"},\"employee\":{\"employeeId\":\"172.19.64.57\",\"employeeName\":\"CONN_VIEW\",\"employeeUserId\":\"172.19.64.57\"},\"operationType\":\"PARTY_A_QUERY_BY_CID\",\"lockKeySuffix\":\"\"}}]"));
////        String str = "aaa{} bbb{} ccc{}";
////        System.out.println(StringUtil.format(str, "1", "2", "3"));
//    }
}
