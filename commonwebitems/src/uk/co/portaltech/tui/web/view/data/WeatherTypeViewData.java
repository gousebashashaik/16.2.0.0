/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author gagan
 *
 */
public class WeatherTypeViewData {

    private String weatherType;

    private List<WeatherTypeValueViewData> weatherTypeValueViewDataList;

    /**
     * @return the weatherType
     */
    public String getWeatherType() {
        return weatherType;
    }

    /**
     * @param weatherType the weatherType to set
     */
    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    /**
     * @return the weatherTypeValueViewDataList
     */
    public List<WeatherTypeValueViewData> getWeatherTypeValueViewDataList() {
        return weatherTypeValueViewDataList;
    }

    /**
     * @param weatherTypeValueViewDataList the weatherTypeViewDataList to set
     */
    public void setWeatherTypeValueViewDataList(List<WeatherTypeValueViewData> weatherTypeValueViewDataList) {
        this.weatherTypeValueViewDataList = weatherTypeValueViewDataList;
    }
}
