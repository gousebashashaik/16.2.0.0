/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.CountryViewData;

/**
 * @author sunilkumar.sahu
 *
 */
public class CountryViewDataConverter extends
   AbstractPopulatingConverter<LocationModel, CountryViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public CountryViewData createTarget()
   {

      return new CountryViewData();
   }

   @Override
   public void populate(final LocationModel source, final CountryViewData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      target.setId(source.getCode());
      target.setName(source.getName());
      target.setType(source.getType());
   }
}
