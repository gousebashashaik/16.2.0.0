/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author gaurav.b
 *
 */
public class AccommodationStandardWrapper {

    private Map<String, List<AccommodationViewData>> hotelTypeAccomViewDataListMap;

    @JsonIgnore
    private double budgetPriceFrom;

    @JsonIgnore
    private double standardPriceFrom;

    @JsonIgnore
    private double superiorPriceFrom;

    private String budgetFromPrice;

    private String standardFromPrice;

    private String superiorFromPrice;

    /**
     * @return the hotelTypeAccomViewDataListMap
     */
    public Map<String, List<AccommodationViewData>> getHotelTypeAccomViewDataListMap() {
        return hotelTypeAccomViewDataListMap;
    }

    /**
     * @param hotelTypeAccomViewDataListMap
     *            the hotelTypeAccomViewDataListMap to set
     */
    public void setHotelTypeAccomViewDataListMap(
            Map<String, List<AccommodationViewData>> hotelTypeAccomViewDataListMap) {
        this.hotelTypeAccomViewDataListMap = hotelTypeAccomViewDataListMap;
    }

    /**
     * @return the budgetPriceFrom
     */
    public double getBudgetPriceFrom() {
        return budgetPriceFrom;
    }

    /**
     * @param budgetPriceFrom the budgetPriceFrom to set
     */
    public void setBudgetPriceFrom(double budgetPriceFrom) {
        this.budgetPriceFrom = budgetPriceFrom;
    }

    /**
     * @return the standardPriceFrom
     */
    public double getStandardPriceFrom() {
        return standardPriceFrom;
    }

    /**
     * @param standardPriceFrom the standardPriceFrom to set
     */
    public void setStandardPriceFrom(double standardPriceFrom) {
        this.standardPriceFrom = standardPriceFrom;
    }

    /**
     * @return the superiorPriceFrom
     */
    public double getSuperiorPriceFrom() {
        return superiorPriceFrom;
    }

    /**
     * @param superiorPriceFrom the superiorPriceFrom to set
     */
    public void setSuperiorPriceFrom(double superiorPriceFrom) {
        this.superiorPriceFrom = superiorPriceFrom;
    }

    /**
     * @return the budgetFromPrice
     */
    public String getBudgetFromPrice() {
        return budgetFromPrice;
    }

    /**
     * @param budgetFromPrice the budgetFromPrice to set
     */
    public void setBudgetFromPrice(String budgetFromPrice) {
        this.budgetFromPrice = budgetFromPrice;
    }

    /**
     * @return the standardFromPrice
     */
    public String getStandardFromPrice() {
        return standardFromPrice;
    }

    /**
     * @param standardFromPrice the standardFromPrice to set
     */
    public void setStandardFromPrice(String standardFromPrice) {
        this.standardFromPrice = standardFromPrice;
    }

    /**
     * @return the superiorFromPrice
     */
    public String getSuperiorFromPrice() {
        return superiorFromPrice;
    }

    /**
     * @param superiorFromPrice the superiorFromPrice to set
     */
    public void setSuperiorFromPrice(String superiorFromPrice) {
        this.superiorFromPrice = superiorFromPrice;
    }
}
