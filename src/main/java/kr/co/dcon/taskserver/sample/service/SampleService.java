package kr.co.dcon.taskserver.sample.service;


import com.google.gson.Gson;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.sample.dto.SampleDTO;
import kr.co.dcon.taskserver.sample.dto.SampleListDTO;
import kr.co.dcon.taskserver.sample.dto.SampleListReqDTO;
import kr.co.dcon.taskserver.sample.dto.SampleReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
@Slf4j
public class SampleService {

    CurrentUserService currentUserService;
    @Value("${taskserver.url}")
    private String sampleUrl;

    public SampleService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    private static final String RESULT = "result";


    public ResponseDTO<SampleListDTO> selectSampleList(SampleListReqDTO reqDTO) {

        SampleListDTO sampleListDTO = new SampleListDTO();
        String userName = currentUserService.getCurrentUser().getUserName();
        String userEmail = currentUserService.getCurrentUser().getUserEmail();
        reqDTO.setUserEmail(userEmail);
        reqDTO.setUserName(userName);

        String url = sampleUrl + "/sample/list";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<SampleListDTO>>() {
        });

    }


    public ResponseDTO<SampleDTO> selectSampleDetail(SampleListReqDTO reqDTO) {
        String url = sampleUrl + "/sample/detail";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<SampleDTO>>() {
        });

    }

    public boolean sampleInsert(SampleReqDTO reqDTO) {

        boolean result = true;

        Map<String, Object> memberMap = this.memberSampleCount(reqDTO).getResultData();

        int memberCount = Integer.parseInt(memberMap.get(RESULT).toString());

        if (memberCount > 0) {

            throw new RuntimeExceptionBase(ResultCode.USER_EXISTS_EXCEPTION);

        } else {

            String memberInsertResult = this.insertSample(reqDTO).getResultData().get(RESULT).toString();
            if ("N".equals(memberInsertResult)) {
                result = false;
            }
        }


        return result;
    }

    public ResponseDTO<Map<String, Object>> memberSampleCount(SampleReqDTO reqDTO) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        String url = sampleUrl + "/sample/sampleUserCount";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<Map<String, Object>>>() {
        });

    }

    public ResponseDTO<Map<String, Object>> insertSample(SampleReqDTO reqDTO) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));

        String url = sampleUrl + "/sample/insert";
        return RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());

    }

    public boolean sampleUpdate(SampleReqDTO reqDTO) {
        boolean result = true;

        Map<String, Object> memberMap = this.memberSampleCount(reqDTO).getResultData();
        int memberCount = Integer.parseInt(memberMap.get(RESULT).toString());
        if (memberCount > 0) {
            String url = sampleUrl + "/sample/update";
            RestTemplateUtil.putForResponseDTO(url, reqDTO);

        } else {
            throw new RuntimeExceptionBase(ResultCode.USER_NOT_EXISTS_EXCEPTION);
        }
        return result;
    }

    public ResponseDTO<Boolean> deleteSample(String userEmail) {
        // public void deleteSample(String userEmail) {
        SampleReqDTO reqDTO = new SampleReqDTO();
        reqDTO.setUserEmail(userEmail);
        Map<String, Object> memberMap = this.memberSampleCount(reqDTO).getResultData();
        int memberCount = Integer.parseInt(memberMap.get(RESULT).toString());
        if (memberCount > 0) {
            String url = sampleUrl + "/sample/delete?userEmail=" + userEmail;
            //   RestTemplateUtil.putForResponseDTO(url, reqDTO);
            return RestTemplateUtil.getForResponseDTO(url, new ParameterizedTypeReference<ResponseDTO<Boolean>>() {
            });

        } else {
            throw new RuntimeExceptionBase(ResultCode.USER_NOT_EXISTS_EXCEPTION);
        }

    }
    /*



     */

}

