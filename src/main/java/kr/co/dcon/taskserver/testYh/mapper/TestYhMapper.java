package kr.co.dcon.taskserver.testYh.mapper;

import kr.co.dcon.taskserver.testYh.dto.TestYhDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhListReqDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhOverView;
import kr.co.dcon.taskserver.testYh.dto.TestYhReqDTO;

import java.util.List;
import java.util.Map;

public interface TestYhMapper {
    Integer selectTestYhListCountTotal(TestYhListReqDTO reqDTO);

    List<TestYhOverView> selectTestYhList(TestYhListReqDTO reqDTO);

    TestYhDTO selectYhDetail(TestYhReqDTO reqDTO);

    void testYhInsert(TestYhReqDTO reqDTO);
    void testYhUpdate(TestYhReqDTO reqDTO);
    void deleteTestYh(Map<String, Object> paramMap);
}
