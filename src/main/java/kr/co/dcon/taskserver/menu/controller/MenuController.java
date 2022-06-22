package kr.co.dcon.taskserver.menu.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.menu.dto.MenuLeftListReqDTO;
import kr.co.dcon.taskserver.menu.dto.MenuListDTO;
import kr.co.dcon.taskserver.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Api(value = "MENU API")
@RequestMapping(value = "/api/v1/menu")
@RestController
public class MenuController {

    MenuService menuService;

    public MenuController(MenuService menuService){
        this.menuService = menuService;
    }

    @ApiOperation(value = "menu list", notes = " menu list")
    @GetMapping("/list")
    public ResponseDTO<MenuListDTO> selectMenuList(@Valid MenuLeftListReqDTO reqDTO) {
        return menuService.selectMenuList(reqDTO);

    }
}