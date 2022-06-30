package kr.co.dcon.taskserver.user.mapper;

import kr.co.dcon.taskserver.auth.dto.RealmInfoDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserListDTO;
import kr.co.dcon.taskserver.user.dto.UserListProjectReqDTO;

import java.util.List;

public interface UserMapper {

    int selectKeyCloakUserCount(UserCreateDTO user);

    String selectKeyCloakUserId(String userEmail);

    RealmInfoDTO selectKecloakRealmInfo();

    List<UserListDTO> selectProjectUserList(UserListProjectReqDTO reqDTO);

    UserListDTO selectProjectUserDetail(UserListProjectReqDTO reqDTO);
}
