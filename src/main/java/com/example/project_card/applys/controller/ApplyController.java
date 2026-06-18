package com.example.project_card.applys.controller;

import com.example.project_card.comCodes.service.ComCodeService;
import com.example.project_card.applys.dto.request.ReceiveApplyDTO;
import com.example.project_card.applys.service.ApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/apply")
public class ApplyController {

    private final ComCodeService comCodeService;
    private final ApplyService applyService;

    @GetMapping("/register")
    public String GetReceiveApply(
            Model model
    )
    {
        model.addAttribute("receiveApplyDTO", ReceiveApplyDTO.EmptyReceiveApplyInfo("", ""));  // 입회 신청 request (접수일자)
        ComCodeSelect(model);
        return "apply/register";
    }

    @PostMapping("/register")
    public String ReceiveApply(
            @ModelAttribute ReceiveApplyDTO receiveApplyDTO,
            Model model
    )
    {
        log.info("{ ApplyController } : ReceiveApply 생성 진입");
        model.addAttribute("receiveApplyDTO", applyService.ReceiveApply(receiveApplyDTO));
        ComCodeSelect(model);
        log.info("{ ApplyController } : ReceiveApply 생성 완료");
        return "apply/register";
    }

    @PostMapping("/detail")
    public String ReceiveApplyDetail(
            @ModelAttribute ReceiveApplyDTO receiveApplyDTO,
            Model model
    )
    {
        log.info("{ ApplyController } : ReceiveApplyDetail 조회 진입");
        ComCodeSelect(model);
        try {
            model.addAttribute("receiveApplyDTO", applyService.ReceiveApplyDetail(receiveApplyDTO));
            model.addAttribute("message", "조회 성공");
            log.info("{ ApplyController } : ReceiveApplyDetail 조회 완료");
            return "apply/register";
        } catch (Exception e){
            model.addAttribute("receiveApplyDTO", ReceiveApplyDTO.EmptyReceiveApplyInfo(receiveApplyDTO.ssn1(), receiveApplyDTO.ssn2()));
            model.addAttribute("message", e.getMessage());
            log.info("{ ApplyController } : ReceiveApplyDetail 조회 실패");
            return "apply/register";
        }
    }

    @PostMapping("/update")
    public String ReceiveApplyModify(
            @ModelAttribute ReceiveApplyDTO receiveApplyDTO,
            Model model
    )
    {
        log.info("{ ApplyController } : ReceiveApplyModify 수정 진입");
        model.addAttribute("receiveApplyDTO", applyService.ReceiveApplyModify(receiveApplyDTO));
        ComCodeSelect(model);
        log.info("{ ApplyController } : ReceiveApplyModify 수정 완료");
        return "apply/register";
    }

    public void ComCodeSelect(Model model) {
        model.addAttribute("C002", comCodeService.SelectCommonCodeDetail("C002"));
        model.addAttribute("C003", comCodeService.SelectCommonCodeDetail("C003"));
        model.addAttribute("C004", comCodeService.SelectCommonCodeDetail("C004"));
        model.addAttribute("C005", comCodeService.SelectCommonCodeDetail("C005"));
        model.addAttribute("C006", comCodeService.SelectCommonCodeDetail("C006"));
        model.addAttribute("C007", comCodeService.SelectCommonCodeDetail("C007"));
        model.addAttribute("C008", comCodeService.SelectCommonCodeDetail("C008"));
        model.addAttribute("C009", comCodeService.SelectCommonCodeDetail("C009"));
    }
}
