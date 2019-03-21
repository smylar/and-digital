package com.andd.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.andd.Application;
import com.andd.entity.Telephone;
import com.andd.repository.TelephoneRepository;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

/**
 * Integration tests for retrieving/manipulating telephone numbers
 * 
 * @author paul
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class TelephoneControllerTests {
	
	private static final String BASE_URL = "http://localhost:%s/%s/telephone/%s";
	
	private HttpClient http = HttpClients.createDefault();
	
	private Gson gson = new Gson();
	
	@SuppressWarnings("serial")
	private Type telephoneListType = new TypeToken<ArrayList<Telephone>>(){}.getType();
	
	@Value("${server.port}")
	private String port;
	
	@Value("${server.servlet.context-path}")
	private String context;
	
	@Autowired
	private TelephoneRepository telephoneRepository;
	
	@Test
	public void testGetAll() throws ClientProtocolException, IOException {
		List<Telephone> result = getList("all");
		assertEquals(3, result.size());
		
		Map<Long, Integer> customerGroup = result.stream()
												 .collect(Collectors.groupingBy(t -> t.getCustomerId() , 
														  Collectors.summingInt(t -> 1)));
		assertEquals(2, customerGroup.getOrDefault(4L, 0), 0);
		assertEquals(1, customerGroup.getOrDefault(5L, 0), 0);
	}
	
	@Test
	public void testGetAllForCustomerPositiveResult() throws ClientProtocolException, IOException {
		List<Telephone> result = getList("customer/5");
		assertEquals(1, result.size());
		assertEquals("+44 78462 39993993", result.get(0).getTelephoneNumber());
	}
	
	@Test
	public void testGetAllForCustomerNegativeResult() throws ClientProtocolException, IOException {
		List<Telephone> result = getList("customer/10");
		assertEquals(0, result.size());
	}
	
	@Test
	public void testActivate() throws ClientProtocolException, IOException {
		String result = getString("activate/7");
		assertEquals("true", result);
		assertTrue(telephoneRepository.findById(7L).map(t -> t.isActivated()).orElse(false));
		assertFalse(telephoneRepository.findById(8L).map(t -> t.isActivated()).orElse(true));
	}
	
	@Test
	public void testActivateNoRecord() throws ClientProtocolException, IOException {
		String result = getString("activate/77");
		assertEquals("false", result);
	}
	
	private List<Telephone> getList(String path) throws ClientProtocolException, IOException {
		HttpResponse response = http.execute(new HttpGet(String.format(BASE_URL, port, context, path)));
		try (Reader reader = new InputStreamReader(response.getEntity().getContent())) {
			 return gson.fromJson(reader, telephoneListType);
		}
	}
	
	private String getString(String path) throws ClientProtocolException, IOException {
		HttpResponse response = http.execute(new HttpPut(String.format(BASE_URL, port, context, path)));
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
			 return reader.readLine();
		}
	}
}
