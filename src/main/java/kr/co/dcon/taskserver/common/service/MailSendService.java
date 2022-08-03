package kr.co.dcon.taskserver.common.service;

import kr.co.dcon.taskserver.common.dto.SendEmailDTO;
import kr.co.dcon.taskserver.common.mapper.CommonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailSendService {

    CommonMapper commonMapper;

    public MailSendService(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public int insertMailSend(SendEmailDTO sendEmailDTO) {
        return commonMapper.insertMailSend(sendEmailDTO);
    }
}
