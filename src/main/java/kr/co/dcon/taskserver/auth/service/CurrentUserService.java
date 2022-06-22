package kr.co.dcon.taskserver.auth.service;


import kr.co.dcon.taskserver.auth.dto.RealmInfoDTO;
import kr.co.dcon.taskserver.auth.dto.UserSimpleDTO;

public interface CurrentUserService {
	UserSimpleDTO getCurrentUser();

	RealmInfoDTO getRealmInfo();
	void setRealmInfo();
}
