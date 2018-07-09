package com.trendyol.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.trendyol.JsonProperties;
import com.trendyol.entity.Product;
import com.trendyol.logger.ServiceLogger;
import com.trendyol.storage.Postgres;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("products")
@Api(value = "products")
@Produces({ "application/json" })
public class ProductService {

	/**
	 * Postgresql utility class.
	 */
	private Postgres postgres = Postgres.getInstance();

	/**
	 * Saves {@link Product} object with given data if possible and returns result
	 * of the action.
	 *
	 * @param jsonStr
	 *            data of the {@link Product} object to be saved.
	 * @param request
	 *            request from client.
	 * @return {@link Response} instance which contains {@link JsonObject} formatted
	 *         result of the action.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates product", response = Product.class)
	public Response createProduct(@BeanParam Product product, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			String jsonStr = product.toJson().toString();
			ServiceLogger.logSaveObjectRequest(jsonStr, Product.class);
			int savedProductId = postgres.saveObject(product);
			if (savedProductId != 0) {
				product.setId(savedProductId);
				response.add(JsonProperties.PRODUCT, product.toJson());
			} else {
				response.addProperty(JsonProperties.MESSAGE,
						String.format("\"saving product failed: {request:%s}\"", jsonStr));
			}
		} catch (Exception e) {
			ServiceLogger.logSaveObjectError(product, Product.class, e);
			response.addProperty(JsonProperties.MESSAGE,
					String.format("\"unexpected error during creating product: {request:%s}\"", product));
		}
		return ServiceUtils.createResponse200(response.toString());
	}

}
