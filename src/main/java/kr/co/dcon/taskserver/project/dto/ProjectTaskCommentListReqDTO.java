package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProjectTaskCommentListReqDTO implements Forwardable {

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_11", required = true)
    private String taskId;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?projectId=").append(projectId);
        urlParam.append("&taskId=").append(taskId);

        return urlParam.toString();
    }
}
