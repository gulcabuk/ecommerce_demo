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
import com.trendyol.entity.Coupon;
import com.trendyol.logger.ServiceLogger;
import com.trendyol.storage.Postgres;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("coupons")
@Api(value = "coupons")
@Produces({ "application/json" })
public class CouponService {

	/**
	 * Postgresql utility class.
	 */
	private Postgres postgres = Postgres.getInstance();

	/**
	 * Saves {@link coupon} object with given data if possible and returns result of
	 * the action.
	 *
	 * @param jsonStr
	 *            data of the {@link coupon} object to be saved.
	 * @param request
	 *            request from client.
	 * @return {@link Response} instance which contains {@link JsonObject} formatted
	 *         result of the action.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates coupon", response = Coupon.class)
	public Response createCoupon(@BeanParam Coupon coupon, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			String jsonStr = coupon.toJson().toString();
			ServiceLogger.logSaveObjectRequest(jsonStr, Coupon.class);
			int savedcouponId = postgres.saveObject(coupon);
			if (savedcouponId != 0) {
				coupon.setId(savedcouponId);
				response.add(JsonProperties.COUPON, coupon.toJson());
			} else {
				response.addProperty(JsonProperties.MESSAGE,
						String.format("\"saving coupon failed: {request:%s}\"", jsonStr));
			}
		} catch (Exception e) {
			ServiceLogger.logSaveObjectError(coupon, Coupon.class, e);
			response.addProperty(JsonProperties.MESSAGE,
					String.format("\"unexpected error during creating coupon: {request:%s}\"", coupon));
		}
		return ServiceUtils.createResponse200(response.toString());
	}

}
