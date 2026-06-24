package com.example.project_card.applys.repository;

import com.example.project_card.applys.domain.ReceiveApply;
import com.example.project_card.applys.domain.ReceiveApplyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiveApplyRepository extends JpaRepository<ReceiveApply, ReceiveApplyId> {

    ReceiveApply findBySsnAndRcvDAndImpsbClas(String ssn, String rcvD, String impsbClas);

    ReceiveApply findBySsnAndRcvDAndRcvSeqNo(String ssn, String rcvD, String rcvSeqNo);

    @Query("SELECT r " +
            "FROM ReceiveApply r " +
            "WHERE r.rcvD >= :rcvD1 AND r.rcvD <= :rcvD2 " +
            "AND (:applClas IS NULL OR r.applClas = :applClas) " +
            "AND (:ssn IS NULL OR :ssn = '' OR r.ssn = :ssn) " +
            "ORDER BY r.rcvD DESC, CAST(r.rcvSeqNo AS INTEGER) DESC")
    List<ReceiveApply> findAllByRcvD1AndRcvD2AndApplClasAndSsnOrderByRcvDDescAndRcvSeqNoDesc(String rcvD1, String rcvD2, String applClas, String ssn);
}
