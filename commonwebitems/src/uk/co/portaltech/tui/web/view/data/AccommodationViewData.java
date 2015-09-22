/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;

/**
 * @author omonikhide
 *
 */
public class AccommodationViewData extends ProductData implements HasFeatures, Serializable
{
   private List<BenefitViewData> accommodationBenefits;

   private List<FacilityViewData> facilities;

   private final Map<String, List<Object>> featureCodesAndValues;

   private List<ProductRangeViewData> productRanges;

   private List<ArticleViewData> articles;

   private TripadvisorData tripadvisorData;

   private LocationData resort;

   private LocationData destination;

   private List<MediaViewData> heroCarouselMedia;

   private MediaViewData thumbnail;

   private BookingData bookingData;

   private String priceFrom;

   private Date availableFrom;

   private String tRating;

   private String stayPeriod;

   private String commercialPriority;

   private String accommodationType;

   private String departurePoint;

   private String departureCode;

   private String displayOrder;

   private String count;

   private String roomOccupancy;

   private Collection<MediaViewData> galleryImages;

   private List<MediaViewData> galleryVideos;

   private String featuredImgUrl;

   private String deepLinkUrl;

   private String recommendationImgUrl;

   private String locationMapUrl;

   private String facilitiesUrl;

   private String reviewRating;

   private int galleryImageVisibleItems;

   private List<AccommodationRoomViewData> roomsData;

   private List<BoardBasisDataResponse> boardBasis;

   private String brand;

   private boolean multiSelect;

   // New attribute added to hold facility data with priority
   private List<FacilityViewData> priorityBasedFacilityData;

   private List<FacilityViewData> villaLayoutFacility;

   private String reviewCount;

   private String ratingBar;

   private boolean accomwifi;

   private String destinationName;

   private String resortName;

   private String countryName;

   private Map<String, String> baynoteTrackingMap;

   private BayNoteTrackingData baynoteTrackingData;

   // Added for Accommodation Standard
   private String hotelType;

   private boolean multiCentre;

   private List<MultiCentreData> multiCentreDatas;

   private Map locations;

   private String aniteBrand;

   private String baynoteHotelId;

   private String recBoardBasis;

   private int comPriority;

   private String departureDate;

   private int duration;

   private String recBookUrl;

   private String baynotePid;

   private String baynoteSlot;

   private String baynoteWidget;

   private List<SpecialOfferViewData> specialOfferViewDataList;

   private List<BrandType> brands;

   public AccommodationViewData()
   {
      this.featureCodesAndValues = new HashMap<String, List<Object>>();
      this.galleryImages = new ArrayList<MediaViewData>();
   }

   /**
    * @return the recBookUrl
    */
   public String getRecBookUrl()
   {
      return recBookUrl;
   }

   /**
    * @return the specialOfferViewDataList
    */
   public List<SpecialOfferViewData> getSpecialOfferViewDataList()
   {
      return specialOfferViewDataList;
   }

   /**
    * @param specialOfferViewDataList the specialOfferViewDataList to set
    */
   public void setSpecialOfferViewDataList(final List<SpecialOfferViewData> specialOfferViewDataList)
   {
      this.specialOfferViewDataList = specialOfferViewDataList;
   }

   /**
    * @param recBookUrl the recBookUrl to set
    */
   public void setRecBookUrl(final String recBookUrl)
   {
      this.recBookUrl = recBookUrl;
   }

   /**
    * @return the duration
    */
   public int getDuration()
   {
      return duration;
   }

