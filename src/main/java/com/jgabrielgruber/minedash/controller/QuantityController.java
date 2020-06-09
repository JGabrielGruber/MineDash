package com.jgabrielgruber.minedash.controller;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jgabrielgruber.minedash.model.Resource;
import com.jgabrielgruber.minedash.model.Category;
import com.jgabrielgruber.minedash.model.Quantity;
import com.jgabrielgruber.minedash.repository.ResourceRepository;
import com.jgabrielgruber.minedash.repository.CategoryRepository;
import com.jgabrielgruber.minedash.repository.QuantityRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/categories/{category}/resources/{resource}/")
public class QuantityController {

	@Autowired
	QuantityRepository quantityRepository;

	@Autowired
	ResourceRepository resourceRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping("quantities")
	public ResponseEntity<List<Quantity>> getAllquantities(@PathVariable("resource") String resource) {
		try {
			List<Quantity> quantities;

			try {
				quantities = quantityRepository.findByResourceId(Long.parseLong(resource));
			} catch (NumberFormatException e) {
				quantities = quantityRepository.findByResourceTitleOrderByTimestamp(resource);
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
	public ResponseEntity<Quantity> createQuantity(@PathVariable("resource") String query,
			@RequestBody Quantity Quantity) {
		Resource resource;
		try {
			resource = new Resource(Long.parseLong(query));
		} catch (NumberFormatException e) {
			resource = resourceRepository.findByTitleContaining(query).get();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		try {
			Quantity quantity = quantityRepository.save(new Quantity(resource, Quantity.getAmount()));
			return new ResponseEntity<>(quantity, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("qtd")
	public ResponseEntity<Quantity> createQuantityP(@PathVariable("category") String query_c,
			@PathVariable("resource") String query_r, @RequestParam String amount) {
		Category category;
		Resource resource;
		try {
			resource = new Resource(Long.parseLong(query_r));
		} catch (NumberFormatException e) {
			Optional<Category> categories = categoryRepository.findByTitleContaining(query_c);
			if (categories.isPresent()) {
				category = categories.get();
				Optional<Resource> resources = resourceRepository.findByTitleContaining(query_r);
				if (resources.isPresent())
					resource = resources.get();
				else
					resource = resourceRepository.save(new Resource(category, query_r));
			} else {
				throw new RuntimeErrorException(null, "NOT_FOUND Category");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		try {
			Quantity quantity = quantityRepository.save(new Quantity(resource, Integer.parseInt(amount)));
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
