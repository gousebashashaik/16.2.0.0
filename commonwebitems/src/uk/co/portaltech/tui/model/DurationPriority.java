/**
 *
 */
package uk.co.portaltech.tui.model;


import java.util.ArrayList;
import java.util.List;

/**
 * @author gaurav.b
 *
 */
public class DurationPriority {

    private List<String> prioritizedCountries = new ArrayList<String>();

    private String priorityOrderForCountry;

    private String priorityOrderForShortHaul;

    private String priorityOrderForLongHaul;

    private String priorityOrderForDefault;

    private String destinationCountry;

    private String haulType;

    private String priorityOrder;

    public DurationPriority() {
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
     * @param prioritizedCountries the prioritizedCountries to set
     */
    public void setPrioritizedCountries(List<String> prioritizedCountries) {
        this.prioritizedCountries = prioritizedCountries;
    }

    /**
     * @param priorityOrderForCountry the priorityOrderForCountry to set
     */
    public void setPriorityOrderForCountry(String priorityOrderForCountry) {
        this.priorityOrderForCountry = priorityOrderForCountry;
    }

    /**
     * @param priorityOrderForDefault the priorityOrderForDefault to set
     */
    public void setPriorityOrderForDefault(String priorityOrderForDefault) {
        this.priorityOrderForDefault = priorityOrderForDefault;
    }

    /**
     * @param priorityOrderForLongHaul the priorityOrderForLongHaul to set
     */
    public void setPriorityOrderForLongHaul(String priorityOrderForLongHaul) {
        this.priorityOrderForLongHaul = priorityOrderForLongHaul;
    }

    /**
     * @param priorityOrderForShortHaul the priorityOrderForShortHaul to set
     */
    public void setPriorityOrderForShortHaul(String priorityOrderForShortHaul) {
        this.priorityOrderForShortHaul = priorityOrderForShortHaul;
    }
}
