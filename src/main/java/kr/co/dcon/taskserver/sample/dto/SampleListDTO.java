package kr.co.dcon.taskserver.sample.dto;

import kr.co.dcon.taskserver.common.dto.PagingDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class SampleListDTO {

    private PagingDTO pagingDTO;

    @ApiModelProperty(value = "sample 상세", notes = "sample 상세")
    SampleDTO sampleDTO;

    @ApiModelProperty(value = "sample List", notes = "sample List")
    List<SampleOverView> list;
}
