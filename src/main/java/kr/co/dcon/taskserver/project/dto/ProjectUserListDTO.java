package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;

@Data
public class ProjectUserListDTO implements Forwardable {

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    private String userId;
    private String userName;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?projectId=").append(projectId);

        return urlParam.toString();
    }
}
