package com.example.project_card.users.service;

import com.example.project_card.global.exception.BaseException;
import com.example.project_card.users.domain.Bill;
import com.example.project_card.users.domain.Customer;
import com.example.project_card.users.dto.response.UserDetailDTO;
import com.example.project_card.users.exception.BillErrorCode;
import com.example.project_card.users.exception.CustomerErrorCode;
import com.example.project_card.users.repository.BillRepository;
import com.example.project_card.users.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;

    public UserDetailDTO SelectUserDetail(String hgNm, String birthD, String hdpNo)
    {
        log.info("{ UserService } : SelectUserDetail 조회");
        log.info(" >> hgNm - "+hgNm);
        log.info(" >> birthD - "+birthD);
        log.info(" >> hdpNo - "+hdpNo);
        Customer customer = customerRepository.findByHgNmAndBirthDAndHdpNo(hgNm, birthD, hdpNo);
        if(customer == null)
            throw BaseException.type(CustomerErrorCode.CUSTOMER_NOT_FOUND);

        Bill bill = billRepository.findByCustNo(customer.getCustNo());
        if(bill == null)
            throw BaseException.type(BillErrorCode.BILL_NOT_FOUND);

        UserDetailDTO userDetailDTO = UserDetailDTO.fromUserDetail(customer, bill);
        log.info("{ UserService } : SelectUserDetail 조회");
        return userDetailDTO;
    }
}
