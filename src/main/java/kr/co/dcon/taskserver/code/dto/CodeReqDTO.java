package kr.co.dcon.taskserver.code.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@Slf4j
public class CodeReqDTO{

    @ApiModelProperty(value = "codeGroupId", notes = "codeGroupId", example = "task_status")
    @NotBlank
    private String codeGroupId;

}
