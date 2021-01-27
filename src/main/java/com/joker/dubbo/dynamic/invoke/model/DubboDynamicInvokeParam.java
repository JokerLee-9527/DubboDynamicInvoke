package com.joker.dubbo.dynamic.invoke.model;

import com.joker.dubbo.dynamic.invoke.util.ParamCheckerUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Description: 动态执行dubbo的入参
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/25 14:56
* @version V1.0
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DubboDynamicInvokeParam {
    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数的json字符串
     */
    private String jsonParam;

    /**
     * jar包的路径
     */
    private String jarPath = ".\\lib_tem\\";

    // protocol, host, port
    /**
     * dubbo协议--dubbo,rest,http,hessian...
     */
    private String dubboProtocol = "dubbo";

    /**
     * dubbo版本
     */
    private String dubboVersion;

    /**
     * zk的host
     */
    private String zkHost = "127.0.0.1";

    /**
     * zk的port
     */
    private Integer zkPort = 2181;


    public void checkParam() {

        ParamCheckerUtil.notBlank(this.interfaceName, "接口名称不能为空");
        ParamCheckerUtil.notBlank(this.methodName, "方法名称不能为空");
        ParamCheckerUtil.notBlank(this.jarPath, "jar包的路径不能为空");

        ParamCheckerUtil.notBlank(this.dubboProtocol, "dubboProtocol不能为空");
        ParamCheckerUtil.notBlank(this.zkHost, "zkHost不能为空");
        ParamCheckerUtil.nonNull(this.zkPort, "zkPort不能为空");
    }

}
