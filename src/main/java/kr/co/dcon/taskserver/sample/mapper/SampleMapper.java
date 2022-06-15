package kr.co.dcon.taskserver.sample.mapper;

import kr.co.dcon.taskserver.sample.dto.*;

import java.util.List;
import java.util.Map;

public interface SampleMapper {

    Integer selectSampleListCountTotal(SampleListReqDTO reqDTO);

    List<SampleOverView> selectSampleList(SampleListReqDTO reqDTO);

    SampleDTO selectSampleDetail(SampleListReqDTO reqDTO);

    void sampleInsert(SampleReqDTO reqDTO);

    void sampleUpdate(SampleReqDTO reqDTO);

    int deleteSample(Map<String, Object> paramMap);


}
