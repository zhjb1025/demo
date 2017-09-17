package com.demo.framework.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.framework.validate.ValidatorService;

@Configuration
@EnableConfigurationProperties(value = DubboProperties.class)
@ConditionalOnClass(DubboGenericService.class)
@ConditionalOnProperty(prefix = "dubbo.service", value = "enable", matchIfMissing = false)
public class DubboServiceAutoConfiguration {

    @Autowired
    private DubboProperties dubboProperties;
    
    @Autowired
    private DubboServiceConfig dubboServiceConfig;
    @Autowired
    private ValidatorService validatorService;

    @Bean
    @ConditionalOnMissingBean(DubboGenericService.class)
    public DubboGenericService dubboGenericService() throws Exception {
    	DubboGenericService dubboGenericService = new DubboGenericService();
    	dubboGenericService.setDubboProperties(dubboProperties);
    	dubboGenericService.setDubboServiceConfig(dubboServiceConfig);
    	dubboGenericService.setValidatorService(validatorService);
    	dubboGenericService.init();
        return dubboGenericService;
    }
}
