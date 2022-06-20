package kr.co.dcon.taskserver.sample.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;

@Data
@Slf4j
public class SampleReqDTO implements Forwardable {

    @ApiModelProperty(value = "사용자 이름",notes = "이메일")
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "번호", notes = "번호", example = "1")
    private String seq;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
     //   urlParam.append("/sample/sampleUserCount/");
       // urlParam.append(userEmail);
        urlParam.append("?userEmail="+userEmail);
        urlParam.append("&userName="+userName);
        log.info("urlParam.toString()::{}",urlParam.toString());
        return urlParam.toString();
    }
}
//http://localhost:8082/task/v1/sample/sampleUserCount/test123@1.com?userEmail=test123@1.com?userName=test1111
//http://localhost:8082/task/v1/sample/sampleUserCount/test123@1.com?userEmail=test123%401.com&userName=test1111