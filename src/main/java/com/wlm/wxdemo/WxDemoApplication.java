package com.wlm.wxdemo;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author wuliming
 */
@SpringBootApplication
@EnableSwagger2WebMvc
@EnableKnife4j
@MapperScan("com.wlm.wxdemo.dao")
public class WxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxDemoApplication.class, args);
    }

}
