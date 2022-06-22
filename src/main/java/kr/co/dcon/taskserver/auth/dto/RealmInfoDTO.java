package kr.co.dcon.taskserver.auth.dto;


import lombok.Data;

@Data
public class RealmInfoDTO {
    private String realmName;
    private String clientId;
    private String clientSecret;
    private String userName;
    private String password;
}
