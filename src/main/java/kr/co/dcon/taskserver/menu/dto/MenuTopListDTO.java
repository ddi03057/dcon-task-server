package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuTopListDTO {


    @ApiModelProperty(value = "menu id", notes = "menu id", example = "menu_1")
    private String menuId;

    @ApiModelProperty(value = "project id", notes = "project id", example = "dcon_project_1")
    private String projectId;

    @ApiModelProperty(value = "메뉴명", notes = "메뉴명", example = "Home")
    private String menuName;

    @ApiModelProperty(value = "메뉴 순서", notes = "메뉴 순서", example = "메뉴 순서")
    private int menuOrder;

    @ApiModelProperty(value = "사용 유무", notes = "사용 유무", example = "Y")
    private String useYn;


    @ApiModelProperty(value = "li class", notes = "li class", example = "nav-item")
    private String menuLiClass;

    @ApiModelProperty(value = "li a class", notes = "li a class", example = "nav-link")
    private String menuLiAClass;
}