package kr.co.dcon.taskserver.sample.service;

//import com.dcon.dcontaskserver.auth.service.CurrentUserService;

import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import kr.co.dcon.taskserver.sample.dto.*;
import kr.co.dcon.taskserver.sample.mapper.SampleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class SampleService {
    @Autowired
    SampleMapper sampleMapper;

   // @Autowired
  //  CurrentUserService currentUserService;

    public SampleListDTO selectSampleList(SampleListReqDTO reqDTO) {
        log.info("req::{}",reqDTO.toString());
        SampleListDTO sampleListDTO = new SampleListDTO();
        // 청구내역 list (화면 : 계약정보의 우측 상단 리스트 )
        List<SampleOverView> list = sampleMapper.selectSampleList(reqDTO);

      //  log.info("currentUserService:::::{}",currentUserService.getCurrentUser());
        sampleListDTO.setList(list);

        sampleListDTO.setPagingDTO(reqDTO);

        return sampleListDTO;
    }

    public SampleDTO selectSampleDetail(SampleListReqDTO reqDTO) {
        return sampleMapper.selectSampleDetail(reqDTO);
    }

    public void sampleInsert(SampleReqDTO reqDTO) {
        try {
            sampleMapper.sampleInsert(reqDTO);
        } catch (Exception e) {
            throw new RuntimeExceptionBase(ResultCode.ETC_ERROR);
        }
    }

    public int sampleUpdate(SampleReqDTO reqDTO) {
        int updateCnt = 1 ;
        try {
            sampleMapper.sampleUpdate(reqDTO);
        } catch (Exception e) {
            updateCnt = 0;
        }
        return updateCnt;
    }

    public void deleteSample(String id) {
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("id" , id);
        sampleMapper.deleteSample(paramMap);
    }

}

