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

    @ApiModelProperty(value = "updateId", notes = "updateId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
    private String updateId;

    @ApiModelProperty(value = "userId", notes = "userId", example = "af14336d-77e3-43df-a4f5-f8f7b5c7f10e'")
    private String userId;

    @ApiModelProperty(value = "taskDesc", notes = "taskDesc", example = "taskDesc")
    private String taskDesc;

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

        return urlParam.toString();
    }


}
