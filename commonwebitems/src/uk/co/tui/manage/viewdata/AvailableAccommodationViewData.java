/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zul.CategoryModel;

import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;



/**
 * @author premkumar.nd
 *
 */
public class AvailableAccommodationViewData
{



    private BoardBasisViewData boardBasisViewData;

    private List<ErrataViewData> errataViewData;

    private List<MemoViewData> specialReqest;

    private AddressViewData addressViewData;

    private String accomUrl;
    //p2:Desc
    private String accomCode;

    //St_Dt
    private String accommStartDate;

    //End_Dt
    private String accommEndDate;

    private PriceViewData accomPrice;

    //Rm_Cd
    private List<RoomViewData> roomViewData;

    private HotelViewData hotelViewData;

    private List<SummaryRoomViewData> summaryRoomViewData;

    private boolean isWelfare;

    private List<CategoryModel> categories;

    private Map<String, List<Object>> featureCodesAndValues;

    private String priceFrom;

    private String tRating;

    private String stayPeriod;

    private String commercialPriority;

    private String accommodationType;

    private String departurePoint;

    private String departureCode;

    private String displayOrder;

    private String count;

    private String roomOccupancy;

    private String brand;

    private boolean multiSelect;

    private String reviewCount;

    private String ratingBar;

    private boolean accomwifi;

    private Map<String, String> baynoteTrackingMap;

    // Added for Accommodation Standard
    private String hotelType;

    private boolean multiCentre;

    private Map locations;

    private String aniteBrand;

    private String name;

    private MediaViewData thumbnail;

    private Collection<MediaViewData> galleryImages;

    private List<MediaViewData> galleryVideos;

    private MediaViewData picture;

    private String code;

    private String id;

    private String type;

    private boolean nonCoreProduct;

    private String locationMapUrl;

    private String facilitiesUrl;

    private String reviewRating;

    private String inventory;

    private MapPositionsData mapPositionsData;

    private String duration;

    /** The Product ranges. */
    private List<ProductRangeViewData> productRanges;

    /** The differentiatedType. */

    private final Map<String, List<Object>> featureCodesAndValuesMap = new HashMap<String, List<Object>>();
    /** To check wheater the product is differntiate or not **/
    private boolean differentiatedProduct;

    /** Holds the value for accommodation Name. */
    private String accomName = StringUtils.EMPTY;

    /** Holds the value for accommodation image URL. */
    private String accomImageUrl = StringUtils.EMPTY;

    /** Holds the value of Destination name of that accommodation. */
    private String destinationName = StringUtils.EMPTY;

    /** Holds the value of resort name of that accommodation. */
    private String resortName = StringUtils.EMPTY;

    /** Holds the rating of the accommodation. */
    private String rating = StringUtils.EMPTY;

    /** Holds the value of board basis name. */
    private String boardBasisName = StringUtils.EMPTY;

    /** The country name. */
    private String countryName = StringUtils.EMPTY;

    /** The empty country name. */
    private boolean emptyCountryName;

    /**
     * @return the specialReqest
     */
    public List<MemoViewData> getSpecialReqest()
    {
        return specialReqest;
    }

    /**
     * @param specialReqest
     *           the specialReqest to set
     */
    public void setSpecialReqest(final List<MemoViewData> specialReqest)
    {
        this.specialReqest = specialReqest;
    }

    /**
     * @return the accomCode
     */
    public String getAccomCode()
    {
        return accomCode;
    }

    /**
     * @param accomCode
     *           the accomCode to set
     */
    public void setAccomCode(final String accomCode)
    {
        this.accomCode = accomCode;
    }


    /**
     * @return the duration
     */
    public String getDuration()
    {
        return duration;
    }

    /**
     * @param duration
     *           the duration to set
     */
    public void setDuration(final String duration)
    {
        this.duration = duration;
    }

    /**
     * @return the isWelfare
     */
    public boolean isWelfare()
    {
        return isWelfare;
    }

    /**
     * @param isWelfare
     *           the isWelfare to set
     */
    public void setWelfare(final boolean isWelfare)
    {
        this.isWelfare = isWelfare;
    }

    /**
     * @return the countryName
     */
    public String getCountryName()
    {
        return countryName;
    }

    /**
     * @param countryName
     *           the countryName to set
     */
    public void setCountryName(final String countryName)
    {
        this.countryName = countryName;
    }

    /**
     * @return the emptyCountryName
     */
    public boolean isEmptyCountryName()
    {
        return emptyCountryName;
    }

    /**
     * @param emptyCountryName
     *           the emptyCountryName to set
     */
    public void setEmptyCountryName(final boolean emptyCountryName)
    {
        this.emptyCountryName = emptyCountryName;
    }

