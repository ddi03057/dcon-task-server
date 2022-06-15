package kr.co.dcon.taskserver.sample.dto;

import kr.co.dcon.taskserver.common.dto.PagingDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=false)
public class SampleListReqDTO extends PagingDTO {

    @ApiModelProperty(value = "사용자 이름", notes = "사용자 이름", example = "홍길동")
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "test1@dc-on.co.kr")
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "email", notes = "email", example = "email")
    private String searchGubun;

    @ApiModelProperty(value = "token", notes = "token", example = "email")
    private String token;
}

