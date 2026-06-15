package com.example.project_card.applys.repository;

import com.example.project_card.applys.domain.SeqNo;
import com.example.project_card.applys.domain.SeqNoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeqNoRepository extends JpaRepository<SeqNo, SeqNoId> {

    @Query("SELECT MAX(s.custNo) FROM SeqNo s")
    String findMaxCustNo();

    @Query("SELECT MAX(s.crdNo) FROM SeqNo s")
    String findMaxCrdNo();
}
