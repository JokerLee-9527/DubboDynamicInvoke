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

import com.alibaba.dubbo.common.utils.StringUtils;
import com.joker.dubbo.dynamic.invoke.util.ParamCheckerUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DependencyModel {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isBroken() {
        return StringUtils.isEmpty(groupId) || StringUtils.isEmpty(artifactId) || StringUtils.isEmpty(version);
    }

    public void checkParam() {
        ParamCheckerUtil.notBlank(groupId, "groupId不能为空");
        ParamCheckerUtil.notBlank(artifactId, "artifactId不能为空");
        ParamCheckerUtil.notBlank(version, "version不能为空");

    }

}
