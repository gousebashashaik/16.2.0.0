/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author gagan
 *
 *         A generic populator for populating image data list from a collection of media container
 *         model.
 */
public class ImageDataPopulator implements
   Populator<Collection<MediaContainerModel>, List<ImageData>>
{

   private MediaService mediaService;

   private MediaContainerService mediaContainerService;

   private ImageFormatMapping imageFormatMapping;

   private List<String> imageFormats;

   private Converter<MediaModel, ImageData> imageConverter;

   private static final TUILogUtils LOG = new TUILogUtils("ImageDataPopulator");

   @Required
   public void setMediaService(final MediaService mediaService)
   {
      this.mediaService = mediaService;
   }

   @Required
   public void setMediaContainerService(final MediaContainerService mediaContainerService)
   {
      this.mediaContainerService = mediaContainerService;
   }

   @Required
   public void setImageFormatMapping(final ImageFormatMapping imageFormatMapping)
   {
      this.imageFormatMapping = imageFormatMapping;
   }

   public List<String> getImageFormats()
   {
      return imageFormats;
   }

   public void setImageFormats(final List<String> imageFormats)
   {
      this.imageFormats = imageFormats;
   }

   @Required
   public void setImageConverter(final Converter<MediaModel, ImageData> imageConverter)
   {
      this.imageConverter = imageConverter;
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final Collection<MediaContainerModel> mediaCollectionModelList,
      final List<ImageData> imageDataList) throws ConversionException
   {
      int galleryIndex = 0;
      for (final MediaContainerModel mediaContainerModel : mediaCollectionModelList)
      {
         addImagesInFormats(mediaContainerModel, galleryIndex++, imageDataList);
      }

   }

   protected void addImagesInFormats(final MediaContainerModel mediaContainer,
      final int galleryIndex, final List<ImageData> list)
   {
      for (final String imageFormat : imageFormats)
      {
         final String mediaFormatQualifier =
            imageFormatMapping.getMediaFormatQualifierForImageFormat(imageFormat);
         if (mediaFormatQualifier != null)
         {
            final MediaFormatModel mediaFormat = mediaService.getFormat(mediaFormatQualifier);
            if (mediaFormat != null)
            {
               try
               {
                  final MediaModel media =
                     mediaContainerService.getMediaForFormat(mediaContainer, mediaFormat);
                  if (media != null)
                  {
                     addToImageDataList(galleryIndex, list, imageFormat, media);
                  }
               }
               catch (final ModelNotFoundException mnf)
               {
                  LOG.warn("Could not find media for the size " + mediaFormat
                     + "for media mogul Id " + mediaContainer.getMediaMogulId(), mnf);
               }
            }
         }
      }

   }

   /**
    * @param galleryIndex
    * @param list
    * @param imageFormat
    * @param media
    */
   private void addToImageDataList(final int galleryIndex, final List<ImageData> list,
      final String imageFormat, final MediaModel media)
   {
      final ImageData imageData = imageConverter.convert(media);
      imageData.setFormat(imageFormat);
      final String imageType = imageData.getImageType();
      if (ImageData.ImageType.GALLERY.equals(imageType))
      {
         imageData.setGalleryIndex(Integer.valueOf(galleryIndex));
      }
      list.add(imageData);
   }
}
