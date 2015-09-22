/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.tui.web.view.data.WeatherTypeViewData;

/**
 * @author gagan
 *
 */
public class WeatherTypeViewDataConverter extends
   AbstractPopulatingConverter<WeatherTypeModel, WeatherTypeViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public WeatherTypeViewData createTarget()
   {
      return new WeatherTypeViewData();
   }

}
