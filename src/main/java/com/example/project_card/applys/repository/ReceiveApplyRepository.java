package com.example.project_card.applys.repository;

import com.example.project_card.applys.domain.ReceiveApply;
import com.example.project_card.applys.domain.ReceiveApplyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiveApplyRepository extends JpaRepository<ReceiveApply, ReceiveApplyId> {
    ReceiveApply findBySsnAndRcvD(String ssn, String rcvD);
}
