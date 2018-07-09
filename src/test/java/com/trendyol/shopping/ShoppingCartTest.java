package com.trendyol.shopping;

import static com.trendyol.TestData.DISCOUNT_AMOUNT_50_0;
import static com.trendyol.TestData.DISCOUNT_AMOUNT_5_0;
import static com.trendyol.TestData.DISCOUNT_RATE_20_0;
import static com.trendyol.TestData.MINIMUM_AMOUNT_100;
import static com.trendyol.TestData.PRICE_39_99;
import static com.trendyol.TestData.PRICE_49_99;
import static com.trendyol.TestData.QUANTITY_2;
import static com.trendyol.TestData.QUANTITY_3;
import static com.trendyol.TestData.TITLE_K_KIRMIZI_ELBISE;
import static com.trendyol.TestData.TITLE_K_SIYAH_ELBISE;
import static com.trendyol.TestData.prepareCategory_AKSESUAR;
import static com.trendyol.TestData.prepareCategory_ELBISE;
import static com.trendyol.TestData.prepareCategory_GIYIM;
import static com.trendyol.TestData.prepareCategory_SAAT;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.trendyol.TestData;
import com.trendyol.entity.Campaign;
import com.trendyol.entity.Category;
import com.trendyol.entity.Coupon;
import com.trendyol.entity.DiscountType;
import com.trendyol.entity.Product;

public class ShoppingCartTest {

	private ShoppingCart shoppingCart;

	@Before
	public void before() {
		shoppingCart = new ShoppingCart();
	}

	@Test
	public void addItem() throws Exception {
		// setup..
		Product product = new Product(TITLE_K_SIYAH_ELBISE, PRICE_49_99, prepareCategory_ELBISE());

		// execute
		shoppingCart.addItem(product, 2);

		// assert
		int quantity = shoppingCart.getQuantity(product);
		assertEquals(2, quantity);
	}

	@Test
	public void addItem_same() throws Exception {
		// setup..
		Product product = new Product(TITLE_K_SIYAH_ELBISE, PRICE_49_99, prepareCategory_ELBISE());

		// execute
		shoppingCart.addItem(product, 2);
		shoppingCart.addItem(product, 3);

		// assert
		int quantity = shoppingCart.getQuantity(product);
		assertEquals(5, quantity);
	}

	@Test
	public void addItem_multiple() throws Exception {
		// setup..
		Product product = new Product(TITLE_K_SIYAH_ELBISE, PRICE_49_99, prepareCategory_ELBISE());
		Product product2 = new Product(TITLE_K_KIRMIZI_ELBISE, PRICE_39_99, prepareCategory_ELBISE());

		// execute
		shoppingCart.addItem(product, 2);
		shoppingCart.addItem(product2, 3);

		// assert
		assertEquals(2, shoppingCart.getQuantity(product));
		assertEquals(3, shoppingCart.getQuantity(product2));
	}

