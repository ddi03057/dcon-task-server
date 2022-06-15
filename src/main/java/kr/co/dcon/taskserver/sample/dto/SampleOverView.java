package kr.co.dcon.taskserver.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SampleOverView {

    @ApiModelProperty(value = "사용자 이름",notes = "이메일")
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일")
    private String userEmail;

    @ApiModelProperty(value = "번호", notes = "번호")
    private String seq;
}
