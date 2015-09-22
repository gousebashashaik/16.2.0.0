/**
 *
 */
package uk.co.portaltech.tui.web.view.data;


/**
 * @author manju.ts
 *
 */
public class AccommodationOptionData {

    private FilterData holidayType;
    private FilterData bestFor ;
    private FilterData features;

    /**
     * @return the holidayType
     */
    public FilterData getHolidayType() {
        return holidayType;
    }
    /**
     * @param holidayType the holidayType to set
     */
    public void setHolidayType(FilterData holidayType) {
        this.holidayType = holidayType;
    }
    /**
     * @return the bestFor
     */
    public FilterData getBestFor() {
        return bestFor;
    }
    /**
     * @param bestFor the bestFor to set
     */
    public void setBestFor(FilterData bestFor) {
        this.bestFor = bestFor;
    }
    /**
     * @return the features
     */
    public FilterData getFeatures() {
        return features;
    }
    /**
     * @param features the features to set
     */
    public void setFeatures(FilterData features) {
        this.features = features;
    }




}
