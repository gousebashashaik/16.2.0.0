/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author geethanjali.k
 *
 */
public class BayNoteRequestData
{
    private String rating;
    private String bestFor;
    private String departureDate;
    private String depAirport;
    private Boolean cspSorting;
    private String productCode;

    /**
     * @return the rating
     */
    public String getRating()
    {
        return rating;
    }
    /**
     * @param rating the rating to set
     */
    public void setRating(String rating)
    {
        this.rating = rating;
    }
    /**
     * @return the bestFor
     */
    public String getBestFor()
    {
        return bestFor;
    }
    /**
     * @param bestFor the bestFor to set
     */
    public void setBestFor(String bestFor)
    {
        this.bestFor = bestFor;
    }
    /**
     * @return the departureDate
     */
    public String getDepartureDate()
    {
        return departureDate;
    }
    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(String departureDate)
    {
        this.departureDate = departureDate;
    }
    /**
     * @return the depAirport
     */
    public String getDepAirport()
    {
        return depAirport;
    }
    /**
     * @param depAirport the depAirport to set
     */
    public void setDepAirport(String depAirport)
    {
        this.depAirport = depAirport;
    }
    /**
     * @return the cspSorting
     */
    public Boolean getCspSorting()
    {
        return cspSorting;
    }
    /**
     * @param cspSorting the cspSorting to set
     */
    public void setCspSorting(Boolean cspSorting)
    {
        this.cspSorting = cspSorting;
    }
    /**
     * @return the productCode
     */
    public String getProductCode()
    {
        return productCode;
    }
    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }



}
