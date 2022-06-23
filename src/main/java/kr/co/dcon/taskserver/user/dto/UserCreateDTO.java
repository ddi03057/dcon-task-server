package kr.co.dcon.taskserver.user.dto;

import kr.co.dcon.taskserver.common.util.Utils;
import lombok.Data;

import java.util.Date;

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
}
