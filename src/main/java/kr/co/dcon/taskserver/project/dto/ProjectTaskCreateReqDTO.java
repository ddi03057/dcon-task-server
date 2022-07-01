package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectTaskCreateReqDTO{

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "createId", notes = "createId", example = "20878cc7-4397-4d26-8269-73cd220c95a3", required = true)
    private String createId;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_11", required = true)
    private String taskId;

    @ApiModelProperty(value = "taskName", notes = "taskName", example = "task_1", required = true)
    private String taskName;

    @ApiModelProperty(value = "taskStatus", notes = "taskStatus", example = "open")
    private String taskStatus;

    @ApiModelProperty(value = "taskDesc", notes = "taskDesc", example = "taskDesc")
    private String taskDesc;

    @ApiModelProperty(value = "userId", notes = "userId", example = "af14336d-77e3-43df-a4f5-f8f7b5c7f10e", required = true)
    private String userId;


    @ApiModelProperty(value = "userName", notes = "userName", example = "박수석")
    private String userName;

    @ApiModelProperty(value = "userEmail", notes = "userEmail", example = "suseokpasrk@dc-on.co.kr")
    private String userEmail;


    @ApiModelProperty(value = "createDate", notes = "createDate", example = "2022-06-29 13:14:47")
    private String createDate;


}
