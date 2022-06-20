package kr.co.dcon.taskserver.sample.mapper;

import kr.co.dcon.taskserver.sample.dto.SampleDTO;
import kr.co.dcon.taskserver.sample.dto.SampleListReqDTO;
import kr.co.dcon.taskserver.sample.dto.SampleOverView;
import kr.co.dcon.taskserver.sample.dto.SampleReqDTO;

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
