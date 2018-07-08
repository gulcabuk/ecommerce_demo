package com.trendyol.service;

import javax.ws.rs.core.Response;

public class ServiceUtils {
	
	/**
	 * Extracted method for status 200 responses reuse.
	 *
	 * @param object Response's status(200)'s entity parameter.
	 * @return Response status code 200 with entity object.
	 */
	public static Response createResponse200(Object object) {
		return Response.status(200).entity(object).build();
	}

}
