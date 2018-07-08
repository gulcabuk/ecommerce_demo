package com.trendyol;

import com.trendyol.entity.Category;

public class TestData {

	public static final String PARENT_CATEGORY_KADIN = "Kadın";
	public static final String CATEGORY_ELBISE = "Elbise";
	public static final String CATEGORY_SAAT = "Saat";
	public static final String PARENT_CATEGORY_GIYIM = "Giyim";
	public static final String PARENT_CATEGORY_AKSESUAR = "Aksesuar";

	public static final String TITLE_K_KIRMIZI_ELBISE = "Kadın Kırmızı Uzun Elbise";
	public static final String TITLE_K_SIYAH_ELBISE = "Kadın Siyah File Detaylı Elbise";
	public static final String TITLE_K_NINE_WEST_SAAT = "Nine West Watch";

	public static final double PRICE_49_99 = 49.99;
	public static final double PRICE_39_99 = 39.99;
	public static final double PRICE_199_99 = 199.99;

	public static final int QUANTITY_2 = 2;
	public static final int QUANTITY_3 = 3;

	public static final double DISCOUNT_RATE_20_0 = 20.0;
	public static final double DISCOUNT_AMOUNT_5_0 = 5.0;
	public static final double DISCOUNT_AMOUNT_50_0 = 50.0;

	public static final double MINIMUM_AMOUNT_100 = 100.00;

	/**
	 * Prepares test category.
	 */
	public static Category prepareCategory_ELBISE() {
		Category category = new Category(CATEGORY_ELBISE);
		category.setParentCategory(prepareCategory_GIYIM());
		return category;
	}

	/**
	 * Prepares test category.
	 */
	public static Category prepareCategory_GIYIM() {
		return new Category(PARENT_CATEGORY_GIYIM);
	}

	/**
	 * Prepares test category.
	 */
	public static Category prepareCategory_AKSESUAR() {
		return new Category(PARENT_CATEGORY_AKSESUAR);
	}

	/**
	 * Prepares test category.
	 */
	public static Category prepareCategory_SAAT() {
		Category category = new Category(CATEGORY_SAAT);
		category.setParentCategory(prepareCategory_AKSESUAR());
		return category;
	}

}
