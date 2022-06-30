package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserListProjectReqDTO {
    @ApiModelProperty(value = "userId", notes = "userId", example = "20878cc7-4397-4d26-8269-73cd220c95a3")
    @NotBlank
    public String userId;

    @ApiModelProperty(value = "projectRealm", notes = "projectRealm", example = "dcon")
    public String projectRealm;

    @ApiModelProperty(value = "realmMasterUseUSerId", notes = "realmMasterUseUSerId", example = "03e70271-4736-4d5b-ae4f-5016c765c8db")
    public String realmMasterUseUserId;


}
