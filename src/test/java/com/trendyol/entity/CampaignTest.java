package com.trendyol.entity;

import static org.junit.Assert.*;
import static com.trendyol.TestData.*;

import org.junit.Test;

public class CampaignTest {

	@Test
	public void create_withRate() throws Exception {
		// execute
		Category category = prepareCategory_ELBISE();
		Campaign campaign = new Campaign(category, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);

		// assert
		assertEquals(category, campaign.getCategory());
		assertEquals(DISCOUNT_RATE_20_0, campaign.getDiscount(), 0);
		assertEquals(QUANTITY_3, campaign.getQuantity());
		assertEquals(DiscountType.Rate, campaign.getDiscountType());

	}

	@Test
	public void create_withAmount() throws Exception {
		// execute
		Category category = prepareCategory_ELBISE();
		Campaign campaign = new Campaign(category, DISCOUNT_AMOUNT_5_0, QUANTITY_2, DiscountType.Amount);

		// assert
		assertEquals(category, campaign.getCategory());
		assertEquals(DISCOUNT_AMOUNT_5_0, campaign.getDiscount(), 0);
		assertEquals(QUANTITY_2, campaign.getQuantity());
		assertEquals(DiscountType.Amount, campaign.getDiscountType());
	}

}
