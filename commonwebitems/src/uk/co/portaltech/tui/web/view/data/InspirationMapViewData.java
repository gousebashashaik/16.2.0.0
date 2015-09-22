/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;


/**
 * @author s.consolino
 *
 */
public class InspirationMapViewData
{
    private String title;
    private String pictureUrl;
    private List<MarkerMapViewData> markerMapViewDataList;
    private int markupListCount;
    private String locationType;
    private String siteBrand;



    /**
     * @return the locationType
     */
    public String getLocationType()
    {
        return locationType;
    }

    /**
     * @return the siteBrand
     */
    public String getSiteBrand()
    {
        return siteBrand;
    }

    /**
     * @param siteBrand
     *           the siteBrand to set
     */
    public void setSiteBrand(final String siteBrand)
    {
        this.siteBrand = siteBrand;
    }

    /**
     * @param locationType
     *           the locationType to set
     */
    public void setLocationType(final String locationType)
    {
        this.locationType = locationType;
    }

    /**
     * @return the picture
     */
    public String getPictureUrl()
    {
        return pictureUrl;
    }

    /**
     * @param picture
     *           the picture to set
     */
    public void setPictureUrl(final String picture)
    {
        this.pictureUrl = picture;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *           the title to set
     */
    public void setTitle(final String title)
    {
        this.title = title;
    }

    /**
     * @return the markerMapViewDataList
     */
    public List<MarkerMapViewData> getMarkerMapViewDataList()
    {
        return markerMapViewDataList;
    }

    /**
     * @param markerMapViewDataList
     *           the markerMapViewDataList to set
     */
    public void setMarkerMapViewDataList(final List<MarkerMapViewData> markerMapViewDataList)
    {
        this.markerMapViewDataList = markerMapViewDataList;
    }

    /**
     * @return the markupListCount
     */
    public int getMarkupListCount()
    {
        return markupListCount;
    }

    /**
     * @param markupListCount
     *           the markupListCount to set
     */
    public void setMarkupListCount(final int markupListCount)
    {
        this.markupListCount = markupListCount;
    }
}
