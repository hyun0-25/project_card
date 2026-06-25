package com.example.project_card.users.controller;

import com.example.project_card.users.dto.response.UserListDTO;
import com.example.project_card.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public String SelectUserList(
            @RequestParam(required = false) Optional<String> hgNm,
            @RequestParam(required = false) Optional<String> birthD,
            @RequestParam(required = false) Optional<String> hdpNo,
            Model model
    )
    {
        log.info("{ UserController } : SelectUserList 상세조회");
        if(!hgNm.isPresent() && !birthD.isPresent() && !hdpNo.isPresent()){
            model.addAttribute("userListDTO", UserListDTO.EmptyUserListDTO("", "", ""));
        }
        else
        {
            try{
                model.addAttribute("userListDTO", userService.SelectUserList(hgNm.get(), birthD.get(), hdpNo.get()));
                model.addAttribute("message", "조회 성공!");
                log.info("{ UserController } : SelectUserList 조회 완료");
            } catch (Exception e){
                model.addAttribute("userListDTO", UserListDTO.EmptyUserListDTO(hgNm.get(), birthD.get(), hdpNo.get()));
                model.addAttribute("message", e.getMessage());
                log.info("{ UserController } : SelectUserList 조회 실패");
            }
        }
        return "user/list";
    }
}
