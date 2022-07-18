package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProjectSubItemCRUDReqDTO implements Forwardable {

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1")
    private String projectId;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_2")
    private String taskId;

    @ApiModelProperty(value = "userId", notes = "userId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String userId;

    @ApiModelProperty(value = "taskSubId", notes = "taskSubId", example = "commentDesc")
    private String taskSubId;

    @ApiModelProperty(value = "taskSubName", notes = "taskSubName", example = "commentDesc")
    private String taskSubName;


    @ApiModelProperty(value = "updateId", notes = "updateId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String updateId;

    @ApiModelProperty(value = "updateDate", notes = "updateDate", example = "1", required = true)
    private String updateDate;

    @ApiModelProperty(value = "createId", notes = "createId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String createId;

    @ApiModelProperty(value = "updateDate", notes = "updateDate", example = "1")
    private String createDate;

    @ApiModelProperty(value = "seq", notes = "seq", example = "1", required = true)
    private String seq;
    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?projectId=").append(projectId);
        urlParam.append("&taskId=").append(taskId);
        urlParam.append("&userId=").append(userId);
        urlParam.append("&taskSubName=").append(taskSubName);
        urlParam.append("&taskSubId=").append(taskSubId);
        urlParam.append("&seq=").append(seq);
        return urlParam.toString();
    }
}
