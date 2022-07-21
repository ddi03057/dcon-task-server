package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProjectListReqDTO implements Forwardable {

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "taskStatus", notes = "taskStatus", example = "open", required = true)
    private String taskStatus;

    @ApiModelProperty(value = "작업자,생성자,수정자 구분", notes = "작업자,생성자,수정자 구분", example = "assign_id")
    private String issueSearchCode;
    @ApiModelProperty(value = "작업자,생성자,수정자", notes = "작업자,생성자,수정자", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String issueSearchUser;
    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append(projectId);
        urlParam.append("?projectId=").append(projectId);
        urlParam.append("&taskStatus=").append(taskStatus);
        urlParam.append("&issueSearchCode=").append(issueSearchCode);
        urlParam.append("&issueSearchUser=").append(issueSearchUser);

        return urlParam.toString();
    }
}
