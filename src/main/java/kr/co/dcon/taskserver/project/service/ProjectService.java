package kr.co.dcon.taskserver.project.service;

import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.project.dto.ProjectListDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListReqDTO;
import kr.co.dcon.taskserver.project.dto.ProjectPutReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProjectService {
    @Value("${taskserver.url}")
    private String taskUrl;

    public ResponseDTO<List<ProjectListDTO>> selectIssueList(ProjectListReqDTO reqDTO) {
        String url = taskUrl + "/project/issueList/";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<ProjectListDTO>>>() {
        });
    }

    public ResponseDTO<ProjectPutReqDTO> updateProjectStatus(ProjectPutReqDTO reqDTO) {
        Map<String, Object> result = new HashMap<>();
        String url = taskUrl + "/project/taskStatus/"+reqDTO.getTaskStatus();

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectPutReqDTO>>() {
        });
    }

    public ResponseDTO<ProjectPutReqDTO> updateProjectTaskAssign(ProjectPutReqDTO reqDTO) {
        Map<String, Object> result = new HashMap<>();
        String url = taskUrl + "/project/taskAssign/"+reqDTO.getTaskId();

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<ProjectPutReqDTO>>() {
        });
    }
}
