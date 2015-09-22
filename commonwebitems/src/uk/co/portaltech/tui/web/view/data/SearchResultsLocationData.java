/**
 *
 */
package uk.co.portaltech.tui.web.view.data;


public class SearchResultsLocationData
{

    private SearchResultsDestinationData destination = new SearchResultsDestinationData();
    private SearchResultsResortData resort = new SearchResultsResortData();
    private SearchResultsRegionData region = new SearchResultsRegionData();
    private SearchResultsCountryData country = new SearchResultsCountryData();

    /**
     * @return the destination
     */
    public SearchResultsDestinationData getDestination()
    {
        return destination;
    }

    /**
     * @return the resort
     */
    public SearchResultsResortData getResort()
    {
        return resort;
    }

    /**
     * @param destination
     *           the destination to set
     */
    public void setDestination(SearchResultsDestinationData destination)
    {
        this.destination = destination;
    }

    /**
     * @param resort
     *           the resort to set
     */
    public void setResort(SearchResultsResortData resort)
    {
        this.resort = resort;
    }

    /**
     * @return the region
     */
    public SearchResultsRegionData getRegion()
    {
        return region;
    }

    /**
     * @param region
     *           the region to set
     */
    public void setRegion(SearchResultsRegionData region)
    {
        this.region = region;
    }

    /**
     * @return the country
     */
    public SearchResultsCountryData getCountry()
    {
        return country;
    }

    /**
     * @param country
     *           the country to set
     */
    public void setCountry(SearchResultsCountryData country)
    {
        this.country = country;
    }

}
