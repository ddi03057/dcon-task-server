package kr.co.dcon.taskserver.user.mapper;

import kr.co.dcon.taskserver.user.dto.UserCreateDTO;

public interface UserMapper {

    int selectKeyCloakUserCount(UserCreateDTO user);

    String selectKeyCloakUserId(String userEmail);
}
