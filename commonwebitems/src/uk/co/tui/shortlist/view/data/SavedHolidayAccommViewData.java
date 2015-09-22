/**
 *
 */
package uk.co.tui.shortlist.view.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * This class holds Accommodation related view data.
 *
 * @author Sravani
 */
public class SavedHolidayAccommViewData
{
   /** Holds the value for accommodation Code. */
   private String accomCode = StringUtils.EMPTY;

   /**
    * Holds the Room Relevant View Data.
    */
   private List<SavedHolidayRoomViewData> roomViewData;

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

   /** The no of rooms. */
   private int noOfRooms;

   /** The differentiatedType. */
   private Map<String, List<Object>> featureCodesAndValues = new HashMap<String, List<Object>>();

   /** The Product ranges. */
   private List<ProductRangeViewData> productRanges;

   /** The summay room view data. */
   private List<SavedHolidaySummaryRoomViewData> summaryRoomViewData;

   /** To check wheater the product is differntiate or not **/
   private boolean differentiatedProduct;

   /**
    * Holds Hotel Type
    */
   private String differentiatedType;

   /**
    * Holds Hotel Type Code
    */
   private String differentiatedCode;

   private Map<String, String> villaFacilities;

   /**
    * @return the villaFacilities
    */
   private Map<String, List<Object>> sizeInformation;

   private Map<String, List<Object>> poolsBarsAndRestaurants;

   /** Holds the value for book accom url. */
   private String accomUrl;

   private String[] facilityCodes;

   private Map<String, List<String>> roomsMap;

   private Map<String, Boolean> holidayFacilities;

   private Double averageTemperature;

   private Map<String, List<Object>> locationInformation;

   private Double averageRainfall;

   private Map<String, List<Object>> hotelRoomsAndFloors;

   /**
    * @return the poolsBarsAndRestaurants
    */
   public Map<String, List<Object>> getPoolsBarsAndRestaurants()
   {
      return poolsBarsAndRestaurants;
   }

   /**
    * @param poolsBarsAndRestaurants the poolsBarsAndRestaurants to set
    */
   public void setPoolsBarsAndRestaurants(final Map<String, List<Object>> poolsBarsAndRestaurants)
   {
      this.poolsBarsAndRestaurants = poolsBarsAndRestaurants;
   }

   /**
    * @return the sizeInformation
    */
   public Map<String, List<Object>> getSizeInformation()
   {
      return sizeInformation;
   }

   /**
    * @param sizeInformation the sizeInformation to set
    */
   public void setSizeInformation(final Map<String, List<Object>> sizeInformation)
   {
      this.sizeInformation = sizeInformation;
   }

   /**
    * Gets the room view data.
    *
    * @return the roomViewData
    */
   public List<SavedHolidayRoomViewData> getRoomViewData()
   {
      if (CollectionUtils.isEmpty(this.roomViewData))
      {
         this.roomViewData = new ArrayList<SavedHolidayRoomViewData>();
      }
      return this.roomViewData;
   }

   /**
    * @return the accomCode
    */
   public String getAccomCode()
   {
      return accomCode;
   }

   /**
    * @param accomCode the accomCode to set
    */
   public void setAccomCode(final String accomCode)
   {
      this.accomCode = accomCode;
   }

   /**
    * Gets the accom name.
    *
    * @return the accomName
    */
   public String getAccomName()
   {
      return this.accomName;
   }

   /**
    * Sets the accom name.
    *
    * @param accomName the accomName to set
    */
   public void setAccomName(final String accomName)
   {
      this.accomName = accomName;
   }

   /**
    * Gets the accom image url.
    *
    * @return the accomImageUrl
    */
   public String getAccomImageUrl()
   {
      return this.accomImageUrl;
   }

   /**
    * Sets the accom image url.
    *
    * @param accomImageUrl the accomImageUrl to set
    */
   public void setAccomImageUrl(final String accomImageUrl)
   {
      this.accomImageUrl = accomImageUrl;
   }

   /**
    * Gets the destination name.
    *
    * @return the destinationName
    */
   public String getDestinationName()
   {
      return this.destinationName;
   }

   /**
    * Sets the destination name.
    *
    * @param destinationName the destinationName to set
    */
   public void setDestinationName(final String destinationName)
   {
      this.destinationName = destinationName;
   }

   /**
    * Gets the resort name.
    *
    * @return the resortName
    */
   public String getResortName()
   {
      return this.resortName;
   }

   /**
    * Sets the resort name.
    *
    * @param resortName the resortName to set
    */
   public void setResortName(final String resortName)
   {
      this.resortName = resortName;
   }

   /**
    * Gets the rating.
    *
    * @return the rating
    */
   public String getRating()
   {
      return rating;
   }

   /**
    * Sets the rating.
    *
    * @param rating the rating to set
    */
   public void setRating(final String rating)
   {
      this.rating = rating;
   }

   /**
    * Gets the board basis name.
    *
    * @return the boardBasisName
    */
   public String getBoardBasisName()
   {
      return boardBasisName;
   }

   /**
    * Sets the board basis name.
    *
    * @param boardBasisName the boardBasisName to set
    */
   public void setBoardBasisName(final String boardBasisName)
   {
      this.boardBasisName = boardBasisName;
   }

   /**
    * Return the string representation of the object.
    *
    * @return the string representation of the object.
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return ToStringBuilder.reflectionToString(this);
   }

   /**
    * @return the noOfRooms
    */
   public int getNoOfRooms()
   {
      return noOfRooms;
   }

   /**
    * @param noOfRooms the noOfRooms to set
    */
   public void setNoOfRooms(final int noOfRooms)
   {
      this.noOfRooms = noOfRooms;
   }

