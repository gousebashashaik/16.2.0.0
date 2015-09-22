/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;


/**
 * @author arun.y
 *
 */

public class PhoenixDealsViewDataForMobile implements Comparator
{

    private String name;
    private LocationData resort;
    private int priceFrom;
    private List<ProductRangeViewData> productRanges;
    private Map<String, List<Object>> featureCodesAndValues;
    private String tRating;
    private String availableFrom;
    private String stayPeriod;
    private String departurePoint;
    private LocationData destination;
    private String locationMapUrl;
    private Collection<MediaViewData> galleryImages;
    private List<BoardBasisDataResponse> boardBasis;


    /**
     * @return the boardBasis
     */
    public List<BoardBasisDataResponse> getBoardBasis()
    {
        return boardBasis;
    }

    /**
     * @param boardBasis
     *           the boardBasis to set
     */
    public void setBoardBasis(List<BoardBasisDataResponse> boardBasis)
    {
        this.boardBasis = boardBasis;
    }

    /**
     * @return the galleryImages
     */
    public Collection<MediaViewData> getGalleryImages()
    {
        return galleryImages;
    }

    /**
     * @param galleryImages
     *           the galleryImages to set
     */
    public void setGalleryImages(Collection<MediaViewData> galleryImages)
    {
        this.galleryImages = galleryImages;
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
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the resort
     */
    public LocationData getResort()
    {
        return resort;
    }

    /**
     * @param resort
     *           the resort to set
     */
    public void setResort(LocationData resort)
    {
        this.resort = resort;
    }

    /**
     * @return the priceFrom
     */
    public int getPriceFrom()
    {
        return priceFrom;
    }

    /**
     * @param priceFrom
     *           the priceFrom to set
     */
    public void setPriceFrom(int priceFrom)
    {
        this.priceFrom = priceFrom;
    }

    /**
     * @return the productRanges
     */
    public List<ProductRangeViewData> getProductRanges()
    {
        return productRanges;
    }

    /**
     * @param productRanges
     *           the productRanges to set
     */
    public void setProductRanges(List<ProductRangeViewData> productRanges)
    {
        this.productRanges = productRanges;
    }

    /**
     * @return the featureCodesAndValues
     */
    public Map<String, List<Object>> getFeatureCodesAndValues()
    {
        return featureCodesAndValues;
    }

    /**
     * @param featureCodesAndValues
     *           the featureCodesAndValues to set
     */
    public void setFeatureCodesAndValues(Map<String, List<Object>> featureCodesAndValues)
    {
        this.featureCodesAndValues = featureCodesAndValues;
    }

    /**
     * @return the tRating
     */
    public String gettRating()
    {
        return tRating;
    }

    /**
     * @param tRating
     *           the tRating to set
     */
    public void settRating(String tRating)
    {
        this.tRating = tRating;
    }

    /**
     * @return the availableFrom
     */
    public String getAvailableFrom()
    {
        return availableFrom;
    }

    /**
     * @param availableFrom
     *           the availableFrom to set
     */
    public void setAvailableFrom(String availableFrom)
    {
        this.availableFrom = availableFrom;
    }

    /**
     * @return the stayPeriod
     */
    public String getStayPeriod()
    {
        return stayPeriod;
    }

    /**
     * @param stayPeriod
     *           the stayPeriod to set
     */
    public void setStayPeriod(String stayPeriod)
    {
        this.stayPeriod = stayPeriod;
    }

    /**
     * @return the departurePoint
     */
    public String getDeparturePoint()
    {
        return departurePoint;
    }

    /**
     * @param departurePoint
     *           the departurePoint to set
     */
    public void setDeparturePoint(String departurePoint)
    {
        this.departurePoint = departurePoint;
    }

    /**
     * @return the destination
     */
    public LocationData getDestination()
    {
        return destination;
    }

    /**
     * @param destination
     *           the destination to set
     */
    public void setDestination(LocationData destination)
    {
        this.destination = destination;
    }

    /**
     * @return the locationMapUrl
     */
    public String getLocationMapUrl()
    {
        return locationMapUrl;
    }

    /**
     * @param locationMapUrl
     *           the locationMapUrl to set
     */
    public void setLocationMapUrl(String locationMapUrl)
    {
        this.locationMapUrl = locationMapUrl;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Object o1, final Object o2)
    {
        final PhoenixDealsViewDataForMobile price1 = (PhoenixDealsViewDataForMobile) o1;
        final PhoenixDealsViewDataForMobile price2 = (PhoenixDealsViewDataForMobile) o2;
        if (price1.getPriceFrom() == price2.getPriceFrom())
        {
            return 0;
        }
        else if (price1.getPriceFrom() > price2.getPriceFrom())
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
