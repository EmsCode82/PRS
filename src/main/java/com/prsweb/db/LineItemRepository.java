package com.prsweb.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prsweb.business.LineItem;

public interface LineItemRepository extends JpaRepository<LineItem, Integer> {
	List<LineItem> findAllByRequestId(int id);
}
