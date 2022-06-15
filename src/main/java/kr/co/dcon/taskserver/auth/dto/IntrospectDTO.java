package kr.co.dcon.taskserver.auth.dto;

import lombok.Data;

import java.util.List;
@Data
public class IntrospectDTO {
	private String sub;
	private String userName;
	private String companyCode;
	private String customerCdoe;
	private String customerDeptCode;
	private List<String> groups;
	private boolean active;
	
	/*public String getAuth() {
		String groupPath  = groups.get(0);
		GroupEnum group = GroupEnum.getGroupWithPath(groupPath);
		return group.getOcpAuthStr();
	}*/
}
