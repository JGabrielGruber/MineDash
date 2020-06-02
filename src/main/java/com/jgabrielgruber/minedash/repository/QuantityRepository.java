package com.jgabrielgruber.minedash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jgabrielgruber.minedash.model.Quantity;

public interface QuantityRepository extends JpaRepository<Quantity, Long> {
	List<Quantity> findByOreId(long oreId);

	List<Quantity> findByOreTitle(String oreTitle);

	Quantity findOneByOreTitle(String oreTitle);
}