    /**
     * @return the rating
     */
    public String getRating()
    {
        return rating;
    }

    /**
     * @param rating
     *           the rating to set
     */
    public void setRating(final String rating)
    {
        this.rating = rating;
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
     * @return the boardBasisName
     */
    public String getBoardBasisName()
    {
        return boardBasisName;
    }

    /**
     * @param boardBasisName
     *           the boardBasisName to set
     */
    public void setBoardBasisName(final String boardBasisName)
    {
        this.boardBasisName = boardBasisName;
    }

    /**
     * @return the accomName
     */
    public String getAccomName()
    {
        return accomName;
    }

    /**
     * @param accomName
     *           the accomName to set
     */
    public void setAccomName(final String accomName)
    {
        this.accomName = accomName;
    }

    /**
     * @return the accomImageUrl
     */
    public String getAccomImageUrl()
    {
        return accomImageUrl;
    }

    /**
     * @param accomImageUrl
     *           the accomImageUrl to set
     */
    public void setAccomImageUrl(final String accomImageUrl)
    {
        this.accomImageUrl = accomImageUrl;
    }

    /**
     * @return the mapPositionsData
     */
    public MapPositionsData getMapPositionsData()
    {
        return mapPositionsData;
    }

    /**
     * @param mapPositionsData
     *           the mapPositionsData to set
     */
    public void setMapPositionsData(final MapPositionsData mapPositionsData)
    {
        this.mapPositionsData = mapPositionsData;
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
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *           the id to set
     */
    public void setId(final String id)
    {
        this.id = id;
    }

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
     * @return the nonCoreProduct
     */
    public boolean isNonCoreProduct()
    {
        return nonCoreProduct;
    }

    /**
     * @param nonCoreProduct
     *           the nonCoreProduct to set
     */
    public void setNonCoreProduct(final boolean nonCoreProduct)
    {
        this.nonCoreProduct = nonCoreProduct;
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
    public void setLocationMapUrl(final String locationMapUrl)
    {
        this.locationMapUrl = locationMapUrl;
    }

    /**
     * @return the facilitiesUrl
     */
    public String getFacilitiesUrl()
    {
        return facilitiesUrl;
    }

    /**
     * @param facilitiesUrl
     *           the facilitiesUrl to set
     */
    public void setFacilitiesUrl(final String facilitiesUrl)
    {
        this.facilitiesUrl = facilitiesUrl;
    }

    /**
     * @return the reviewRating
     */
    public String getReviewRating()
    {
        return reviewRating;
    }

    /**
     * @param reviewRating
     *           the reviewRating to set
     */
    public void setReviewRating(final String reviewRating)
    {
        this.reviewRating = reviewRating;
    }

    /**
     * @return the picture
     */
    public MediaViewData getPicture()
    {
        return picture;
    }

    /**
     * @param picture
     *           the picture to set
     */
    public void setPicture(final MediaViewData picture)
    {
        this.picture = picture;
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
     * @return the galleryVideos
     */
    public List<MediaViewData> getGalleryVideos()
    {
        return galleryVideos;
    }

    /**
     * @param galleryVideos
     *           the galleryVideos to set
     */
    public void setGalleryVideos(final List<MediaViewData> galleryVideos)
    {
        this.galleryVideos = galleryVideos;
    }

    /**
     * @return the thumbnail
     */
    public MediaViewData getThumbnail()
    {
        return thumbnail;
    }

    /**
     * @param thumbnail
     *           the thumbnail to set
     */
    public void setThumbnail(final MediaViewData thumbnail)
    {
        this.thumbnail = thumbnail;
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
     * @param featureCodesAndValues
     *           the featureCodesAndValues to set
     */
    public void setFeatureCodesAndValues(final Map<String, List<Object>> featureCodesAndValues)
    {
        this.featureCodesAndValues = featureCodesAndValues;
    }

    /**
     * @return the priceFrom
     */
    public String getPriceFrom()
    {
        return priceFrom;
    }

    /**
     * @param priceFrom
     *           the priceFrom to set
     */
    public void setPriceFrom(final String priceFrom)
    {
        this.priceFrom = priceFrom;
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
    public void settRating(final String tRating)
    {
        this.tRating = tRating;
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
    public void setStayPeriod(final String stayPeriod)
    {
        this.stayPeriod = stayPeriod;
    }

    /**
     * @return the commercialPriority
     */
    public String getCommercialPriority()
    {
        return commercialPriority;
    }

    /**
     * @param commercialPriority
     *           the commercialPriority to set
     */
    public void setCommercialPriority(final String commercialPriority)
    {
        this.commercialPriority = commercialPriority;
    }

    /**
     * @return the accommodationType
     */
    public String getAccommodationType()
    {
        return accommodationType;
    }

    /**
     * @param accommodationType
     *           the accommodationType to set
     */
    public void setAccommodationType(final String accommodationType)
    {
        this.accommodationType = accommodationType;
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
    public void setDeparturePoint(final String departurePoint)
    {
        this.departurePoint = departurePoint;
    }

    /**
     * @return the departureCode
     */
    public String getDepartureCode()
    {
        return departureCode;
    }

    /**
     * @param departureCode
     *           the departureCode to set
     */
    public void setDepartureCode(final String departureCode)
    {
        this.departureCode = departureCode;
    }

    /**
     * @return the displayOrder
     */
    public String getDisplayOrder()
    {
        return displayOrder;
    }

    /**
     * @param displayOrder
     *           the displayOrder to set
     */
    public void setDisplayOrder(final String displayOrder)
    {
        this.displayOrder = displayOrder;
    }

    /**
     * @return the count
     */
    public String getCount()
    {
        return count;
    }

    /**
     * @param count
     *           the count to set
     */
    public void setCount(final String count)
    {
        this.count = count;
    }

    /**
     * @return the roomOccupancy
     */
    public String getRoomOccupancy()
    {
        return roomOccupancy;
    }

    /**
     * @param roomOccupancy
     *           the roomOccupancy to set
     */
    public void setRoomOccupancy(final String roomOccupancy)
    {
        this.roomOccupancy = roomOccupancy;
    }

    /**
     * @return the brand
     */
    public String getBrand()
    {
        return brand;
    }

    /**
     * @param brand
     *           the brand to set
     */
    public void setBrand(final String brand)
    {
        this.brand = brand;
    }

    /**
     * @return the multiSelect
     */
    public boolean isMultiSelect()
    {
        return multiSelect;
    }

    /**
     * @param multiSelect
     *           the multiSelect to set
     */
    public void setMultiSelect(final boolean multiSelect)
    {
        this.multiSelect = multiSelect;
    }

    /**
     * @return the reviewCount
     */
    public String getReviewCount()
    {
        return reviewCount;
    }

    /**
     * @param reviewCount
     *           the reviewCount to set
     */
    public void setReviewCount(final String reviewCount)
    {
        this.reviewCount = reviewCount;
    }

    /**
     * @return the ratingBar
     */
    public String getRatingBar()
    {
        return ratingBar;
    }

    /**
     * @param ratingBar
     *           the ratingBar to set
     */
    public void setRatingBar(final String ratingBar)
    {
        this.ratingBar = ratingBar;
    }

    /**
     * @return the accomwifi
     */
    public boolean isAccomwifi()
    {
        return accomwifi;
    }

    /**
     * @param accomwifi
     *           the accomwifi to set
     */
    public void setAccomwifi(final boolean accomwifi)
    {
        this.accomwifi = accomwifi;
    }

    /**
     * @return the destinationName
     */
    public String getDestinationName()
    {
        return destinationName;
    }

    /**
     * @param destinationName
     *           the destinationName to set
     */
    public void setDestinationName(final String destinationName)
    {
        this.destinationName = destinationName;
    }

    /**
     * @return the resortName
     */
    public String getResortName()
    {
        return resortName;
    }

    /**
     * @param resortName
     *           the resortName to set
     */
    public void setResortName(final String resortName)
    {
        this.resortName = resortName;
    }

    /**
     * @return the baynoteTrackingMap
     */
    public Map<String, String> getBaynoteTrackingMap()
    {
        return baynoteTrackingMap;
    }

    /**
     * @param baynoteTrackingMap
     *           the baynoteTrackingMap to set
     */
    public void setBaynoteTrackingMap(final Map<String, String> baynoteTrackingMap)
    {
        this.baynoteTrackingMap = baynoteTrackingMap;
    }

    /**
     * @return the hotelType
     */
    public String getHotelType()
    {
        return hotelType;
    }

    /**
     * @param hotelType
     *           the hotelType to set
     */
    public void setHotelType(final String hotelType)
    {
        this.hotelType = hotelType;
    }

    /**
     * @return the multiCentre
     */
    public boolean isMultiCentre()
    {
        return multiCentre;
    }

    /**
     * @param multiCentre
     *           the multiCentre to set
     */
    public void setMultiCentre(final boolean multiCentre)
    {
        this.multiCentre = multiCentre;
    }

    /**
     * @return the locations
     */
    public Map getLocations()
    {
        return locations;
    }

    /**
     * @param locations
     *           the locations to set
     */
    public void setLocations(final Map locations)
    {
        this.locations = locations;
    }

    /**
     * @return the aniteBrand
     */
    public String getAniteBrand()
    {
        return aniteBrand;
    }

    /**
     * @param aniteBrand
     *           the aniteBrand to set
     */
    public void setAniteBrand(final String aniteBrand)
    {
        this.aniteBrand = aniteBrand;
    }

    /**
     * @return the featureCodesAndValues
     */
    public Map<String, List<Object>> getFeatureCodesAndValues()
    {
        return featureCodesAndValues;
    }



    /**
     * @return the accommStartDate
     */
    public String getAccommStartDate()
    {
        return accommStartDate;
    }

    /**
     * @param accommStartDate
     *           the accommStartDate to set
     */
    public void setAccommStartDate(final String accommStartDate)
    {
        this.accommStartDate = accommStartDate;
    }

    /**
     * @return the accommEndDate
     */
    public String getAccommEndDate()
    {
        return accommEndDate;
    }

    /**
     * @param accommEndDate
     *           the accommEndDate to set
     */
    public void setAccommEndDate(final String accommEndDate)
    {
        this.accommEndDate = accommEndDate;
    }


    /**
     * @return the hotelViewData
     */
    public HotelViewData getHotelViewData()
    {
        return hotelViewData;
    }

    /**
     * @param hotelViewData
     *           the hotelViewData to set
     */
    public void setHotelViewData(final HotelViewData hotelViewData)
    {
        this.hotelViewData = hotelViewData;
    }



    /**
     * @return the categories
     */
    public List<CategoryModel> getCategories()
    {
        return categories;
    }

    /**
     * @param categories
     *           the categories to set
     */
    public void setCategories(final List<CategoryModel> categories)
    {
        this.categories = categories;
    }

    /**
     * @return the featureCodesAndValuesMap
     */
    public Map<String, List<Object>> getFeatureCodesAndValuesMap()
    {
        return featureCodesAndValuesMap;
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
    public void setProductRanges(final List<ProductRangeViewData> productRanges)
    {
        this.productRanges = productRanges;
    }

    /**
     * @return the boardBasisViewData
     */

    public BoardBasisViewData getBoardBasisViewData()
    {
        return boardBasisViewData;
    }

    /*
     * @param boardBasisViewData the boardBasisViewData to set
     */

    public void setBoardBasisViewData(final BoardBasisViewData boardBasisViewData)
    {
        this.boardBasisViewData = boardBasisViewData;
    }


    /**
     * @return the accomUrl
     */
    public String getAccomUrl()
    {
        return accomUrl;
    }

    /**
     * @param accomUrl
     *           the accomUrl to set
     */
    public void setAccomUrl(final String accomUrl)
    {
        this.accomUrl = accomUrl;
    }


    /**
     * @return the errataViewData
     */
    public List<ErrataViewData> getErrataViewData()
    {
        return errataViewData;
    }

    /**
     * @param errataViewData
     *           the errataViewData to set
     */
    public void setErrataViewData(final List<ErrataViewData> errataViewData)
    {
        this.errataViewData = errataViewData;
    }

    /**
     * @return the accomPrice
     */
    public PriceViewData getAccomPrice()
    {
        return accomPrice;
    }

    /**
     * @param accomPrice
     *           the accomPrice to set
     */
    public void setAccomPrice(final PriceViewData accomPrice)
    {
        this.accomPrice = accomPrice;
    }

    /**
     * @return the summaryRoomViewData
     */
    public List<SummaryRoomViewData> getSummaryRoomViewData()
    {
        if (summaryRoomViewData == null)
        {
            summaryRoomViewData = new ArrayList<SummaryRoomViewData>();
        }
        return summaryRoomViewData;
    }

    /**
     * @param summaryRoomViewData
     *           the summaryRoomViewData to set
     */
    public void setSummaryRoomViewData(final List<SummaryRoomViewData> summaryRoomViewData)
    {
        this.summaryRoomViewData = summaryRoomViewData;
    }

    /**
     * @return the roomViewData
     */
    public List<RoomViewData> getRoomViewData()
    {
        return roomViewData;
    }

    /**
     * @param roomViewData
     *           the roomViewData to set
     */
    public void setRoomViewData(final List<RoomViewData> roomViewData)
    {
        this.roomViewData = roomViewData;
    }

    /**
     * @return the inventory
     */
    public String getInventory()
    {
        return inventory;
    }

    /**
     * @param inventory
     *           the inventory to set
     */
    public void setInventory(final String inventory)
    {
        this.inventory = inventory;
    }

    /**
     * @param addressViewData
     *           the addressViewData to set
     */
    public void setAddressViewData(final AddressViewData addressViewData)
    {
        this.addressViewData = addressViewData;
    }

    /**
     * @return the addressViewData
     */
    public AddressViewData getAddressViewData()
    {
        return addressViewData;
    }

}
