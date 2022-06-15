package kr.co.dcon.taskserver.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	String version = "V1";
	String title = "API " + version;

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
		log.info("version : {}", version); // 소나큐브 코드스멜로 잡혀 추후 사용하는 것으로 판단 하여 로깅으로 제거 함.
		return new ApiInfoBuilder().title("DcOn API List " + title)
				.description("DcOn API List " + title).build();

	}
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}
	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}
	@Bean
    public Docket apiV2() {
        version = "V2";
        title = "API " + version;
        return new Docket(DocumentationType.SWAGGER_2)
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
