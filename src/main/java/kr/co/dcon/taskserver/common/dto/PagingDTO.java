package kr.co.dcon.taskserver.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PagingDTO implements Serializable {

	@ApiModelProperty(value = "검색할 페이지 번호", notes = "검색할 페이지 번호", example = "0", required = true)
	private Integer pageNo = 1;			// 리스트 페이지 번호. 0부터 시

	@ApiModelProperty(value = "전체 게시물 수", notes = "전체 게시물 수", example = "30", required = false, hidden = true)
	private Integer totalCount;			// 전체 레코드 수

	@ApiModelProperty(value = "페이지당 출력 갯수 ", notes =  "페이지당 출력 갯수 ", example = "20", required = false)
	private Integer size = 20;			// 페이지당 출력 갯수  값이 없으면 default 20

	@ApiModelProperty(value = "페이지 limit", notes = "페이지 limit", example = "0", required = false, hidden = true)
	private Integer startIndex = 0;

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getStartIndex() {
		return (this.pageNo - 1) * this.size;
	}
}
