package kr.co.dcon.taskserver.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	String version = "V1";
	String title = "API " + version;

	private ApiKey apiKey() {
		return new ApiKey("access_token", "Authorization", "header");
	}
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("access_token", authorizationScopes));
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false) // 기존적인 응답메시지 미사용
				.groupName(version)
				.apiInfo(apiInfo(version))
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/v1/**")) // 그중 /api/** 인 URL들만 필터링
				.build();
	}

	private ApiInfo apiInfo(String version) {
		log.info("version : {}", version);
		return new ApiInfoBuilder().title("DC-ON API List " + title)
				.description("DC-ON API List " + title).build();

	}

	@Bean
	public Docket apiV2() {
		version = "V2";
		title = "API " + version;
		List<Parameter> global = new ArrayList<>();
		global.add(new ParameterBuilder().name("Authorization").description("Access Token").parameterType("header").required(false).modelRef(new ModelRef("string")).build());
		return new Docket(DocumentationType.SWAGGER_2).globalOperationParameters(global)
				.useDefaultResponseMessages(false) // 기존적인 응답메시지 미사용
				.groupName(version)
				.apiInfo(apiInfo(version))
				.select()
				.apis(RequestHandlerSelectors.any())
				// .paths(PathSelectors.any())
				.paths(PathSelectors.ant("/api/v2/**")) // 그중 /api/** 인 URL들만 필터링
				.build();

	}




}
