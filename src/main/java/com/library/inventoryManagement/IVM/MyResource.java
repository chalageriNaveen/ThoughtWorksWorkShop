package com.library.inventoryManagement.IVM;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("inventoryManagement")
public class MyResource {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/create")
	public String procureBooks(String input) {
		InventoryManagement bean;
		try {
			bean = new ObjectMapper().readValue(input, InventoryManagement.class);
			InventoryManagementDAO ivmdao = new InventoryManagementDAO();
			ivmdao.addInventoryManagement(bean);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Inventory Management";
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search/{isbn}")
	public Response searchForBooks(@PathParam("isbn") String ISBN) {
		InventoryManagement ivm = null;
		String jsonString = "";

		try {
			InventoryManagementDAO dao = new InventoryManagementDAO();
			ivm = dao.serachInventory(ISBN);
			if (ivm == null) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			jsonString = new ObjectMapper().writeValueAsString(ivm);
			return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@PUT
	@Path("/update/{isbn}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInventory(@PathParam("isbn") String ISBN, String status) {
		String bookStatus = "";
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			map = new ObjectMapper().readValue(status, new TypeReference<Map<String, Object>>() {
			});
			bookStatus = (String) map.get("status");
			System.out.println("Status: " + bookStatus);

			InventoryManagementDAO dao = new InventoryManagementDAO();
			int count = dao.updateInventoryManagement(ISBN, bookStatus);
			if (count == 0) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			return Response.ok().build();
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@DELETE
	@Path("/{isbn}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteInventory(@PathParam("isbn") String ISBN) {
		InventoryManagementDAO dao = new InventoryManagementDAO();
		int count = dao.deleteInventoryManagement(ISBN);
		if (count == 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.ok().build();
	}
}
