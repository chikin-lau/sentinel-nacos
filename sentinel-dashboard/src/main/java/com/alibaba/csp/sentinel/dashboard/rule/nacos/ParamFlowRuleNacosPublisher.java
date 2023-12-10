package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: ParamFlowRuleNacosPublisher
 * @Description: TODO
 * @Author: ChiKin Lau @ SUSE
 * @Date: 2023/11/8 10:08
 * @version: 1.0
 **/
@Component("paramFlowRuleNacosPublisher")
public class ParamFlowRuleNacosPublisher implements DynamicRulePublisher<List<ParamFlowRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<ParamFlowRuleEntity>, String> converter;
    @Value("${sentinel.nacos.groupId}")
    private String groupId;
    @Value("${sentinel.custom.PARAM_FLOW_DATA_ID_POSTFIX}")
    private String dataIdPostfix;

    @Override
    public void publish(String app, List<ParamFlowRuleEntity> rules) throws Exception {
        if (dataIdPostfix.isEmpty()){
            dataIdPostfix = NacosConfigUtil.PARAM_FLOW_DATA_ID_POSTFIX;
        }
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
//        configService.publishConfig(app + NacosConfigUtil.FLOW_DATA_ID_POSTFIX,
//            NacosConfigUtil.GROUP_ID, converter.convert(rules));
        configService.publishConfig(app + dataIdPostfix,
                groupId, converter.convert(rules));
    }
}
