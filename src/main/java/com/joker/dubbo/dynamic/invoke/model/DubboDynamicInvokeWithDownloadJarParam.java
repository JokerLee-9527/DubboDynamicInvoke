package com.joker.dubbo.dynamic.invoke.model;

import com.joker.dubbo.dynamic.invoke.util.ParamCheckerUtil;
import lombok.Data;

import static com.joker.dubbo.dynamic.invoke.util.FileUtil.getFilePath;

/**
* @Description: 动态执行dubbo的入参
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/25 14:56
* @version V1.0
*/
@Data
public class DubboDynamicInvokeWithDownloadJarParam extends DubboDynamicInvokeParam {

    public DubboDynamicInvokeWithDownloadJarParam(StartDownloadJarParam startDownloadJarParam) {
        this.startDownloadJarParam = startDownloadJarParam;
        super.setJarPath(getFilePath(startDownloadJarParam.getPomXmlFileName()));
    }

    private StartDownloadJarParam startDownloadJarParam;

    public void checkParam() {
        super.checkParam();
        ParamCheckerUtil.nonNull(startDownloadJarParam, "startDownloadJarParam不能为空");
        startDownloadJarParam.checkParam();
    }

    public String getJarPath() {
        return getFilePath(startDownloadJarParam.getPomXmlFileName());
    }






}
