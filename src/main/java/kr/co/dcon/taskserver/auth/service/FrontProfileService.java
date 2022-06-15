package kr.co.dcon.taskserver.auth.service;

import kr.co.dcon.taskserver.auth.dto.FrontProfileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FrontProfileService {
    @Value("${front.env}")
    private String env;

    @Value("${keycloak.auth-server-url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

public FrontProfileDTO selectFrontFile(String rootUrl) {

    return FrontProfileDTO.builder()
            .env(this.env)
            .url(this.url)
            .realm(realm)
          //  .realm(client.getWhiteLabelClient(rootUrl).getRealm())
            .build();
}
}
