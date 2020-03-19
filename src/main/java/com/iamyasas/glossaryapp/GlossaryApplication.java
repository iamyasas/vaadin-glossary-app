package com.iamyasas.glossaryapp;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.iamyasas.glossaryapp.repository.DataSource;
import com.iamyasas.glossaryapp.repository.DataSourceRepository;
import com.iamyasas.glossaryapp.repository.Element;
import com.iamyasas.glossaryapp.repository.ElementRepository;

@SpringBootApplication
public class GlossaryApplication {

	private static final Logger log = LoggerFactory.getLogger(GlossaryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GlossaryApplication.class);
	}

	@Bean
	public CommandLineRunner loadData(ElementRepository repository, DataSourceRepository sourceRepository) {
		return (args) -> {
			// save a couple of customers
			
			Set<DataSource> dataSources = new HashSet<>();
			dataSources.add(new DataSource("DS1"));
			dataSources.add(new DataSource("DS2"));
			repository.save(new Element("002", "Jack", "Bauer", dataSources));
			
			dataSources = new HashSet<>();
			dataSources.add(new DataSource("DS3"));
			dataSources.add(new DataSource("DS4"));
			repository.save(new Element("012", "Chloe", "O'Brian", dataSources));
			
			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Element customer : repository.findAll()) {
				log.info(customer.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Element customer = repository.findById(1L).get();
			log.info("Customer found with findOne(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastNameStartsWithIgnoreCase('Bauer'):");
			log.info("--------------------------------------------");
			for (Element bauer : repository
					.findByNameStartsWithIgnoreCase("Bauer")) {
				log.info(bauer.toString());
			}
			log.info("");
		};
	}

}
