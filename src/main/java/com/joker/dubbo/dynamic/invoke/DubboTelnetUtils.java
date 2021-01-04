package com.joker.dubbo.dynamic.invoke;

import com.google.gson.Gson;
import com.joker.dubbo.dynamic.invoke.model.DubboTelnetParam;
import com.joker.dubbo.dynamic.invoke.model.PointModel;
import com.joker.dubbo.dynamic.invoke.util.ParamUtil;
import com.joker.dubbo.dynamic.invoke.util.StringUtil;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: Telnet命令执行dubbo
 * http://dubbo.apache.org/zh/docs/v2.7/user/references/telnet/
 * @Author: Joker
 * @CreateDate: 2018/12/26 18:58
 * @UpdateUser: Joker
 * @UpdateDate: 2018/12/26 18:58
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class DubboTelnetUtils {


    public static String executeCommand(DubboTelnetParam dto) {

        PointModel model = ParamUtil.parsePointModel(dto.getConn());

        TelnetClient telnetClient = null;
        try {
            // 指明Telnet终端类型，否则会返回来的数据中文会乱码
            telnetClient = new TelnetClient("VT220");
            TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler(
                    "VT220", false, false, true, false);
            telnetClient.setDefaultTimeout(dto.getTimeout() <= 0 ? 5000 : dto.getTimeout());
            // 建立一个连接,默认端口是23
            telnetClient.connect(model.getIp(), model.getPort());
            // 读取命令的流
            InputStream in = telnetClient.getInputStream();
            // 写命令的流
//            PrintStream out = new PrintStream(telnetClient.getOutputStream());
            PrintStream out = new PrintStream(telnetClient.getOutputStream(), false, "UTF-8");


            String command = dto.getCommand();

            out.println("\r\n");
            out.println(command);
            out.println("\r\n");
            out.flush();

            // handle inputStream
            StringBuilder sb = new StringBuilder();
            BufferedInputStream bi = new BufferedInputStream(in);

            while (true) {
                byte[] buffer = new byte[1024];
                int len = bi.read(buffer);
                if (len <= -1) {
                    break;
                }

                String msg = new String(buffer, 0, len, "UTF-8");
                sb.append(msg);
                if (msg.endsWith("dubbo>")) {
                    break;
                }
            }

            // 写命令
            out.println("exit");
            // 将命令发送到telnet Server
            out.flush();

            String ret = sb.toString();
            return ret.replace(StringUtil.getSystemLineSeparator() + "dubbo>","");
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (null != telnetClient) {
                    telnetClient.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> transformParam(String inputText) {

        List<String> res = new ArrayList<>();

        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(inputText, Map.class);


//        JSONArray jsonArray = JSON.parseObject(inputText, JSONArray.class);
//        TreeMap<String,String> treeMap = new TreeMap<>();
//        for (Object o : jsonArray) {
//            JSONObject jsonObject = (JSONObject) o;
//            for (Map.Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
//                treeMap.put(stringObjectEntry.getKey(),stringObjectEntry.getValue().toString());
//            }
//        }
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            res.add(gson.toJson( stringStringEntry.getValue()));
        }
        return res;
    }

//    public static void main(String[] args) {
//                String str = "[{\"param1\":{\"uuid\":null,\"cid\":\"C00000001012649\",\"clientIp\":\"172.19.64.57\",\"clientType\":\"CONN_VIEW\",\"partyAStore\":{\"storeId\":\"A859316\",\"storeType\":\"cloudShop\"},\"employee\":{\"employeeId\":\"172.19.64.57\",\"employeeName\":\"CONN_VIEW\",\"employeeUserId\":\"172.19.64.57\"},\"operationType\":\"PARTY_A_QUERY_BY_CID\",\"lockKeySuffix\":\"\"}},{\"param2\":{\"uuid\":null,\"cid\":\"C00000001012649\",\"clientIp\":\"172.19.64.57\",\"clientType\":\"CONN_VIEW\",\"partyAStore\":{\"storeId\":\"A859316\",\"storeType\":\"cloudShop\"},\"employee\":{\"employeeId\":\"172.19.64.57\",\"employeeName\":\"CONN_VIEW\",\"employeeUserId\":\"172.19.64.57\"},\"operationType\":\"PARTY_A_QUERY_BY_CID\",\"lockKeySuffix\":\"\"}}]";
//        List<String> strings = transformParam(str);
//        System.out.println(strings.size());
//
//
//
//    }


//    public static void main(String[] args) {
//        DubboTelnetParam dubboTelnetParam = new DubboTelnetParam();
//
////        dubboTelnetParam.setServiceName("com.qianmi.uc.connection.api.b2c.retail.partya.RetailConnectionPartyAQueryProvider");
////        dubboTelnetParam.setTimeout(1000);
////        dubboTelnetParam.addParam("{\"uuid\":null,\"cid\":\"U5rclojbb6v1og6\",\"clientIp\":\"172.19.64.57\",\"clientType\":\"CONN_VIEW\",\"partyAStore\":{\"storeId\":\"A899782\",\"storeType\":\"cloudShop\"},\"employee\":{\"employeeId\":\"172.19.64.57\",\"employeeName\":\"CONN_VIEW\",\"employeeUserId\":\"172.19.64.57\"}}");
////        dubboTelnetParam.setMethodName("queryDetailWithCardInfoByCid");
////        dubboTelnetParam.setConn("172.19.66.136:20880");
//
//        dubboTelnetParam.setServiceName("com.joker.plugin.dubbo.api.DemoServiceProvider");
//        dubboTelnetParam.setTimeout(1000);
//        dubboTelnetParam.addParam("{\"birthday\":\"\",\"nickName\":\"码 昵称(微信昵称、手机号码)\",\"logo\":\"头像\",\"class\":\"com.joker.plugin.dubbo.api.demain.UserProfile\"}");
//        dubboTelnetParam.addParam("\"111\"");
//        dubboTelnetParam.setMethodName("sayHello");
//        dubboTelnetParam.setConn("127.0.0.1:20880");
//        System.out.println(dubboTelnetParam.getParams());
//
////        dubboTelnetParam.setCommandType(DubboTelnetCommandTypeEnum.LS_INTERFACE);
////        String strLsInterface = executeCommand(dubboTelnetParam);
////        System.out.println(strLsInterface);
//
////        dubboTelnetParam.setCommandType(DubboTelnetCommandTypeEnum.LS_METHOD);
////        String strLsInterface = executeCommand(dubboTelnetParam);
////        System.out.println(strLsInterface);
//
//
//        dubboTelnetParam.setCommandType(DubboTelnetCommandTypeEnum.INVOKE);
//        String strInvoke = executeCommand(dubboTelnetParam);
//        System.out.println((strInvoke));
//
//
//    }

}

