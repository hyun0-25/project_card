package com.example.project_card.users.controller;

import com.example.project_card.comCodes.service.ComCodeService;
import com.example.project_card.users.dto.request.ReceiveApplyDTO;
import com.example.project_card.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final ComCodeService comCodeService;
    private final UserService userService;

    @GetMapping("/register")
    public String GetReceiveApply(
            Model model
    )
    {
        model.addAttribute("receiveApplyDTO", ReceiveApplyDTO.EmptyReceiveApplyInfo());  // 입회 신청 request (접수일자)
        model.addAttribute("C002", comCodeService.SelectCommonCodeDetail("C002"));
        model.addAttribute("C003", comCodeService.SelectCommonCodeDetail("C003"));
        model.addAttribute("C004", comCodeService.SelectCommonCodeDetail("C004"));
        model.addAttribute("C005", comCodeService.SelectCommonCodeDetail("C005"));
        model.addAttribute("C006", comCodeService.SelectCommonCodeDetail("C006"));
        model.addAttribute("C007", comCodeService.SelectCommonCodeDetail("C007"));
        model.addAttribute("C008", comCodeService.SelectCommonCodeDetail("C008"));
        model.addAttribute("C009", comCodeService.SelectCommonCodeDetail("C009"));
        return "apply/register";
    }

    @PostMapping("/register")
    public String ReceiveApply(
            @ModelAttribute ReceiveApplyDTO receiveApplyDTO,
            Model model
    )
    {
        log.info("{ UserController } : ReceiveApply 생성 진입");
        model.addAttribute("receiveApplyDTO", userService.ReceiveApply(receiveApplyDTO));
        log.info("{ UserController } : ReceiveApply 생성 완료");
        return "apply/register";
    }
}
