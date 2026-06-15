package com.example.project_card.cards.repository;

import com.example.project_card.cards.domain.Card;
import com.example.project_card.cards.domain.CardId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, CardId> {

    Long countBySsn(String ssn);

    Long countBySsnAndBrd(String ssn, String brd);

    Card findBySsnAndBrdAndLstCrdF(String ssn, String brd, String crdF);
}
