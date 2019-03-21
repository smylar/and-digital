package com.andd.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.andd.entity.Telephone;
import com.andd.repository.TelephoneRepository;

/**
 * Unit tests testing the telephone service
 * 
 * @author paul
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TelephoneServiceTests {

	@Mock
	private TelephoneRepository telephoneRepository;
	
	@InjectMocks
	private TelephoneService telephoneService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	
	@Test
	public void testGetAllAndOrdering() {
		Telephone t1 = defaultTelephone();
		
		Telephone t2 = Telephone.builder().id(102L)
				   .customerId(49L)
				   .telephoneNumber("+44 222222222")
				   .activated(false).build();
		when(telephoneRepository.findAll()).thenReturn(List.of(t1, t2));
		
		List<Telephone> result = telephoneService.findAll();
		assertEquals(2, result.size());

		assertEquals(t2, result.get(0));
		assertEquals(t1, result.get(1));
	}
	
	@Test
	public void testGetAllForCustomerPositiveResult() {
		Telephone t1 = defaultTelephone();
		Telephone t2 = Telephone.builder().id(102L)
				   .customerId(50L)
				   .telephoneNumber("+44 222222222")
				   .activated(true).build();
		
		when(telephoneRepository.findByCustomerId(50L)).thenReturn(List.of(t1,t2).stream());
		List<Telephone> result = telephoneService.findByCustomer(50L);
		
		assertEquals(2, result.size());

		assertEquals(t2, result.get(0)); //activated one should be first
		assertEquals(t1, result.get(1));

	}
	
	@Test
	public void testGetAllForCustomerNegativeResult() {
		when(telephoneRepository.findByCustomerId(anyLong())).thenReturn(Stream.empty());
		List<Telephone> result = telephoneService.findByCustomer(50L);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testGetAllForCustomerNullArgument() {
		expectedException.expect(NullPointerException.class);
	    expectedException.expectMessage("Customer ID not supplied");
		
		telephoneService.findByCustomer(null);
	}
	
	@Test
	public void testActivate() {
		Telephone t1 = defaultTelephone();
		Telephone expected = t1.toBuilder().activated(true).build();
		when(telephoneRepository.findById(101L)).thenReturn(Optional.of(t1));
		when(telephoneRepository.save(expected)).thenReturn(expected);
		
		assertTrue(telephoneService.activate(101L));
		verify(telephoneRepository, times(1)).save(expected);
	}

	@Test
	public void testActivateNoRecord() {
		when(telephoneRepository.findById(101L)).thenReturn(Optional.empty());
		
		assertFalse(telephoneService.activate(101L));
		verify(telephoneRepository, times(0)).save(any(Telephone.class));
	}
	
	private Telephone defaultTelephone() {
		return Telephone.builder().id(101L)
				   .customerId(50L)
				   .telephoneNumber("+44 11111111111")
				   .activated(false).build();
	}
}
