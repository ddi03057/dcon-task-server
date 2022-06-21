package kr.co.dcon.taskserver.menu.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import lombok.Data;

@Data
public class MenuLeftListReqDTO implements Forwardable {

    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home", required = true)
    private String menuPath;

    @ApiModelProperty(value = "메뉴펼침여부", notes = "메뉴펼침여부", example = "true", required = true)
    private boolean collapsed;

    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home", required = true)
    private String id;

    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home", required = true)
    private String boardType;

    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home")
    private String menuPathOrg;
    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home")
    private String projectUrlPath;

    @ApiModelProperty(value = "menu path", notes = "menu path", example = "/home")
    private String projectId;

    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);

        return urlParam.toString();
    }
}
