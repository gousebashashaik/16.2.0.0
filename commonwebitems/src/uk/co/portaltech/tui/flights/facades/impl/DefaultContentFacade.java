/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package uk.co.portaltech.tui.flights.facades.impl;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.fest.util.Collections;

import uk.co.portaltech.travel.model.ContentItemModel;
import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.tui.flights.facades.ContentFacade;
import uk.co.tui.flights.data.ContentMediaViewData;
import uk.co.tui.flights.data.ContentViewData;
import uk.co.tui.flights.populators.BeforeYouFlyComponentPopulator;
import uk.co.tui.flights.service.ContentService;
import uk.co.tui.flights.web.view.data.BeforeYouFlyViewData;


/**
 * @author sivaramya.t
 * @description This class delegates call to service layer.
 */
public class DefaultContentFacade implements ContentFacade
{
	@Resource
	private ContentService contentService;

	@Resource
	private BeforeYouFlyComponentPopulator beforeYouFlyComponentPopulator;

	@Resource
	private Populator<ContentValueModel, ContentViewData> contentPopulator;

	@Resource
	private Populator<MediaModel, ContentMediaViewData> contentMediaPopulator;


	/**
	 * This method is used to get epic content with given parameters
	 *
	 * @param code
	 * @param names
	 * @return {@link BeforeYouFlyViewData}
	 */
	@Override
	public BeforeYouFlyViewData getBeforeYouFlyViewData(final String code, final List<String> names)
	{

		final BeforeYouFlyViewData beforeYouFlyViewData = new BeforeYouFlyViewData();

		final List<ContentValueModel> contentValueModels = contentService.getEpicContentValue(code, names);
		beforeYouFlyViewData.setNames(names);
		beforeYouFlyViewData.setCode(code);
		beforeYouFlyComponentPopulator.populate(contentValueModels, beforeYouFlyViewData);

		return beforeYouFlyViewData;
	}


	/**
	 * This method will fetch data from DAO layer and convert it into content view data object.
	 *
	 * @param name
	 * @param code
	 * @return ContentViewData
	 *
	 */
	@Override
	public ContentViewData getContentValue(final String code, final String name)
	{

		ContentViewData contentViewData = null;

		contentViewData = new ContentViewData();
		final ContentValueModel contentvaluModel = contentService.getContentValue(code, name);
		contentPopulator.populate(contentvaluModel, contentViewData);


		return contentViewData;
	}

	/**
	 * This method will fetch data from DAO layer and convert it into media view data object.
	 *
	 * @param code
	 * @return List<ContentMediaViewData>
	 */
	@Override
	public List<ContentMediaViewData> getContentItemMedia(final String code)
	{

		final ContentItemModel contentItemModel = contentService.getContentItemMedia(code);
		final ContentMediaViewData mediaViewData = new ContentMediaViewData();
		final List<ContentMediaViewData> listOfMediaViewData = new ArrayList<ContentMediaViewData>();
		final List<MediaContainerModel> mediaContainerModels = contentItemModel.getMedias();
		if (checkMediaContainerModels(mediaContainerModels))
		{
			final MediaContainerModel mediaContainer = mediaContainerModels.get(0);
			final Collection<MediaModel> medias = mediaContainer.getMedias();
			for (final MediaModel mediaModel2 : medias)
			{
				if (verifyMediaFormat(mediaModel2))
				{

					contentMediaPopulator.populate(mediaModel2, mediaViewData);
					listOfMediaViewData.add(mediaViewData);
				}
			}
			return listOfMediaViewData;
		}

		return listOfMediaViewData;

	}

	/**
	 * @param code
	 * @return List<ContentMediaViewData>
	 */
	@Override
	public List<ContentMediaViewData> getCarouselImages(final String code)
	{

		final ContentItemModel contentItemModel = contentService.getContentItemMedia(code);
		final ContentMediaViewData mediaViewData = new ContentMediaViewData();
		final List<ContentMediaViewData> listOfMediaViewData = new ArrayList<ContentMediaViewData>();
		final List<MediaContainerModel> mediaContainerModels = contentItemModel.getMedias();
		if (checkMediaContainerModels(mediaContainerModels))
		{
			final MediaContainerModel mediaContainer = mediaContainerModels.get(0);
			final Collection<MediaModel> medias = mediaContainer.getMedias();
			for (final MediaModel mediaModel2 : medias)
			{
				contentMediaPopulator.populate(mediaModel2, mediaViewData);
				listOfMediaViewData.add(mediaViewData);

			}
			return listOfMediaViewData;
		}

		return listOfMediaViewData;

	}

	/**
	 * @param mediaModel2
	 * @return boolean
	 */
	private boolean verifyMediaFormat(final MediaModel mediaModel2)
	{
		return mediaModel2.getMediaFormat() != null && "small".equalsIgnoreCase(mediaModel2.getMediaFormat().getName());
	}

	/**
	 * @param mediaContainerModels
	 * @return boolean
	 */
	private boolean checkMediaContainerModels(final List<MediaContainerModel> mediaContainerModels)
	{
		return mediaContainerModels != null && !Collections.isEmpty(mediaContainerModels);
	}

}
