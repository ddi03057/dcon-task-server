package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserListProjectReqDTO {
    @ApiModelProperty(value = "userId", notes = "userId", example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610")
    @NotBlank
    public String userId;

    @ApiModelProperty(value = "projectRealm", notes = "projectRealm", example = "dcon")
    public String projectRealm;

    @ApiModelProperty(value = "realmMasterUseUSerId", notes = "realmMasterUseUSerId", example = "43bedf60-581b-4cd7-9d7f-4cbdc046d1cc")
    public String realmMasterUseUserId;


}
