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

import com.joker.dubbo.dynamic.invoke.util.ParamCheckerUtil;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RepositoryModel {

    private String id;
    private String name;
    private String url;

    public void checkParam() {
        ParamCheckerUtil.notBlank(id, "id不能为空");
        ParamCheckerUtil.notBlank(name, "name不能为空");
        ParamCheckerUtil.notBlank(url, "url不能为空");

    }

}
