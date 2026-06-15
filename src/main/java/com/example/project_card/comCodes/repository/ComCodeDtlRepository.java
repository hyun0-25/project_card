package com.example.project_card.comCodes.repository;

import com.example.project_card.comCodes.domain.ComCodeDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComCodeDtlRepository extends JpaRepository<ComCodeDtl, String> {

    List<ComCodeDtl> findByGroupCd(String groupCd);

    ComCodeDtl findByGroupCdAndCode(String groupCd, String code);

}
