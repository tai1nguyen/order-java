package org.nguyen.orderjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ApiDocConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("org.nguyen.orderjava.controllers"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(info());
    }

    private ApiInfo info() {
        return new ApiInfoBuilder().title("Order Java Application")
            .description("API documentation for the Order Java Application")
            .version("Local-Dev")
            .build();
    }
}
