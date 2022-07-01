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

    @ApiModelProperty(value = "taskUserId", notes = "taskUserId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
    private String taskUserId;
}
