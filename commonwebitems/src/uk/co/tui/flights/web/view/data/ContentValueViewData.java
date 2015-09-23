/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with hybris.
 */
package uk.co.tui.flights.web.view.data;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.MediaViewData;


/**
 *
 */
public class ContentValueViewData
{
	private String value;

	private String name;

	private List<MediaViewData> galleryImages;

	private List<MediaViewData> galleryVideos;

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *           the value to set
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the galleryImages
	 */
	public List<MediaViewData> getGalleryImages()
	{
		return galleryImages;
	}

	/**
	 * @param galleryImages
	 *           the galleryImages to set
	 */
	public void setGalleryImages(final List<MediaViewData> galleryImages)
	{
		this.galleryImages = galleryImages;
	}

	/**
	 * @return the galleryVideos
	 */
	public List<MediaViewData> getGalleryVideos()
	{
		return galleryVideos;
	}

	/**
	 * @param galleryVideos
	 *           the galleryVideos to set
	 */
	public void setGalleryVideos(final List<MediaViewData> galleryVideos)
	{
		this.galleryVideos = galleryVideos;
	}

}
