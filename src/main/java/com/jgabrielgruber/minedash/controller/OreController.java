package com.jgabrielgruber.minedash.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jgabrielgruber.minedash.model.Ore;
import com.jgabrielgruber.minedash.repository.OreRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/")
public class OreController {

	@Autowired
	OreRepository OreRepository;

	@GetMapping("/ores")
	public ResponseEntity<List<Ore>> getAllOres() {
		try {
			List<Ore> ores = new ArrayList<Ore>();

			OreRepository.findAll().forEach(ores::add);

			if (ores.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(ores, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/ores/{query}")
	public ResponseEntity<Ore> getOreById(@PathVariable("query") String query) {
		Optional<Ore> oreData;
		try {
			oreData = OreRepository.findById(Long.parseLong(query));
		} catch (NumberFormatException e) {
			oreData = OreRepository.findByTitleContaining(query);
		}

		if (oreData.isPresent()) {
			return new ResponseEntity<>(oreData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/ores")
	public ResponseEntity<Ore> createOre(@RequestBody Ore Ore) {
		try {
			Ore ore = OreRepository.save(new Ore(Ore.getTitle()));
			return new ResponseEntity<>(ore, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/ores/{id}")
	public ResponseEntity<Ore> updateOre(@PathVariable("id") long id, @RequestBody Ore Ore) {
		Optional<Ore> oreData = OreRepository.findById(id);

		if (oreData.isPresent()) {
			Ore _Ore = oreData.get();
			_Ore.setTitle(Ore.getTitle());
			return new ResponseEntity<>(OreRepository.save(_Ore), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/ores/{id}")
	public ResponseEntity<HttpStatus> deleteOre(@PathVariable("id") long id) {
		try {
			OreRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/ores")
	public ResponseEntity<HttpStatus> deleteAllOres() {
		try {
			OreRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

}