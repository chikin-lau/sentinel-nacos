package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AuthRuleNacosProvider
 * @Description: TODO
 * @Author: ChiKin Lau @ SUSE
 * @Date: 2023/11/7 17:09
 * @version: 1.0
 **/
@Component("authorityRuleNacosProvider")
public class AuthorityRuleNacosProvider implements DynamicRuleProvider<List<AuthorityRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<AuthorityRuleEntity>> converter;
    @Value("${sentinel.nacos.groupId}")
    private String groupId;
    @Value("${sentinel.custom.AUTHORITY_DATA_ID_POSTFIX}")
    private String dataIdPostfix;

    @Override
    public List<AuthorityRuleEntity> getRules(String appName) throws Exception {
        if (dataIdPostfix.isEmpty()){
            dataIdPostfix = NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX;
        }
        String rules = configService.getConfig(appName + dataIdPostfix,
                groupId, 3000);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
