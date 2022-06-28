package kr.co.dcon.taskserver.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListReqDTO;
import kr.co.dcon.taskserver.project.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(value = "PROJECT API")
@RequestMapping(value = "/api/v1/project")
@RestController
public class ProjectController {

    ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;

    }

    @ApiOperation(value = "issueList", notes = "issueList")
    @GetMapping("/issueList")
    public ResponseDTO<List<ProjectListDTO>> selectIssueList(@Valid ProjectListReqDTO reqDTO){
        return  projectService.selectIssueList(reqDTO);

    }

}
