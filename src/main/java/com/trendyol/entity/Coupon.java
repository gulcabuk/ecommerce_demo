package com.trendyol.entity;

import java.math.BigDecimal;

import com.trendyol.shopping.ShoppingCart;

/**
 * This class is used to encapsulates {@link Coupon}s which are applied to
 * {@link ShoppingCart}
 * 
 * @author zehragulcabukkeskin
 */
public class Coupon {

	/**
	 * Minimum amount which is needed by coupon
	 */
	private double minimumAmount;
	/**
	 * Discount amount.
	 */
	private double discount;
	/**
	 * {@link DiscountType} of the coupon.
	 */
	private DiscountType discountType;

	/**
	 * Default contstructor of the {@link Coupon}.
	 * 
	 * @param minimumAmount
	 *            minimum amount which is needed by coupon
	 * @param discount
	 *            discount amount
	 * @param discountType
	 *            discount type which is used during calculation.
	 */
	public Coupon(double minimumAmount, double discount, DiscountType discountType) {
		this.minimumAmount = minimumAmount;
		this.discount = discount;
		this.discountType = discountType;
	}

	/**
	 * GETTERS
	 */
	double getMinimumAmount() {
		return minimumAmount;
	}

	double getDiscount() {
		return discount;
	}

	DiscountType getDiscountType() {
		return discountType;
	}

	/**
	 * Calculates discount for this coupon is calculated by {@link DiscountType}.
	 * 
	 * @param currentTotalPrice
	 *            current total price is used to calculate discount.
	 * @return calculated discount.
	 */
	public BigDecimal calculateDiscount(BigDecimal currentTotalPrice) {
		return getDiscountType().calculateDiscount(currentTotalPrice, this);
	}

}
