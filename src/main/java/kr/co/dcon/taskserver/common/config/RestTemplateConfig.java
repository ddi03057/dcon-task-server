package kr.co.dcon.taskserver.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

@Configuration
@Slf4j
public class RestTemplateConfig {

	@Value("${dcon.keycloak.rest.connectionTimeout}")
	private int apiConnectionTimeout;

	@Value("${dcon.keycloak.rest.readTimeout}")
	private int apiReadTimeout;

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, Environment env) {
		Integer connectionTimeout = 3000;
		Integer readTimeout = 3000;
		try {
			connectionTimeout = apiConnectionTimeout;
			readTimeout = apiReadTimeout;
		} catch (Exception e) {
			log.warn("error init resttemplate properties client - use default");
		}

		RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofMillis(connectionTimeout))
				.setReadTimeout(Duration.ofMillis(readTimeout)).build();

		restTemplate.getInterceptors().add((request, body, execution) -> {
			if(Objects.equals(request.getMethod(), HttpMethod.PUT)){
				request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			}else{
				request.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			}
			request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			return execution.execute(request, body);
		});

		return restTemplate;
	}


}
