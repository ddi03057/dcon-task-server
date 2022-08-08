package kr.co.dcon.taskserver.testYh.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.PagingDTO;
import lombok.Data;

import java.util.List;

@Data
public class TestYhListDTO {
    private PagingDTO pagingDTO;

    @ApiModelProperty(value = "testYh 상세", notes = "testYh 상세")
    TestYhDTO testYhDTO;

    @ApiModelProperty(value = "testYh list", notes = "testYh list")
    List<TestYhOverView> list;
}
