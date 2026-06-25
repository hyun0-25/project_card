package com.example.project_card.users.service;

import com.example.project_card.global.exception.BaseException;
import com.example.project_card.users.domain.Bill;
import com.example.project_card.users.domain.Customer;
import com.example.project_card.users.dto.response.UserDetailDTO;
import com.example.project_card.users.dto.response.UserListDTO;
import com.example.project_card.users.exception.BillErrorCode;
import com.example.project_card.users.exception.CustomerErrorCode;
import com.example.project_card.users.repository.BillRepository;
import com.example.project_card.users.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;

    public UserListDTO SelectUserList(String hgNm, String birthD, String hdpNo)
    {
        log.info("{ UserService } : SelectUserList 조회");
        log.info(" >> hgNm - "+hgNm);
        log.info(" >> birthD - "+birthD);
        log.info(" >> hdpNo - "+hdpNo);
        List<Customer> customerList = customerRepository.findAllByHgNmAndBirthDAndHdpNo(hgNm, birthD, hdpNo);
        if(customerList == null || customerList.size() == 0)
            throw BaseException.type(CustomerErrorCode.CUSTOMER_NOT_FOUND);

        List<UserDetailDTO> userDetailDTOList = new ArrayList<>();
        for(Customer customer: customerList)
        {
            Bill bill = billRepository.findByCustNo(customer.getCustNo());
            if(bill == null)
                throw BaseException.type(BillErrorCode.BILL_NOT_FOUND);

            userDetailDTOList.add(UserDetailDTO.fromUserDetail(customer, bill));
        }

        UserListDTO userListDTO = UserListDTO.fromUserList(hgNm, birthD, hdpNo, userDetailDTOList);
        log.info("{ UserService } : SelectUserList 조회");
        return userListDTO;
    }
}
