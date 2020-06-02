package com.jgabrielgruber.minedash.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "ore")
@Table(name = "ore")
public class Ore {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "title")
	private String title;

	@OneToMany(mappedBy = "ore", cascade = CascadeType.ALL)
	private List<Quantity> quantity;

	public Ore() {
	}

	public Ore(String title) {
		this.title = title;
	}

	public Ore(long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Iron [id =" + id + ", title=" + title + "]";
	}

}
