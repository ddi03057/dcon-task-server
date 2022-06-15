package kr.co.dcon.taskserver.user.controller;

import io.swagger.annotations.ApiParam;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

        UserSimpleDTO currentUser = currentUserService.getCurrentUser();
        try {
            UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
            log.info("userChangePasswordDTO::{}",userChangePasswordDTO.toString());

            if (StringUtils.isEmpty(changePassword.getUserId())) {
                userChangePasswordDTO.setUserId(currentUser.getUserId());
            }else {
                log.info("changePassword.getUserId()::{}",changePassword.getUserId());
                userChangePasswordDTO.setUserId(changePassword.getUserId());
            }
            log.info("changePassword.getNewCredential()::{}",changePassword.getNewCredential());
            log.info("changePassword.getNewCredentialConfirm()::{}",changePassword.getNewCredentialConfirm());
            userChangePasswordDTO.setNewCredential(changePassword.getNewCredential());
            userChangePasswordDTO.setNewCredentialConfirm(changePassword.getNewCredentialConfirm());
            userService.updateUserPassword(userChangePasswordDTO);
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
            userService.updateUser(user.getUserId(), user);
            token.put(RESULT_STRING, CommonConstants.YES);
        } catch (Exception e) {
            token.put(RESULT_STRING, CommonConstants.NO);
        }

        return new ResponseDTO<>(ResultCode.OK, token);
    }
    @ApiOperation(value = "로그인한 사용자 추가")
    @PostMapping("")
    public ResponseDTO<NoResultDTO> createUser(@Valid @RequestBody UserCreateDTO user) {

        try {
            userService.createUser(user);
        } catch (Exception e) {
            new ResponseDTO<>(ResultCode.ETC_ERROR);
        }
        return new ResponseDTO<>(ResultCode.OK);
    }
}
