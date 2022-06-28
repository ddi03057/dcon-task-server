package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectListDTO {

    @ApiModelProperty(value = "taskStatus", notes = "taskStatus", example = "open", required = true)
    private String taskStatus;

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "taskName", notes = "taskName", example = "사용자 정보 수정")
    private String taskName;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_1")
    private String taskId;

    @ApiModelProperty(value = "taskDesc", notes = "taskDesc", example = "이슈 등록을 한다")
    private String taskDesc;

    @ApiModelProperty(value = "seq", notes = "seq", example = "1")
    private String seq;

    @ApiModelProperty(value = "userId", notes = "userId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
    private String userId;
}
