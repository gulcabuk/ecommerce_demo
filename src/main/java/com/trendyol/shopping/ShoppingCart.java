package com.trendyol.shopping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import static com.trendyol.JsonProperties.*;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.trendyol.entity.Campaign;
import com.trendyol.entity.Category;
import com.trendyol.entity.Coupon;
import com.trendyol.entity.Product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * {@link ShoppingCart} is a class which is used to encapsulates
 * {@link Product}s and applies some {@link Campaign}s and {@link Coupon}s over
 * it.
 * 
 * @author zehragulcabukkeskin
 */
@ApiModel(description = "Shopping Cart")
public class ShoppingCart {

	/**
	 * Identifier of the {@link ShoppingCart}.
	 */
	@Expose
	private long id;

	/**
	 * Products are stored according to their child categories.
	 */
	@Expose
	private Map<Category, Map<Product, Integer>> productsWithCategories;
	/**
	 * Total discount amount of campaigns.
	 */
	@Expose
	private BigDecimal campaignDiscount = new BigDecimal(0);
	/**
	 * Total discount amount of coupons.
	 */
	@Expose
	private BigDecimal couponDiscount = new BigDecimal(0);
	/**
	 * Total amount.
	 */
	@Expose
	private BigDecimal totalAmount = new BigDecimal(0);
	/**
	 * Total amount after discount.
	 */
	@Expose
	private BigDecimal totalAmountAfterDiscount = new BigDecimal(0);

	public ShoppingCart() {}
	/**
	 * Adds given product to the {@link #productsWithCategories} map.
	 * 
	 * @param product
	 *            unique product
	 * @param quantity
	 *            quantity of the product
	 */
	public void addItem(Product product, int quantity) {
		Category category = product.getCategory();
		if (getProductsWithCategories().containsKey(category)) {
			Map<Product, Integer> productMap = getProductsWithCategories().get(category);
			if (productMap.containsKey(product)) {
				productMap.put(product, productMap.get(product) + quantity);
			} else {
				productMap.put(product, quantity);
			}
		} else {
			HashMap<Product, Integer> productMap = new LinkedHashMap<>();
			productMap.put(product, quantity);
			getProductsWithCategories().put(category, productMap);
		}
	}

	/**
	 * Prepares a {@link JsonObject} for this {@link ShoppingCart}.
	 */
	public JsonObject print() {
		JsonObject printed = new JsonObject();
		Set<Entry<Category, Map<Product, Integer>>> categories = getProductsWithCategories().entrySet();
		JsonArray categoriesArrayJson = new JsonArray();
		for (Entry<Category, Map<Product, Integer>> categoryEntry : categories) {
			Category category = categoryEntry.getKey();
			Map<Product, Integer> products = categoryEntry.getValue();
			JsonObject categoryJson = new JsonObject();
			categoryJson.addProperty(TITLE, category.getTitle());
			JsonArray productArrayJson = new JsonArray();
			Set<Entry<Product, Integer>> productSet = products.entrySet();
			for (Entry<Product, Integer> productEntry : productSet) {
				Product product = productEntry.getKey();
				Integer quantity = productEntry.getValue();
				BigDecimal unitPrice = product.getPriceBigDecimal().setScale(2, RoundingMode.HALF_EVEN);

				JsonObject productJson = new JsonObject();
				productJson.addProperty(NAME, product.getTitle());
				productJson.addProperty(QUANTITY, quantity);
				productJson.addProperty(UNIT_PRICE, unitPrice);
				productJson.addProperty(TOTAL_AMOUNT, calculateTotalPrice(quantity, unitPrice));
				productArrayJson.add(productJson);
			}
			categoryJson.add(PRODUCTS, productArrayJson);
			categoriesArrayJson.add(categoryJson);
		}
		printed.add(CATEGORIES, categoriesArrayJson);
		printed.addProperty(TOTAL_AMOUNT, getTotalAmount());
		printed.addProperty(TOTAL_AMOUNT_AFTER_DISCOUNT, getTotalAmountAfterDiscounts());
		return printed;
	}

	@ApiModelProperty(value = "unique identifier")
	public long getId() {
		return id;
	}

	@ApiModelProperty(value = "campaign discount")
	public double getCampaignDiscount() {
		return getCampaignDiscountBigDecimal().setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}

	@ApiModelProperty(value = "coupon discount")
	public double getCouponDiscount() {
		return getCouponDiscountBigDecimal().setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}

	@ApiModelProperty(value = "total amount")
	public double getTotalAmount() {
		return getTotalAmountBigDecimal().setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}

	@ApiModelProperty(value = "total amount after discounts")
	public double getTotalAmountAfterDiscounts() {
		return getTotalAmountAfterDiscountsBigDecimal().setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * Getter for {@link #productsWithCategories} which contains {@link Product}s
	 * according to their sub-categories.
	 */
	@ApiModelProperty(value = "product-quantity map according to their categories")
	public Map<Category, Map<Product, Integer>> getProductsWithCategories() {
		if (productsWithCategories == null) {
			productsWithCategories = new HashMap<Category, Map<Product, Integer>>();
		}
		return productsWithCategories;
	}

	public void setId(long id) {
		this.id = id;
	}

	public static ShoppingCart fromJson(String jsonStr) throws JsonSyntaxException {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonStr, ShoppingCart.class);
	}

	public JsonObject toJson() {
		return new JsonParser().parse(toJsonString()).getAsJsonObject();
	}

	String toJsonString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}

