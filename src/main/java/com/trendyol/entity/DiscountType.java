package com.trendyol.entity;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;

/**
 * This class is used to differentiate {@link DiscountType}s. Enumeration values
 * also contains apply methods for {@link Campaign} and {@link Coupon} classes
 * according to their type.
 * 
 * @author zehragulcabukkeskin
 *
 */
@ApiModel(description = "DiscountType")
public enum DiscountType implements DiscountStrategy {
	Rate {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.trendyol.entity.DiscountStrategy#applyDiscount(java.math.BigDecimal,
		 * com.trendyol.entity.Coupon)
		 */
		@Override
		public BigDecimal calculateDiscount(BigDecimal amount, Coupon coupon) {
			BigDecimal discount;
			if (coupon.getMinimumAmount() <= amount.doubleValue()) {
				discount = amount.multiply(BigDecimal.valueOf(coupon.getDiscount())).divide(BigDecimal.valueOf(100));
			} else {
				discount = new BigDecimal(0);
			}
			return discount;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.trendyol.entity.DiscountStrategy#applyDiscount(java.math.BigDecimal,
		 * com.trendyol.entity.Campaign)
		 */
		@Override
		public BigDecimal calculateDiscount(BigDecimal amount, Campaign campaign) {
			return amount.multiply(BigDecimal.valueOf(campaign.getDiscount())).divide(BigDecimal.valueOf(100));
		}
	},
	Amount {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.trendyol.entity.DiscountStrategy#applyDiscount(java.math.BigDecimal,
		 * com.trendyol.entity.Coupon)
		 */
		@Override
		public BigDecimal calculateDiscount(BigDecimal amount, Coupon coupon) {
			BigDecimal discount;
			if (coupon.getMinimumAmount() <= amount.doubleValue()) {
				discount = BigDecimal.valueOf(coupon.getDiscount());
			} else {
				discount = new BigDecimal(0);
			}
			return discount;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.trendyol.entity.DiscountStrategy#applyDiscount(java.math.BigDecimal,
		 * com.trendyol.entity.Campaign)
		 */
		@Override
		public BigDecimal calculateDiscount(BigDecimal amount, Campaign campaign) {
			return BigDecimal.valueOf(campaign.getDiscount());
		}
	}

}
