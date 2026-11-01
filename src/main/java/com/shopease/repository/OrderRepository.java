package com.shopease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopease.entity.AppUser;
import com.shopease.entity.CustomerOrder;

public interface OrderRepository
        extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByUser(AppUser user);
}