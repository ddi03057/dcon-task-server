package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuTopProjectListDTO {

    @ApiModelProperty(value = "menu id", notes = "menu id", example = "menu_1")
    private String menuId;

    @ApiModelProperty(value = "parentId", notes = "parentId", example = "0")
    private String parentId;
    @ApiModelProperty(value = "menuName", notes = "menuName", example = "0")
    private String menuName;
    @ApiModelProperty(value = "menuUrl", notes = "menuUrl", example = "0")
    private String menuUrl;
    @ApiModelProperty(value = "childParentId", notes = "menuUrl", example = "0")
    private String childParentId;


    @ApiModelProperty(value = "project id", notes = "project id", example = "dcon_project_1")
    private String projectName;

    @ApiModelProperty(value = "사용 유무", notes = "사용 유무", example = "Y")
    private String useYn;

    @ApiModelProperty(value = "url", notes = "url", example = "/project/issue/dcon_project_1")
    private String href;

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "/dcon_project_1")
    private String projectId;

}
