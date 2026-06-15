package com.example.project_card.applys.repository;

import com.example.project_card.applys.domain.NoSeq;
import com.example.project_card.applys.domain.NoSeqId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoSeqRepository extends JpaRepository<NoSeq, NoSeqId> {

    @Query("SELECT MAX(n.rcvSeqNo) FROM NoSeq n " +
            "WHERE n.rcvD = :rcvD")
    String findMaxNoSeqByRcvD(String rcvD);
}
