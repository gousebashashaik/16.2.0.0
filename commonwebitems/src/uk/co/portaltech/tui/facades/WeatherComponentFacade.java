/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.WeatherViewData;

/**
 * @author gagan
 *
 */
public interface WeatherComponentFacade {

    LocationModel getCurrentLocationDetails(String categoryCode, String prodCode);

    WeatherViewData getWeatherData(LocationModel locationModel);

    WeatherViewData getWeatherDataForLocation(LocationModel locationModel);

    /**
     * This retrieves weather data at country level, where it gets all the data for the country sub locations
     *
     * @param locationModel
     *            The country location Model
     * @return A list of weather data
     */

    List<WeatherViewData> getCountryWeatherData(LocationModel locationModel);

    LocationModel getDestinationsForCountryOrRegion(LocationModel locationModel, int depthLevel);

    List<WeatherViewData> getSpainWeatherData(final LocationModel locationModel);

    /**
     * @param resortModel
     *            resort model to search the destination for
     * @return the destination
     */
    LocationModel getDestinationsForResort(LocationModel resortModel);

    List<LocationModel> getDestinations(LocationModel locationModel);

}
