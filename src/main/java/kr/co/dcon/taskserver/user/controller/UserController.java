package kr.co.dcon.taskserver.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.auth.dto.UserDetailsDTO;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.NoResultDTO;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.util.DateUtils;
import kr.co.dcon.taskserver.user.dto.*;
import kr.co.dcon.taskserver.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
    public ResponseDTO<NoResultDTO> updateUserPassword(@RequestBody UserChangePasswordDTO changePassword) {
        return new ResponseDTO<>(userService.updateUserPassword(changePassword), null);
    }

    @ApiOperation(value = "사용자 정보 업데이트")
    @PutMapping
    public ResponseDTO<NoResultDTO> userUpdate(@Valid @RequestBody UserChangeDTO user) {
        return new ResponseDTO<>(userService.updateUser(user), null);
    }

    @ApiOperation(value = "로그인한 사용자 추가")
    @PostMapping("")
    public ResponseDTO<NoResultDTO> createUser(@Valid @RequestBody UserCreateDTO createUser) {
        return new ResponseDTO<>(userService.createUser(createUser), null);
    }

    @ApiOperation(value = "사용자 조회 상세")
    @GetMapping(value = "{userId}")
    public ResponseDTO<UserDetailsDTO> selectUserDetail(@ApiParam(value = "userId", required = true, example = "653e297b-5d10-4982-bf1e-d1114415ac97") @PathVariable String userId) throws Exception {
        return new ResponseDTO<>(ResultCode.OK, userService.selectUserDetail(userId));
    }

    @ApiOperation(value = "사용자 탈퇴")
    @DeleteMapping("{userId}")
    public ResponseDTO<NoResultDTO> userWithdraw(@ApiParam(value = "userId", required = true, example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6611") @PathVariable String userId) throws Exception{
        return new ResponseDTO<>(userService.withdraw(userId), null);
    }

    @ApiOperation(value = "사용자 리스트")
    @GetMapping("allUser/{userId}")
    public ResponseDTO<List<UserRepresentation>> selectUserList(@ApiParam(value = "userId", required = true, example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610") @PathVariable String userId) throws Exception {
        return new ResponseDTO<>(ResultCode.OK, userService.selectUserList(userId));
    }
    @ApiOperation(value = "사용자 리스트")
    @GetMapping("/task/allUser")
    public ResponseDTO<List<UserListDTO>> selectProjectUserList(@Valid UserListProjectReqDTO reqDTO) {
        return new ResponseDTO<>(ResultCode.OK, userService.selectProjectUserList(reqDTO));
    }
    @ApiOperation(value = "사용자 리스트")
    @GetMapping("/task/allUser/{userId}")
    public ResponseDTO<UserListDTO> selectProjectUserDetail(@Valid UserListProjectReqDTO reqDTO) {
        return new ResponseDTO<>(ResultCode.OK, userService.selectProjectUserDetail(reqDTO));
    }
}
