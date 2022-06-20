package kr.co.dcon.taskserver.sample.service;

//import com.dcon.dcontaskserver.auth.service.CurrentUserService;

import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.sample.dto.*;
import kr.co.dcon.taskserver.sample.mapper.SampleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SampleService {
    @Autowired
    SampleMapper sampleMapper;

    @Autowired
    CurrentUserService currentUserService;
    @Value("${taskserver.url}")
    private String sampleUrl;

    public ResponseDTO<SampleListDTO> selectSampleList(SampleListReqDTO reqDTO) {
        log.info("req::{}", reqDTO.toString());
        log.info("currentUserService::{}", currentUserService.getCurrentUser().toString());
        SampleListDTO sampleListDTO = new SampleListDTO();
        String userName = currentUserService.getCurrentUser().getUserName();
        String userEmail = currentUserService.getCurrentUser().getUserEmail();
        reqDTO.setUserEmail(userEmail);
        reqDTO.setUserName(userName);

        String url = sampleUrl+"/sample/list";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<SampleListDTO>>() {});

    }


    public ResponseDTO<SampleDTO> selectSampleDetail(SampleListReqDTO reqDTO) {
        String url = sampleUrl+"/sample/detail";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<SampleDTO>>() {});

    }
/*
    public void sampleInsert(SampleReqDTO reqDTO) {
        try {
            sampleMapper.sampleInsert(reqDTO);
        } catch (Exception e) {
            throw new RuntimeExceptionBase(ResultCode.ETC_ERROR);
        }
    }

    public int sampleUpdate(SampleReqDTO reqDTO) {
        int updateCnt = 1;
        try {
            sampleMapper.sampleUpdate(reqDTO);
        } catch (Exception e) {
            updateCnt = 0;
        }
        return updateCnt;
    }

    public void deleteSample(String id) {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("id", id);
        sampleMapper.deleteSample(paramMap);
    }
*/

}

