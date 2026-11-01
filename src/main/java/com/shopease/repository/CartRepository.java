package com.shopease.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopease.entity.AppUser;
import com.shopease.entity.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(AppUser user);

    Optional<CartItem> findByUserAndProductId(AppUser user, Long productId);

    void deleteByUser(AppUser user);
}