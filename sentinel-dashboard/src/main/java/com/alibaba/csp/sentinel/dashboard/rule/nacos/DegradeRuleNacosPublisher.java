package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: DegradeRuleNacosPublisher
 * @Description: TODO
 * @Author: ChiKin Lau @ SUSE
 * @Date: 2023/11/7 17:11
 * @version: 1.0
 **/
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<DegradeRuleEntity>, String> converter;
    @Value("${sentinel.nacos.groupId}")
    private String groupId;
    @Value("${sentinel.custom.DEGRADE_DATA_ID_POSTFIX}")
    private String dataIdPostfix;

    @Override
    public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {
        if (dataIdPostfix.isEmpty()){
            dataIdPostfix = NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX;
        }
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        configService.publishConfig(app + dataIdPostfix,
                groupId, converter.convert(rules));
    }
}
