package kr.co.dcon.taskserver.project.service;

import com.google.gson.Gson;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.NoResultDTO;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
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

    public ProjectService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public ResponseDTO<List<ProjectListDTO>> selectIssueList(ProjectListReqDTO reqDTO) {
        String url = taskUrl + "/project/issueList/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectListDTO>>>() {
        });
    }

    public ResultCode updateProjectTaskStatus(ProjectTaskUpdateReqDTO reqDTO) {
        ResultCode resultCode;

        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskStatus/" + reqDTO.getProjectId() + "/" + reqDTO.getTaskId() + "/" + reqDTO.getTaskStatus();

        ResponseDTO<ProjectTaskUpdateReqDTO> result;
        try {
            result = RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {
            });
//
            resultCode = ResultCode.valueOf(result.getCodeName());
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
            log.info("Exception!!");
            commonCatch(e, "updateProjectTaskStatus");
        }
        return resultCode;
    }

    public ResultCode updateProjectTaskAssign(ProjectTaskUpdateReqDTO reqDTO) {
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskAssign/" + reqDTO.getProjectId() + "/" + reqDTO.getTaskId();

        ResponseDTO<ProjectTaskUpdateReqDTO> result;
        ResultCode resultCode;
        try {
            result = RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {});
            resultCode = ResultCode.valueOf(result.getCodeName());
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }

    public ResultCode insertTask(ProjectTaskCreateReqDTO reqDTO) {
        reqDTO.setCreateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskAdd/";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        ResponseDTO<NoResultDTO> result;
        ResultCode resultCode;
        try {
            result = RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
            resultCode = ResultCode.valueOf(result.getCodeName());
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return resultCode;
    }

    public ResponseDTO<ProjectTaskDetailDTO> selectTaskDetail(ProjectTaskDetailReqDTO reqDTO) {
        String url = taskUrl + "/project/taskDetail/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskDetailDTO>>() {
        });
    }

    public ResultCode updateProjectTaskDetail(ProjectTaskUpdateReqDTO reqDTO) {
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());
        String url = taskUrl + "/project/taskDetail/update";

        ResponseDTO<ProjectTaskUpdateReqDTO> result;
        ResultCode resultCode;

        try {
            result = RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {});
            resultCode = ResultCode.valueOf(result.getCodeName());
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return resultCode;
    }

    public ResultCode updateProjectTaskListStatus(ProjectTaskListUpdateReqDTO reqDTO) {
        ResultCode resultCode;

        String url = taskUrl + "/project/taskStatus/" + reqDTO.getProjectId() + "/taskList/" + reqDTO.getTaskStatus();

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }

    public ResponseDTO<List<ProjectUserListDTO>> selectProjectUserList(ProjectUserListDTO reqDTO) {
        String url = taskUrl + "/project/projectUserList/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectUserListDTO>>>() {
        });
    }

    public ResultCode deleteTaskList(ProjectTaskListUpdateReqDTO reqDTO) {

        ResultCode resultCode;
        String url = taskUrl + "/project/deleteTaskList/";
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }

    public ResultCode insertProject(ProjectCreateReqDTO reqDTO) {

        ResultCode resultCode;
        try {
            reqDTO.setCreateId(currentUserService.getCurrentUser().getUserId());
            String url = taskUrl + "/project/projectAdd/";
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

            parameters.add("reqDTO", new Gson().toJson(reqDTO));

            RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
            resultCode = ResultCode.OK;

        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return resultCode;
    }

    public ResponseDTO<ProjectDetailDTO> selectProjectDetail(ProjectDetailReqDTO reqDTO) {
        String url = taskUrl + "/project/projectDetail/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectDetailDTO>>() {
        });
    }

    public ResultCode updateProject(ProjectUpdateReqDTO reqDTO) {
        ResultCode resultCode;
        reqDTO.setUpdateId(currentUserService.getCurrentUser().getUserId());

        String url = taskUrl + "/project/" + reqDTO.getProjectId();
        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }

    public ResponseDTO<String> selectUserFirstProjectId(String userId) {

        String url = taskUrl + "/project/userFirstProjectId/" + userId;
        return RestTemplateUtil.getForResponseDTO(url, new ParameterizedTypeReference<ResponseDTO<String>>() {
        });
    }

    public ResponseDTO<List<ProjectTaskCommentListDTO>> selectTaskCommentList(ProjectTaskCommentListReqDTO reqDTO) {
        String url = taskUrl + "/project/taskDetail/commentList";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectTaskCommentListDTO>>>() {
        });
    }

    public ResultCode updateTaskComment(ProjectCommentCRUDReqDTO reqDTO) {
        ResultCode resultCode;

        String url = taskUrl + "/project/taskComment/update/" + reqDTO.getTaskId();

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }

    public ResultCode insertTaskComment(ProjectCommentCRUDReqDTO reqDTO) {

        ResultCode resultCode;
        String url = taskUrl + "/project/taskComment/insert";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        try {
            RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return resultCode;
    }

    public ResultCode deleteTaskComment(ProjectCommentCRUDReqDTO reqDTO) {
        ResultCode resultCode;
        String url = taskUrl + "/project/taskComment/delete/" + reqDTO.getTaskId();

        try {
            RestTemplateUtil.putForResponseDTO(url, reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }

    public ResultCode insertTaskSubItem(ProjectSubItemCRUDReqDTO reqDTO) {
        ResultCode resultCode;
        String url = taskUrl + "/project/taskSubItem/insert";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        reqDTO.setUserId(currentUserService.getCurrentUser().getUserId());
        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        try {
            RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }

        return resultCode;
    }

    public ResponseDTO<List<ProjectTaskSubItemListDTO>> selectTaskSubList(ProjectTaskCommentListReqDTO reqDTO) {
        String url = taskUrl + "/project/task/subItemList/" + reqDTO.getTaskId();
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectTaskSubItemListDTO>>>() {
        });
    }

    public ResultCode updateTaskSubList(ProjectSubItemCRUDReqDTO reqDTO) {
        ResultCode resultCode;

        String url = taskUrl + "/project/task/subItemList/update/" + reqDTO.getTaskSubId();

        try {
            RestTemplateUtil.putForResponseDTO(reqDTO.getUrlToForward(url), reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }
    public ResultCode deleteTaskSubList(ProjectSubItemCRUDReqDTO reqDTO) {
        ResultCode resultCode;
        String url = taskUrl + "/project/task/subItemList/delete/" + reqDTO.getTaskSubId();

        try {
            RestTemplateUtil.putForResponseDTO(reqDTO.getUrlToForward(url), reqDTO);
            resultCode = ResultCode.OK;
        } catch (Exception e) {
            resultCode = ResultCode.ETC_ERROR;
        }
        return resultCode;
    }

    public ResponseDTO<ProjectDetailDTO> selectTaskPrefixProject(ProjectPrefixExistReqDTO reqDTO) {
        String url = taskUrl + "/project/taskPrefixExist/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectDetailDTO>>() {
        });
    }

    public void commonCatch(Exception e, String method) {

        log.error("method()::{}", method);
        log.error("getMessage()::{}", e.getMessage());
        StackTraceElement[] elem = e.getStackTrace();
        for (int i = 0; i < elem.length; i++) {
            log.error(elem[i].toString());
            for (StackTraceElement stackTraceElement : elem) {
                log.error(stackTraceElement.toString());
            }
        }
    }


}
