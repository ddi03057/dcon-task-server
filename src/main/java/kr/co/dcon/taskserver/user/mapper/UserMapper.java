package kr.co.dcon.taskserver.user.mapper;

import kr.co.dcon.taskserver.auth.dto.RealmInfoDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserListDTO;
import kr.co.dcon.taskserver.user.dto.UserListProjectReqDTO;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    int selectKeyCloakUserCount(UserCreateDTO user);

    String selectKeyCloakUserId(Map<String, Object> paramMap);

    RealmInfoDTO selectKeycloakRealmInfo();

    List<UserListDTO> selectProjectUserList(UserListProjectReqDTO reqDTO);

    UserListDTO selectProjectUserDetail(UserListProjectReqDTO reqDTO);

    String selectUserLocale(String userId);
}
