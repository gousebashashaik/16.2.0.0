/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;


/**
 *
 */
public class SearchResultAccomodationViewData
{




    private String code;
    private String name;
    private String imageUrl;

    private final Map<String, List<String>> imageUrlsMap = new HashMap<String, List<String>>();

    private boolean differentiatedProduct;

    //old diffrnetial type
    private String differentiatedType;

    private String differentiatedCode;

    private String strapline;

    private SearchResultsLocationData location = new SearchResultsLocationData();

    private Map locationMap = new HashMap<String, String>();

    private RatingsData ratings = new RatingsData();

    private List<SearchResultRoomsData> rooms = new ArrayList<SearchResultRoomsData>();

    private int tripAdvisorReviewCount;

    private int commercialPriority;

    private final Map<String, List<Object>> featureCodesAndValues = new HashMap<String, List<Object>>();

    private String latitude;

    private String longitude;

    private Collection<MediaViewData> galleryImages;

    private Map<Integer, List<MediaViewData>> images = new HashMap<Integer, List<MediaViewData>>();

    private MapPositionsData positionsData = new MapPositionsData();

    private Map<String, String> dataTitle = new HashMap<String, String>();


    private String url;

    private String accomType;

    private boolean isVideoPresent;

    private List<String> multiStayImages;

    private List<String> specialOfferPromotions;

    private List<BrandType> brands;

    private BrandType brand;


    /**
     * @return the specialOfferPromotions
     */
    public List<String> getSpecialOfferPromotions()
    {
        return specialOfferPromotions;
    }

    /**
     * @param specialOfferPromotions
     *           the specialOfferPromotions to set
     */
    public void setSpecialOfferPromotions(final List<String> specialOfferPromotions)
    {
        this.specialOfferPromotions = specialOfferPromotions;
    }

    /**
     * @return the isVideoPresent
     */
    public boolean isVideoPresent()
    {
        return isVideoPresent;
    }

