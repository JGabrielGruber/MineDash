package com.jgabrielgruber.minedash.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity(name = "quantity")
@Table(name = "quantity")
public class Quantity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "ore_id")
	private Ore ore;

	@Column(name = "amount")
	private int amount;

	private Date timestamp;

	public Quantity() {
	}

	public Quantity(Ore ore, int amount) {
		this.ore = ore;
		this.amount = amount;
	}

	@PrePersist
	protected void onCreate() {
		timestamp = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Ore getOre() {
		return ore;
	}

	public void setOre(Ore ore) {
		this.ore = ore;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
