package com.prsweb.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prsweb.business.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
