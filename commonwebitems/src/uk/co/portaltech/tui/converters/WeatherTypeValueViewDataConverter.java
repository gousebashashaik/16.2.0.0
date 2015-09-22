/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.travel.model.WeatherTypeValueModel;
import uk.co.portaltech.tui.web.view.data.WeatherTypeValueViewData;

/**
 * @author gagan
 *
 */
public class WeatherTypeValueViewDataConverter extends
   AbstractPopulatingConverter<WeatherTypeValueModel, WeatherTypeValueViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public WeatherTypeValueViewData createTarget()
   {
      return new WeatherTypeValueViewData();
   }

}
