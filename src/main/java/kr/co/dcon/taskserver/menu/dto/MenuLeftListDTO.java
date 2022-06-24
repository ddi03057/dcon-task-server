package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuLeftListDTO {

    @ApiModelProperty(value = "href", notes = "href")
    private String href;

    @ApiModelProperty(value = "title", notes = "title")
    private String title;

    @ApiModelProperty(value = "title", notes = "title")
    private String icon;

    @ApiModelProperty(value = "seq", notes = "seq")
    private String seq;

    @ApiModelProperty(value = "메뉴 순서", notes = "메뉴 순서", example = "메뉴 순서")
    private int menuOrder;
}
