package kr.co.dcon.taskserver.common.mapper;

import kr.co.dcon.taskserver.common.dto.SendEmailDTO;

public interface CommonMapper {

    int insertMailSend(SendEmailDTO sendEmailDTO);
}
