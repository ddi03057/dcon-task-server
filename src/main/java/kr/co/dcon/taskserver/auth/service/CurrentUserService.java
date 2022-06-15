package kr.co.dcon.taskserver.auth.service;


import kr.co.dcon.taskserver.auth.dto.UserSimpleDTO;

public interface CurrentUserService {
	UserSimpleDTO getCurrentUser();
	
}
