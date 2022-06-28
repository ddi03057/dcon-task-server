package kr.co.dcon.taskserver.project.service;

import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.menu.dto.MenuTopListDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListDTO;
import kr.co.dcon.taskserver.project.dto.ProjectListReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
