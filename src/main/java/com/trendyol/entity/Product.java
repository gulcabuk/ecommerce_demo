package com.trendyol.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Product is used to define items on system.
 * 
 * @author zehragulcabukkeskin
 */
@ApiModel(description = "Product")
public class Product {
	
	/**
	 * Identifier of the {@link Product}.
	 */
	@Expose
	private long id;

	/**
	 * title of the product
	 */
	@Expose
	private String title;

	/**
	 * price of the product
	 */
	@Expose
	private BigDecimal price;

	/**
	 * category of product
	 */
	@Expose
	private Category category;

	/**
	 * Default constructor for the {@link Product}
	 * 
	 * @param title
	 *            title of the product
	 * @param price
	 *            price of the product
	 */
	public Product(String title, double price, Category category) {
		this.title = title;
		this.price = BigDecimal.valueOf(price);
		this.category = category;
	}
	
	public Product() {}

	/**
	 * GETTERS
	 */
	@ApiModelProperty(value = "title")
	public String getTitle() {
		return title;
	}
	
	@ApiModelProperty(value = "unit price")
	public double getPrice() {
		return price.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}

	@ApiModelProperty(value = "category of product")
	public Category getCategory() {
		return category;
	}
	
	@ApiModelProperty(value = "unique identifier")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public BigDecimal getPriceBigDecimal() {
		return price;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public static Product fromJson(String jsonStr) throws JsonSyntaxException {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonStr, Product.class);
	}
	
	public JsonObject toJson() {
		return new JsonParser().parse(toJsonString()).getAsJsonObject();
	}

	String toJsonString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}
}
