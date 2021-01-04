package com.joker.dubbo.dynamic.invoke.model;

/**
* @Description:    通过telnet调用dubbo的命令类型
*
* @Author:         Joker
* @CreateDate:     2018/12/27 14:06
* @UpdateUser:     Joker
* @UpdateDate:     2018/12/27 14:06
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public enum DubboTelnetCommandTypeEnum {
    INVOKE("invoke"),
    LS_INTERFACE("lsInterface"),
    LS_METHOD("lsMethod"),
    ;

    private final String type;

    DubboTelnetCommandTypeEnum(String type) {
        this.type = type;
    }
}