	/**
	 * Returns quantity of the given product if exists in {@link ShoppingCart}
	 * otherwise 0.
	 * 
	 * @param product
	 *            to find its quantity.
	 * @return quantity of the given product if exists in {@link ShoppingCart}
	 *         otherwise 0.
	 */
	int getQuantity(Product product) {
		if (getProductsWithCategories().containsKey(product.getCategory())) {
			Map<Product, Integer> productMap = getProductsWithCategories().get(product.getCategory());
			if (productMap.containsKey(product)) {
				return productMap.get(product);
			}
		}
		return 0;
	}

	/**
	 * Applies {@link Campaign}s to this {@link ShoppingCart}.
	 * 
	 * @param campaigns
	 *            to apply.
	 */
	void applyDiscounts(Campaign... campaigns) {
		BigDecimal totalDiscount = BigDecimal.ZERO;
		BigDecimal maximumDiscount = BigDecimal.ZERO;

		Set<Entry<Category, Map<Product, Integer>>> productsWithCategoriesSet = getProductsWithCategories().entrySet();
		for (Entry<Category, Map<Product, Integer>> entry : productsWithCategoriesSet) {
			Category category = entry.getKey();
			Set<Campaign> relatedCampaigns = category.findRelatedCampaigns(campaigns);
			for (Campaign campaign : relatedCampaigns) {
				BigDecimal currentDiscount = campaign.calculateDiscount(entry.getValue());
				if (currentDiscount.compareTo(maximumDiscount) == 1) {
					maximumDiscount = currentDiscount;
				}
			}
			totalDiscount = totalDiscount.add(maximumDiscount);
			maximumDiscount = BigDecimal.ZERO;
		}
		campaignDiscount = campaignDiscount.add(totalDiscount);
	}

	/**
	 * Applies coupon to this {@link ShoppingCart}.
	 * 
	 * @param coupon
	 *            to apply.
	 */
	void applyCoupon(Coupon coupon) {
		couponDiscount = couponDiscount.add(coupon.calculateDiscount(getTotalAmountBigDecimal()
				.subtract(getCampaignDiscountBigDecimal()).subtract(getCouponDiscountBigDecimal())));
	}

	/**
	 * Getter for campaign discount which is calculated by
	 * {@link #applyDiscounts(Campaign...)}.
	 */
	BigDecimal getCampaignDiscountBigDecimal() {
		return campaignDiscount;
	}

	/**
	 * Getter for coupon discount which is calculated by
	 * {@link #applyCoupon(Coupon)}.
	 */
	BigDecimal getCouponDiscountBigDecimal() {
		return couponDiscount;
	}

	/**
	 * Calculates total price of the {@link ShoppingCart}.
	 * 
	 * @return total price as {@link BigDecimal}.
	 */
	BigDecimal getTotalAmountBigDecimal() {
		this.totalAmount = new BigDecimal(0);
		Collection<Map<Product, Integer>> productMapCollection = getProductsWithCategories().values();
		for (Map<Product, Integer> productMap : productMapCollection) {
			Set<Entry<Product, Integer>> productSet = productMap.entrySet();
			for (Entry<Product, Integer> productEntry : productSet) {
				Integer quantity = productEntry.getValue();
				totalAmount = totalAmount.add(productEntry.getKey().getPriceBigDecimal().multiply(BigDecimal.valueOf(quantity)));
			}
		}
		return totalAmount;
	}

	/**
	 * Calculates total amount after discounts in this {@link ShoppingCart}.
	 * 
	 * @return total amount after discounts.
	 */
	BigDecimal getTotalAmountAfterDiscountsBigDecimal() {
		this.totalAmountAfterDiscount = getTotalAmountBigDecimal().subtract(getCampaignDiscountBigDecimal())
				.subtract(getCouponDiscountBigDecimal());
		return this.totalAmountAfterDiscount;
	}

	/**
	 * Finds different number of categories to use in
	 * {@link DeliveryCostCalculator#calculateFor(ShoppingCart)}.
	 * 
	 * @return different numbers of categories.
	 */
	int findNumberOfDeliveries() {
		return getProductsWithCategories().size();
	}

	/**
	 * Finds number of products to use in
	 * {@link DeliveryCostCalculator#calculateFor(ShoppingCart)}. This method only
	 * calculates number of different products in the cart. It does not consider
	 * quantity of products.
	 * 
	 * @return number of different products for different categories.
	 */
	int findNumberOfProducts() {
		int numberOfProducts = 0;
		Collection<Map<Product, Integer>> allProducts = getProductsWithCategories().values();
		for (Map<Product, Integer> productsMap : allProducts) {
			numberOfProducts += productsMap.size();
		}
		return numberOfProducts;
	}

	/**
	 * Calculates total price for a product which unit price and quantity are given.
	 * 
	 * @param quantity
	 *            quantity of product.
	 * @param unitPrice
	 *            unit price of product.
	 * @return calculated total price for product.
	 */
	private BigDecimal calculateTotalPrice(Integer quantity, BigDecimal unitPrice) {
		return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_EVEN);
	}

}
