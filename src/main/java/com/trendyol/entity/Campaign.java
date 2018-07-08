package com.trendyol.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import com.trendyol.shopping.ShoppingCart;

import java.util.Set;

/**
 * This class is used to encapsulates {@link Campaign}s which are applied to
 * {@link Product}s according to their {@link Category}ies.
 * 
 * @author zehragulcabukkeskin
 */
public class Campaign {

	/**
	 * Category of {@link Campaign}.
	 */
	private Category category;
	/**
	 * Discount amount of {@link Campaign}.
	 */
	private double discount;
	/**
	 * Minimum quantity of product in {@link ShoppingCart} for {@link #category} of
	 * {@link Campaign}.
	 */
	private int quantity;
	/**
	 * {@link DiscountType} can be rate or amount.
	 */
	private DiscountType discountType;

	/**
	 * Default constructor for {@link Campaign}.
	 * 
	 * @param category
	 *            campaign is applied according to categories.
	 * @param discount
	 *            discount amount according to {@link DiscountType}.
	 * @param quantity
	 *            quantity of the {@link Product} which is used to decide whether
	 *            campaign is applied or not.
	 * @param discountType
	 *            type of the discount (rate or amount)
	 */
	public Campaign(Category category, double discount, int quantity, DiscountType discountType) {
		this.category = category;
		this.discount = discount;
		this.quantity = quantity;
		this.discountType = discountType;
	}

	/**
	 * GETTERS
	 */
	Category getCategory() {
		return category;
	}

	double getDiscount() {
		return discount;
	}

	int getQuantity() {
		return quantity;
	}

	DiscountType getDiscountType() {
		return discountType;
	}

	/**
	 * Calculates discount amount according to product map which belongs to a
	 * {@link Category}.
	 * 
	 * @param productMap
	 *            product map which belongs to category.
	 * @return calculated discount.
	 */
	public BigDecimal calculateDiscount(Map<Product, Integer> productMap) {
		int totalQuantity = 0;
		BigDecimal totalPrice = BigDecimal.ZERO;
		// calculate totals..
		Set<Entry<Product, Integer>> productSet = productMap.entrySet();
		for (Entry<Product, Integer> entry : productSet) {
			Integer productQuantity = entry.getValue();
			totalQuantity += productQuantity;
			totalPrice = totalPrice.add(entry.getKey().getPrice().multiply(BigDecimal.valueOf(productQuantity)));
		}

		// calculate discount..
		BigDecimal discount = BigDecimal.ZERO;
		if (totalQuantity >= getQuantity()) {
			discount = getDiscountType().calculateDiscount(totalPrice, this);

		}
		return discount;

	}

}
