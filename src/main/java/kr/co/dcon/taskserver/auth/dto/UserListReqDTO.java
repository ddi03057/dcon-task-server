package kr.co.dcon.taskserver.auth.dto;

import kr.co.dcon.taskserver.common.dto.PagingDTO;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;

import javax.ws.rs.NotSupportedException;

@Data
public class UserListReqDTO extends PagingDTO{
	private String siteCode;
	private static final long serialVersionUID = 1L;
	private String customerCode;
    private String customerDeptCode;
    private String auth;
    private String orderCol;
    private String searchText;
    private Boolean  desc;

    
    @Deprecated
    public LinkedMultiValueMap<String, String> getLinkedMultiValueMap(){
        throw new NotSupportedException();
    }


}
