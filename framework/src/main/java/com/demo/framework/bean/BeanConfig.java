package com.demo.framework.bean;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

//	@Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//       FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//       FastJsonConfig fastJsonConfig = new FastJsonConfig();
//       fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//       List<MediaType> fastMediaTypes = new ArrayList<>();
//       fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//       fastConverter.setSupportedMediaTypes(fastMediaTypes);
//       fastConverter.setFastJsonConfig(fastJsonConfig);
//       HttpMessageConverter<?> converter = fastConverter;
//       return new HttpMessageConverters(converter);
//
//    }

//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        return corsConfiguration;
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig()); // 4
//        return new CorsFilter(source);
//    }


    @Bean
	public Validator validate(){
		 ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	     return factory.getValidator();
	}
	
}
