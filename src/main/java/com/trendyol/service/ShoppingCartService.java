package com.trendyol.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.trendyol.JsonProperties;
import com.trendyol.entity.Product;
import com.trendyol.logger.ServiceLogger;
import com.trendyol.shopping.ShoppingCart;
import com.trendyol.storage.Postgres;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("shoppingCarts")
@Api(value = "shoppingCarts")
@Produces({ "application/json" })
public class ShoppingCartService {

	/**
	 * Postgresql utility class.
	 */
	private Postgres postgres = Postgres.getInstance();

	/**
	 * Saves {@link ShoppingCart} object with given data if possible and returns
	 * result of the action.
	 *
	 * @param jsonStr
	 *            data of the {@link shoppingCart} object to be saved.
	 * @param request
	 *            request from client.
	 * @return {@link Response} instance which contains {@link JsonObject} formatted
	 *         result of the action.
	 */
	@POST
	@Path("createShoppingCart")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates shopping cart", response = ShoppingCart.class)
	public Response createShoppingCart(@BeanParam ShoppingCart shoppingCart, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			String jsonStr = shoppingCart.toJson().toString();
			ServiceLogger.logSaveObjectRequest(jsonStr, ShoppingCart.class);
			int savedshoppingCartId = postgres.saveObject(shoppingCart);
			if (savedshoppingCartId != 0) {
				shoppingCart.setId(savedshoppingCartId);
				response.add(JsonProperties.SHOPPING_CART, shoppingCart.toJson());
			} else {
				response.addProperty(JsonProperties.MESSAGE,
						String.format("\"saving shoppingCart failed: {request:%s}\"", jsonStr));
			}
		} catch (Exception e) {
			ServiceLogger.logSaveObjectError(shoppingCart, ShoppingCart.class, e);
			response.addProperty(JsonProperties.MESSAGE,
					String.format("\"unexpected error during creating shoppingCart: {request:%s}\"", shoppingCart));
		}
		return ServiceUtils.createResponse200(response.toString());
	}

	/**
	 * Adding {@link Product} to {@link ShoppingCart} object whose id's is given.
	 * 
	 * @param shoppingCartId
	 *            identifier of shopping cart.
	 * @param productId
	 *            identifier of product
	 * @param quantity
	 *            quantity of product
	 * @param jsonStr
	 *            data of the {@link shoppingCart}.
	 * @param request
	 *            request from client.
	 * @return {@link Response} instance which contains {@link JsonObject} formatted
	 *         result of the action.
	 */
	@PUT
	@Path("{cartId}/products/{productId}/quantity/{quantity}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProductToShoppingCart(@PathParam("shoppingCartId") long shoppingCartId,
			@PathParam("productId") long productId, @PathParam("quantity") int quantity,
			@Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			ServiceLogger.logaddProductToShoppingCartRequest(shoppingCartId, productId, quantity, ShoppingCart.class);
			ShoppingCart cart = postgres.getObjectByID(shoppingCartId, ShoppingCart.class);
			if (cart != null) {
				Product product = postgres.getObjectByID(productId, Product.class);
				if (product != null) {
					cart.addItem(product, quantity);
					response.add(JsonProperties.SHOPPING_CART, cart.toJson());
				} else {
					response.addProperty(JsonProperties.MESSAGE,
							String.format("\"not found product: {productId:%s}\"", productId));
				}
			} else {
				response.addProperty(JsonProperties.MESSAGE,
						String.format("\"not found shopping cart: {cartId:%s}\"", shoppingCartId));
			}
		} catch (Exception e) {
			response.addProperty(JsonProperties.MESSAGE, String.format(
					"\"unexpected error during adding product to shopping cart: {cartId:%s, productId:%s, quantity:%s}\"",
					shoppingCartId, productId, quantity));
			ServiceLogger.logAddShoppingCartObjectError(response.toString(), ShoppingCart.class, e);
		}
		return ServiceUtils.createResponse200(response.toString());
	}

	/**
	 * Printing {@link ShoppingCart} object whose id's is given.
	 * 
	 * @param shoppingCartId
	 *            identifier of shopping cart.
	 * @param request
	 *            request from client.
	 * @return {@link Response} instance which contains {@link JsonObject} formatted
	 *         result of the action.
	 */
	@GET
	@Path("{cartId}/print")
	@Produces(MediaType.APPLICATION_JSON)
	public Response print(@PathParam("shoppingCartId") long shoppingCartId, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			ServiceLogger.logPrintObjectRequest(ShoppingCart.class, shoppingCartId);
			ShoppingCart cart = postgres.getObjectByID(shoppingCartId, ShoppingCart.class);
			if (cart != null) {
				response.add(JsonProperties.REPORT, cart.print());
			} else {
				response.addProperty(JsonProperties.MESSAGE,
						String.format("\"not found shopping cart: {shoppingCartId:%s}\"", shoppingCartId));
			}
		} catch (Exception e) {
			ServiceLogger.logPrintObjectError(ShoppingCart.class, shoppingCartId, e);
			response.addProperty(JsonProperties.MESSAGE,
					String.format("\"unexpected error during printing shopping cart: {id:%s}\"", shoppingCartId));
		}
		return ServiceUtils.createResponse200(response.toString());
	}

}
