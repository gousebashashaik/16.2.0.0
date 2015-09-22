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

import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.model.WeatherTypeValueModel;
import uk.co.portaltech.tui.web.view.data.WeatherTypeValueViewData;
import uk.co.portaltech.tui.web.view.data.WeatherTypeViewData;

/**
 * @author gagan
 *
 */
public class WeatherTypeViewDataPopulator implements Populator<WeatherTypeModel, WeatherTypeViewData> {

    @Resource
    private Converter<WeatherTypeValueModel, WeatherTypeValueViewData>                 weatherTypeValueViewDataConverter;

    private Populator<WeatherTypeValueModel, WeatherTypeValueViewData>                 weatherTypeValueViewDataPopulator;

    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(WeatherTypeModel weatherTypeModel, WeatherTypeViewData weatherTypeViewData) throws ConversionException {
        weatherTypeViewData.setWeatherType(weatherTypeModel.getWeatherTypeName().getCode());
        List<WeatherTypeValueModel> weatherTypeValueModelList = weatherTypeModel.getWeatherTypeValues();
        if(weatherTypeValueModelList != null && !weatherTypeValueModelList.isEmpty()) {
            List<WeatherTypeValueViewData> weatherTypeValueViewDataList = new ArrayList<WeatherTypeValueViewData>();
            for (WeatherTypeValueModel weatherTypeValueModel : weatherTypeValueModelList) {
                WeatherTypeValueViewData weatherTypeValueViewData = weatherTypeValueViewDataConverter.convert(weatherTypeValueModel);
                weatherTypeValueViewDataPopulator.populate(weatherTypeValueModel, weatherTypeValueViewData);
                weatherTypeValueViewDataList.add(weatherTypeValueViewData);
            }
            weatherTypeViewData.setWeatherTypeValueViewDataList(weatherTypeValueViewDataList);
        }
    }

    /**
     * @return the weatherTypeValueViewDataPopulator
     */
    public Populator<WeatherTypeValueModel, WeatherTypeValueViewData> getWeatherTypeValueViewDataPopulator() {
        return weatherTypeValueViewDataPopulator;
    }

    /**
     * @param weatherTypeValueViewDataPopulator the weatherTypeValueViewDataPopulator to set
     */
    public void setWeatherTypeValueViewDataPopulator(Populator<WeatherTypeValueModel, WeatherTypeValueViewData> weatherTypeValueViewDataPopulator) {
        this.weatherTypeValueViewDataPopulator = weatherTypeValueViewDataPopulator;
    }

}