    /**
     * @param isVideoPresent
     *           the isVideoPresent to set
     */
    public void setVideoPresent(final boolean isVideoPresent)
    {
        this.isVideoPresent = isVideoPresent;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl()
    {
        return imageUrl;
    }

    /**
     * @return the imageUrlsMap
     */
    public Map<String, List<String>> getImageUrlsMap()
    {
        return imageUrlsMap;
    }

    /**
     * @return the differentiatedProduct
     */
    public boolean isDifferentiatedProduct()
    {
        return differentiatedProduct;
    }

    /**
     * @return the differentiatedType
     */
    public String getDifferentiatedType()
    {
        return differentiatedType;
    }

    /**
     * @return the strapline
     */
    public String getStrapline()
    {
        return strapline;
    }

    /**
     * @return the location
     */
    public SearchResultsLocationData getLocation()
    {
        return location;
    }

    /**
     * @return the ratings
     */
    public RatingsData getRatings()
    {
        return ratings;
    }


    /**
     * @return the rooms
     */
    public List<SearchResultRoomsData> getRooms()
    {
        return rooms;
    }

    /**
     * @return the tripAdvisorReviewCount
     */
    public int getTripAdvisorReviewCount()
    {
        return tripAdvisorReviewCount;
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
     * @param name
     *           the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @param imageUrl
     *           the imageUrl to set
     */
    public void setImageUrl(final String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    /**
     * @param imageUrlsMap
     *           the imageUrlsMap to set
     */
    public void setImageUrlsMap(final Map<String, List<String>> imageUrlsMap)
    {

        this.imageUrlsMap.putAll(imageUrlsMap);
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
     * @param differentiatedType
     *           the differentiatedType to set
     */
    public void setDifferentiatedType(final String differentiatedType)
    {
        this.differentiatedType = differentiatedType;
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
     * @param location
     *           the location to set
     */
    public void setLocation(final SearchResultsLocationData location)
    {
        this.location = location;
    }

    /**
     * @param ratings
     *           the ratings to set
     */
    public void setRatings(final RatingsData ratings)
    {
        this.ratings = ratings;
    }


    /**
     * @param rooms
     *           the rooms to set
     */
    public void setRooms(final List<SearchResultRoomsData> rooms)
    {
        this.rooms = rooms;
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
     * @return the featureCodesAndValues
     */
    public Map<String, List<Object>> getFeatureCodesAndValues()
    {
        return featureCodesAndValues;
    }

    public void putFeatureCodesAndValues(final Map<String, List<Object>> featureCodesAndValues)
    {
        this.featureCodesAndValues.putAll(featureCodesAndValues);
    }

    public String getFeatureValue(final String featureCode)
    {
        if (featureCodesAndValues.containsKey(featureCode) && featureCodesAndValues.get(featureCode) != null
                && !featureCodesAndValues.get(featureCode).isEmpty())
        {
            return featureCodesAndValues.get(featureCode).get(0).toString();
        }

        return StringUtils.EMPTY;
    }

    /**
     * @return the commercialPriority
     */
    public int getCommercialPriority()
    {
        return commercialPriority;
    }

    /**
     * @param commercialPriority
     *           the commercialPriority to set
     */
    public void setCommercialPriority(final int commercialPriority)
    {
        this.commercialPriority = commercialPriority;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url
     *           the url to set
     */
    public void setUrl(final String url)
    {
        this.url = url;
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
     * @return the locationMap
     */
    public Map getLocationMap()
    {
        return locationMap;
    }

    /**
     * @param locationMap
     *           the locationMap to set
     */
    public void setLocationMap(final Map locationMap)
    {
        this.locationMap = locationMap;
    }

    /**
     * @return the accomType
     */
    public String getAccomType()
    {
        return accomType;
    }

    /**
     * @param accomType
     *           the accomType to set
     */
    public void setAccomType(final String accomType)
    {
        this.accomType = accomType;
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
    public void setGalleryImages(final Collection<MediaViewData> galleryImages)
    {
        this.galleryImages = galleryImages;
    }

    /**
     * @return the latitude
     */
    public String getLatitude()
    {
        return latitude;
    }

    /**
     * @param latitude
     *           the latitude to set
     */
    public void setLatitude(final String latitude)
    {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude()
    {
        return longitude;
    }

    /**
     * @param longitude
     *           the longitude to set
     */
    public void setLongitude(final String longitude)
    {
        this.longitude = longitude;
    }

    /**
     * @return the images
     */
    public Map<Integer, List<MediaViewData>> getImages()
    {
        return images;
    }

    /**
     * @param images
     *           the images to set
     */
    public void setImages(final Map<Integer, List<MediaViewData>> images)
    {
        this.images = images;
    }

    /**
     * @return the positionsData
     */
    public MapPositionsData getPositionsData()
    {
        return positionsData;
    }

    /**
     * @param positionsData
     *           the positionsData to set
     */
    public void setPositionsData(final MapPositionsData positionsData)
    {
        this.positionsData = positionsData;
    }

    /**
     * @return the dataTitle
     */
    public Map<String, String> getDataTitle()
    {
        return dataTitle;
    }

    public void setDataTitle(final Map<String, String> dataTitle)
    {
        this.dataTitle = dataTitle;
    }

    /**
     * @return the multiStayImages
     */
    public List<String> getMultiStayImages()
    {
        return multiStayImages;
    }

    /**
     * @param multiStayImages
     *           the multiStayImages to set
     */
    public void setMultiStayImages(final List<String> multiStayImages)
    {
        this.multiStayImages = multiStayImages;
    }

    /**
     * @return the brands
     */
    public List<BrandType> getBrands()
    {
        return brands;
    }

    /**
     * @param brands
     *           the brands to set
     */
    public void setBrands(final List<BrandType> brands)
    {
        this.brands = brands;
    }

    /**
     * @return the brand
     */
    public BrandType getBrand()
    {
        return brand;
    }

    /**
     * @param brand
     *           the brand to set
     */
    public void setBrand(final BrandType brand)
    {
        this.brand = brand;
    }


}
