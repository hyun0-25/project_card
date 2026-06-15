package com.example.project_card.users.repository;

import com.example.project_card.users.domain.Customer;
import com.example.project_card.users.domain.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, CustomerId> {
}
