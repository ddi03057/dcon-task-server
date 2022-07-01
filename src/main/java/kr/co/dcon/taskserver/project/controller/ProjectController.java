package kr.co.dcon.taskserver.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.project.dto.ProjectListDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListReqDTO;
import kr.co.dcon.taskserver.project.dto.ProjectTaskCreateReqDTO;
import kr.co.dcon.taskserver.project.dto.ProjectTaskUpdateReqDTO;
import kr.co.dcon.taskserver.project.service.ProjectService;
import kr.co.dcon.taskserver.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(value = "PROJECT API")
@RequestMapping(value = "/api/v1/project")
@RestController
public class ProjectController {

    ProjectService projectService;
    UserService userService;


    public ProjectController(ProjectService projectService,UserService userService) {
        this.projectService = projectService;
        this.userService = userService;

    }

    @ApiOperation(value = "issueList", notes = "issueList")
    @GetMapping("/issueList")
    public ResponseDTO<List<ProjectListDTO>> selectIssueList(@Valid ProjectListReqDTO reqDTO) {
        return projectService.selectIssueList(reqDTO);

    }

    @ApiOperation(value = "task Status 수정", notes = "task Status")
    @GetMapping("/taskStatus/{projectId}/{taskId}/{taskStatus}")
    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskStatus(@Valid ProjectTaskUpdateReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.updateProjectTaskStatus(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);

    }

    @ApiOperation(value = "task assign 수정", notes = "task assign")
    @GetMapping("/taskAssign/{projectId}/{taskId}")

    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskAssign(@Valid ProjectTaskUpdateReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.updateProjectTaskAssign(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);

    }
    @ApiOperation(value = "task insert", notes = "task insert")
    @PostMapping("/taskAdd")
    public ResponseDTO<ProjectTaskCreateReqDTO> insertTaskinsert(@Valid @RequestBody ProjectTaskCreateReqDTO reqDTO){
        ResultCode resultCode = ResultCode.OK;
        log.info("controller reqDTO.toString(){}",reqDTO.toString());
        try {
            projectService.insertTask(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);

    }
}
