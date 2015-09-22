/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.tui.web.view.data.WeatherViewData;

/**
 * @author gagan
 *
 */
public class WeatherViewDataConverter extends
   AbstractPopulatingConverter<WeatherModel, WeatherViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public WeatherViewData createTarget()
   {
      return new WeatherViewData();
   }

}
