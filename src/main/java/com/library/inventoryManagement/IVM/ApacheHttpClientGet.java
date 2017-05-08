package com.library.inventoryManagement.IVM;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ApacheHttpClientGet {

	public static void main(String[] args) {
		ApacheHttpClientGet apClient = new ApacheHttpClientGet();
		apClient.addingBook();
		apClient.rentBook();
	}

	public void addingBook() {
		Client client = Client.create();
		WebResource webResource = client.resource("http://10.132.126.74:8080/Library/v1/books");

		String input = "{\"id\":\"100\",\"userId\":\"12345\",\"name\":\"Core java\",\"Author\":\"Andrew Hall\",\"isbn\":\"sc10\",\"genre\":\"CS\",\"price\":\"250\"}";
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		System.out.println("Got the Output: " + output);
	}

	/*Just to check the functionality used this call. Not exactly as per the Inventory management. I couldnt get the end points from respective teammates*/
	public void rentBook() {
		Client client = Client.create();
		WebResource webResource = client.resource("http://10.132.126.74:8080/BookRentalApp/webapi/bookrental/rent");

		String input = "{\"isbn\":\"sc10\",\"userId\":\"12345\"}";

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);
	}

}
