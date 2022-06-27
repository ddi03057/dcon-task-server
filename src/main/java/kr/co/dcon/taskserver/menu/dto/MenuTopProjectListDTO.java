package kr.co.dcon.taskserver.menu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuTopProjectListDTO {

    @ApiModelProperty(value = "menu id", notes = "menu id", example = "menu_1")
    private String projectId;

    @ApiModelProperty(value = "project id", notes = "project id", example = "dcon_project_1")
    private String projectName;

    @ApiModelProperty(value = "사용 유무", notes = "사용 유무", example = "Y")
    private String useYn;

    @ApiModelProperty(value = "url", notes = "url", example = "/project/issue/dcon_project_1")
    private String href;
}
