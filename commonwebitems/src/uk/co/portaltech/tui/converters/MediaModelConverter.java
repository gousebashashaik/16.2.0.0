/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.media.MediaModel;

import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * @author gagan
 *
 */
public class MediaModelConverter extends AbstractPopulatingConverter<MediaModel, MediaViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public MediaViewData createTarget()
   {
      return new MediaViewData();
   }

}
