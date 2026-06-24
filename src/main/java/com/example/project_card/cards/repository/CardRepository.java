package com.example.project_card.cards.repository;

import com.example.project_card.cards.domain.Card;
import com.example.project_card.cards.domain.CardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, CardId> {

    Long countBySsn(String ssn);

    Long countBySsnAndBrd(String ssn, String brd);

    Card findBySsnAndBrdAndLstCrdF(String ssn, String brd, String crdF);

    @Query("SELECT c " +
            "FROM Card c " +
            "WHERE c.ssn = :ssn " +
            "AND (:crdNo IS NULL OR :crdNo = '' OR c.crdNo = :crdNo)")
    List<Card> findAllBySsnAndCrdNo(String ssn, String crdNo);

    Card findByCrdNoAndCustNo(String crdNo, String custNo);
}
