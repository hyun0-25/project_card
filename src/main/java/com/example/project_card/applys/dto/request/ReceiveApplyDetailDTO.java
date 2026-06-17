package com.example.project_card.applys.dto.request;

import java.time.LocalDate;

public record ReceiveApplyDetailDTO(
        String ssn1,
        String ssn2,
        LocalDate rcvD,
        String rcvSeqNo
) {
}
