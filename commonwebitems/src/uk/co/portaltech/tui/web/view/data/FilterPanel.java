/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Map;

/**
 * @author extcs5
 *
 */
public class FilterPanel {


    private String priceView;
    private SliderMainData budget;
    private SliderMainData totalbudget;
    private SliderMainData ppbudget;

    private SliderMainData temperatureOptions;

    private SliderMainData rating;

    private DestinationOptionData destinationOptions;

    private FilterData accommodationOptions;

    private FilterData flightOptions;

    private FilterData generalOptions;

    private Map<String, Boolean> filterVisibility;

    private boolean repaint;

    /**
     * @return the priceView
     */
    public String getPriceView()
    {
        return priceView;
    }

    /**
     * @param priceView
     *           the priceView to set
     */
    public void setPriceView(final String priceView)
    {
        this.priceView = priceView;
    }

    /**
     * @return the destinationOptions
     */
    public DestinationOptionData getDestinationOptions() {
        return destinationOptions;
    }
    /**
     * @param destinationOptions the destinationOptions to set
     */

    public void setDestinationOptions(DestinationOptionData destinationOptionData) {
        this.destinationOptions = destinationOptionData;
    }

    /**
     * @return the generalOptions
     */
    public FilterData getGeneralOptions() {
        return generalOptions;
    }

    /**
     * @param generalOptions the generalOptions to set
     */
    public void setGeneralOptions(FilterData generalOptions) {
        this.generalOptions = generalOptions;
    }



    /**
     * @return the temperatureOptions
     */
    public SliderMainData getTemperatureOptions() {
        return temperatureOptions;
    }

    /**
     * @param temperatureOptions the temperatureOptions to set
     */
    public void setTemperatureOptions(SliderMainData temperatureOptions) {
        this.temperatureOptions = temperatureOptions;
    }

    /**
     * @return the rating
     */
    public SliderMainData getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(SliderMainData rating) {
        this.rating = rating;
    }

    /**
     * @return the accommodationOptions
     */
    public FilterData getAccommodationOptions() {
        return accommodationOptions;
    }

    /**
     * @param accommodationOptions the accommodationOptions to set
     */
    public void setAccommodationOptions(FilterData accommodationOptions) {
        this.accommodationOptions = accommodationOptions;
    }

    /**
     * @return the flightOption
     */
    public FilterData getFlightOptions() {
        return flightOptions;
    }

    /**
     * @param flightOptions the flightOption to set
     */
    public void setFlightOptions(FilterData flightOptions) {
        this.flightOptions = flightOptions;
    }

    /**
     * @return the budget
     */
    public SliderMainData getBudget() {
        return budget;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(SliderMainData budget) {
        this.budget = budget;
    }

    /**
     * @return the ppbudget
     */
    public SliderMainData getPpbudget() {
        return ppbudget;
    }

    /**
     * @param ppbudget the ppbudget to set
     */
    public void setPpbudget(SliderMainData ppbudget) {
        this.ppbudget = ppbudget;
    }

    /**
     * @return the totalbudget
     */
    public SliderMainData getTotalbudget() {
        return totalbudget;
    }
    /**
     * @param totalbudget the totalbudget to set
     */
    public void setTotalbudget(SliderMainData totalbudget) {
        this.totalbudget = totalbudget;
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
