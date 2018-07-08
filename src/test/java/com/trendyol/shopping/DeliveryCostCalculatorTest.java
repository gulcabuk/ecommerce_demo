package com.trendyol.shopping;

import static com.trendyol.TestData.PRICE_39_99;
import static com.trendyol.TestData.PRICE_49_99;
import static com.trendyol.TestData.TITLE_K_KIRMIZI_ELBISE;
import static com.trendyol.TestData.TITLE_K_SIYAH_ELBISE;
import static com.trendyol.TestData.prepareCategory_ELBISE;
import static com.trendyol.TestData.prepareCategory_SAAT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.trendyol.TestData;
import com.trendyol.entity.Category;
import com.trendyol.entity.Product;

public class DeliveryCostCalculatorTest {

	@Test
	public void calculateFor() throws Exception {
		// setup..
		ShoppingCart shoppingCart = new ShoppingCart();
		Product product = new Product(TITLE_K_SIYAH_ELBISE, PRICE_49_99, prepareCategory_ELBISE());
		Product product2 = new Product(TITLE_K_KIRMIZI_ELBISE, PRICE_39_99, prepareCategory_ELBISE());
		shoppingCart.addItem(product, 2);
		shoppingCart.addItem(product2, 3);

		// execute..
		double costPerDelivery = 3;
		double costPerProduct = 1;
		double fixedCost = 2.19;
		DeliveryCostCalculator calculator = new DeliveryCostCalculator(costPerDelivery, costPerProduct, fixedCost);
		double deliveryCost = calculator.calculateFor(shoppingCart);

		// assert
		assertEquals(7.19, deliveryCost, 0);

	}
	

	@Test
	public void calculateFor_multipleCategories_Products() throws Exception {
		// setup..
		ShoppingCart shoppingCart = new ShoppingCart();
		Category category_ELBISE = prepareCategory_ELBISE();
		Category category_SAAT = prepareCategory_SAAT();
		shoppingCart.addItem(new Product(TestData.TITLE_K_SIYAH_ELBISE, TestData.PRICE_49_99, category_ELBISE), 2);
		shoppingCart.addItem(new Product(TestData.TITLE_K_KIRMIZI_ELBISE, TestData.PRICE_39_99, category_ELBISE), 3);
		shoppingCart.addItem(new Product(TestData.TITLE_K_NINE_WEST_SAAT, TestData.PRICE_199_99, category_SAAT), 1);
		
		// execute..
		double costPerDelivery = 3.20;
		double costPerProduct = 1;
		double fixedCost = 2.99;
		DeliveryCostCalculator calculator = new DeliveryCostCalculator(costPerDelivery, costPerProduct, fixedCost);
		double deliveryCost = calculator.calculateFor(shoppingCart);

		// assert
		assertEquals(12.39, deliveryCost, 0);
	}
	
	@Test
	public void calculateFor_emptyCart() throws Exception {
		// setup..
		ShoppingCart shoppingCart = new ShoppingCart();

		// execute..
		double costPerDelivery = 3;
		double costPerProduct = 1;
		double fixedCost = 2.19;
		DeliveryCostCalculator calculator = new DeliveryCostCalculator(costPerDelivery, costPerProduct, fixedCost);
		double deliveryCost = calculator.calculateFor(shoppingCart);

		// assert
		assertEquals(2.19, deliveryCost, 0);

	}

}
