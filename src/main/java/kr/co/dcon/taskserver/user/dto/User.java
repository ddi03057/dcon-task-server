package kr.co.dcon.taskserver.user.dto;

import lombok.Data;

@Data
public class User {

    private String seq;
    private String userId;
    private String userName;
    private String userPwd;
    private String userEmail;
    private String userTelNo;
    private String disWelcomeYn;
    private String useYn;
    private String lastLoginDt;
    private String loginCnt;
    private String pwdInitYn;
    private String pwdChgDt;
    private String creaateDate;
    private String createId;
    private String createIpAddr;
    private String updateDate;
    private String updateId;
    private String updateIpAddr;
    private String auth;
    private String agreementTermYn;
    private String privacyPolishYn;
    private int errorCnt;
    private String searchAuth;
    private String authNm;
    private String useYnNm;
    private String email;
    private String userDetail;
    private String token;
}
