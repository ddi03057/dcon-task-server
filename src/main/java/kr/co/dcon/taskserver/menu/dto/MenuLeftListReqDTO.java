package kr.co.dcon.taskserver.menu.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MenuLeftListReqDTO implements Forwardable {

    @ApiModelProperty(value = "userId", notes = "userId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "사용 유무", notes = "사용 유무", example = "Y")
    private String useYn;

    @ApiModelProperty(value = "menu path", notes = "menu path", example = "dcon_project_1")
    private String projectId;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("/");
        urlParam.append("?userid=" + userId);
        urlParam.append("&projectId=" + projectId);

        return urlParam.toString();
    }
}
