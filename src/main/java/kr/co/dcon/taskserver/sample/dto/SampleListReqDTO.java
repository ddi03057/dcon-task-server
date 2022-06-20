package kr.co.dcon.taskserver.sample.dto;

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
@EqualsAndHashCode(callSuper=false)
@Slf4j
public class SampleListReqDTO extends PagingDTO implements Forwardable {

    @ApiModelProperty(value = "사용자 이름", notes = "사용자 이름", example = "홍길동")
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "test1@dc-on.co.kr")
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "email", notes = "email", example = "email")
    private String searchGubun;


    @Override
    public String getUrlToForward(String baseUrl) {
        StringBuilder urlParam = new StringBuilder();

        urlParam.append(baseUrl);

//        if(StringUtils.isNotEmpty(userName)) {
//            urlParam.append("?userName=" + userName);
//        }
        urlParam.append("?userEmail="+userEmail);
        if(StringUtils.isNotEmpty(searchGubun)) {
            urlParam.append("&searchGubun=" + searchGubun);
        }
        urlParam.append("&size=" + this.getSize());
        urlParam.append("&pageNo=" + this.getPageNo());
        log.info("urlParam.toString()::{}",urlParam.toString());
        return urlParam.toString();
    }
}

