package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;

@Data
public class ProjectPrefixExistReqDTO implements Forwardable {

    @ApiModelProperty(value = "taskPrefix", notes = "taskPrefix", example = "dcon_project_1", required = true)
    private String taskPrefix;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?taskPrefix=").append(taskPrefix);

        return urlParam.toString();
    }
}
