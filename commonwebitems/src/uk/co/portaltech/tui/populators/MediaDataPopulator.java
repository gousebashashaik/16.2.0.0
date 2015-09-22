package uk.co.portaltech.tui.populators;

/*
 * Originating Unit: Portal Technology Systems Ltd http://www.portaltech.co.uk
 * 
 * Copyright Portal Technology Systems Ltd.
 * 
 * $Id: $
 */

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * A basic class to convert a MediaModel into our MediaData.
 *
 * @author James Johnstone
 * @author Omon
 */

public class MediaDataPopulator implements Populator<MediaModel, MediaViewData>
{

   static List mediaList = new ArrayList<String>();

   static
   {

      mediaList.add("small");
      mediaList.add("medium");
      mediaList.add("large");
      mediaList.add("xlarge");
   }

   public List<MediaViewData> convert(final MediaModel mediaModel)
   {
      final List<MediaViewData> mediaDatas = new ArrayList<MediaViewData>();
      if (mediaModel == null)
      {
         mediaDatas.add(new MediaViewData());
         return mediaDatas;
      }
      final MediaViewData mediaData = new MediaViewData();
      mediaData.setMainSrc(mediaModel.getURL());
      mediaData.setDescription(mediaModel.getDescription());
      mediaDatas.add(mediaData);

      return mediaDatas;
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final MediaModel sourceModel, final MediaViewData targetData)
      throws ConversionException
   {
      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");

      if (mediaList.contains(sourceModel.getMediaFormat().getName()))
      {
         targetData.setCode(formatCode(sourceModel));
         targetData.setAltText(sourceModel.getAltText());
         targetData.setDescription(sourceModel.getDescription());
         targetData.setMime(sourceModel.getMime());
         targetData.setMainSrc(sourceModel.getURL());
         targetData.setSize(sourceModel.getMediaFormat().getName());

      }
      else
      {
         targetData.setCode(formatCode(sourceModel));
         targetData.setAltText(sourceModel.getAltText());
         targetData.setDescription(sourceModel.getDescription());
         targetData.setMime(sourceModel.getMime());
         targetData.setSize(sourceModel.getMediaFormat().getName());
         targetData.setRoomPlanImages(sourceModel.getURL());
         // Data required for Hybris Content services
         targetData.setMainSrc(sourceModel.getURL());

      }
   }

   private String formatCode(final MediaModel sourceModel)
   {
      final String[] parts = sourceModel.getCode().split("_");
      return parts[0];
   }
}
