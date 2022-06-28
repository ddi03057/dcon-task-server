package kr.co.dcon.taskserver.code.service;

import kr.co.dcon.taskserver.code.dto.CodeListDTO;
import kr.co.dcon.taskserver.code.dto.CodeReqDTO;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CodeService {
    @Value("${taskserver.url}")
    private String taskUrl;

    public ResponseDTO<List<CodeListDTO>> selectCodeList(CodeReqDTO reqDTO) {
        String url = taskUrl + "/code/codeList/" + reqDTO.getCodeGroupId();
        return RestTemplateUtil.getForResponseDTO(url, new ParameterizedTypeReference<ResponseDTO<List<CodeListDTO>>>() {
        });
    }
}
