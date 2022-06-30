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

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append(projectId);
        urlParam.append("?projectId=").append(projectId);
        urlParam.append("&taskStatus=").append(taskStatus);

        return urlParam.toString();
    }
}
