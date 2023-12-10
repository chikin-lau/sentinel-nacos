package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: ParamFlowRuleNacosProvider
 * @Description: TODO
 * @Author: ChiKin Lau @ SUSE
 * @Date: 2023/11/8 10:08
 * @version: 1.0
 **/
@Component("paramFlowRuleNacosProvider")
public class ParamFlowRuleNacosProvider {

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<ParamFlowRuleEntity>> converter;
    // 增加的代码
    @Value("${sentinel.nacos.groupId}")
    private String groupId;
    @Value("${sentinel.custom.PARAM_FLOW_DATA_ID_POSTFIX}")
    private String dataIdPostfix;

    public CompletableFuture<List<ParamFlowRuleEntity>> getRules(String appName) throws Exception {
        // 增加的代码 用于判断是否自定义dataid后缀
        if (dataIdPostfix.isEmpty()){
            dataIdPostfix = NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX;
        }
        String rules = configService.getConfig(appName + dataIdPostfix,
                groupId, 3000);
//        if (StringUtil.isEmpty(rules)) {
//            return new CompletableFuture<>();
//        }
        // 增加的代码 用于适应不同的返回类型
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(rules);
        return future.thenApply(json -> JSON.parseArray(json, ParamFlowRuleEntity.class));
    }
}
