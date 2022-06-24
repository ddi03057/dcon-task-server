package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@Slf4j
public class MenuTopListReqDTO implements Forwardable {
    @ApiModelProperty(value = "userId", notes = "userId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "사용 유무", notes = "사용 유무", example = "Y")
    private String useYn;

    @ApiModelProperty(value = "사용자 권한", notes = "사용자 권한", example = "admin")
    private String userAuth;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?useYn=").append(useYn);
        urlParam.append("&userAuth=").append(userAuth);
        urlParam.append("&userid=").append(userId);

        log.info("urlParam.toString()::{}",urlParam.toString());

        return urlParam.toString();
    }
}
