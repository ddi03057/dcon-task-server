package kr.co.dcon.taskserver.sample.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class SampleReqDTO {

    @ApiModelProperty(value = "사용자 이름",notes = "이메일")
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "번호", notes = "번호", example = "1")
    private String seq;
}
