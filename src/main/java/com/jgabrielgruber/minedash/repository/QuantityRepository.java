package com.jgabrielgruber.minedash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jgabrielgruber.minedash.model.Quantity;

public interface QuantityRepository extends JpaRepository<Quantity, Long> {
	List<Quantity> findByResourceId(long resourceId);

	List<Quantity> findByResourceTitleOrderByTimestamp(String resourceTitle);

	Quantity findOneByResourceTitle(String resourceTitle);
}
