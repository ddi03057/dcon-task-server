package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectTaskListUpdateReqDTO {
    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "taskStatus", notes = "taskStatus", example = "open", required = true)
    private String taskStatus;

    @ApiModelProperty(value = "taskId", notes = "taskId", example = "task_1", required = true)
    private List<String> taskIdList;

    @ApiModelProperty(value = "updateId", notes = "updateId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
    private String updateId;

    @ApiModelProperty(value = "userId", notes = "userId", example = "af14336d-77e3-43df-a4f5-f8f7b5c7f10e'")
    private String userId;

}
