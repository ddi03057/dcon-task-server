package kr.co.dcon.taskserver.testYh.service;


import com.google.gson.Gson;
import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.testYh.dto.TestYhDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhListDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhListReqDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;


@Service
@Slf4j
public class TestYhService {
    CurrentUserService currentUserService;

    @Value("${taskserver.url}")
    private String testYhUrl;

    public TestYhService(CurrentUserService currentUserService){
        this.currentUserService = currentUserService;
    }

    private static final String RESULT = "result";

    public ResponseDTO<TestYhListDTO> selectTestYhList(TestYhListReqDTO reqDTO){
        TestYhListDTO testYhListDTO = new TestYhListDTO();
        String userName = currentUserService.getCurrentUser().getUserName();
        String userEmail = currentUserService.getCurrentUser().getUserEmail();
        reqDTO.setUserEmail(userEmail);
        reqDTO.setUserName(userName);

        String url = testYhUrl + "/testYh/list";

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<TestYhListDTO>>() {
        });
    }


    public ResponseDTO<TestYhDTO> selectTestYhDetail(TestYhListReqDTO reqDTO){
        String url = testYhUrl + "/testYh/detail";
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<TestYhDTO>>() {
        });
    }


    public boolean testYhInsert(TestYhReqDTO reqDTO){
        boolean result = true;

        Map<String, Object> memberMap = this.memberTestYhCount(reqDTO).getResultData();

        int memberCount = Integer.parseInt(memberMap.get(RESULT).toString());

        if(memberCount > 0){

            throw new RuntimeExceptionBase(ResultCode.USER_EXISTS_EXCEPTION);
        } else{

            String memberInsertResult = this.insertTestYh(reqDTO).getResultData().get(RESULT).toString();
            if("N".equals(memberInsertResult)){
                result = false;
            }
        }

        return result;
    }

    public ResponseDTO<Map<String, Object>> memberTestYhCount(TestYhReqDTO reqDTO){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("reqDTO", new Gson().toJson(reqDTO));//객체를 json으로 변환

        String url = testYhUrl+"/testYh/testYhUserCount";

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url),
                new ParameterizedTypeReference<ResponseDTO<Map<String, Object>>>(){});
    }

    public ResponseDTO<Map<String, Object>> insertTestYh(TestYhReqDTO reqDTO){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("reqDTO", new Gson().toJson(reqDTO));
        String url = testYhUrl+"/testYh/insert";

        return RestTemplateUtil.postJsonResponseDTO(url, parameters, new ResponseDTO<>());
    }

    public boolean testYhUpdate(TestYhReqDTO reqDTO){
        boolean result = true;

        Map<String, Object> memberMap = this.memberTestYhCount(reqDTO).getResultData();
        int memberCount = Integer.parseInt(memberMap.get(RESULT).toString());
        if(memberCount>0){
            String url = testYhUrl +"/testYh/update";
            RestTemplateUtil.putForResponseDTO(url,reqDTO);
        } else {
            throw new RuntimeExceptionBase(ResultCode.USER_NOT_EXISTS_EXCEPTION);
        }
        return result;
    }
    public ResponseDTO<Boolean> deleteTestYh(String userEmail){
        TestYhReqDTO reqDTO = new TestYhReqDTO();
        reqDTO.setUserEmail(userEmail);
        Map<String, Object> memberMap = this.memberTestYhCount(reqDTO).getResultData();
        int memberCount = Integer.parseInt(memberMap.get(RESULT).toString());
        if(memberCount > 0){
            String url = testYhUrl + "testYh/delete?userEmail=" + userEmail;
            return RestTemplateUtil.getForResponseDTO(url, new ParameterizedTypeReference<ResponseDTO<Boolean>>() {
            });
        } else {
            throw new RuntimeExceptionBase(ResultCode.USER_NOT_EXISTS_EXCEPTION);
        }
    }
}