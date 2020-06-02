package com.jgabrielgruber.minedash.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jgabrielgruber.minedash.model.Ore;
import com.jgabrielgruber.minedash.model.Quantity;
import com.jgabrielgruber.minedash.repository.OreRepository;
import com.jgabrielgruber.minedash.repository.QuantityRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/ores/{ore}/")
public class QuantityController {

	@Autowired
	QuantityRepository quantityRepository;

	@Autowired
	OreRepository oreRepository;

	@GetMapping("quantities")
	public ResponseEntity<List<Quantity>> getAllquantities(@PathVariable("ore") String ore) {
		try {
			List<Quantity> quantities;

			try {
				quantities = quantityRepository.findByOreId(Long.parseLong(ore));
			} catch (NumberFormatException e) {
				quantities = quantityRepository.findByOreTitle(ore);
			}

			if (quantities.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(quantities, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("quantities")
	public ResponseEntity<Quantity> createQuantity(@PathVariable("ore") String query, @RequestBody Quantity Quantity) {
		Ore ore;
		try {
			ore = new Ore(Long.parseLong(query));
		} catch (NumberFormatException e) {
			ore = oreRepository.findByTitleContaining(query).get();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		try {
			Quantity quantity = quantityRepository.save(new Quantity(ore, Quantity.getAmount()));
			return new ResponseEntity<>(quantity, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("quantities")
	public ResponseEntity<HttpStatus> deleteAllQuantities() {
		try {
			quantityRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}
}