/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;
import java.util.List;

import uk.co.portaltech.travel.media.services.domain.Media;
import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * @author chethanram.bv
 *
 */
public class MediaPopulatorLite implements Populator<Collection<Media>, List<MediaViewData>>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final Collection<Media> source, final List<MediaViewData> target)
      throws ConversionException
   {
      for (final Media media : source)
      {
         final MediaViewData mediaViewData = new MediaViewData();
         mediaViewData.setCode(media.getCode());
         mediaViewData.setAltText(media.getAltText());
         mediaViewData.setDescription(media.getDescription());
         mediaViewData.setMime(media.getMime());
         mediaViewData.setMainSrc(media.getMainSrc());
         if (null != media.getSize())
         {
            mediaViewData.setSize(media.getSize().toString().toLowerCase());
         }
         mediaViewData.setRoomPlanImages(media.getRoomPlanImages());
         mediaViewData.setCaption(media.getCaption());
         mediaViewData.setBrand(media.getBrand());
         target.add(mediaViewData);
      }
   }
}
