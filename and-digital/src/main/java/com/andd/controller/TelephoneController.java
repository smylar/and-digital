package com.andd.controller;

import java.util.List;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.andd.controller.exception.RequestException;
import com.andd.entity.Telephone;
import com.andd.service.TelephoneService;

import lombok.extern.slf4j.Slf4j;

/**
 * REST endpoints for handling telephone numbers
 * 
 * @author paul
 *
 */
@RestController
@Slf4j
@RequestMapping("/telephone")
public class TelephoneController {
	
	@Autowired
	private TelephoneService telephoneService;
	
	/**
	 * End point that retrieves all phone numbers
	 * 
	 * @param request
	 * @return List of {@link Telephone}
	 */
	@GetMapping(value="/all")
    @ResponseBody
	public List<Telephone> getAll(final HttpServletRequest request) {
		return tryCatch(request, () -> telephoneService.findAll());
	}
	
	/**
	 * End point that retrives all phone numbers for a given customer ID
	 * 
	 * @param request
	 * @param id		The customer ID
	 * @return List of {@link Telephone}
	 */
	@GetMapping(value="/customer/{id}")
    @ResponseBody
	public List<Telephone> getByCustomer(final HttpServletRequest request, @PathVariable final Long id) {
		return tryCatch(request, () -> telephoneService.findByCustomer(id));
	}

	/**
	 * End point to activate phone number
	 * 
	 * @param request
	 * @param id ID of the telephone record
	 * @return The new activated value
	 */
	@PutMapping(value="/activate/{id}")
    @ResponseBody
	public Boolean activate(final HttpServletRequest request, @PathVariable final Long id) {
		return tryCatch(request, () -> telephoneService.activate(id));
	}
	
	private <R> R tryCatch(HttpServletRequest request, Supplier<R> supplier) {
		try {
			return supplier.get();
		} catch (Exception e) {
			log.error("Error on {}", request.getServletPath(), e);
			throw new RequestException(e);
		}
	}

}
