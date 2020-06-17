package com.application.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * API页面展示配置
 *
 * @author yanghaiyong 2019年11月5日 23:42:01
 */
@Configuration
@EnableSwagger2
//@EnableSwaggerBootstrapUI
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Configuration {
    private final TypeResolver typeResolver;

    public Swagger2Configuration(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }


    @Bean(name = "defaultApi2")
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> globalOperationParameters = new ArrayList<>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        globalOperationParameters.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()// 生成构造器
                .apis(RequestHandlerSelectors.basePackage("com.application.jpa.web.rest"))//为当前包路径
                .paths(paths())//设置路径选择方式
                .build()
                .ignoredParameterTypes(HttpServletRequest.class, HttpServletResponse.class,
                        HttpSession.class, Pageable.class, Sort.class,
                        com.querydsl.core.types.Predicate.class)
                .apiInfo(apiInfo())// 文档头部构建
                .pathMapping("/")// 为Servlet映射路径,如果存在的Servlet
                .directModelSubstitute(LocalDateTime.class, String.class)// 将LocalDateTime转String(直接替换)
                .genericModelSubstitutes(ResponseEntity.class)// 设置泛型替换
                .alternateTypeRules(
                        newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)))//添加新的响应实体规则
                .useDefaultResponseMessages(true)// 声明是否使用http response codes
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(new ResponseMessageBuilder()
                                .code(500)
                                .message("500 服务器异常")
                                .responseModel(new ModelRef("Error"))
                                .build()))// 设置通用响应信息
                .securitySchemes(securitySchemes())// 设置安全认证头部
                .securityContexts(securityContexts())
                .enableUrlTemplating(true);
        //.globalOperationParameters(globalOperationParameters);
        //.additionalModels(typeResolver.resolve(AdditionalModel.class));// 手动添加实体解析
    }

    private Predicate<String> paths() {
        return or(
                regex("/business.*"),
                regex("/api.*"),
                regex("/contacts.*"),
                regex("/pet.*"),
                regex("/springsRestController.*"),
                regex("/test.*"));// 选择路径匹配
    }

    private List<ApiKey> securitySchemes() {
        return newArrayList(
                new ApiKey("Authorization", "Authorization", "header"));
    }

    private List<SecurityContext> securityContexts() {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build()
        );
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("Authorization", authorizationScopes));
    }

    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("test-app-client-id")
                .clientSecret("test-app-client-secret")
                .realm("test-app-realm")
                .appName("test-app")
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .build();
    }

    /*@Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }*/

    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 使用 Swagger2 构建RESTFul API")
                //创建人
                .contact(new Contact("杨海勇", "http://blog.bianxh.top/", "925418240@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .license("所有权归杨海勇")
                .build();
    }
}
