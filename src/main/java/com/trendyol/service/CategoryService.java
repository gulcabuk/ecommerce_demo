package com.trendyol.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.trendyol.JsonProperties;
import com.trendyol.entity.Category;
import com.trendyol.entity.Product;
import com.trendyol.logger.ServiceLogger;
import com.trendyol.storage.Postgres;

import io.swagger.annotations.Api;

@Path("categories")
@Api(value = "categories")
@Produces({ "application/json" })
public class CategoryService {
	
	/**
	 * Postgresql utility class.
	 */
	private Postgres postgres = Postgres.getInstance();

	/**
	 * Saves {@link Category} object with given data if possible and returns result
	 * of the action.
	 *
	 * @param jsonStr
	 *            data of the {@link Category} object to be saved.
	 * @param request
	 *            request from client.
	 * @return {@link Response} instance which contains {@link JsonObject} formatted
	 *         result of the action.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCategory(String jsonStr, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			ServiceLogger.logSaveObjectRequest(jsonStr, Product.class);
			Category category = Category.fromJson(jsonStr);
			int savedCategoryId = postgres.saveObject(category);
			if (savedCategoryId != 0) {
				response.addProperty(JsonProperties.ID, savedCategoryId);
				response.addProperty(JsonProperties.SUCCESS, true);
			} else {
				response.addProperty(JsonProperties.FAILURE,
						String.format("\"saving category failed: {request:%s}\"", jsonStr));
			}
		} catch (Exception e) {
			ServiceLogger.logSaveObjectError(jsonStr, Product.class, e);
			response.addProperty(JsonProperties.FAILURE,
					String.format("\"unexpected error during creating category: {request:%s}\"", jsonStr));
		}
		return ServiceUtils.createResponse200(response.toString());
	}

}
