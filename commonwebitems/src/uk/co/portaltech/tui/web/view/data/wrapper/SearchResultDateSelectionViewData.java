/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author laxmibai.p
 *
 */
public class SearchResultDateSelectionViewData {

    private  List <String> availableValues= new ArrayList<String>();
    private String maxValue;
    private String minValue;
    private  List <Date> availableValuesForMobile= new ArrayList<Date>();

    private String depatureDate;
    private int flexibleDays;

    /**
     * @return the availableValues
     */
    public List<String> getAvailableValues() {
        return availableValues;
    }
    /**
     * @param availableValues the availableValues to set
     */
    public void setAvailableValues(List<String> availableValues) {
        this.availableValues = availableValues;
    }
    /**
     * @return the maxValue
     */
    public String getMaxValue() {
        return maxValue;
    }
    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }
    /**
     * @return the minValue
     */
    public String getMinValue() {
        return minValue;
    }
    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }
    /**
     * @return the availableValuesForMobile
     */
    public List<Date> getAvailableValuesForMobile() {
        return availableValuesForMobile;
    }
    /**
     * @param availableValuesForMobile the availableValuesForMobile to set
     */
    public void setAvailableValuesForMobile(List<Date> availableValuesForMobile) {
        this.availableValuesForMobile = availableValuesForMobile;
    }

    /**
     * @return the depatureDate
     */
    public String getDepatureDate() {
        return depatureDate;
    }

    /**
     * @param depatureDate the depatureDate to set
     */
    public void setDepatureDate(String depatureDate) {
        this.depatureDate = depatureDate;
    }

    /**
     * @return the flexibleDays
     */
    public int getFlexibleDays() {
        return flexibleDays;
    }

    /**
     * @param flexibleDays the flexibleDays to set
     */
    public void setFlexibleDays(int flexibleDays) {
        this.flexibleDays = flexibleDays;
    }
}
