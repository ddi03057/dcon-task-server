package kr.co.dcon.taskserver.common.dto;


import kr.co.dcon.taskserver.common.constants.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> implements Serializable {
    private Date timestamp = new Date();

    private Integer code;

    private String codeName;

    private String desc;

    private String descKr;

    private T resultData;

    public ResponseDTO(ResultCode resultCode) {
        init(resultCode);
    }

    public ResponseDTO(ResultCode resultCode, T resultData) {
        init(resultCode);
        this.resultData = resultData;
    }

    private void init(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.codeName = resultCode.name();
        this.desc = resultCode.getDescription();
        this.descKr = resultCode.getDescriptionKr();
    }
}
