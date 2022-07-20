package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectTaskSubItemListDTO {

    @ApiModelProperty(value = "seq", notes = "seq")
    private String seq;

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1")
    private String projectId;

    @ApiModelProperty(value = "taskSubId", notes = "taskSubId", example = "dcon_project_1")
    private String taskSubId;

    @ApiModelProperty(value = "taskSubName", notes = "taskSubName", example = "dcon_project_1")
    private String taskSubName;


    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_1")
    private String taskId;
    @ApiModelProperty(value = "completeYn", notes = "completeYn", example = "Y")
    private String completeYn;


}
