package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuInfoDTO {

    @ApiModelProperty(value = "번호", notes = "번호", example = "번호")
    private String seq;

    @ApiModelProperty(value = "메뉴헤더명", notes = "메뉴헤더명", example = "메뉴헤더명")
    private String header;

    @ApiModelProperty(value = "메뉴헤더명", notes = "메뉴헤더명", example = "메뉴헤더명")
    private boolean hiddenOnCollapse;


}
