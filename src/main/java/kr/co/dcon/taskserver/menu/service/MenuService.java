package kr.co.dcon.taskserver.menu.service;

import kr.co.dcon.taskserver.auth.service.CurrentUserService;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.util.RestTemplateUtil;
import kr.co.dcon.taskserver.menu.dto.MenuLeftListReqDTO;
import kr.co.dcon.taskserver.menu.dto.MenuListDTO;
import kr.co.dcon.taskserver.menu.dto.MenuTopListDTO;
import kr.co.dcon.taskserver.menu.dto.MenuTopListReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MenuService {

    CurrentUserService currentUserService;

    @Value("${taskserver.url}")
    private String taskUrl;



    public MenuService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public ResponseDTO<List<MenuTopListDTO>> selectMenuTopList(MenuTopListReqDTO reqDTO) {
        String url = taskUrl + "/menu/menuTop/" + reqDTO.getUserId();
        reqDTO.setUserAuth(currentUserService.getCurrentUser().getAuth());
        reqDTO.setUseYn(currentUserService.getCurrentUser().getUseYn());
        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<List<MenuTopListDTO>>>() {
        });
    }


    public ResponseDTO<MenuListDTO> selectMenuList(MenuLeftListReqDTO reqDTO) {
        String url = taskUrl + "/menu/list/" + reqDTO.getUserId();;

        return RestTemplateUtil.getForResponseDTO(reqDTO.getUrlToForward(url), new ParameterizedTypeReference<ResponseDTO<MenuListDTO>>() {
        });
    }

}
