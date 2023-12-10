package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
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
 * @ClassName: DegradeRuleNacosProvider
 * @Description: TODO
 * @Author: ChiKin Lau @ SUSE
 * @Date: 2023/11/7 17:09
 * @version: 1.0
 **/
@Component("degradeRuleNacosProvider")
public class DegradeRuleNacosProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<DegradeRuleEntity>> converter;
    @Value("${sentinel.nacos.groupId}")
    private String groupId;
    @Value("${sentinel.custom.DEGRADE_DATA_ID_POSTFIX}")
    private String dataIdPostfix;

    @Override
    public List<DegradeRuleEntity> getRules(String appName) throws Exception {
        if (dataIdPostfix.isEmpty()){
            dataIdPostfix = NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX;
        }
        String rules = configService.getConfig(appName + dataIdPostfix,
                groupId, 3000);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