   /**
    * @param duration the duration to set
    */
   public void setDuration(final int duration)
   {
      this.duration = duration;
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
   public void setDepartureDate(final String departureDate)
   {
      this.departureDate = departureDate;
   }

   /**
    * @return the comPriority
    */
   public int getComPriority()
   {
      return comPriority;
   }

   /**
    * @param comPriority the comPriority to set
    */
   public void setComPriority(final int comPriority)
   {
      this.comPriority = comPriority;
   }

   /**
    * @return the recBoardBasis
    */
   public String getRecBoardBasis()
   {
      return recBoardBasis;
   }

   /**
    * @param recBoardBasis the recBoardBasis to set
    */
   public void setRecBoardBasis(final String recBoardBasis)
   {
      this.recBoardBasis = recBoardBasis;
   }

   /**
    * @return the bayNoteHotelId
    */
   public String getBaynoteHotelId()
   {
      return baynoteHotelId;
   }

   /**
    * @param bayNoteHotelId the bayNoteHotelId to set
    */
   public void setBaynoteHotelId(final String baynoteHotelId)
   {
      this.baynoteHotelId = baynoteHotelId;
   }

   /**
    * @return the aniteBrand
    */
   public String getAniteBrand()
   {
      return aniteBrand;
   }

   /**
    * @param aniteBrand the aniteBrand to set
    */
   public void setAniteBrand(final String aniteBrand)
   {
      this.aniteBrand = aniteBrand;
   }

   /**
    * @return the accomwifi
    */
   public boolean isAccomwifi()
   {
      return accomwifi;
   }

   /**
    * @param accomwifi the accomwifi to set
    */
   public void setAccomwifi(final boolean accomwifi)
   {
      this.accomwifi = accomwifi;
   }

   /**
    * @return the ratingBar
    */
   public String getRatingBar()
   {
      return ratingBar;
   }

   /**
    * @param ratingBar the ratingBar to set
    */
   public void setRatingBar(final String ratingBar)
   {
      this.ratingBar = ratingBar;
   }

   /**
    * @return the reviewCount
    */
   public String getReviewCount()
   {
      return reviewCount;
   }

   /**
    * @param reviewCount the reviewCount to set
    */
   public void setReviewCount(final String reviewCount)
   {
      this.reviewCount = reviewCount;
   }

   /**
    * @return the priorityBasedFacilityData
    */
   public List<FacilityViewData> getPriorityBasedFacilityData()
   {
      return priorityBasedFacilityData;
   }

   /**
    * @param priorityBasedFacilityData the priorityBasedFacilityData to set
    */
   public void setPriorityBasedFacilityData(final List<FacilityViewData> priorityBasedFacilityData)
   {
      this.priorityBasedFacilityData = priorityBasedFacilityData;
   }

   public List<AccommodationRoomViewData> getRoomsData()
   {
      return roomsData;
   }

   public void setRoomsData(final List<AccommodationRoomViewData> roomsData)
   {
      this.roomsData = roomsData;
   }

   public List<MediaViewData> getGalleryVideos()
   {
      return galleryVideos;
   }

   public void setGalleryVideos(final List<MediaViewData> galleryVideos)
   {
      this.galleryVideos = galleryVideos;
   }

   /**
    * @return the facilitiesUrl
    */
   public String getFacilitiesUrl()
   {
      return facilitiesUrl;
   }

   /**
    * @param facilitiesUrl the facilitiesUrl to set
    */
   public void setFacilitiesUrl(final String facilitiesUrl)
   {
      this.facilitiesUrl = facilitiesUrl;
   }

   /**
    * @return the locationMapUrl
    */
   public String getLocationMapUrl()
   {
      return locationMapUrl;
   }

   /**
    * @param locationMapUrl the locationMapUrl to set
    */
   public void setLocationMapUrl(final String locationMapUrl)
   {
      this.locationMapUrl = locationMapUrl;
   }

   /**
    * @return the displayOrder
    */
   public String getDisplayOrder()
   {
      return displayOrder;
   }

   /**
    * @param displayOrder the displayOrder to set
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
    * @param count the count to set
    */
   public void setCount(final String count)
   {
      this.count = count;
   }

   /**
    * @return the stayPeriod
    */
   public String getStayPeriod()
   {
      return stayPeriod;
   }

   /**
    * @param stayPeriod the stayPeriod to set
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
    * @param commercialPriority the commercialPriority to set
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
    * @param accommodationType the accommodationType to set
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
    * @param departurePoint the departurePoint to set
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
    * @param departureCode the departureCode to set
    */
   public void setDepartureCode(final String departureCode)
   {
      this.departureCode = departureCode;
   }

   /**
    * @return the tRating
    */
   public String gettRating()
   {
      return tRating;
   }

   /**
    * @param tRating the tRating to set
    */
   public void settRating(final String tRating)
   {
      this.tRating = tRating;
   }

   /**
    * @return the availableFrom
    */
   public Date getAvailableFrom()
   {
      if (this.availableFrom != null)
      {
         return new Date(availableFrom.getTime());
      }
      return null;
   }

   /**
    * @param availableFrom the availableFrom to set
    */
   public void setAvailableFrom(final Date availableFrom)
   {
      if (availableFrom != null)
      {
         this.availableFrom = new Date(availableFrom.getTime());
      }
   }

   /**
    * @return the priceFrom
    */
   public String getPriceFrom()
   {
      return priceFrom;
   }

   /**
    * @param priceFrom the priceFrom to set
    */
   public void setPriceFrom(final String priceFrom)
   {
      this.priceFrom = priceFrom;
   }

   /**
    * @return the resort
    */
   public LocationData getResort()
   {
      return resort;
   }

   /**
    * @param resort the resort to set
    */
   public void setResort(final LocationData resort)
   {
      this.resort = resort;
   }

   /**
    * @return the destination
    */
   public LocationData getDestination()
   {
      return destination;
   }

   /**
    * @param destination the destination to set
    */
   public void setDestination(final LocationData destination)
   {
      this.destination = destination;
   }

   /**
    * @return the productRanges
    */
   public List<ProductRangeViewData> getProductRanges()
   {
      return productRanges;
   }

   /**
    * @param productRanges the productRanges to set
    */
   public void setProductRanges(final List<ProductRangeViewData> productRanges)
   {
      this.productRanges = productRanges;
   }

   public List<BenefitViewData> getAccommodationBenefits()
   {
      return accommodationBenefits;
   }

   public void setAccommodationBenefits(final List<BenefitViewData> accommodationBenefits)
   {
      this.accommodationBenefits = accommodationBenefits;
   }

   public List<FacilityViewData> getFacilities()
   {
      return facilities;
   }

   public void setFacilities(final List<FacilityViewData> facilities)
   {
      this.facilities = facilities;
   }

   @Override
   public Map<String, List<Object>> getFeatureCodesAndValues()
   {
      return featureCodesAndValues;
   }

   @Override
   public void putFeatureCodesAndValues(final Map<String, List<Object>> featureCodesAndValues)
   {
      this.featureCodesAndValues.putAll(featureCodesAndValues);
   }

   @Override
   public void putFeatureValue(final String featureCode, final List<Object> featureValues)
   {
      featureCodesAndValues.put(featureCode, featureValues);
   }

   public void putFeatureValueNonObjectType(final String featureCode,
      final List<? extends Object> featureValues)
   {
      featureCodesAndValues.put(featureCode, (List<Object>) featureValues);
   }

   @Override
   public List<Object> getFeatureValues(final String featureCode)
   {
      return featureCodesAndValues.get(featureCode);
   }

   public String getFeatureValue(final String featureCode)
   {
      final List<Object> featurelist = featureCodesAndValues.get(featureCode);
      if (featureCodesAndValues.containsKey(featureCode) && CollectionUtils.isNotEmpty(featurelist))
      {
         return featurelist.get(0).toString();
      }
      return StringUtils.EMPTY;
   }

   /**
    * @return the articles
    */
   public List<ArticleViewData> getArticles()
   {
      return articles;
   }

   /**
    * @param articles the articles to set
    */
   public void setArticles(final List<ArticleViewData> articles)
   {
      this.articles = articles;
   }

   /**
    * @return the tripadvisorData
    */
   public TripadvisorData getTripadvisorData()
   {
      return tripadvisorData;
   }

   /**
    * @param tripadvisorData the tripadvisorData to set
    */
   public void setTripadvisorData(final TripadvisorData tripadvisorData)
   {
      this.tripadvisorData = tripadvisorData;
   }

   public List<MediaViewData> getHeroCarouselMedia()
   {
      return heroCarouselMedia;
   }

   public void setHeroCarouselMedia(final List<MediaViewData> heroCarouselMedia)
   {
      this.heroCarouselMedia = heroCarouselMedia;
   }

   public MediaViewData getThumbnail()
   {
      return thumbnail;
   }

   public void setThumbnail(final MediaViewData thumbnail)
   {
      this.thumbnail = thumbnail;
   }

   /**
    * @return the bookingData
    */
   public BookingData getBookingData()
   {
      return bookingData;
   }

   /**
    * @param bookingData the bookingData to set
    */
   public void setBookingData(final BookingData bookingData)
   {
      this.bookingData = bookingData;
   }

   /**
    * @return the roomOccupancy
    */
   public String getRoomOccupancy()
   {
      return roomOccupancy;
   }

   /**
    * @param roomOccupancy the roomOccupancy to set
    */
   public void setRoomOccupancy(final String roomOccupancy)
   {
      this.roomOccupancy = roomOccupancy;
   }

   /**
    * @return the galleryImages
    */
   public Collection<MediaViewData> getGalleryImages()
   {
      return galleryImages;
   }

   /**
    * @param galleryImages the galleryImages to set
    */
   public void setGalleryImages(final Collection<MediaViewData> galleryImages)
   {
      this.galleryImages = galleryImages;
   }

   /**
    * @return the reviewRating
    */
   public String getReviewRating()
   {
      return reviewRating;
   }

   /**
    * @param reviewRating the reviewRating to set
    */
   public void setReviewRating(final String reviewRating)
   {
      this.reviewRating = reviewRating;
   }

   /**
    * @return the galleryImageVisibleItems
    */
   public int getGalleryImageVisibleItems()
   {
      return galleryImageVisibleItems;
   }

   /**
    * @param galleryImageVisibleItems the galleryImageVisibleItems to set
    */
   public void setGalleryImageVisibleItems(final int galleryImageVisibleItems)
   {
      this.galleryImageVisibleItems = galleryImageVisibleItems;
   }

   /**
    * @return the boardBasis
    */
   public List<BoardBasisDataResponse> getBoardBasis()
   {
      return boardBasis;
   }

   /**
    * @param boardBasis the boardBasis to set
    */
   public void setBoardBasis(final List<BoardBasisDataResponse> boardBasis)
   {
      this.boardBasis = boardBasis;
   }

   /**
    *
    * @return the default boardBasis.
    */
   public String getDefaultBoardBasis()
   {

      if (CollectionUtils.isNotEmpty(getBoardBasis()))
      {
         for (final BoardBasisDataResponse response : getBoardBasis())
         {
            if (response.isDefaultBoardBasis())
            {
               return response.getName();
            }
         }
      }
      return StringUtils.EMPTY;
   }

   /**
    * @return the villaLayoutFacility
    */
   public List<FacilityViewData> getVillaLayoutFacility()
   {
      return villaLayoutFacility;
   }

   /**
    * @param villaLayoutFacility the villaLayoutFacility to set
    */
   public void setVillaLayoutFacility(final List<FacilityViewData> villaLayoutFacility)
   {
      this.villaLayoutFacility = villaLayoutFacility;
   }

   /**
    * @return the brand
    */
   public String getBrand()
   {
      return brand;
   }

   /**
    * @param brand the brand to set
    */
   public void setBrand(final String brand)
   {
      this.brand = brand;
   }

   /**
    * @return the hotelType
    */
   public String getHotelType()
   {
      return hotelType;
   }

   /**
    * @param hotelType the hotelType to set
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
    * @param multiCentre the multiCentre to set
    */
   public void setMultiCentre(final boolean multiCentre)
   {
      this.multiCentre = multiCentre;
   }

   /**
    * @param multiCentreDatas the multiCentreDatas to set
    */
   public void setMultiCentreDatas(final List<MultiCentreData> multiCentreDatas)
   {
      this.multiCentreDatas = multiCentreDatas;
   }

   /**
    * @return the multiCentreDatas
    */
   public List<MultiCentreData> getMultiCentreDatas()
   {
      return multiCentreDatas;
   }

   /**
    * @return the multiSelect
    */
   public boolean isMultiSelect()
   {
      return multiSelect;
   }

   /**
    * @param multiSelect the multiSelect to set
    */
   public void setMultiSelect(final boolean multiSelect)
   {
      this.multiSelect = multiSelect;
   }

   /**
    * @return the locations
    */
   public Map getLocations()
   {
      return locations;
   }

   /**
    * @param locations the locations to set
    */
   public void setLocations(final Map locations)
   {
      this.locations = locations;
   }

   /**
    * DestinationName.
    *
    * @return destinationName
    */
   public String getDestinationName()
   {
      return destinationName;
   }

   public void setDestinationName(final String destinationName)
   {
      this.destinationName = destinationName;
   }

   public String getResortName()
   {
      return resortName;
   }

   public void setResortName(final String resortName)
   {
      this.resortName = resortName;
   }

   public Map<String, String> getBaynoteTrackingMap()
   {
      return baynoteTrackingMap;
   }

   public void setBaynoteTrackingMap(final Map<String, String> baynoteTrackingMap)
   {
      this.baynoteTrackingMap = baynoteTrackingMap;
   }

   /**
    * @return the brands
    */
   public List<BrandType> getBrands()
   {
      return brands;
   }

   /**
    * @param brands the brands to set
    */
   public void setBrands(final List<BrandType> brands)
   {
      this.brands = brands;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {

      if (obj == null)
      {
         return false;
      }
      if (!(obj instanceof AccommodationViewData))
      {
         return false;
      }

      return new EqualsBuilder().append(this.getCode(), ((AccommodationViewData) obj).getCode())
         .isEquals();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {

      return new HashCodeBuilder().append(this.getCode()).toHashCode();
   }

   /**
    * @return the baynoteTrackingData
    */
   public BayNoteTrackingData getBaynoteTrackingData()
   {
      return baynoteTrackingData;
   }

   /**
    * @param baynoteTrackingData the baynoteTrackingData to set
    */
   public void setBaynoteTrackingData(final BayNoteTrackingData baynoteTrackingData)
   {
      this.baynoteTrackingData = baynoteTrackingData;
   }

   /**
    * @return the countryName
    */
   public String getCountryName()
   {
      return countryName;
   }

   /**
    * @param countryName the countryName to set
    */
   public void setCountryName(final String countryName)
   {
      this.countryName = countryName;
   }

   /**
    * @return the recommendationImgUrl
    */
   public String getRecommendationImgUrl()
   {
      return recommendationImgUrl;
   }

   /**
    * @param recommendationImgUrl the recommendationImgUrl to set
    */
   public void setRecommendationImgUrl(final String recommendationImgUrl)
   {
      this.recommendationImgUrl = recommendationImgUrl;
   }

   /**
    * @return the baynotePid
    */
   public String getBaynotePid()
   {
      return baynotePid;
   }

   /**
    * @param baynotePid the baynotePid to set
    */
   public void setBaynotePid(final String baynotePid)
   {
      this.baynotePid = baynotePid;
   }

   /**
    * @return the baynoteSlot
    */
   public String getBaynoteSlot()
   {
      return baynoteSlot;
   }

   /**
    * @param baynoteSlot the baynoteSlot to set
    */
   public void setBaynoteSlot(final String baynoteSlot)
   {
      this.baynoteSlot = baynoteSlot;
   }

   /**
    * @return the baynoteWidget
    */
   public String getBaynoteWidget()
   {
      return baynoteWidget;
   }

   /**
    * @param baynoteWidget the baynoteWidget to set
    */
   public void setBaynoteWidget(final String baynoteWidget)
   {
      this.baynoteWidget = baynoteWidget;
   }

   /**
    * @return the featuredImgUrl
    */
   public String getFeaturedImgUrl()
   {
      return featuredImgUrl;
   }

   /**
    * @param featuredImgUrl the featuredImgUrl to set
    */
   public void setFeaturedImgUrl(final String featuredImgUrl)
   {
      this.featuredImgUrl = featuredImgUrl;
   }

   /**
    * @return the deepLinkUrl
    */
   public String getDeepLinkUrl()
   {
      return deepLinkUrl;
   }

   /**
    * @param deepLinkUrl the deepLinkUrl to set
    */
   public void setDeepLinkUrl(final String deepLinkUrl)
   {
      this.deepLinkUrl = deepLinkUrl;
   }

}
