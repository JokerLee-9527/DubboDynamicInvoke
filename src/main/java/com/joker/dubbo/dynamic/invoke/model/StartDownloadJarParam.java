package com.joker.dubbo.dynamic.invoke.model;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.joker.dubbo.dynamic.invoke.util.ParamCheckerUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author JokerLee
 * @version V1.0
 * @Description: 下载jar的参数
 * @date 2021/1/7 16:06
 */
@Data
@Builder
public class StartDownloadJarParam {

    /**
     * pom文件名(相对路径)
     */
    private String pomXmlFileName;

    /**
     *  pom->dependency
     */
    private List<DependencyModel> dependencyModelList;

    /**
     * pom->Repository
     */
    private List<RepositoryModel> repositoryModelList;

    public void checkParam() {
        ParamCheckerUtil.notBlank(pomXmlFileName, "pomXmlFileName不能为空");
        ParamCheckerUtil.notEmpty(dependencyModelList,"dependencyModelList不能为空");
        dependencyModelList.forEach(DependencyModel::checkParam);

        if (CollectionUtils.isNotEmpty(repositoryModelList)) {
            repositoryModelList.forEach(RepositoryModel::checkParam);
        }
    }


}
