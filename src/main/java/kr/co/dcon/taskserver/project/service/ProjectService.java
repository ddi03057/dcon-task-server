package kr.co.dcon.taskserver.project.service;

import com.google.gson.Gson;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.project.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@Slf4j
public class ProjectService {
    @Value("${taskserver.url}")
    private String taskUrl;
    private CurrentUserService currentUserService;

    public ProjectService(CurrentUserService currentUserService){
        this.currentUserService = currentUserService;
    }
    public ResponseDTO<List<ProjectListDTO>> selectIssueList(ProjectListReqDTO reqDTO) {
        String url = taskUrl + "/project/issueList/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectListDTO>>>() {
        });
    }

    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskStatus(ProjectTaskUpdateReqDTO reqDTO) {
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskStatus/"+reqDTO.getProjectId()+"/"+reqDTO.getTaskId()+"/"+reqDTO.getTaskStatus();
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {
        });
    }

    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskAssign(ProjectTaskUpdateReqDTO reqDTO) {
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskAssign/"+reqDTO.getProjectId()+"/"+reqDTO.getTaskId();

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {
        });
    }

    public ResponseDTO<ProjectTaskUpdateReqDTO> insertTask(ProjectTaskCreateReqDTO reqDTO) {
        reqDTO.setCreateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskAdd/";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        return RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
    }


    public ResponseDTO<ProjectTaskDetailDTO> selectTaskDetail(ProjectTaskDetailReqDTO reqDTO) {
        String url = taskUrl + "/project/taskDetail/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskDetailDTO>>() {
        });
    }

    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskDetail(ProjectTaskUpdateReqDTO reqDTO) {
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskDetail/update";

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {
        });
    }
    public boolean updateProjectTaskListStatus(ProjectTaskListUpdateReqDTO reqDTO) {
        boolean result = true;

        String url = taskUrl + "/project/taskStatus/"+reqDTO.getProjectId()+"/taskList/"+reqDTO.getTaskStatus();

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
        } catch (Exception e) {
            throw new RuntimeExceptionBase(ResultCode.ETC_ERROR);
        }
        return result;
    }

}