   /**
    * @param roomViewData the roomViewData to set
    */
   public void setRoomViewData(final List<SavedHolidayRoomViewData> roomViewData)
   {
      this.roomViewData = roomViewData;
   }

   /**
    * @return the productRanges
    */
   public List<ProductRangeViewData> getProductRanges()
   {
      if (CollectionUtils.isEmpty(this.productRanges))
      {
         this.productRanges = new ArrayList<ProductRangeViewData>();
      }
      return productRanges;
   }

   /**
    * @param productRanges the productRanges to set
    */
   public void setProductRanges(final List<ProductRangeViewData> productRanges)
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

   public void putFeatureCodesAndValues(final Map<String, List<Object>> featureCodesAndValues)
   {
      this.featureCodesAndValues.putAll(featureCodesAndValues);
   }

   /**
    * @return the summaryRoomViewData
    */
   public List<SavedHolidaySummaryRoomViewData> getSummaryRoomViewData()
   {
      if (summaryRoomViewData == null)
      {
         this.summaryRoomViewData = new ArrayList<SavedHolidaySummaryRoomViewData>();
      }
      return summaryRoomViewData;
   }

   /**
    * @param summaryRoomViewData the summaryRoomViewData to set
    */
   public void setSummaryRoomViewData(
      final List<SavedHolidaySummaryRoomViewData> summaryRoomViewData)
   {
      this.summaryRoomViewData = summaryRoomViewData;
   }

   /**
    * @param featureCodesAndValues the featureCodesAndValues to set
    */
   public void setFeatureCodesAndValues(final Map<String, List<Object>> featureCodesAndValues)
   {
      this.featureCodesAndValues = featureCodesAndValues;
   }

   /**
    * @return the differentiatedProduct
    */
   public boolean isDifferentiatedProduct()
   {
      return differentiatedProduct;
   }

   /**
    * @param differentiatedProduct the differentiatedProduct to set
    */
   public void setDifferentiatedProduct(final boolean differentiatedProduct)
   {
      this.differentiatedProduct = differentiatedProduct;
   }

   /**
    * @return the differentiatedType
    */
   public String getDifferentiatedType()
   {
      return differentiatedType;
   }

   /**
    * @param differentiatedType the differentiatedType to set
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
    * @param differentiatedCode the differentiatedCode to set
    */
   public void setDifferentiatedCode(final String differentiatedCode)
   {
      this.differentiatedCode = differentiatedCode;
   }

   /**
    * @return the averageRainfall
    */
   public Double getAverageRainfall()
   {
      return averageRainfall;
   }

   /**
    * @param averageRainfall the averageRainfall to set
    */
   public void setAverageRainfall(final Double averageRainfall)
   {
      this.averageRainfall = averageRainfall;
   }

   /**
    * @return the averageTemperature
    */
   public Double getAverageTemperature()
   {
      return averageTemperature;
   }

   /**
    * @param averageTemperature the averageTemperature to set
    */
   public void setAverageTemperature(final Double averageTemperature)
   {
      this.averageTemperature = averageTemperature;
   }

   /**
    * @return the locationInformation
    */
   public Map<String, List<Object>> getLocationInformation()
   {
      return locationInformation;
   }

   /**
    * @param locationInformation the locationInformation to set
    */
   public void setLocationInformation(final Map<String, List<Object>> locationInformation)
   {
      this.locationInformation = locationInformation;
   }

   /**
    * @return the hotelRoomsAndFloors
    */
   public Map<String, List<Object>> getHotelRoomsAndFloors()
   {
      return hotelRoomsAndFloors;
   }

   /**
    * @param hotelRoomsAndFloors the hotelRoomsAndFloors to set
    */
   public void setHotelRoomsAndFloors(final Map<String, List<Object>> hotelRoomsAndFloors)
   {
      this.hotelRoomsAndFloors = hotelRoomsAndFloors;
   }

   /**
    * @return the facilityCodes
    */
   public String[] getFacilityCodes()
   {
      if (facilityCodes == null)

      {
         return new String[0];
      }
      return Arrays.copyOf(facilityCodes, this.facilityCodes.length);
   }

   /**
    * @param facilityCodes the facilityCodes to set
    */
   public void setFacilityCodes(final String[] facilityCodes)
   {
      final String[] facilityCode = facilityCodes.clone();
      this.facilityCodes = facilityCode;
   }

   /**
    * @return the holidayFacilities
    */
   public Map<String, Boolean> getHolidayFacilities()
   {
      return holidayFacilities;
   }

   /**
    * @param holidayFacilities the holidayFacilities to set
    */
   public void setHolidayFacilities(final Map<String, Boolean> holidayFacilities)
   {
      this.holidayFacilities = holidayFacilities;
   }

   /**
    * @return the villaFacilities
    */
   public Map<String, String> getVillaFacilities()
   {
      return villaFacilities;
   }

   /**
    * @param villaFacilities the villaFacilities to set
    */
   public void setVillaFacilities(final Map<String, String> villaFacilities)
   {
      this.villaFacilities = villaFacilities;
   }

   /**
    * @return the roomsMap
    */
   public Map<String, List<String>> getRoomsMap()
   {
      return roomsMap;
   }

   /**
    * @param roomsMap the roomsMap to set
    */
   public void setRoomsMap(final Map<String, List<String>> roomsMap)
   {
      this.roomsMap = roomsMap;
   }

   /**
    * @return the accomUrl
    */
   public String getAccomUrl()
   {
      return accomUrl;
   }

   /**
    * @param accomUrl the accomUrl to set
    */
   public void setAccomUrl(final String accomUrl)
   {
      this.accomUrl = accomUrl;
   }

}
