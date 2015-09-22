/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author premkumar.nd
 *
 */
public class ShipViewData
{
    private String name;

    private String code;

    private String type;

    private String itineraryShipSelectorUrl;

    private final Map<String, String> featureCodeNValueMap = new HashMap<String, String>();

    private List<MediaViewData> galleryImages;

    private String differentiatedType;

    private String differentiatedCode;

    private boolean differentiatedProduct;

    private int tripAdvisorReviewCount;

    private String strapline;


    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *           the type to set
     */
    public void setType(final String type)
    {
        this.type = type;
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

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *           the code to set
     */
    public void setCode(final String code)
    {
        this.code = code;
    }

    /**
     * @return the itineraryShipSelectorUrl
     */
    public String getItineraryShipSelectorUrl()
    {
        return itineraryShipSelectorUrl;
    }

    /**
     * @param itineraryShipSelectorUrl
     *           the itineraryShipSelectorUrl to set
     */
    public void setItineraryShipSelectorUrl(final String itineraryShipSelectorUrl)
    {
        this.itineraryShipSelectorUrl = itineraryShipSelectorUrl;
    }

    /**
     * @return the galleryImages
     */
    public List<MediaViewData> getGalleryImages()
    {
        return galleryImages;
    }

    /**
     * @param galleryImages
     *           the galleryImages to set
     */
    public void setGalleryImages(final List<MediaViewData> galleryImages)
    {
        this.galleryImages = galleryImages;
    }

    /**
     * @return the differentiatedType
     */
    public String getDifferentiatedType()
    {
        return differentiatedType;
    }

    /**
     * @param differentiatedType
     *           the differentiatedType to set
     */
    public void setDifferentiatedType(final String differentiatedType)
    {
        this.differentiatedType = differentiatedType;
    }

    /**
     * @return the differentiatedCode
     */
    public String getDifferentiatedCode()
    {
        return differentiatedCode;
    }

    /**
     * @param differentiatedCode
     *           the differentiatedCode to set
     */
    public void setDifferentiatedCode(final String differentiatedCode)
    {
        this.differentiatedCode = differentiatedCode;
    }

    /**
     * @return the differentiatedProduct
     */
    public boolean isDifferentiatedProduct()
    {
        return differentiatedProduct;
    }

    /**
     * @param differentiatedProduct
     *           the differentiatedProduct to set
     */
    public void setDifferentiatedProduct(final boolean differentiatedProduct)
    {
        this.differentiatedProduct = differentiatedProduct;
    }

    /**
     * @return the tripAdvisorReviewCount
     */
    public int getTripAdvisorReviewCount()
    {
        return tripAdvisorReviewCount;
    }

    /**
     * @param tripAdvisorReviewCount
     *           the tripAdvisorReviewCount to set
     */
    public void setTripAdvisorReviewCount(final int tripAdvisorReviewCount)
    {
        this.tripAdvisorReviewCount = tripAdvisorReviewCount;
    }

    /**
     * @return the strapline
     */
    public String getStrapline()
    {
        return strapline;
    }

    /**
     * @param strapline
     *           the strapline to set
     */
    public void setStrapline(final String strapline)
    {
        this.strapline = strapline;
    }

    /**
     * @return the featureCodeNValueMap
     */
    public Map<String, String> getFeatureCodeNValueMap()
    {
        return featureCodeNValueMap;
    }



}
