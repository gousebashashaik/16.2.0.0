/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;



/**
 * @author Manju.ts
 *
 */
public class IGFilterRequest {


    private SliderRequest fcRating;

    private SliderRequest tripadvisorrating;

    private List<FilterRequest> features;

    private List<FilterRequest> holidayType;

    private List<FilterRequest> bestfor;

    private List<FilterRequest> destinations;



    /**
     * @return the fcRating
     */
    public SliderRequest getFcRating() {
        return fcRating;
    }

    /**
     * @param fcRating the fcRating to set
     */
    public void setFcRating(SliderRequest fcRating) {
        this.fcRating = fcRating;
    }

    /**
     * @return the tripadvisorrating
     */
    public SliderRequest getTripadvisorrating() {
        return tripadvisorrating;
    }

    /**
     * @param tripadvisorrating the tripadvisorrating to set
     */
    public void setTripadvisorrating(SliderRequest tripadvisorrating) {
        this.tripadvisorrating = tripadvisorrating;
    }


    /**
     * @return the features
     */
    public List<FilterRequest> getFeatures() {
        return features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(List<FilterRequest> features) {
        this.features = features;
    }

    /**
     * @return the holidayType
     */
    public List<FilterRequest> getHolidayType() {
        return holidayType;
    }

    /**
     * @param holidayType the holidayType to set
     */
    public void setHolidayType(List<FilterRequest> holidayType) {
        this.holidayType = holidayType;
    }

    /**
     * @return the bestfor
     */
    public List<FilterRequest> getBestfor() {
        return bestfor;
    }

    /**
     * @param bestfor the bestfor to set
     */
    public void setBestfor(List<FilterRequest> bestfor) {
        this.bestfor = bestfor;
    }

    /**
     * @return the destinations
     */
    public List<FilterRequest> getDestinations() {
        return destinations;
    }

    /**
     * @param destinations the destinations to set
     */
    public void setDestinations(List<FilterRequest> destinations) {
        this.destinations = destinations;
    }



}
