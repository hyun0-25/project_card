package com.example.project_card.users.service;

import com.example.project_card.users.domain.ReceiveApply;
import com.example.project_card.users.dto.request.ReceiveApplyDTO;
import com.example.project_card.users.repository.ReceiveApplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final ReceiveApplyRepository receiveApplyRepository;

    public ReceiveApplyDTO ReceiveApply(ReceiveApplyDTO receiveApplyDTO)
    {
        log.info("{ UserService } : ReceiveApply 생성");
        String rcvSeqNo = ""; // 접수 일련 번호
        String crdNo = ""; // 카드 번호
        String impsbClas = ""; // 불능 구분
        String impsbCd = "";  // 불능 코드

        // 불능 체크
        impsbClas = "1"; // 1,불능


        // 정상 등록
        ReceiveApply receiveApply = ReceiveApply.createReceiveApply(
                receiveApplyDTO.ssn1() + "-" +receiveApplyDTO.ssn2(),
                receiveApplyDTO.rcvD().toString(),
                rcvSeqNo,
                receiveApplyDTO.applD(),
                receiveApplyDTO.birthD(),
                receiveApplyDTO.hgNm(),
                receiveApplyDTO.engNm(),
                receiveApplyDTO.stlMtd(),
                receiveApplyDTO.stlAct(),
                receiveApplyDTO.bnkCd(),
                receiveApplyDTO.stlDd(),
                "", // 관리 영업점
                receiveApplyDTO.applClas(),
                receiveApplyDTO.stmtSndMtd(),
                receiveApplyDTO.billAdr1(),
                receiveApplyDTO.billAdr2(),
                receiveApplyDTO.billZip(),
                receiveApplyDTO.hdpNo(),
                receiveApplyDTO.brd(),
                receiveApplyDTO.scrtNo(),
                receiveApplyDTO.emailAdr1()+ "@" + receiveApplyDTO.emailAdr2(),
                crdNo,
                impsbClas,
                impsbCd
        );
        ReceiveApply newReceiveApply = receiveApplyRepository.save(receiveApply);
        ReceiveApplyDTO newReceiveApplyDTO = ReceiveApplyDTO.fromReceiveApply(newReceiveApply);
        log.info("{ UserService } : ReceiveApply 생성 완료");
        return newReceiveApplyDTO;
    }
}
