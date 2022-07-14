package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProjectTaskUpdateReqDTO implements Forwardable {
    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "taskStatus", notes = "taskStatus", example = "open", required = true)
    private String taskStatus;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_1", required = true)
    private String taskId;

    @ApiModelProperty(value = "updateId", notes = "updateId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String updateId;

    @ApiModelProperty(value = "userId", notes = "userId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    private String userId;

    @ApiModelProperty(value = "taskDesc", notes = "taskDesc", example = "taskDesc")
    private String taskDesc;

    @ApiModelProperty(value = "useYn", notes = "useYn", example = "Y")
    private String useYn;
    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?projectId="+projectId);
        urlParam.append("&taskStatus="+taskStatus);
        urlParam.append("&taskId="+taskId);
        urlParam.append("&updateId="+updateId);
        urlParam.append("&userId="+userId);
        urlParam.append("&taskDesc="+taskDesc);
        urlParam.append("&useYn="+useYn);

        return urlParam.toString();
    }


}
