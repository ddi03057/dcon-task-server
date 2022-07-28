package kr.co.dcon.taskserver.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.NoResultDTO;
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
    public ResponseDTO<NoResultDTO> updateProjectTaskStatus(@Valid ProjectTaskUpdateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.updateProjectTaskStatus(reqDTO), null);
    }

    @ApiOperation(value = "task assign 수정", notes = "task assign")
    @GetMapping("/taskAssign/{projectId}/{taskId}")
    public ResponseDTO<NoResultDTO> updateProjectTaskAssign(@Valid ProjectTaskUpdateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.updateProjectTaskAssign(reqDTO), null);
    }

    @ApiOperation(value = "task insert", notes = "task insert")
    @PostMapping("/taskAdd")
    public ResponseDTO<NoResultDTO> insertTask(@Valid @RequestBody ProjectTaskCreateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.insertTask(reqDTO), null);
    }

    @ApiOperation(value = "task detail", notes = "task detail")
    @GetMapping("/taskDetail")
    public ResponseDTO<ProjectTaskDetailDTO> selectTaskDetail(@Valid ProjectTaskDetailReqDTO reqDTO) {
        return projectService.selectTaskDetail(reqDTO);
    }

    @ApiOperation(value = "task update", notes = "task update")
    @PutMapping(value = "/taskDetail/update")
    public ResponseDTO<NoResultDTO> updateProjectTaskDetail(@Valid @RequestBody ProjectTaskUpdateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.updateProjectTaskDetail(reqDTO), null);
    }

    @ApiOperation(value = "task Status 수정", notes = "task Status")
    @PutMapping("/taskStatus/{projectId}/taskList/{taskStatus}")
    public ResponseDTO<NoResultDTO> updateProjectTaskListStatus(@RequestBody @Valid ProjectTaskListUpdateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.updateProjectTaskListStatus(reqDTO), null);
    }

    @ApiOperation(value = "project user list", notes = "task detail")
    @GetMapping("/projectUserList")
    public ResponseDTO<List<ProjectUserListDTO>> selectProjectUserList(@Valid ProjectUserListDTO reqDTO) {
        return projectService.selectProjectUserList(reqDTO);
    }

    @ApiOperation(value = "delete task list", notes = "task detail")
    @DeleteMapping("/taskList")
    public ResponseDTO<NoResultDTO> deleteTaskList(@Valid ProjectTaskListUpdateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.deleteTaskList(reqDTO), null);
    }

    @ApiOperation(value = "project insert", notes = "project insert")
    @PostMapping("/projectAdd")
    public ResponseDTO<NoResultDTO> insertProject(@Valid @RequestBody ProjectCreateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.insertProject(reqDTO), null);
    }

    @ApiOperation(value = "project detail", notes = "task detail")
    @GetMapping("/projectDetail")
    public ResponseDTO<ProjectDetailDTO> selectProjectDetail(@Valid ProjectDetailReqDTO reqDTO) {
        return projectService.selectProjectDetail(reqDTO);
    }

    @ApiOperation(value = "project update", notes = "project insert")
    @PutMapping("/{projectId}")
    public ResponseDTO<NoResultDTO> updateProject(@Valid @RequestBody ProjectUpdateReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.updateProject(reqDTO), null);
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
    public ResponseDTO<NoResultDTO> updateTaskComment(@Valid @RequestBody ProjectCommentCRUDReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.updateTaskComment(reqDTO), null);
    }

    @ApiOperation(value = "comment add", notes = "comment add")
    @PostMapping("/taskComment/insert/{tasskId}")
    public ResponseDTO<NoResultDTO> insertTaskComment(@Valid @RequestBody ProjectCommentCRUDReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.insertTaskComment(reqDTO), null);
    }

    @ApiOperation(value = "task comment delete", notes = "task comment delete")
    @DeleteMapping("/taskComment/delete/{taskId}")
    public ResponseDTO<NoResultDTO> deleteTaskComment(@Valid ProjectCommentCRUDReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.deleteTaskComment(reqDTO), null);
    }

    @ApiOperation(value = "task sub item add", notes = "task sub item add")
    @PostMapping("/taskSubItem/insert/{tasskId}")
    public ResponseDTO<NoResultDTO> insertTaskSubItem(@Valid @RequestBody ProjectSubItemCRUDReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.insertTaskSubItem(reqDTO), null);
    }

    @ApiOperation(value = "task subItem list", notes = "task subItem list")
    @GetMapping("/taskSubItem/list/{taskId}")
    public ResponseDTO<List<ProjectTaskSubItemListDTO>> selectIssueList(@Valid ProjectTaskCommentListReqDTO reqDTO) {
        return projectService.selectTaskSubList(reqDTO);
    }

    @ApiOperation(value = "task sub item update", notes = "task sub item update")
    @PutMapping("/task/subItemList/update/{taskSubId}")
    public ResponseDTO<ProjectSubItemCRUDReqDTO> updateTaskSubList(@Valid @RequestBody ProjectSubItemCRUDReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.updateTaskSubList(reqDTO), null);
    }

    @ApiOperation(value = "task sub item delete", notes = "task sub item delete")
    @DeleteMapping("/task/subItemList/delete/{taskSubId}")
    public ResponseDTO<NoResultDTO> deleteTaskSubList(@Valid ProjectSubItemCRUDReqDTO reqDTO) {
        return new ResponseDTO<>(projectService.deleteTaskSubList(reqDTO), null);
    }

    @ApiOperation(value = "task prefix", notes = "task Status")
    @GetMapping("/taskPrefixExist")
    public ResponseDTO<ProjectDetailDTO> taskPrefixExist(@Valid ProjectPrefixExistReqDTO reqDTO) {
        return projectService.selectTaskPrefixProject(reqDTO);
    }
}