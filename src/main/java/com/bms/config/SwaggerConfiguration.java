package com.bms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
 * @author 咸鱼
 * @date 2018/12/24 21:08
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //api所在包路径（这个包指的是我们在哪些类中使用swagger2来测试）
                .apis(RequestHandlerSelectors.basePackage("com.bms.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("api文档")
                //描述
                .description("restful 风格接口")
                //创建人
                .contact(new Contact("咸鱼", "https://blog.csdn.net", "744763941@qq.com"))
                //版本号
                .version("1.0")
                .build();
    }
}
