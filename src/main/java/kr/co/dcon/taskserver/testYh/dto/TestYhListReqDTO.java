package kr.co.dcon.taskserver.testYh.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.dto.Forwardable;
import kr.co.dcon.taskserver.common.dto.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class TestYhListReqDTO extends PagingDTO implements Forwardable {
    @ApiModelProperty(value = "사용자 이름", notes = "사용자 이름", example = "배영호")
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "test2@dc-on.co.kr")
    @NotBlank
    @Email(message="이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "email", notes = "email", example = "email")
    private String searchGubun;

    @Override
    public String getUrlToForward(String baseUrl){
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);
        urlParam.append("?userEmail=").append(userEmail);
        if(StringUtils.isNotEmpty(searchGubun)){
            urlParam.append("&searchGubun="+searchGubun);
        }
        urlParam.append("&Size=").append(this.getSize());
        urlParam.append("&pageNo=").append(this.getPageNo());
        return urlParam.toString();
    }


}
