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
 * $RCSfile: MediaContainerModelPopulator.java$
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
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.flights.data.MediaContainerData;
import uk.co.tui.flights.data.MediaData;


/**
 * @author abir.p
 * @description This class is used to MediaData from MediaModel
 */
public class MediaContainerPopulator implements
   Populator<MediaContainerModel, MediaContainerData>
{

   private static final TUILogUtils LOGGER = new TUILogUtils("MediaContainerModelPopulator");

   /**
    *
    */
   private static final int THOUSAND = 1000;

   @Resource
   private uk.co.tui.flights.populators.MediaValuePopulator mediaValuePopulator;

   @Resource
   private BrandUtils brandUtils;

   /**
    * @author abir.p
    * @param source
    * @param target
    * @description This class is used to MediaData from MediaModel
    */
   @Override
   public void populate(final MediaContainerModel source, final MediaContainerData target)
      throws ConversionException
   {

      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");

      for (final MediaModel media : source.getMedias())
      {
         try
         {
            final MediaData mediaData = new MediaData();
            mediaValuePopulator.populate(media, mediaData);
            target.getGalleryImages().add(mediaData);
         }
         catch (final IllegalStateException e)
         {
            LOGGER.warn("MediaFormat Error : ", e);
         }
      }
      if (CollectionUtils.isNotEmpty(source.getBrands()))
      {
         target.setBrandCode(brandUtils.getFeatureServiceBrand(source.getBrands()));
      }
      target.setCaption(source.getCaption());
      target.setMediaMugalId(source.getMediaMogulId());
      target.setSeqNo(getSeqNo(source.getSeqNo()));
   }

   private Integer getSeqNo(final Integer seqNo)
   {
      Integer seq = THOUSAND;
      if (null != seqNo && seqNo != 0)
      {
         seq = seqNo;
      }
      return seq;
   }

}