	@Test
	public void applyCampaign_oneCampaignForCategory() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);

		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);

		// execute
		shoppingCart.applyDiscounts(campaign);

		// assert
		assertEquals(43.99, shoppingCart.getCampaignDiscountBigDecimal().doubleValue(), 0);
	}

	@Test
	public void applyCampaign_twoCampaignForCategory_oneParent() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_GIYIM = prepareCategory_GIYIM();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);

		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_GIYIM, DISCOUNT_AMOUNT_50_0, QUANTITY_3, DiscountType.Amount);

		// execute
		shoppingCart.applyDiscounts(campaign, campaign2);

		// assert
		assertEquals(50.0, shoppingCart.getCampaignDiscountBigDecimal().doubleValue(), 0);
	}

	@Test
	public void applyCampaign_threeCampaignForCategory_oneNotRelated() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_GIYIM = prepareCategory_GIYIM();
		Category category_AKSESUAR = prepareCategory_AKSESUAR();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);

		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_GIYIM, DISCOUNT_AMOUNT_50_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_AKSESUAR, DISCOUNT_AMOUNT_5_0, QUANTITY_2, DiscountType.Amount);

		// execute
		shoppingCart.applyDiscounts(campaign, campaign2, campaign3);

		// assert
		assertEquals(50.0, shoppingCart.getCampaignDiscountBigDecimal().doubleValue(), 0);
	}

	@Test
	public void applyCampaign_threeCampaignForCategory() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_GIYIM = prepareCategory_GIYIM();
		Category category_SAAT = prepareCategory_SAAT();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_NINE_WEST_SAAT, TestData.PRICE_199_99, category_SAAT), 2);

		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_GIYIM, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_SAAT, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);

		// execute
		shoppingCart.applyDiscounts(campaign, campaign2, campaign3);

		// assert
		assertEquals(93.99, shoppingCart.getCampaignDiscountBigDecimal().doubleValue(), 0);
	}

	@Test
	public void applyCampaign_threeCampaignForCategory_lessQuantity_notApplied() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_GIYIM = prepareCategory_GIYIM();
		Category category_SAAT = prepareCategory_SAAT();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_NINE_WEST_SAAT, TestData.PRICE_199_99, category_SAAT), 1);

		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_GIYIM, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_SAAT, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);

		// execute
		shoppingCart.applyDiscounts(campaign, campaign2, campaign3);

		// assert
		assertEquals(43.99, shoppingCart.getCampaignDiscountBigDecimal().doubleValue(), 0);
	}

	@Test
	public void applyCoupon_withRate() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		Coupon coupon = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);

		// execute..
		shoppingCart.applyCoupon(coupon);

		// assert
		assertEquals(43.99, shoppingCart.getCouponDiscount(), 0);
	}

	@Test
	public void applyCoupon_withRate_lessAmount_notApplicable() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		Coupon coupon = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);

		// execute..
		shoppingCart.applyCoupon(coupon);

		// assert
		assertEquals(0.0, shoppingCart.getCouponDiscount(), 0);
	}

	@Test
	public void applyCoupon_withAmount() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		Coupon coupon = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);

		// execute..
		shoppingCart.applyCoupon(coupon);

		// assert
		assertEquals(5.0, shoppingCart.getCouponDiscount(), 0);
	}

	@Test
	public void applyCoupon_withAmount_lessAmount_notApplicable() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		Coupon coupon = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);

		// execute..
		shoppingCart.applyCoupon(coupon);

		// assert
		assertEquals(0.0, shoppingCart.getCouponDiscount(), 0);
	}

	@Test
	public void applyCoupons_withAmount_withRate() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		Coupon coupon1 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);
		Coupon coupon2 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);

		// execute..
		shoppingCart.applyCoupon(coupon1);
		shoppingCart.applyCoupon(coupon2);

		// assert
		assertEquals(47.99, shoppingCart.getCouponDiscount(), 0);
	}

	@Test
	public void applyCoupons_withRate_withAmount() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		Coupon coupon1 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);
		Coupon coupon2 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);

		// execute..
		shoppingCart.applyCoupon(coupon2);
		shoppingCart.applyCoupon(coupon1);

		// assert
		assertEquals(48.99, shoppingCart.getCouponDiscount(), 0);
	}

	@Test
	public void getTotalPrice() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);

		// execute
		// assert
		assertEquals(BigDecimal.valueOf(219.950), shoppingCart.getTotalAmountBigDecimal());
	}

	@Test
	public void getTotalPrice_emptyCart() throws Exception {
		// execute
		// assert
		assertEquals(BigDecimal.valueOf(0), shoppingCart.getTotalAmountBigDecimal());
	}

	/**
	 * @see #applyCampaign_threeCampaignForCategory()
	 */
	@Test
	public void getTotalAmountAfterDiscounts_campaigns() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_GIYIM = prepareCategory_GIYIM();
		Category category_SAAT = prepareCategory_SAAT();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_NINE_WEST_SAAT, TestData.PRICE_199_99, category_SAAT), 2);

		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_GIYIM, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_SAAT, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);

		shoppingCart.applyDiscounts(campaign, campaign2, campaign3);

		// execute
		double totalAmountAfterDiscounts = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(525.94, totalAmountAfterDiscounts, 0);
	}

	@Test
	public void getTotalAmountAfterDiscounts_coupons() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		Coupon coupon1 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);
		Coupon coupon2 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);

		// execute
		shoppingCart.applyCoupon(coupon1);
		shoppingCart.applyCoupon(coupon2);
		double totalAmountAfterDiscounts = shoppingCart.getTotalAmountAfterDiscounts();

		// assert
		assertEquals(171.96, totalAmountAfterDiscounts, 0);
	}

	@Test
	public void getTotalAmountAfterDiscounts_campaigns_coupons() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_GIYIM = prepareCategory_GIYIM();
		Category category_SAAT = prepareCategory_SAAT();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_NINE_WEST_SAAT, TestData.PRICE_199_99, category_SAAT), 2);

		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_GIYIM, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_SAAT, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);

		shoppingCart.applyDiscounts(campaign, campaign2, campaign3);

		Coupon coupon1 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);
		Coupon coupon2 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);

		shoppingCart.applyCoupon(coupon1);
		shoppingCart.applyCoupon(coupon2);

		// execute
		double totalAmountAfterDiscounts = shoppingCart.getTotalAmountAfterDiscounts();

		// assert
		assertEquals(416.75, totalAmountAfterDiscounts, 0);
	}

	@Test
	public void getTotalAmountAfterDiscounts_emptyCart() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_GIYIM = prepareCategory_GIYIM();
		Category category_SAAT = prepareCategory_SAAT();
		Campaign campaign = new Campaign(category_ELBISE, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_GIYIM, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_SAAT, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);
		shoppingCart.applyDiscounts(campaign, campaign2, campaign3);

		Coupon coupon1 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);
		Coupon coupon2 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);
		shoppingCart.applyCoupon(coupon1);
		shoppingCart.applyCoupon(coupon2);

		// execute
		double totalAmountAfterDiscounts = shoppingCart.getTotalAmountAfterDiscounts();

		// assert
		assertEquals(0.0, totalAmountAfterDiscounts, 0);
	}

	@Test
	public void findNumberOfDeliveries() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_SAAT = prepareCategory_SAAT();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_NINE_WEST_SAAT, TestData.PRICE_199_99, category_SAAT), 1);

		// execute
		int numberOfDeliveries = shoppingCart.findNumberOfDeliveries();

		// assert
		assertEquals(2, numberOfDeliveries);
	}

	@Test
	public void findNumberOfDeliveries_emptyCart() throws Exception {
		// execute
		int numberOfDeliveries = shoppingCart.findNumberOfDeliveries();

		// assert
		assertEquals(0, numberOfDeliveries);
	}

	@Test
	public void findNumberOfProducts() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_SAAT = prepareCategory_SAAT();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_NINE_WEST_SAAT, TestData.PRICE_199_99, category_SAAT), 1);

		// execute
		int numberOfProducts = shoppingCart.findNumberOfProducts();

		// assert
		assertEquals(3, numberOfProducts);
	}

	@Test
	public void findNumberOfProducts_emptyCart() throws Exception {
		// execute
		int numberOfProducts = shoppingCart.findNumberOfProducts();

		// assert
		assertEquals(0, numberOfProducts);
	}

	@Test
	public void print() throws Exception {
		// setup
		Category category_ELBISE = prepareCategory_ELBISE();
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		Coupon coupon1 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_AMOUNT_5_0, DiscountType.Amount);
		Coupon coupon2 = new Coupon(MINIMUM_AMOUNT_100, DISCOUNT_RATE_20_0, DiscountType.Rate);
		shoppingCart.applyCoupon(coupon1);
		shoppingCart.applyCoupon(coupon2);

		// execute
		String printed = shoppingCart.print().toString();

		// assert
		assertEquals(
				"{\"categories\":[{\"title\":\"Elbise\",\"products\":[{\"name\":\"Kadın Kırmızı Uzun Elbise\",\"quantity\":3,\"unitPrice\":39.99,\"totalAmount\":119.97},{\"name\":\"Kadın Siyah File Detaylı Elbise\",\"quantity\":2,\"unitPrice\":49.99,\"totalAmount\":99.98}]}],\"totalAmount\":219.95,\"totalAmountAfterDiscount\":171.96}",
				printed);

	}

}
