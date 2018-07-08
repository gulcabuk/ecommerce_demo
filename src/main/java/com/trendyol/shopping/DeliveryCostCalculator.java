package com.trendyol.shopping;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * {@link DeliveryCostCalculator} class is used to calculate delivery cost for
 * {@link ShoppingCart}s dynamically.
 * 
 * @author zehragulcabukkeskin
 *
 */
public class DeliveryCostCalculator {

	/**
	 * Cost per delivery
	 */
	private BigDecimal costPerDelivery;
	/**
	 * Cost per product
	 */
	private BigDecimal costPerProduct;
	/**
	 * Fixed cost
	 */
	private BigDecimal fixedCost;

	/**
	 * Default constructor.
	 * 
	 * @param costPerDelivery
	 *            Cost per delivery
	 * @param costPerProduct
	 *            Cost per products
	 * @param fixedCost
	 *            fixed cost
	 */
	public DeliveryCostCalculator(double costPerDelivery, double costPerProduct, double fixedCost) {
		this.costPerDelivery = BigDecimal.valueOf(costPerDelivery);
		this.costPerProduct = BigDecimal.valueOf(costPerProduct);
		this.fixedCost = BigDecimal.valueOf(fixedCost);
	}

	/**
	 * Calculates delivery cost for given shopping cart.
	 * 
	 * @param shoppingCart
	 *            to calculate delivery cost according to
	 *            {@link ShoppingCart#findNumberOfDeliveries()} and
	 *            {@link ShoppingCart#findNumberOfProducts()}
	 * @return calculated delivery cost.
	 */
	public double calculateFor(ShoppingCart shoppingCart) {
		return costPerDelivery.multiply(BigDecimal.valueOf(shoppingCart.findNumberOfDeliveries()))
				.add(costPerProduct.multiply(BigDecimal.valueOf(shoppingCart.findNumberOfProducts()))).add(fixedCost)
				.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
}
