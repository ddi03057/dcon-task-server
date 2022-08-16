package kr.co.dcon.taskserver.common.util;


import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class RestTemplateUtil {

    @Autowired
    private static RestTemplate restTemplate;

    RestTemplateUtil(RestTemplate xrestTemplate){
        restTemplate = xrestTemplate;
    }


    public static <T> ResponseDTO<T> getForResponseDTO(String url, ParameterizedTypeReference<ResponseDTO<T>> responseType) {
        return RestTemplateUtil.getForResponseDTO(url, null, responseType);
    }
//    public static ResponseEntity<AlertNowDTO> getForResponseDTO(String url, HttpHeaders headers, Map<String, Object> params) throws URISyntaxException {
//        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
//        log.info("result Test : {}", restTemplate.exchange(new URI(url), HttpMethod.GET, requestEntity, AlertNowDTO.class));
//        return restTemplate.exchange(new URI(url), HttpMethod.GET, requestEntity, AlertNowDTO.class);
//    }

    /*
    	@Override
	public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request,
			Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {

		RequestCallback requestCallback = httpEntityCallback(request, responseType);
		ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
		return nonNull(execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables));
	}
     */
    public static <T> ResponseDTO<T> getForResponseDTO(String url, Map<String, Object> params, ParameterizedTypeReference<ResponseDTO<T>> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(params);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType).getBody();
    }

    public static <T> ResponseDTO<T> postJsonResponseDTO(String url, MultiValueMap<String, String> parameters, ResponseDTO<T> responseType) {
        return restTemplate.postForObject(url, parameters, responseType.getClass());
    }


    public static void putForResponseDTO(String url, Object obj) {
        restTemplate.put(url, obj);
    }


    public static String putForResponseResult(String url, HttpHeaders headers, Object obj) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(obj, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class).getBody();
    }


}
