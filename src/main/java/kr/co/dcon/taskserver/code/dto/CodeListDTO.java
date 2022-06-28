package kr.co.dcon.taskserver.code.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CodeListDTO {

    @ApiModelProperty(value = "codeId", notes = "codeId", example = "backLog")
    @NotBlank
    private String codeId;

    @ApiModelProperty(value = "codeName", notes = "codeName", example = "backLog")
    @NotBlank
    private String codeName;
}
