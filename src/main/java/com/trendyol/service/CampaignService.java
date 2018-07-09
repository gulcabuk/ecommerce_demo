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
import com.trendyol.entity.Campaign;
import com.trendyol.logger.ServiceLogger;
import com.trendyol.storage.Postgres;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("campaigns")
@Api(value = "campaigns")
@Produces({ "application/json" })
public class CampaignService {

	/**
	 * Postgresql utility class.
	 */
	private Postgres postgres = Postgres.getInstance();

	/**
	 * Saves {@link campaign} object with given data if possible and returns result
	 * of the action.
	 *
	 * @param jsonStr
	 *            data of the {@link campaign} object to be saved.
	 * @param request
	 *            request from client.
	 * @return {@link Response} instance which contains {@link JsonObject} formatted
	 *         result of the action.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates campaign", response = Campaign.class)
	public Response createCampaign(@BeanParam Campaign campaign, @Context HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			String campaignJson = campaign.toJson().toString();
			ServiceLogger.logSaveObjectRequest(campaignJson, Campaign.class);
			int savedcampaignId = postgres.saveObject(campaign);
			if (savedcampaignId != 0) {
				campaign.setId(savedcampaignId);
				response.add(JsonProperties.CAMPAIGN, campaign.toJson());
			} else {
				response.addProperty(JsonProperties.MESSAGE,
						String.format("\"saving campaign failed: {request:%s}\"", campaignJson));
			}
		} catch (Exception e) {
			ServiceLogger.logSaveObjectError(campaign, Campaign.class, e);
			response.addProperty(JsonProperties.MESSAGE,
					String.format("\"unexpected error during creating campaign: {request:%s}\"", campaign));
		}
		return ServiceUtils.createResponse200(response.toString());
	}

}
