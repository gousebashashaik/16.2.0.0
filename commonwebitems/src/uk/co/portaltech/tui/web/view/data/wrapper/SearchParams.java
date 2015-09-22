/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.List;


/**
 * @author arya.ap
 *
 */
public class SearchParams
{

    private int noOfAdults;

    private int noOfChildren;

    private List<AirportViewData> airports;

    private List<AccomInfoViewData> units;

    private int duration;



    /**
     * @return the noOfChildren
     */
    public int getNoOfChildren()
    {
        return noOfChildren;
    }

    /**
     * @param noOfChildren
     *           the noOfChildren to set
     */
    public void setNoOfChildren(final int noOfChildren)
    {
        this.noOfChildren = noOfChildren;
    }


    /**
     * @return the noOfAdults
     */
    public int getNoOfAdults()
    {
        return noOfAdults;
    }

    /**
     * @param noOfAdults
     *           the noOfAdults to set
     */
    public void setNoOfAdults(final int noOfAdults)
    {
        this.noOfAdults = noOfAdults;
    }

    /**
     * @return the airports
     */
    public List<AirportViewData> getAirports()
    {
        return airports;
    }

    /**
     * @param airports
     *           the airports to set
     */
    public void setAirports(final List<AirportViewData> airports)
    {
        this.airports = airports;
    }

    /**
     * @return the units
     */
    public List<AccomInfoViewData> getUnits()
    {
        return units;
    }

    /**
     * @param units
     *           the units to set
     */
    public void setUnits(final List<AccomInfoViewData> units)
    {
        this.units = units;
    }

    /**
     * @return the duration
     */
    public int getDuration()
    {
        return duration;
    }

    /**
     * @param duration
     *           the duration to set
     */
    public void setDuration(final int duration)
    {
        this.duration = duration;
    }



}
