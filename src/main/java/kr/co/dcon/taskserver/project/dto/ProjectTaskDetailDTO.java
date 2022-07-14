package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectTaskDetailDTO {

    @ApiModelProperty(value = "projectName", notes = "projectName", example = "dcon_project_1", required = true)
    private String projectName;

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_1")
    private String taskId;

    @ApiModelProperty(value = "taskName", notes = "taskName", example = "이슈 등록")
    private String taskName;

    @ApiModelProperty(value = "taskDesc", notes = "taskDesc", example = "taskDesc")
    private String taskDesc;

    @ApiModelProperty(value = "taskStatus", notes = "taskStatus", example = "open")
    private String taskStatus;

    @ApiModelProperty(value = "taskUserId", notes = "taskUserId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String taskUserId;

    @ApiModelProperty(value = "useYn", notes = "useYn", example = "Y")
    private String useYn;
}
