package com.trendyol.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.trendyol.shopping.ShoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class is used to encapsulates {@link Campaign}s which are applied to
 * {@link Product}s according to their {@link Category}ies.
 * 
 * @author zehragulcabukkeskin
 */
@ApiModel(description = "Campaign")
public class Campaign {

	/**
	 * Identifier of the {@link Campaign}.
	 */
	@Expose
	private long id;

	/**
	 * Category of {@link Campaign}.
	 */
	@Expose
	private Category category;
	/**
	 * Discount amount of {@link Campaign}.
	 */
	@Expose
	private double discount;
	/**
	 * Minimum quantity of product in {@link ShoppingCart} for {@link #category} of
	 * {@link Campaign}.
	 */
	@Expose
	private int quantity;
	/**
	 * {@link DiscountType} can be rate or amount.
	 */
	@Expose
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
	
	public Campaign() {}

	/**
	 * GETTERS
	 */
	@ApiModelProperty(value = "category")
	public Category getCategory() {
		return category;
	}

	@ApiModelProperty(value = "discount")
	public double getDiscount() {
		return discount;
	}

	@ApiModelProperty(value = "minimum quantity")
	public int getQuantity() {
		return quantity;
	}

	@ApiModelProperty(value = "discount type")
	public DiscountType getDiscountType() {
		return discountType;
	}

	@ApiModelProperty(value = "unique identifier")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
			totalPrice = totalPrice
					.add(entry.getKey().getPriceBigDecimal().multiply(BigDecimal.valueOf(productQuantity)));
		}

		// calculate discount..
		BigDecimal discount = BigDecimal.ZERO;
		if (totalQuantity >= getQuantity()) {
			discount = getDiscountType().calculateDiscount(totalPrice, this);

		}
		return discount;

	}

	public static Campaign fromJson(String jsonStr) throws JsonSyntaxException {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonStr, Campaign.class);
	}

	public JsonObject toJson() {
		return new JsonParser().parse(toJsonString()).getAsJsonObject();
	}

	String toJsonString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}

}
