package com.example.project_card.comCodes.service;

import com.example.project_card.comCodes.domain.ComCodeDtl;
import com.example.project_card.comCodes.dto.response.ComCodeDtlList;
import com.example.project_card.comCodes.repository.ComCodeDtlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ComCodeService {

    private final ComCodeDtlRepository comCodeDtlRepository;

    public List<ComCodeDtlList> SelectCommonCodeDetail(String groupCd)
    {
        log.info("{ ComCodeService } : ComCodeDetailList 조회");
        log.info(">> groupCd : {}", groupCd);

        List<ComCodeDtl> comCodeDtls = comCodeDtlRepository.findByGroupCd(groupCd);
        List<ComCodeDtlList> comCodeDtlList = new ArrayList<>();
        for(ComCodeDtl comCodeDtl: comCodeDtls)
        {
            comCodeDtlList.add(ComCodeDtlList.fromComCodeDetailList(comCodeDtl));
        }

        log.info("{ ComCodeService } : ComCodeDetailList 조회 완료");
        return comCodeDtlList;
    }


}
