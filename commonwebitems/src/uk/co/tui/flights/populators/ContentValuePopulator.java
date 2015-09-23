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
 * $RCSfile: ContentItemPopulator.java$
 *
 * $Revision: $
 *
 * $Date: Jan 04, 2014$
 *
 * Author: sunilkumar.murthy
 *
 *
 * $Log: $
 */

package uk.co.tui.flights.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.tui.flights.data.ContentValueData;
import uk.co.tui.flights.data.MediaContainerData;
import uk.co.tui.flights.data.MediaData;


/**
 *
 */
public class ContentValuePopulator implements Populator<ContentValueModel, ContentValueData>
{

	private static final String BCVIDEO = "BCVIDEO";

	private static final String IMAGE = "IMAGE";

   /**
    * Method to implement the code for Contentvaluedata of tittle and Description
    *
    */
   @Resource
   private uk.co.tui.flights.populators.MediaContainerPopulator mediaContainerPopulator;

   @Override
   public void populate(final ContentValueModel source, final ContentValueData target)
      throws ConversionException
   {

      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");

      if (source.getValue() != null)
      {
         target.setName(source.getName());
         target.setValue(source.getValue().toString());

      }

      final List<MediaData> galleryImages = new ArrayList<MediaData>();
      final List<MediaData> galleryVideos = new ArrayList<MediaData>();
      if (CollectionUtils.isNotEmpty(source.getMedias()))
      {

         final List<MediaContainerModel> mediaContainerList = source.getMedias();

         for (final MediaContainerModel mediaContainer : mediaContainerList)
         {
            prepareMediaImageData(mediaContainer, galleryImages, galleryVideos);

         }

         target.setGalleryImages(galleryImages);
         target.setGalleryVideos(galleryVideos);
      }

   }

   /**
    *
    */
   private void prepareMediaImageData(MediaContainerModel mediaContainer,
      List<MediaData> galleryImages, List<MediaData> galleryVideos)
   {
      final MediaContainerData mediaContainerData = new MediaContainerData();
      if (mediaContainer.getMediaType().equalsIgnoreCase(IMAGE))
      {
      	mediaContainerPopulator.populate(mediaContainer, mediaContainerData);
         galleryImages.addAll(mediaContainerData.getGalleryImages());
      }
      if (mediaContainer.getMediaType().equalsIgnoreCase(BCVIDEO))
      {
         final MediaData mediaData = new MediaData();
         mediaData.setCode(mediaContainer.getQualifier());
         galleryVideos.add(mediaData);
      }

   }

}
