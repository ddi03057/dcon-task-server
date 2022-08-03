package kr.co.dcon.taskserver.common.dto;

import lombok.Data;

@Data
public class SendEmailDTO {
    private String userId;
    private String userName;
    private String userEmail;
    private String sendYn;
    private String sendType;
    private String createDate;
    private String sendDate;

}
