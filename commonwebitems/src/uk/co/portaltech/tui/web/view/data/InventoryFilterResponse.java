/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Map;


public class InventoryFilterResponse {

    private SliderData fcRating = new SliderData();

    private SliderData tripAdivsorRating = new SliderData();

    private CommonFilterData bestFor = new CommonFilterData();

    private CommonFilterData features = new CommonFilterData();

    private CommonFilterData collection = new CommonFilterData();

    private CommonFilterData destination = new CommonFilterData();

    private Map<String, Boolean> filterVisibility;

    private boolean repaint;

    /**
     * @return the fcRating
     */
    public SliderData getFcRating() {
        return fcRating;
    }

    /**
     * @param fcRating the fcRating to set
     */
    public void setFcRating(SliderData fcRating) {
        this.fcRating = fcRating;
    }

    /**
     * @return the tripAdivsorRating
     */
    public SliderData getTripAdivsorRating() {
        return tripAdivsorRating;
    }

    /**
     * @param tripAdivsorRating the tripAdivsorRating to set
     */
    public void setTripAdivsorRating(SliderData tripAdivsorRating) {
        this.tripAdivsorRating = tripAdivsorRating;
    }

    /**
     * @return the bestFor
     */
    public CommonFilterData getBestFor() {
        return bestFor;
    }

    /**
     * @param bestFor the bestFor to set
     */
    public void setBestFor(CommonFilterData bestFor) {
        this.bestFor = bestFor;
    }

    /**
     * @return the features
     */
    public CommonFilterData getFeatures() {
        return features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(CommonFilterData features) {
        this.features = features;
    }

    /**
     * @return the collection
     */
    public CommonFilterData getCollection() {
        return collection;
    }

    /**
     * @param collection the collection to set
     */
    public void setCollection(CommonFilterData collection) {
        this.collection = collection;
    }

    /**
     * @return the destination
     */
    public CommonFilterData getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(CommonFilterData destination) {
        this.destination = destination;
    }

    /**
     * @return the filterVisibility
     */
    public Map<String, Boolean> getFilterVisibility() {
        return filterVisibility;
    }

    /**
     * @param filterVisibility the filterVisibility to set
     */
    public void setFilterVisibility(Map<String, Boolean> filterVisibility) {
        this.filterVisibility = filterVisibility;
    }

    /**
     * @return the repaint
     */
    public boolean isRepaint() {
        return repaint;
    }

    /**
     * @param repaint the repaint to set
     */
    public void setRepaint(boolean repaint) {
        this.repaint = repaint;
    }


}
