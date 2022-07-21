package kr.co.dcon.taskserver.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.project.dto.*;
import kr.co.dcon.taskserver.project.service.ProjectService;
import kr.co.dcon.taskserver.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "PROJECT API")
@RequestMapping(value = "/api/v1/project")
@RestController
public class ProjectController {

    ProjectService projectService;
    UserService userService;

    private final String RESULT_STRING = "result";

    public ProjectController(ProjectService projectService, UserService userService) {
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
    public ResponseDTO<ProjectTaskCreateReqDTO> insertTask(@Valid @RequestBody ProjectTaskCreateReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.insertTask(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "task detail", notes = "task detail")
    @GetMapping("/taskDetail")
    public ResponseDTO<ProjectTaskDetailDTO> selectTaskDetail(@Valid ProjectTaskDetailReqDTO reqDTO) {
        return projectService.selectTaskDetail(reqDTO);
    }

    @ApiOperation(value = "task update", notes = "task update")
    @PutMapping(value = "/taskDetail/update")
    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskDetail(@Valid @RequestBody ProjectTaskUpdateReqDTO reqDTO) {
        ResultCode resultCode = null;
        try {
            projectService.updateProjectTaskDetail(reqDTO);
            resultCode = ResultCode.OK;
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);

    }

    @ApiOperation(value = "task Status 수정", notes = "task Status")
    @PutMapping("/taskStatus/{projectId}/taskList/{taskStatus}")
    public ResponseDTO<ProjectTaskListUpdateReqDTO> updateProjectTaskListStatus(@RequestBody @Valid ProjectTaskListUpdateReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.updateProjectTaskListStatus(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "project user list", notes = "task detail")
    @GetMapping("/projectUserList")
    public ResponseDTO<List<ProjectUserListDTO>> selectProjectUserList(@Valid ProjectUserListDTO reqDTO) {
        return projectService.selectProjectUserList(reqDTO);
    }

    @ApiOperation(value = "delete task list", notes = "task detail")
    @DeleteMapping("/taskList")
    public ResponseDTO<ProjectTaskListUpdateReqDTO> deleteTaskList(@Valid ProjectTaskListUpdateReqDTO reqDTO) {
        ResultCode resultCode = null;
        try {
            projectService.deleteTaskList(reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "project insert", notes = "project insert")
    @PostMapping("/projectAdd")
    public ResponseDTO<ProjectCreateReqDTO> insertTaskinsert(@Valid @RequestBody ProjectCreateReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.insertProject(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "project detail", notes = "task detail")
    @GetMapping("/projectDetail")
    public ResponseDTO<ProjectDetailDTO> selectProjectDetail(@Valid ProjectDetailReqDTO reqDTO) {
        return projectService.selectProjectDetail(reqDTO);
    }

    @ApiOperation(value = "project update", notes = "project insert")
    @PutMapping("/{projectId}")
    public ResponseDTO<ProjectUpdateReqDTO> updateProject(@Valid @RequestBody ProjectUpdateReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.updateProject(reqDTO);

        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "userFirstProjectId", notes = "userFirstProjectId")
    @GetMapping("/userFirstProjectId/{userId}")
    public ResponseDTO<String> selectUserFirstProjectId(@ApiParam(value = "userId", required = true, example = "6ef8ef43-428c-44a6-9bf3-9e57d90d6610") @PathVariable String userId) {
        return projectService.selectUserFirstProjectId(userId);
    }

    @ApiOperation(value = "project detail", notes = "task detail")
    @GetMapping("/taskDetail/commentList")
    public ResponseDTO<List<ProjectTaskCommentListDTO>> selectTaskCommentList(@Valid ProjectTaskCommentListReqDTO reqDTO) {
        return projectService.selectTaskCommentList(reqDTO);
    }

    @ApiOperation(value = "task comment 수정", notes = "task comment update")
    @PutMapping("/taskComment/update/{taskId}")
    public ResponseDTO<ProjectCommentCRUDReqDTO> updateTaskComment(@Valid @RequestBody ProjectCommentCRUDReqDTO reqDTO) {
        ResultCode resultCode = null;
        boolean result = false;

        result =  projectService.updateTaskComment(reqDTO);
        if(result){
            resultCode = ResultCode.OK;
        }else{
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);

    }

    @ApiOperation(value = "comment add", notes = "comment add")
    @PostMapping("/taskComment/insert/{tasskId}")
    public ResponseDTO<ProjectCommentCRUDReqDTO> insertTaskComment(@Valid @RequestBody ProjectCommentCRUDReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.insertTaskComment(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "task comment delete", notes = "task comment delete")
    @DeleteMapping("/taskComment/delete/{taskId}")
    public ResponseDTO<ProjectCommentCRUDReqDTO> deleteTaskComment(@Valid  ProjectCommentCRUDReqDTO reqDTO) {
        ResultCode resultCode = null;

        boolean result = false;

        result =  projectService.deleteTaskComment(reqDTO);
        if(result){
            resultCode = ResultCode.OK;
        }else{
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);

    }

    @ApiOperation(value = "task sub item add", notes = "task sub item add")
    @PostMapping("/taskSubItem/insert/{tasskId}")
    public ResponseDTO<ProjectSubItemCRUDReqDTO> insertTaskSubItem(@Valid @RequestBody ProjectSubItemCRUDReqDTO reqDTO) {
        ResultCode resultCode = ResultCode.OK;

        try {
            projectService.insertTaskSubItem(reqDTO);
        } catch (RuntimeExceptionBase runtimeExceptionBase) {
            resultCode = ResultCode.USER_NOT_AVAILABLE_EXCEPTION;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "task subItem list", notes = "task subItem list")
    @GetMapping("/taskSubItem/list/{taskId}")
    public ResponseDTO<List<ProjectTaskSubItemListDTO>> selectIssueList(@Valid ProjectTaskCommentListReqDTO reqDTO) {
        return projectService.selectTaskSubList(reqDTO);

    }

    @ApiOperation(value = "task sub item update", notes = "task sub item update")
    @PutMapping("/task/subItemList/update/{taskSubId}")
    public ResponseDTO<ProjectSubItemCRUDReqDTO> updateTaskSubList(@Valid @RequestBody ProjectSubItemCRUDReqDTO reqDTO) {
        ResultCode resultCode = null;
        boolean result = false;

        result =  projectService.updateTaskSubList(reqDTO);
        if(result){
            resultCode = ResultCode.OK;
        }else{
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);

    }

    @ApiOperation(value = "task sub item delete", notes = "task sub item delete")
    @DeleteMapping("/task/subItemList/delete/{taskSubId}")
    public ResponseDTO<ProjectSubItemCRUDReqDTO> deleteTaskSubList(@Valid  ProjectSubItemCRUDReqDTO reqDTO) {
        ResultCode resultCode = null;

        boolean result = false;

        result = projectService.deleteTaskSubList(reqDTO);
        if (result) {
            resultCode = ResultCode.OK;
        } else {
            resultCode = ResultCode.ETC_ERROR;
        }
        return new ResponseDTO<>(resultCode, reqDTO);
    }

    @ApiOperation(value = "task prefix", notes = "task Status")
    @GetMapping("/taskPrefixExist")
    public ResponseDTO<ProjectDetailDTO> taskPrefixExist(@Valid ProjectPrefixExistReqDTO reqDTO) {
        return projectService.selectTaskPrefixProject(reqDTO);
    }
}
