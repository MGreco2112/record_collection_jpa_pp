package com.recordcollection.recorddatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecorddatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecorddatabaseApplication.class, args);
	}

	//Sort records alphabetically by Name, by Artist, and by Collector
	/*
	Create Offer Object:
		-Collector sender: Collector asking for the trade/sale
		-Collector receiver: Collector getting the offer for their item
		-Record record: the item sender is interested in
		-Integer offerAmount: currency offered for record
	 */
	//Create Admin Entity that has similar permissions to creating Records as Users

}
