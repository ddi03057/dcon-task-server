package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.user.dto.UserInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProjectUpdateReqDTO {

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "userInfo", notes = "userInfo", example = "task_1", required = false)
    private List<UserInfoDTO> userList;

    @ApiModelProperty(value = "updateId", notes = "updateId", example = "20878cc7-4397-4d26-8269-73cd220c95a3", required = false)
    private String updateId;

    @ApiModelProperty(value = "updateDate", notes = "updateDate", example = "2022-06-29 13:14:47")
    private String updateDate;

    @ApiModelProperty(value = "사용여부", notes = "Y,N", example = "Y")
    private String useYn;
}
