package kr.co.dcon.taskserver.auth.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailsListDTO {
    private List<UserDetailsDTO> users;
    private Integer totalCount;
}
