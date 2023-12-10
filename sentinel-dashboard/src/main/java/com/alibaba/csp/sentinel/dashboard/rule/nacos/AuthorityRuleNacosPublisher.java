package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: AuthRuleNacosPublisher
 * @Description: TODO
 * @Author: ChiKin Lau @ SUSE
 * @Date: 2023/11/7 17:11
 * @version: 1.0
 **/
@Component("authorityRuleNacosPublisher")
public class AuthorityRuleNacosPublisher implements DynamicRulePublisher<List<AuthorityRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<AuthorityRuleEntity>, String> converter;
    @Value("${sentinel.nacos.groupId}")
    private String groupId;
    @Value("${sentinel.custom.AUTHORITY_DATA_ID_POSTFIX}")
    private String dataIdPostfix;

    @Override
    public void publish(String app, List<AuthorityRuleEntity> rules) throws Exception {
        if (dataIdPostfix.isEmpty()){
            dataIdPostfix = NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX;
        }
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        configService.publishConfig(app + dataIdPostfix,
                groupId, converter.convert(rules));
    }
}
