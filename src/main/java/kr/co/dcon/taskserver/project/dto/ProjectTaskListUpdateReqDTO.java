package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectTaskListUpdateReqDTO {
    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "taskStatus", notes = "taskStatus", example = "open", required = true)
    private String taskStatus;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_1", required = true)
    private List<String> taskIdList;

    @ApiModelProperty(value = "updateId", notes = "updateId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String updateId;

    @ApiModelProperty(value = "userId", notes = "userId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String userId;
    @ApiModelProperty(value = "useYn", notes = "useYn", example = "Y")
    private String useYn;
}
