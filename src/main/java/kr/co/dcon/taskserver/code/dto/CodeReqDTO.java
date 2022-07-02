package kr.co.dcon.taskserver.code.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@Slf4j
public class CodeReqDTO{

    @ApiModelProperty(value = "codeGroupId", notes = "codeGroupId", example = "task_status")
    @NotBlank
    private String codeGroupId;

    @ApiModelProperty(value = "codeId", notes = "codeId", example = "open")
    private String codeId;

}
