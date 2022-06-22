package kr.co.dcon.taskserver.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.auth.dto.UserSimpleDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.CommonConstants;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.NoResultDTO;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.util.DateUtils;
import kr.co.dcon.taskserver.user.dto.UserChangeDTO;
import kr.co.dcon.taskserver.user.dto.UserChangePasswordDTO;
import kr.co.dcon.taskserver.user.dto.UserCreateDTO;
import kr.co.dcon.taskserver.user.dto.UserDTO;
import kr.co.dcon.taskserver.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "USER API")
@RequestMapping(value = "/api/v1/user")
@RestController
public class UserController {

    UserService userService;

    private CurrentUserService currentUserService;

    private final String RESULT_STRING = "result";

    public UserController(CurrentUserService currentUserService, UserService userService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @ApiOperation(value = "로그인한 사용자 최신 정보 조회")
    @GetMapping("user-info")
    public ResponseDTO<UserDTO> getCurrentUser() throws Exception {
        return new ResponseDTO<>(ResultCode.OK, userService.selectCurrentUser());
    }

    @ApiOperation(value = "사용자 비밀번호 변경")
    @PutMapping("update-password")
    public ResponseDTO<Map<String, Object>> updateUserPassword(@RequestBody UserChangePasswordDTO changePassword) {

        Map<String, Object> result = new HashedMap<>();
        if (!changePassword.getNewCredential().equals(changePassword.getNewCredentialConfirm())) {
            return new ResponseDTO<>(ResultCode.BAD_REQUEST);
        }
        try {
            result.put(RESULT_STRING, ResultCode.OK);
            result = userService.updateUserPassword(changePassword);
            result.put("lastPasswordChangeDate", DateUtils.getDateStrFronTimeStamp(String.valueOf(System.currentTimeMillis())));
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            result.put(RESULT_STRING, ResultCode.USER_NOT_AVAILABLE_EXCEPTION);
        }
        catch (Exception e) {
            result.put(RESULT_STRING, ResultCode.ETC_ERROR);
        }

        return new ResponseDTO<>(ResultCode.OK, result);
    }

    @ApiOperation(value = "사용자 정보 업데이트")
    @PutMapping
    public ResponseDTO<Map<String, Object>> userUpdate(@Valid @RequestBody UserChangeDTO user) {

        Map<String, Object> result = new HashMap<>();
        try {
            result.put(RESULT_STRING, userService.updateUser(user));
        } catch (Exception e) {
            result.put(RESULT_STRING, ResultCode.ETC_ERROR);
        }

        return new ResponseDTO<>(ResultCode.OK, result);
    }

    @ApiOperation(value = "로그인한 사용자 추가")
    @PostMapping("")
    public ResponseDTO<Map<String, Object>> createUser(@Valid @RequestBody UserCreateDTO createUser) {

        ResultCode resultCode = ResultCode.OK;
        return new ResponseDTO<>(resultCode, userService.createUser(createUser));
    }

    @ApiOperation(value = "사용자 조회 상세")
    @GetMapping(value = "{userId}")
    public ResponseDTO<UserDetailsDTO> selectUserDetail(@ApiParam(value = "userId", required = true, example = "653e297b-5d10-4982-bf1e-d1114415ac97") @PathVariable String userId) throws Exception {
        return new ResponseDTO<>(ResultCode.OK, userService.selectUserDetail(userId));
    }

    @ApiOperation(value = "사용자 탈퇴")
    @DeleteMapping("{userId}")
    public ResponseDTO<Map<String, String>> userWithdraw(@ApiParam(value = "userId", required = true, example = "653e297b-5d10-4982-bf1e-d1114415ac97") @PathVariable String userId) throws Exception{
        userService.withdraw(userId);
        return new ResponseDTO<>(ResultCode.OK);
    }

    @ApiOperation(value = "사용자 리스트")
    @GetMapping("allUser/{userId}")
    public ResponseDTO<Map<String, Object>> selectUserList(@ApiParam(value = "userId", required = true, example = "20878cc7-4397-4d26-8269-73cd220c95a3") @PathVariable String userId) throws Exception {
        return new ResponseDTO<>(ResultCode.OK, userService.selectUserList(userId));
    }
}
