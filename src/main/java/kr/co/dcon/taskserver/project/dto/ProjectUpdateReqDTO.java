package kr.co.dcon.taskserver.project.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.user.dto.UserInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProjectUpdateReqDTO {

    @ApiModelProperty(value = "projectId", notes = "projectId", example = "dcon_project_1", required = true)
    private String projectId;

    @ApiModelProperty(value = "userList", notes = "userList", example = "task_1", required = false)
    private List<UserInfoDTO> userList;

    @ApiModelProperty(value = "taskAssignUpdateList", notes = "deleteUserList", example = "task_1", required = false)
    private List<UserInfoDTO> taskAssignUpdateList;

    @ApiModelProperty(value = "updateId", notes = "updateId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610", required = false)
    private String updateId;

    @ApiModelProperty(value = "updateDate", notes = "updateDate", example = "2022-06-29 13:14:47")
    private String updateDate;

    @ApiModelProperty(value = "사용여부", notes = "Y,N", example = "Y")
    private String useYn;
}
