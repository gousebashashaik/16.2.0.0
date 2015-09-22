/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class MerchandiserViewData
{


    private Map<String, String> sortBy = new HashMap<String, String>();

    private List<SearchResultViewData> holidays = new ArrayList<SearchResultViewData>();

    private InventoryFilterResponse filterPanel = new InventoryFilterResponse();


    private String pageLabel;

    private String name;

    private int endecaResultsCount;

    private int noOfAdults;
    private int noOfChildren;
    private int noOfSeniors;
    private String childrenAge = StringUtils.EMPTY;

    /**
     * @return the sortBy
     */
    public Map<String, String> getSortBy()
    {
        return sortBy;
    }

    /**
     * @param sortBy
     *           the sortBy to set
     */
    public void setSortBy(final Map<String, String> sortBy)
    {
        this.sortBy = sortBy;
    }

    /**
     * @return the holidays
     */
    public List<SearchResultViewData> getHolidays()
    {
        return holidays;
    }

    /**
     * @param holidays
     *           the holidays to set
     */
    public void setHolidays(final List<SearchResultViewData> holidays)
    {
        this.holidays = holidays;
    }


    /**
     * @return the pageLabel
     */
    public String getPageLabel()
    {
        return pageLabel;
    }

    /**
     * @param pageLabel
     *           the pageLabel to set
     */
    public void setPageLabel(final String pageLabel)
    {
        this.pageLabel = pageLabel;
    }


    /**
     * @return the endecaResultsCount
     */
    public int getEndecaResultsCount()
    {
        return endecaResultsCount;
    }

    /**
     * @param endecaResultsCount
     *           the endecaResultsCount to set
     */
    public void setEndecaResultsCount(final int endecaResultsCount)
    {
        this.endecaResultsCount = endecaResultsCount;
    }

    /**
     * @return the filterPanel
     */
    public InventoryFilterResponse getFilterPanel()
    {
        return filterPanel;
    }

    /**
     * @param filterPanel
     *           the filterPanel to set
     */
    public void setFilterPanel(final InventoryFilterResponse filterPanel)
    {
        this.filterPanel = filterPanel;
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
     * @return the childrenAge
     */
    public String getChildrenAge()
    {
        return childrenAge;
    }

    /**
     * @param childrenAge
     *           the childrenAge to set
     */
    public void setChildrenAge(final String childrenAge)
    {
        this.childrenAge = childrenAge;
    }

    /**
     * @return the noOfSeniors
     */
    public int getNoOfSeniors()
    {
        return noOfSeniors;
    }

    /**
     * @param noOfSeniors
     *           the noOfSeniors to set
     */
    public void setNoOfSeniors(final int noOfSeniors)
    {
        this.noOfSeniors = noOfSeniors;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *           the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }




}
