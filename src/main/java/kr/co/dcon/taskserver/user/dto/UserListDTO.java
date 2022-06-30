package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserListDTO {
    @ApiModelProperty(value = "userId", notes = "userId", example = "20878cc7-4397-4d26-8269-73cd220c95a3", required = true)
    public String userId;

    @ApiModelProperty(value = "userEmail", notes = "userEmail", example = "suseokpark@dc-on.co.kr")
    public String userEmail;

    @ApiModelProperty(value = "firstName", notes = "firstName", example = "수석")
    public String firstName;

    @ApiModelProperty(value = "lastName", notes = "lastName", example = "박")
    public String lastName;

    @ApiModelProperty(value = "userName", notes = "userName", example = "박수석")
    public String userName;
}
