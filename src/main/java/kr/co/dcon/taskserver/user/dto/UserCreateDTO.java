package kr.co.dcon.taskserver.user.dto;

import lombok.Data;

@Data
public class UserCreateDTO {

    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String userTelNo;
    private String password;

    private int errorCnt;
    private String updateDate;
    private String updateId;
    private String userAuth;
    private String userEmail;
    private String userLocale;
    private String useYn;
}
