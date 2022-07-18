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
import java.util.Map;

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

    public ResponseDTO<List<ProjectUserListDTO>> selectProjectUserList(ProjectUserListDTO reqDTO) {
        String url = taskUrl + "/project/projectUserList/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectUserListDTO>>>() {
        });
    }

    public boolean deleteTaskList(ProjectTaskListUpdateReqDTO reqDTO) {

        String url = taskUrl + "/project/deleteTaskList/";
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        boolean result = true;
        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
        } catch (Exception e) {
            throw new RuntimeExceptionBase(ResultCode.ETC_ERROR);
        }
        return result;
    }

    public ResponseDTO<ProjectCreateReqDTO> insertProject(ProjectCreateReqDTO reqDTO) {
        reqDTO.setCreateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/projectAdd/";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        return RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
    }

    public ResponseDTO<ProjectDetailDTO> selectProjectDetail(ProjectDetailReqDTO reqDTO) {
        String url = taskUrl + "/project/projectDetail/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectDetailDTO>>() {
        });
    }

    public boolean updateProject(ProjectUpdateReqDTO reqDTO) {
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        boolean result = true;

        String url = taskUrl + "/project/"+reqDTO.getProjectId();

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
        } catch (Exception e) {
            throw new RuntimeExceptionBase(ResultCode.ETC_ERROR);
        }
        return result;
    }

    public ResponseDTO<String> selectUserFirstProjectId(String userId) {

        String url = taskUrl + "/project/userFirstProjectId/"+userId;
        return RestTemplateUtil.getForResponseDTO(url, new ParameterizedTypeReference<ResponseDTO<String>>() {
        });
    }

    public ResponseDTO<List<ProjectTaskCommentListDTO>> selectTaskCommentList(ProjectTaskCommentListReqDTO reqDTO) {
        String url = taskUrl + "/project/taskDetail/commentList";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectTaskCommentListDTO>>>() {
        });
    }

    public boolean updateTaskComment(ProjectCommentCRUDReqDTO reqDTO) {
        boolean result = false;

        String url = taskUrl + "/project/taskComment/update/"+reqDTO.getTaskId();

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
            result = true;
        } catch (Exception e) {
            result = false;
            throw new RuntimeExceptionBase(ResultCode.ETC_ERROR);
        }
        return result;
    }

    public ResponseDTO<ProjectCommentCRUDReqDTO> insertTaskComment(ProjectCommentCRUDReqDTO reqDTO) {

        String url = taskUrl + "/project/taskComment/insert";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        return RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
    }

    public boolean  deleteTaskComment(ProjectCommentCRUDReqDTO reqDTO) {
        boolean result = false;
        String url = taskUrl + "/project/taskComment/delete/"+reqDTO.getTaskId();

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
            result = true;
        } catch (Exception e) {
            result = false;
            throw new RuntimeExceptionBase(ResultCode.ETC_ERROR);
        }
       return result ;
    }

    public ResponseDTO<ProjectSubItemCRUDReqDTO> insertTaskSubItem(ProjectSubItemCRUDReqDTO reqDTO) {
        String url = taskUrl + "/project/taskSubItem/insert";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        reqDTO.setUserId(currentUserService.getCurrentUser().getUserId());
        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        return RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
    }
}
