package kr.co.dcon.taskserver.menu.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class MenuLeftListReqDTO implements Forwardable {

    @ApiModelProperty(value = "userId", notes = "userId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
    @NotBlank
   // @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userId;

//    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home", required = true)
//    private String menuPath;
//
//    @ApiModelProperty(value = "메뉴펼침여부", notes = "메뉴펼침여부", example = "true", required = true)
//    private boolean collapsed;
//
//    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home", required = true)
//    private String id;
//
//    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home", required = true)
//    private String boardType;
//
//    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home")
//    private String menuPathOrg;
//    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home")
//    private String projectUrlPath;
//
//    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home")
//    private String projectId;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("/");
        urlParam.append("?userid="+userId);

        return urlParam.toString();
    }
}
