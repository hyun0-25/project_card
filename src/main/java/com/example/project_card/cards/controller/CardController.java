package com.example.project_card.cards.controller;

import com.example.project_card.cards.dto.response.CardListDTO;
import com.example.project_card.cards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    @GetMapping("/list")
    public String SelectCardList(
            @RequestParam(required = false) String ssn1,
            @RequestParam(required = false) String ssn2,
            @RequestParam(required = false) String crdNo,
            Model model
    )
    {
        log.info("{ CardController } : SelectCardList 조회 진입");
        if(ssn1 == null || ssn1.equals(""))
        {
            model.addAttribute("cardListDTO", CardListDTO.EmptyCardListDTO(ssn1, ssn2, crdNo));
            return "card/list";
        }

        try{
            model.addAttribute("cardListDTO", cardService.SelectCardList(ssn1, ssn2, crdNo));
            model.addAttribute("message", "조회 성공!");
            log.info("{ CardController } : SelectCardList 조회 완료");
            return "card/list";
        } catch (Exception e){
            model.addAttribute("cardListDTO", CardListDTO.EmptyCardListDTO(ssn1, ssn2, crdNo));
            model.addAttribute("message", e.getMessage());
            log.info("{ CardController } : SelectCardList 조회 실패");
            return "card/list";
        }
    }
}
