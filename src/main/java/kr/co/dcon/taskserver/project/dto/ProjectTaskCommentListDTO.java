package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectTaskCommentListDTO {

    @ApiModelProperty(value = "seq", notes = "seq")
    private String seq;

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "userId", notes = "userId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String userId;

    @ApiModelProperty(value = "userName", notes = "userName", example = "박수석")
    private String userName;

    @ApiModelProperty(value = "coomentDesc", notes = "coomentDesc", example = "coomentDesc")
    private String coomentDesc;


    @ApiModelProperty(value = "createDate", notes = "createDate", example = "2022-06-29 13:14:47")
    private String createDate;

}
