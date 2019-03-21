package com.andd.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andd.entity.Telephone;
import com.andd.repository.TelephoneRepository;

@Service
public class TelephoneService {
	
	@Autowired
	private TelephoneRepository telephoneRepository;
	
	/**
	 * Get all the telephone numbers
	 * 
	 * @return List of {@link Telephone} grouped by customer ID
	 */
	public List<Telephone> findAll() {
		return telephoneRepository.findAll()
								  .stream()
								  .sorted((t1,t2) -> t1.getCustomerId().intValue() - t2.getCustomerId().intValue()) 
								  .collect(Collectors.toList());
	}
	
	/**
	 * Get all the telephone number records for a customer
	 * 
	 * @param id ID of the customer
	 * @return List of {@link Telephone}, activated records are listed first
	 */
	@Transactional
	public List<Telephone> findByCustomer(Long id) {
		Objects.requireNonNull(id, "Customer ID not supplied");
		return telephoneRepository.findByCustomerId(id)
								  .sorted((t1,t2) -> compareActivation(t1) - compareActivation(t2))
								  .collect(Collectors.toList());
	}
	
	/**
	 * Find a telephone record and change status to activated
	 * 
	 * @param id ID of the telephone record
	 * @return The new status
	 */
	public boolean activate(Long id) {
		Objects.requireNonNull(id, "Telephone ID not supplied");
		return telephoneRepository.findById(id)
						   		  .map(t -> t.toBuilder().activated(true).build())
						   		  .map(telephoneRepository::save)
						   		  .map(Telephone::isActivated)
						   		  .orElse(false);
	}
	
	private int compareActivation(Telephone tel) {
		return tel.isActivated() ? 0 : 1;
	}
}
