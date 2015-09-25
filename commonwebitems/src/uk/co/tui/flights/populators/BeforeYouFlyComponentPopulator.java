/*
 * Copyright (C)2006 TUI UK Ltd
 *
 * TUI UK Ltd, Columbus House, Westwood Way, Westwood Business Park, Coventry, United Kingdom CV4
 * 8TT
 *
 * Telephone - (024)76282828
 *
 * All rights reserved - The copyright notice above does not evidence any actual or intended
 * publication of this source code.
 *
 * $RCSfile: FeatureListComponentPopulator.java$
 *
 * $Revision: $
 *
 * $Date: Sep 25, 2014$
 *
 * Author:
 *
 *
 * $Log: $
 */package uk.co.tui.flights.populators;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.flights.data.ContentValueData;
import uk.co.tui.flights.data.MediaData;
import uk.co.tui.flights.web.view.data.BeforeYouFlyViewData;


/**
 *
 */
public class BeforeYouFlyComponentPopulator implements Populator<List<ContentValueModel>, BeforeYouFlyViewData>
{



	private static final String POPULATOR_NULL_ERROR = "Converter sources must not be null";

	@Resource
	private uk.co.tui.flights.populators.ContentValuePopulator contentValuePopulator;

	/**
	 * @description creates target of BeforeYouFlyListViewData
	 */
	public BeforeYouFlyViewData createTarget()
	{
		return new BeforeYouFlyViewData();
	}

	/**
	 * @param target
	 * @param source
	 * @description populates BeforeYouFlyListViewData
	 */
	@Override
	public void populate(final List<ContentValueModel> source, final BeforeYouFlyViewData target)
	{
		Assert.notNull(source, POPULATOR_NULL_ERROR);
		Assert.notNull(target, POPULATOR_NULL_ERROR);


		if (CollectionUtils.isNotEmpty(source))
		{
			getDataFromContentValue(target, source);
		}

	}

	private void getDataFromContentValue(final BeforeYouFlyViewData target, final List<ContentValueModel> list)
	{
		if (CollectionUtils.isNotEmpty(list))
		{
			for (final ContentValueModel contentValueModel : list)
			{
				final ContentValueData contentValueData = new ContentValueData();
				contentValuePopulator.populate(contentValueModel, contentValueData);
				populateDescNImage(target, contentValueData);
			}
		}
	}

	private void populateDescNImage(final BeforeYouFlyViewData target, final ContentValueData contentValueData)
	{
		if (target.getNames().get(0).equals(contentValueData.getName()))
		{
			target.setDisplayName(contentValueData.getValue());
		}
		else if (target.getNames().get(1).equals(contentValueData.getName()))
		{
			target.setIntroduction(contentValueData.getValue());
			target.setGalleryImages(convertMediaToMediaViewData(contentValueData.getGalleryImages()));
		}
		else if (target.getNames().get(2).equals(contentValueData.getName()))
		{
			target.setKeyFacts(contentValueData.getValue());

		}

	}

	/**
	 * @description Use this method to populate the MediaViewData from MediaData which is a business
	 *
	 *
	 * @param mediaDatas
	 * @return List<MediaViewData>
	 */
	private List<MediaViewData> convertMediaToMediaViewData(final Collection<MediaData> mediaDatas)
	{
		final List<MediaViewData> mediaViewDatas = new ArrayList<MediaViewData>();
		if (isNotEmpty(mediaDatas))
		{
			for (final MediaData mediaData : mediaDatas)
			{
				final MediaViewData mediaViewData = new MediaViewData();
				mediaViewData.setAltText(mediaData.getAltText());
				mediaViewData.setCode(mediaData.getCode());
				mediaViewData.setDescription(mediaData.getDescription());
				mediaViewData.setMainSrc(mediaData.getUrl());
				mediaViewData.setMime(mediaData.getMime());
				mediaViewData.setSize(mediaData.getSize());
				mediaViewDatas.add(mediaViewData);
			}
		}
		return mediaViewDatas;
	}

}
