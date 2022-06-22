package kr.co.dcon.taskserver.auth.service;

import kr.co.dcon.taskserver.auth.dto.RealmInfoDTO;
import kr.co.dcon.taskserver.auth.dto.UserSimpleDTO;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import kr.co.dcon.taskserver.common.util.TokenUtil;
import kr.co.dcon.taskserver.user.mapper.UserMapper;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("keyCloakCurrentUserService")
public class KeyCloakCurrentUserServiceImpl  implements CurrentUserService{

	@Autowired
	UserMapper userMapper;

	private RealmInfoDTO realmInfoDTO;

	@Override
	public UserSimpleDTO getCurrentUser() {
		AccessToken accessToken = TokenUtil.getAccessToken();
        return buildUserSimple(accessToken);
	}

	@Override
	public RealmInfoDTO getRealmInfo() {
		this.realmInfoDTO = this.realmInfoDTO == null ? userMapper.selectKecloakRealmInfo() : this.realmInfoDTO;
		return this.realmInfoDTO;
	}

	@Override
	public void setRealmInfo() {
		this.realmInfoDTO = this.realmInfoDTO == null ? userMapper.selectKecloakRealmInfo() : this.realmInfoDTO;
	}

	private UserSimpleDTO buildUserSimple(AccessToken accessToken) {
		Map<String, Object> otherClaims = accessToken.getOtherClaims();
		
		UserSimpleDTO userSimple = new UserSimpleDTO();
		userSimple.setUserId(accessToken.getSubject());
		userSimple.setUserEmail(accessToken.getEmail());
		userSimple.setUserName(accessToken.getEmail());
		userSimple.setUserName((String)otherClaims.get(UserOtherClaim.USER_NAME));
//		userSimple.setCompanyCode((String)otherClaims.get(UserOtherClaim.COMPANY_CODE));
//		userSimple.setCustomerCode((String)otherClaims.get(UserOtherClaim.CUSTOMER_CODE));
//		userSimple.setDeptCode((String)otherClaims.get(UserOtherClaim.CUS_DEPT_CD));
//
//		userSimple.setSiteCode(TokenUtil.getContext().getRealm());
		return userSimple;
	}

}
