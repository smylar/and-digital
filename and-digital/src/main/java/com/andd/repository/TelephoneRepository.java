package com.andd.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andd.entity.Telephone;

/**
 * Repository for accessing telephone information
 * 
 * @author paul
 *
 */
public interface TelephoneRepository extends JpaRepository<Telephone, Long> {
	
	/**
	 * Find telephone record by customer ID
	 * 
	 * @param id
	 * @return
	 */
	public Stream<Telephone> findByCustomerId(Long id);
}
