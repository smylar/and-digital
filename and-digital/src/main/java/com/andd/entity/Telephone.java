package com.andd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The telephone entity
 * 
 * @author paul
 *
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(schema="users", name="telephone")
public class Telephone {
	
	@Id
	private Long id;
	
	@Column(name="telephone_number", nullable = false)
	@Size(max = 20)
	private String telephoneNumber;
	
	@Column(name="customer_id", nullable = false)
	private Long customerId;
	
	@Column(nullable = false)
	private boolean activated;
}
