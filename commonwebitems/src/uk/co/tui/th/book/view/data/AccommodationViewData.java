/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.ArrayList;
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
 * @author madhumathi.m
 */
public class AccommodationViewData implements Cloneable
{
   /** Holds the value for accommodation Code. */
   private String accomCode = StringUtils.EMPTY;

   /**
    * Holds the Room Relevant View Data.
    */
   private List<RoomViewData> roomViewData;

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
   private final Map<String, List<Object>> featureCodesAndValues =
      new HashMap<String, List<Object>>();

   /** The Product ranges. */
   private List<ProductRangeViewData> productRanges;

   /** To check wheater the product is differntiate or not **/
   private boolean differentiatedProduct;

   /** The summay room view data. */
   private List<SummaryRoomViewData> summaryRoomViewData;

   /** The country name. */
   private String countryName = StringUtils.EMPTY;

   /** The empty country name. */
   private boolean emptyCountryName;

   private List<MultiCentreViewData> multiCentreData;

   /**
    * Gets the room view data.
    *
    * @return the roomViewData
    */
   public List<RoomViewData> getRoomViewData()
   {
      if (CollectionUtils.isEmpty(this.roomViewData))
      {
         this.roomViewData = new ArrayList<RoomViewData>();
      }
      return this.roomViewData;
   }

   /**
    * Gets the accom code.
    *
    * @return the accomCode
    */
   public String getAccomCode()
   {
      return accomCode;
   }

   /**
    * Sets the accom code.
    *
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
    * This method prevents this class from being cloned.
    *
    * @return the object
    * @throws CloneNotSupportedException the clone not supported exception
    * @see java.lang.Object#clone()
    */
   @Override
   public final Object clone() throws CloneNotSupportedException
   {
      return super.clone();
   }

   /**
    * @return the noOfRooms
    */
   public int getNoOfRooms()
   {
      return noOfRooms;
   }

   /**
    * Sets the no of rooms.
    *
    * @param noOfRooms the noOfRooms to set
    */
   public void setNoOfRooms(final int noOfRooms)
   {
      this.noOfRooms = noOfRooms;
   }

   /**
    * Sets the room view data.
    *
    * @param roomViewData the roomViewData to set
    */
   public void setRoomViewData(final List<RoomViewData> roomViewData)
   {
      this.roomViewData = roomViewData;
   }

   /**
    * Gets the product ranges.
    *
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
    * Sets the product ranges.
    *
    * @param productRanges the productRanges to set
    */
   public void setProductRanges(final List<ProductRangeViewData> productRanges)
   {
      this.productRanges = productRanges;
   }

   /**
    * Gets the feature codes and values.
    *
    * @return the featureCodesAndValues
    */
   public Map<String, List<Object>> getFeatureCodesAndValues()
   {
      return featureCodesAndValues;
   }

   /**
    * Put feature codes and values.
    *
    * @param featureCodesAndValues the feature codes and values
    */
   public void putFeatureCodesAndValues(final Map<String, List<Object>> featureCodesAndValues)
   {
      this.featureCodesAndValues.putAll(featureCodesAndValues);
   }

   /**
    * Checks if is differentiated product.
    *
    * @return the differentiatedProduct
    */
   public boolean isDifferentiatedProduct()
   {
      return differentiatedProduct;
   }

   /**
    * Sets the differentiated product.
    *
    * @param differentiatedProduct the differentiatedProduct to set
    */
   public void setDifferentiatedProduct(final boolean differentiatedProduct)
   {
      this.differentiatedProduct = differentiatedProduct;
   }

   /**
    * Gets the summary room view data.
    *
    * @return the summaryRoomViewData
    */
   public List<SummaryRoomViewData> getSummaryRoomViewData()
   {
      if (summaryRoomViewData == null)
      {
         this.summaryRoomViewData = new ArrayList<SummaryRoomViewData>();
      }
      return summaryRoomViewData;
   }

   /**
    * Sets the summary room view data.
    *
    * @param summaryRoomViewData the summaryRoomViewData to set
    */
   public void setSummaryRoomViewData(final List<SummaryRoomViewData> summaryRoomViewData)
   {
      this.summaryRoomViewData = summaryRoomViewData;
   }

   /**
    * Gets the country name.
    *
    * @return the countryName
    */
   public String getCountryName()
   {
      return countryName;
   }

   /**
    * Sets the country name.
    *
    * @param countryName the countryName to set
    */
   public void setCountryName(final String countryName)
   {
      this.countryName = countryName;
   }

   /**
    * Checks if is empty country name.
    *
    * @return the emptyCountryName
    */
   public boolean isEmptyCountryName()
   {
      return emptyCountryName;
   }

   /**
    * Sets the empty country name.
    *
    * @param emptyCountryName the emptyCountryName to set
    */
   public void setEmptyCountryName(final boolean emptyCountryName)
   {
      this.emptyCountryName = emptyCountryName;
   }

   /**
    * @return the multiCentreData
    */
   public List<MultiCentreViewData> getMultiCentreData()
   {
      if (CollectionUtils.isEmpty(this.multiCentreData))
      {
         this.multiCentreData = new ArrayList<MultiCentreViewData>();
      }
      return this.multiCentreData;

   }

   /**
    * @param multiCentreData the multiCentreData to set
    */
   public void setMultiCentreData(final List<MultiCentreViewData> multiCentreData)
   {
      this.multiCentreData = multiCentreData;
   }

}
