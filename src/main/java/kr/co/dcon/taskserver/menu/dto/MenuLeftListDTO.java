package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuLeftListDTO {

    @ApiModelProperty(value = "href", notes = "href")
    String href;

    @ApiModelProperty(value = "title", notes = "title")
    String title;

    @ApiModelProperty(value = "title", notes = "title")
    String icon;

    @ApiModelProperty(value = "seq", notes = "seq")
    String seq;

    @ApiModelProperty(value = "num", notes = "num")
    String num;

}
