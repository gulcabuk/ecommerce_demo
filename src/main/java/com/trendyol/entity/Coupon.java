package com.trendyol.entity;

import java.math.BigDecimal;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.trendyol.shopping.ShoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class is used to encapsulates {@link Coupon}s which are applied to
 * {@link ShoppingCart}
 * 
 * @author zehragulcabukkeskin
 */
@ApiModel(description = "Coupon")
public class Coupon {

	/**
	 * Identifier of the {@link Coupon}.
	 */
	@Expose
	private long id;
	/**
	 * Minimum amount which is needed by coupon
	 */
	@Expose
	private double minimumAmount;
	/**
	 * Discount amount.
	 */
	@Expose
	private double discount;
	/**
	 * {@link DiscountType} of the coupon.
	 */
	@Expose
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
	
	public Coupon() {}

	/**
	 * GETTERS
	 */
	@ApiModelProperty(value = "minimum amount")
	public double getMinimumAmount() {
		return minimumAmount;
	}
	@ApiModelProperty(value = "discount")
	public double getDiscount() {
		return discount;
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
	 * Calculates discount for this coupon is calculated by {@link DiscountType}.
	 * 
	 * @param currentTotalPrice
	 *            current total price is used to calculate discount.
	 * @return calculated discount.
	 */
	public BigDecimal calculateDiscount(BigDecimal currentTotalPrice) {
		return getDiscountType().calculateDiscount(currentTotalPrice, this);
	}
	
	public static Coupon fromJson(String jsonStr) throws JsonSyntaxException {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonStr, Coupon.class);
	}
	
	public JsonObject toJson() {
		return new JsonParser().parse(toJsonString()).getAsJsonObject();
	}

	String toJsonString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}

}
