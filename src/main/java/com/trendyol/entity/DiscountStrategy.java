package com.trendyol.entity;

import java.math.BigDecimal;

/**
 * {@link DiscountStrategy} is used to define methods which is implemented by
 * {@link DiscountType}s.
 * 
 * @author zehragulcabukkeskin
 *
 */
interface DiscountStrategy {

	/**
	 * Calculates discount according to {@link DiscountType}, if given amount and
	 * coupon expectations are match.
	 * 
	 * @param amount
	 *            current amount of shopping cart.
	 * @param coupon
	 *            coupon instance.
	 * @return calculated discount.(can be 0)
	 */
	BigDecimal calculateDiscount(BigDecimal amount, Coupon coupon);

	/**
	 * Calculates discount according to {@link DiscountType}, if given amount and
	 * campaign expectations are match.
	 * 
	 * @param amount
	 *            current amount of shopping cart.
	 * @param campaign
	 *            campaign instance.
	 * @return calculated discount.(can be 0)
	 */
	BigDecimal calculateDiscount(BigDecimal amount, Campaign campaign);

}
