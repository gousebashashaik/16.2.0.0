/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.util.List;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;

/**
 * @author s.consolino
 *
 */
public class AccommodationInspirationMapConverter extends
   AbstractPopulatingConverter<List<AccommodationModel>, InspirationMapViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public InspirationMapViewData createTarget()
   {
      return new InspirationMapViewData();
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter#populate(java
    * .lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final List<AccommodationModel> source, final InspirationMapViewData target)
   {
      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");

   }
}
