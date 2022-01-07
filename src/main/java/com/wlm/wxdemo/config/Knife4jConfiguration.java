package com.wlm.wxdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * knife4j 配置
 * @author wuliming
 * @date 2021/8/13 17:28
 */
@Configuration
public class Knife4jConfiguration {

    @Value("${knife4j.enable:false}")
    private Boolean swaggerEnable;

    @Bean
    public Docket defaultApi2() {
        String swaggerTitle = "微信公众号测试";
        String swaggerVersion = "1.0";
        String swaggerServiceUrl = "http://localhost:8002/wx";
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerTitle)
                        .description("微信公众号测试")
                        .termsOfServiceUrl(swaggerServiceUrl)
                        .version(swaggerVersion)
                        .build())
                //分组名称
                .groupName("微信公众号测试")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.wlm.wxdemo.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
