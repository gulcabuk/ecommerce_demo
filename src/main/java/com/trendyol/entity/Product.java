package com.trendyol.entity;

import java.math.BigDecimal;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Product is used to define items on system.
 * 
 * @author zehragulcabukkeskin
 */
public class Product {

	/**
	 * title of the product
	 */
	private String title;

	/**
	 * price of the product
	 */
	private BigDecimal price;

	/**
	 * category of product
	 */
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

	/**
	 * GETTERS
	 */
	public String getTitle() {
		return title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Category getCategory() {
		return category;
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
}
