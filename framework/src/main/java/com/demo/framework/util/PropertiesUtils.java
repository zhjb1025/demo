package com.demo.framework.util;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * 获取配置文件的值
 * @author 张建彬
 *
 */
@Component
public class PropertiesUtils implements EmbeddedValueResolverAware {
    private static StringValueResolver valueResolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
    	PropertiesUtils.valueResolver = stringValueResolver;
    }
    /**
     * 获取配置文件KEY对应的值
     * @param key  格式是 ${key}
     * @return
     */
    public static String getPropertiesValue(String key) {
        return valueResolver.resolveStringValue(key);
    }

}