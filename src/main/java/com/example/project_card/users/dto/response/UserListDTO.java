package com.example.project_card.users.dto.response;

import com.example.project_card.users.domain.Bill;
import com.example.project_card.users.domain.Customer;
import lombok.Builder;

import java.util.List;

@Builder
public record UserListDTO(
        String hgNm,
        String birthD,
        String hdpNo,
        List<UserDetailDTO> userDetailDTOList
) {

    public static UserListDTO EmptyUserListDTO(String hgNm, String birthD, String hdpNo)
    {
        return new UserListDTO(hgNm, birthD, hdpNo, null);
    }

    public static UserListDTO fromUserList(String hgNm, String birthD, String hdpNo, List<UserDetailDTO> userDetailDTOList)
    {
        return UserListDTO.builder()
                .hgNm(hgNm)
                .birthD(birthD)
                .hdpNo(hdpNo)
                .userDetailDTOList(userDetailDTOList)
                .build();
    }
}
