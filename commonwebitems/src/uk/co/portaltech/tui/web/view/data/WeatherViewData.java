/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author gagan
 *
 */
public class WeatherViewData {

    private List<WeatherTypeViewData> weatherTypes;

    private String                    locationName;

    /**
     * @return the weatherTypes
     */
    public List<WeatherTypeViewData> getWeatherTypes() {
        return weatherTypes;
    }

    /**
     * @param weatherTypes
     *            the weatherTypes to set
     */
    public void setWeatherTypes(List<WeatherTypeViewData> weatherTypes) {
        this.weatherTypes = weatherTypes;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}
