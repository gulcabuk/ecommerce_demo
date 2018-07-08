package com.trendyol.entity;

import static com.trendyol.TestData.DISCOUNT_AMOUNT_50_0;
import static com.trendyol.TestData.DISCOUNT_AMOUNT_5_0;
import static com.trendyol.TestData.DISCOUNT_RATE_20_0;
import static com.trendyol.TestData.QUANTITY_2;
import static com.trendyol.TestData.QUANTITY_3;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.trendyol.TestData;

public class CategoryTest {

	@Test
	public void create() throws Exception {
		// execute
		Category category = new Category(TestData.CATEGORY_SAAT);
		Category category_AKSESUAR = new Category(TestData.PARENT_CATEGORY_AKSESUAR);
		Category category_KADIN = new Category(TestData.PARENT_CATEGORY_KADIN);
		category_AKSESUAR.setParentCategory(category_KADIN);
		category.setParentCategory(category_AKSESUAR);

		// assert
		assertEquals(TestData.CATEGORY_SAAT, category.getTitle());
		assertCategory(category, TestData.PARENT_CATEGORY_AKSESUAR);
		assertCategory(category.getParentCategory(), TestData.PARENT_CATEGORY_KADIN);

	}

	@Test
	public void findRelatedCampaigns() throws Exception {
		// setup
		Category category = new Category(TestData.CATEGORY_SAAT);
		Category category_AKSESUAR = new Category(TestData.PARENT_CATEGORY_AKSESUAR);
		Category category_KADIN = new Category(TestData.PARENT_CATEGORY_KADIN);
		category_AKSESUAR.setParentCategory(category_KADIN);
		category.setParentCategory(category_AKSESUAR);

		Campaign campaign = new Campaign(category, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_AKSESUAR, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_KADIN, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);

		// execute
		Set<Campaign> relatedCampaigns = category.findRelatedCampaigns(campaign, campaign2, campaign3);

		// assert
		assertEquals(3, relatedCampaigns.size());
	}

	@Test
	public void findRelatedCampaigns_middleCategory() throws Exception {
		// setup
		Category category = new Category(TestData.CATEGORY_SAAT);
		Category category_AKSESUAR = new Category(TestData.PARENT_CATEGORY_AKSESUAR);
		Category category_KADIN = new Category(TestData.PARENT_CATEGORY_KADIN);
		category_AKSESUAR.setParentCategory(category_KADIN);
		category.setParentCategory(category_AKSESUAR);

		Campaign campaign = new Campaign(category, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_AKSESUAR, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_KADIN, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);

		// execute
		Set<Campaign> relatedCampaigns = category_AKSESUAR.findRelatedCampaigns(campaign, campaign2, campaign3);

		// assert
		assertEquals(2, relatedCampaigns.size());
	}

	@Test
	public void findRelatedCampaigns_parentCategory() throws Exception {
		// setup
		Category category = new Category(TestData.CATEGORY_SAAT);
		Category category_AKSESUAR = new Category(TestData.PARENT_CATEGORY_AKSESUAR);
		Category category_KADIN = new Category(TestData.PARENT_CATEGORY_KADIN);
		category_AKSESUAR.setParentCategory(category_KADIN);
		category.setParentCategory(category_AKSESUAR);

		Campaign campaign = new Campaign(category, DISCOUNT_RATE_20_0, QUANTITY_3, DiscountType.Rate);
		Campaign campaign2 = new Campaign(category_AKSESUAR, DISCOUNT_AMOUNT_5_0, QUANTITY_3, DiscountType.Amount);
		Campaign campaign3 = new Campaign(category_KADIN, DISCOUNT_AMOUNT_50_0, QUANTITY_2, DiscountType.Amount);

		// execute
		Set<Campaign> relatedCampaigns = category_KADIN.findRelatedCampaigns(campaign, campaign2, campaign3);

		// assert
		assertEquals(1, relatedCampaigns.size());
		assertEquals(campaign3, relatedCampaigns.iterator().next());
	}

	private void assertCategory(Category category, String parentCategoryName) {
		assertNotNull(category.getParentCategory());
		Category parentCategory = category.getParentCategory();
		assertEquals(parentCategoryName, parentCategory.getTitle());
	}

}
