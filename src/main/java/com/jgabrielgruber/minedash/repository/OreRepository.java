package com.jgabrielgruber.minedash.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jgabrielgruber.minedash.model.Ore;

public interface OreRepository extends JpaRepository<Ore, Long> {
	Optional<Ore> findByTitleContaining(String title);
}
