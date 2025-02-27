package mx.unam.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Profile("!production")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Proyecto de ingenieria de software 2022-1")
                .version("Version 1.0")
                .description("Servicios REST de backend publicados en formato JSON: <a href='/v2/api-docs'>https://app-url/v2/api-docs</a>")
                .license("Apache Licence")
                .termsOfServiceUrl("http://www.qbits.mx")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}


