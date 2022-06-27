package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuListDTO {

    @ApiModelProperty(value = "menu header", notes = "menu header")
    MenuInfoDTO info;

    @ApiModelProperty(value = "menu", notes = "menu")
    List<MenuLeftListDTO> list;
}
