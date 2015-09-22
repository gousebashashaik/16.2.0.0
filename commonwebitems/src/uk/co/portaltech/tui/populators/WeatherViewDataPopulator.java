/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.tui.web.view.data.WeatherTypeViewData;
import uk.co.portaltech.tui.web.view.data.WeatherViewData;

/**
 * @author gagan
 *
 */
public class WeatherViewDataPopulator implements Populator<WeatherModel, WeatherViewData> {

    @Resource
    private Converter<WeatherTypeModel, WeatherTypeViewData>                 weatherTypeViewDataConverter;

    private Populator<WeatherTypeModel, WeatherTypeViewData>                 weatherTypeViewDataPopulator;

    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(WeatherModel weatherModel, WeatherViewData weatherTarget) throws ConversionException {
        List<WeatherTypeModel> weatherTypeModelList = weatherModel.getWeatherTypes();
        if(weatherTypeModelList != null && !weatherTypeModelList.isEmpty()) {
            List<WeatherTypeViewData> weatherTypeViewDataList = new ArrayList<WeatherTypeViewData>();
            for (WeatherTypeModel weatherTypeModel : weatherTypeModelList) {
                WeatherTypeViewData weatherTypeViewData = weatherTypeViewDataConverter.convert(weatherTypeModel);
                weatherTypeViewDataPopulator.populate(weatherTypeModel, weatherTypeViewData);
                weatherTypeViewDataList.add(weatherTypeViewData);
            }
            weatherTarget.setWeatherTypes(weatherTypeViewDataList);
        }
    }

    /**
     * @return the weatherTypeViewDataPopulator
     */
    public Populator<WeatherTypeModel, WeatherTypeViewData> getWeatherTypeViewDataPopulator() {
        return weatherTypeViewDataPopulator;
    }

    /**
     * @param weatherTypeViewDataPopulator the weatherTypeViewDataPopulator to set
     */
    public void setWeatherTypeViewDataPopulator(Populator<WeatherTypeModel, WeatherTypeViewData> weatherTypeViewDataPopulator) {
        this.weatherTypeViewDataPopulator = weatherTypeViewDataPopulator;
    }

}
