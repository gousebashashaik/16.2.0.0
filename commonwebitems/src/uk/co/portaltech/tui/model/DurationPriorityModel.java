/**
 *
 */
package uk.co.portaltech.tui.model;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author gaurav.b
 *
 */
public class DurationPriorityModel {

    private List<String> prioritizedCountries = new ArrayList<String>();

    private String priorityOrderForCountry;

    private String priorityOrderForShortHaul;

    private String priorityOrderForLongHaul;

    private String priorityOrderForDefault;

    private String destinationCountry;

    private String haulType;

    private String priorityOrder;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;



    public DurationPriorityModel() {
        this.destinationCountry = "COUNTRY";
        this.haulType = "HAUL";
    }

    /**
     * @return the prioritizedCountries
     */
    public List<String> getPrioritizedCountries() {
        return prioritizedCountries;
    }

    /**
     * @return the priorityOrderForCountry
     */
    public String getPriorityOrderForCountry() {
        return priorityOrderForCountry;
    }

    /**
     * @return the priorityOrderForShortHaul
     */
    public String getPriorityOrderForShortHaul() {
        return priorityOrderForShortHaul;
    }

    /**
     * @return the priorityOrderForLongHaul
     */
    public String getPriorityOrderForLongHaul() {
        return priorityOrderForLongHaul;
    }

    /**
     * @return the priorityOrderForDefault
     */
    public String getPriorityOrderForDefault() {
        return priorityOrderForDefault;
    }

    /**
     * @return the destinationCountry
     */
    public String getDestinationCountry() {
        return destinationCountry;
    }

    /**
     * @param destinationCountry
     *            the destinationCountry to set
     */
    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    /**
     * @return the haulType
     */
    public String getHaulType() {
        return haulType;
    }

    /**
     * @param haulType
     *            the haulType to set
     */
    public void setHaulType(String haulType) {
        this.haulType = haulType;
    }

    /**
     * @return the priorityOrder
     */
    public String getPriorityOrder() {
        return priorityOrder;
    }

    /**
     * @param priorityOrder
     *            the priorityOrder to set
     */
    public void setPriorityOrder(String priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    /**
     * This method set the duration rule data values from the local.property
     * file.
     *
     */
    public void setDurationRuleDataValues() {
        setPrioritizedCountries(configurationService.getConfiguration().getString(
                "DURATION_PRIORITY_COUNTRIES"));
        priorityOrderForCountry = configurationService.getConfiguration().getString(
                "DURATION_PRIORITY_ORDER_FOR_COUNTRY");
        priorityOrderForLongHaul = configurationService.getConfiguration().getString(
                "DURATION_PRIORITY_ORDER_FOR_LONG_HAUL");
        priorityOrderForShortHaul = configurationService.getConfiguration().getString(
                "DURATION_PRIORITY_ORDER_FOR_SHORT_HAUL");
        priorityOrderForDefault = configurationService.getConfiguration().getString(
                "DURATION_PRIORITY_ORDER_FOR_DEFAULT");
    }

    /**
     * This method splits the comma separated countries name and add into the
     * list
     *
     * @param var
     *            the comma separated countries name.
     */
    private void setPrioritizedCountries(String var) {
        String[] countryArray = var.split(",");
        for (String country : countryArray) {
            prioritizedCountries.add(country.trim().toLowerCase());
        }
    }
}
