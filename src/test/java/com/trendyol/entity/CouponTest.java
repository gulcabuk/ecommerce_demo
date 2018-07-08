package com.trendyol.entity;

import static com.trendyol.TestData.DISCOUNT_AMOUNT_5_0;
import static com.trendyol.TestData.DISCOUNT_RATE_20_0;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.trendyol.TestData;

public class CouponTest {

	@Test
	public void create_withRate() throws Exception {
		// execute
		Coupon coupon = new Coupon(TestData.MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);

		// assert
		assertEquals(TestData.MINIMUM_AMOUNT_100, coupon.getMinimumAmount(), 0);
		assertEquals(DISCOUNT_RATE_20_0, coupon.getDiscount(), 0);
		assertEquals(DiscountType.Rate, coupon.getDiscountType());
	}
	
	@Test
	public void create_withAmount() throws Exception {
		// execute
		Coupon coupon = new Coupon(TestData.MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);

		// assert
		assertEquals(TestData.MINIMUM_AMOUNT_100, coupon.getMinimumAmount(), 0);
		assertEquals(DISCOUNT_AMOUNT_5_0, coupon.getDiscount(), 0);
		assertEquals(DiscountType.Amount, coupon.getDiscountType());
	}

}
