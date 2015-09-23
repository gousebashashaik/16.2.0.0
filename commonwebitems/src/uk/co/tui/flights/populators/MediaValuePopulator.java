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
 * $RCSfile: MediaModelPopulator.java$
 *
 * $Revision: $
 *
 * $Date: Nov 23, 2013$
 *
 * Author: abir.p
 *
 *
 * $Log: $
 */
package uk.co.tui.flights.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import uk.co.tui.flights.data.MediaData;



/**
 * @author abir.p
 * @description This class is used to MediaData from MediaModel
 */
public class MediaValuePopulator implements Populator<MediaModel, MediaData>
{

   public List<MediaData> convert(final MediaModel mediaModel)
   {
      final List<MediaData> mediaDatas = new ArrayList<MediaData>();
      if (mediaModel == null)
      {
         mediaDatas.add(new MediaData());
         return mediaDatas;
      }
      final MediaData mediaData = new MediaData();
      mediaData.setUrl(mediaModel.getURL());
      mediaData.setDescription(mediaModel.getDescription());
      mediaDatas.add(mediaData);

      return mediaDatas;
   }

   /**
    * @author abir.p
    * @param sourceModel
    * @param targetData
    * @description This class is used to MediaData from MediaModel
    */
   @SuppressWarnings("deprecation")
   @Override
   public void populate(final MediaModel sourceModel, final MediaData targetData)
      throws ConversionException
   {
      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");
      targetData.setSize(sourceModel.getMediaFormat().getName());
      targetData.setAltText(sourceModel.getAltText());
      targetData.setDescription(sourceModel.getDescription());
      targetData.setMime(sourceModel.getMime());
      targetData.setUrl(sourceModel.getURL());
      targetData.setCode(sourceModel.getCode());
      targetData.setCronSchedule(sourceModel.getCronSchedule());
      targetData.setMime(sourceModel.getMime());
      targetData.setDownloadUrl(sourceModel.getDownloadurl());

   }
}
