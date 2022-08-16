package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@Slf4j
public class MenuTopListReqDTO implements Forwardable {
    @ApiModelProperty(value = "userId", notes = "userId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "사용 유무", notes = "사용 유무", example = "Y")
    private String useYn;

    @ApiModelProperty(value = "사용자 권한", notes = "사용자 권한", example = "admin")
    private String userAuth;

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "71b6e619-3acf-4284-9e2e-f0a73188a533")
    private String projectId;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?useYn=").append(useYn);
        urlParam.append("&userAuth=").append(userAuth);
        urlParam.append("&userid=").append(userId);
        urlParam.append("&projectId=").append(projectId);

        return urlParam.toString();
    }
}
