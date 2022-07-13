package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectDetailDTO {

    @ApiModelProperty(value = "projectName", notes = "projectName", example = "dcon_project_1", required = true)
    private String projectName;

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "useYn", notes = "useYn", example = "Y")
    private String useYn;

}
