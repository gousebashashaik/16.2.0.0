/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.portaltech.travel.model.WeatherTypeValueModel;
import uk.co.portaltech.tui.web.view.data.WeatherTypeValueViewData;

/**
 * @author gagan
 *
 */
public class WeatherTypeValueViewDataPopulator implements Populator<WeatherTypeValueModel, WeatherTypeValueViewData> {

    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(WeatherTypeValueModel source, WeatherTypeValueViewData target) throws ConversionException {
        Double average = source.getAverage();
        if(average != null) {
            target.setAverage(average.doubleValue());
        }

        Double min = source.getMin();
        if(min != null) {
            target.setMin(min.doubleValue());
        }

        Double max = source.getMax();
        if(max != null) {
            target.setMin(max.doubleValue());
        }

        target.setMonth(source.getMonthType().getCode());
        target.setUnit(source.getWeatherUnitType().getCode());
    }

}
