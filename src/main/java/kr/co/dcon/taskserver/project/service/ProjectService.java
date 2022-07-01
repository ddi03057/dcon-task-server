package kr.co.dcon.taskserver.project.service;

import com.google.gson.Gson;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.project.dto.ProjectListDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListReqDTO;
import kr.co.dcon.taskserver.project.dto.ProjectTaskCreateReqDTO;
import kr.co.dcon.taskserver.project.dto.ProjectTaskUpdateReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProjectService {
    @Value("${taskserver.url}")
    private String taskUrl;

    private static final String RESULT = "result";

    public ResponseDTO<List<ProjectListDTO>> selectIssueList(ProjectListReqDTO reqDTO) {
        String url = taskUrl + "/project/issueList/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectListDTO>>>() {
        });
    }

    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskStatus(ProjectTaskUpdateReqDTO reqDTO) {
        Map<String, Object> result = new HashMap<>();
        String url = taskUrl + "/project/taskStatus/"+reqDTO.getProjectId()+"/"+reqDTO.getTaskId()+"/"+reqDTO.getTaskStatus();
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {
        });
    }

    public ResponseDTO<ProjectTaskUpdateReqDTO> updateProjectTaskAssign(ProjectTaskUpdateReqDTO reqDTO) {
        String url = taskUrl + "/project/taskAssign/"+reqDTO.getProjectId()+"/"+reqDTO.getTaskId();

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectTaskUpdateReqDTO>>() {
        });
    }

    public ResponseDTO<ProjectTaskUpdateReqDTO> insertTask(ProjectTaskCreateReqDTO reqDTO) {
        String url = taskUrl + "/project/taskAdd/";
        log.info("service reqDTO.toString(){}",reqDTO.toString());
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        return RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
    }


}
