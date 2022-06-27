package kr.co.dcon.taskserver.menu.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MenuLeftListReqDTO implements Forwardable {

    @ApiModelProperty(value = "userId", notes = "userId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
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
