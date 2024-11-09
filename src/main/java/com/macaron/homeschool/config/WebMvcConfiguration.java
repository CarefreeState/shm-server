package com.macaron.homeschool.config;


import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.macaron.homeschool.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.macaron.homeschool.common.constants.DateTimeConstants.*;


/**
 *  @author cattleYuan
 *  @date 2024/1/18
 */
//配置类，注册用户端；拦截器
@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
        ;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(Boolean.TRUE)
                .dateFormat(DATE_FORMAT)
                .simpleDateFormat(DATE_TIME_PATTERN)
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .modulesToInstall(new ParameterNamesModule());
        converters.add(new StringHttpMessageConverter());
        converters.add(new ByteArrayHttpMessageConverter()); // 避免 api-docs 编写为 base64 码
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
}
