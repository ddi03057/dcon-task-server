package kr.co.dcon.taskserver.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.project.dto.ProjectListDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListReqDTO;
import kr.co.dcon.taskserver.project.dto.ProjectPutReqDTO;
import kr.co.dcon.taskserver.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "PROJECT API")
@RequestMapping(value = "/api/v1/project")
@RestController
public class ProjectController {

    ProjectService projectService;

    private final String RESULT_STRING = "result";

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;

    }

    @ApiOperation(value = "issueList", notes = "issueList")
    @GetMapping("/issueList")
    public ResponseDTO<List<ProjectListDTO>> selectIssueList(@Valid ProjectListReqDTO reqDTO) {
        return projectService.selectIssueList(reqDTO);

    }

    @ApiOperation(value = "task Status 수정", notes = "task Status")
    @GetMapping("/taskStatus/{taskStatus}")
    public ResponseDTO<ProjectPutReqDTO> updateProjectStatus(@Valid ProjectPutReqDTO reqDTO,
                                                             @ApiParam(value = "taskStatus", required = true, example = "open") @PathVariable String taskStatus) {
        Map<String, Object> result = new HashedMap<>();
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.updateProjectStatus(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);

    }

    @ApiOperation(value = "task assign 수정", notes = "task assign")
    @GetMapping("/taskAssign/{taskId}")
    public ResponseDTO<ProjectPutReqDTO> updateProjectTaskAssign(@Valid ProjectPutReqDTO reqDTO,
                                                             @ApiParam(value = "taskId", required = true, example = "task_1") @PathVariable String taskId) {
        Map<String, Object> result = new HashedMap<>();
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
}
