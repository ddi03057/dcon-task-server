package kr.co.dcon.taskserver.user.controller;

import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.auth.dto.UserSimpleDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.NoResultDTO;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.dcon.taskserver.common.util.DateUtils;
import kr.co.dcon.taskserver.user.dto.UserChangeDTO;
import kr.co.dcon.taskserver.user.dto.UserChangePasswordDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserDTO;
import kr.co.dcon.taskserver.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static java.util.Arrays.asList;
import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "USER API")
@RequestMapping(value = "/api/v1/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private CurrentUserService currentUserService;

    private static final String RESULT_STRING 	= "result";
    @ApiOperation(value = "로그인한 사용자 최신 정보 조회")
    @GetMapping("user-info")
    public ResponseDTO<UserDTO> getCurrentUser() {
        return new ResponseDTO<>(ResultCode.OK, userService.selectCurrentUser());
    }

    @ApiOperation(value = "사용자 비밀번호 변경")
    @PutMapping("update-password")
    public ResponseDTO<Map<String, String>> updateUserPassword(@RequestBody UserChangePasswordDTO changePassword) {
        Map<String, String> map = new HashMap<>();
        if (!changePassword.getNewCredential().equals(changePassword.getNewCredentialConfirm())) {
            return new ResponseDTO<>(ResultCode.BAD_REQUEST);
        }
        log.info("changePassword::{}",changePassword.toString());
        UserSimpleDTO currentUser = currentUserService.getCurrentUser();
        try {
//            UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
//            log.info("userChangePasswordDTO::{}",userChangePasswordDTO.toString());


            log.info("changePassword.getNewCredential()::{}",changePassword.getNewCredential());
            log.info("changePassword.getNewCredentialConfirm()::{}",changePassword.getNewCredentialConfirm());
//            userChangePasswordDTO.setNewCredential(changePassword.getNewCredential());
//            userChangePasswordDTO.setNewCredentialConfirm(changePassword.getNewCredentialConfirm());
            userService.updateUserPassword(changePassword);
            map.put(RESULT_STRING, CommonConstants.YES);
            map.put("lastPasswordChangeDate", DateUtils.getDateStrFronTimeStamp(String.valueOf(System.currentTimeMillis() )));
        } catch (Exception e) {
            map.put(RESULT_STRING, CommonConstants.NO);
        }

        return new ResponseDTO<>(ResultCode.OK, map);
    }

    @ApiOperation(value = "사용자 정보 업데이트")
    @PutMapping
    public ResponseDTO<Map<String, String>> userUpdate(@Valid @RequestBody UserChangeDTO user) {

        Map<String, String> token = new HashMap<>();
        try {
            userService.updateUser(user);
            token.put(RESULT_STRING, CommonConstants.YES);
        } catch (Exception e) {
            token.put(RESULT_STRING, CommonConstants.NO);
        }

        return new ResponseDTO<>(ResultCode.OK, token);
    }
    @ApiOperation(value = "로그인한 사용자 추가")
    @PostMapping("")
    public ResponseDTO<NoResultDTO> createUser(@Valid @RequestBody UserCreateDTO createUser) {
        log.info("user.toString()::{}",createUser.toString());
        ResultCode resultCode = ResultCode.OK;
        int cnt = userService.createUser(createUser);

        if(cnt == 0){
            resultCode = ResultCode.OK;
        }else if(cnt == 1){
            resultCode = ResultCode.USER_EXIST_ALREADY;
        }else if(cnt == 3){
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, null);

    }

    @ApiOperation(value = "사용자 조회 상세")
    @GetMapping(value = "{userId}")
    public ResponseDTO<UserDetailsDTO> selectUserDetail(@ApiParam(value = "userId", required = true, example = "653e297b-5d10-4982-bf1e-d1114415ac97") @PathVariable String userId) {
        return new ResponseDTO<>(ResultCode.OK, userService.selectUserDetail(userId));
    }

    @ApiOperation(value = "사용자 탈퇴")
    @DeleteMapping("{userId}")
    public ResponseDTO<Map<String, String>> userWithdraw(@ApiParam(value = "userId", required = true, example = "653e297b-5d10-4982-bf1e-d1114415ac97") @PathVariable String userId) {
        userService.withdraw(userId);
        return new ResponseDTO<>(ResultCode.OK);
    }
}
