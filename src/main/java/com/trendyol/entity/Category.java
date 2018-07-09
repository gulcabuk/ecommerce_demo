package com.trendyol.entity;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class is used to define categories of system. Each {@link Product} has a
 * {@link Category}. {@link Category} can have a parent {@link Category}.
 * 
 * @author zehragulcabukkeskin
 */
@ApiModel(description = "Category")
public class Category {

	/**
	 * Identifier of the {@link Category}.
	 */
	@Expose
	private long id;
	
	/**
	 * Title of the category.
	 */
	@Expose
	private String title;

	/**
	 * Category can have a parent category. It is an optional field.
	 */
	@Expose
	private Category parentCategory;

	/**
	 * Default constructor for the {@link Category}.
	 * 
	 * @param title
	 *            title of the category.
	 */
	public Category(String title) {
		this.title = title;
	}
	
	public Category() {}

	/**
	 * Sets parent category.
	 * 
	 * @param parentCategory
	 *            parent category.
	 */
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	/**
	 * @return
	 */
	@ApiModelProperty(value = "title")
	public String getTitle() {
		return title;
	}
	@ApiModelProperty(value = "parent category")
	public Category getParentCategory() {
		return parentCategory;
	}
	@ApiModelProperty(value = "unique identifier")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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
		result = prime * result + ((parentCategory == null) ? 0 : parentCategory.hashCode());
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
		Category other = (Category) obj;
		if (parentCategory == null) {
			if (other.parentCategory != null)
				return false;
		} else if (!parentCategory.equals(other.parentCategory))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/**
	 * Finds related campaigns for this {@link Category} and its parent categories.
	 * 
	 * @param campaigns
	 *            campaign list to filter them.
	 */
	public Set<Campaign> findRelatedCampaigns(Campaign... campaigns) {
		Set<Campaign> relatedCampaigns = new HashSet<>();
		for (Campaign campaign : campaigns) {
			if (campaign.getCategory() != null) {
				if (campaign.getCategory().equals(this)) {
					relatedCampaigns.add(campaign);
				} else {
					if (getParentCategory() != null) {
						relatedCampaigns.addAll(getParentCategory().findRelatedCampaigns(campaign));
					}
				}
			}
		}
		return relatedCampaigns;
	}

	public static Category fromJson(String jsonStr) throws JsonSyntaxException {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonStr, Category.class);
	}
	
	public JsonObject toJson() {
		return new JsonParser().parse(toJsonString()).getAsJsonObject();
	}

	String toJsonString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}

}
