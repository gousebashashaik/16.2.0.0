/**
 *
 */
package uk.co.tui.book.view.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.portaltech.tui.web.view.data.HasFeatures;

/**
 * This class holds the Room relevant view data.
 *
 * @author madhumathi.m
 *
 */
public class RoomViewData implements HasFeatures, Serializable
{
   /**
    * Holds the description of the facility, if one sent.
    */
   private String description = StringUtils.EMPTY;

   /** The room code. */
   private String roomCode = StringUtils.EMPTY;

   /** The room type. */
   private String roomType;

   /** The room title. */
   private String roomTitle;

   /** The short description. */
   private String shortDescription;

   /**
    * Holds the available number of room.
    */
   private int quantity;

   /** The no of adults. */
   private int noOfAdults;

   /** The no of children. */
   private int noOfChildren;

   /** The no of infants. */
   private int noOfInfants;

   /** The total children count. */
   private int totalChildrenCount;

   /** The child ages. */
   private List<Integer> childAges;

   /** The room allocation image. */
   private String roomAllocationImage = StringUtils.EMPTY;

   /**
    * Holds the price of room.
    */
   private BigDecimal price;

   /** The currency appended price. */
   private String currencyAppendedPrice = StringUtils.EMPTY;

   /**
    * Holds whether the room is included.
    */
   private boolean included;

   /** The selling code. */
   private String sellingCode;

   /** The package id. */
   private String packageId;

   /** The room image. */
   private List<RoomImage> roomImage;

   /** The min occupancy. */
   private int minOccupancy;

   /** The max occupancy. */
   private int maxOccupancy;

   /** The room name. */
   private String roomName;

   /** The room features. */
   private List<String> roomFeatures;

   /** The errata. */
   private String errata;

   /** The limited availability. */
   private boolean limitedAvailability;

   /** The limited availability. */
   private String limitedAvailabilityText;

   /** The selected. */
   private boolean selected;

   /** The per person price. */
   private BigDecimal perPersonPrice;

   private String currencyAppendedPerPersonPrice;

   /** The difference price. */
   private String differencePrice = StringUtils.EMPTY;

   /** The should display. */
   private boolean shouldDisplay;

   /** The default room. */
   private boolean defaultRoom;

   /** The list of board basis. */
   private List<BoardBasisViewData> listOfBoardBasis;

   /** The room warning message. */
   private String roomWarningMessage;

   private String roomPlanImage;

   /** Feature Values **/
   private final Map<String, List<Object>> featureCodesAndValues =
      new HashMap<String, List<Object>>();

   /**
    * @return the roomPlanImage
    */
   public String getRoomPlanImage()
   {
      return roomPlanImage;
   }

   /**
    * @param roomPlanImage the roomPlanImage to set
    */
   public void setRoomPlanImage(final String roomPlanImage)
   {
      this.roomPlanImage = roomPlanImage;
   }

   /**
    * Gets the description.
    *
    * @return the description
    */

   public String getDescription()
   {
      return this.description;
   }

   /**
    * Sets the description.
    *
    * @param description the description to set
    */
   public void setDescription(final String description)
   {
      this.description = description;
   }

   /**
    * Gets the quantity.
    *
    * @return the quantity
    */
   public int getQuantity()
   {
      return this.quantity;
   }

   /**
    * Sets the quantity.
    *
    * @param quantity the quantity to set
    */
   public void setQuantity(final int quantity)
   {
      this.quantity = quantity;
   }

   /**
    * Gets the price.
    *
    * @return the price
    */
   public BigDecimal getPrice()
   {
      return this.price;
   }

   /**
    * Sets the currency appended price.
    *
    * @param currencyAppendedPrice the currencyAppendedPrice to set
    */
   public void setCurrencyAppendedPrice(final String currencyAppendedPrice)
   {

      if (StringUtils.startsWith(currencyAppendedPrice, "-"))
      {

         this.currencyAppendedPrice =
            "-" + CurrencyUtils.getCurrencySymbol("GBP") + currencyAppendedPrice.replace("-", "");
      }
      else
      {
         this.currencyAppendedPrice =
            CurrencyUtils.getCurrencySymbol("GBP") + currencyAppendedPrice;
      }
   }

   /**
    * Gets the currency appended price.
    *
    * @return the price
    */
   public String getCurrencyAppendedPrice()
   {
      return this.currencyAppendedPrice;
   }

   /**
    * Sets the price.
    *
    * @param price the price to set
    */
   public void setPrice(final BigDecimal price)
   {
      this.price = price;
   }

   /**
    * Checks if is included.
    *
    * @return the included
    */
   public boolean isIncluded()
   {
      return this.included;
   }

   /**
    * Sets the included.
    *
    * @param included the included to set
    */
   public void setIncluded(final boolean included)
   {
      this.included = included;
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
    * Gets the no of adults.
    *
    * @return the noOfAdults
    */
   public int getNoOfAdults()
   {
      return noOfAdults;
   }

   /**
    * Sets the no of adults.
    *
    * @param noOfAdults the noOfAdults to set
    */
   public void setNoOfAdults(final int noOfAdults)
   {
      this.noOfAdults = noOfAdults;
   }

   /**
    * Gets the no of children.
    *
    * @return the noOfChildren
    */
   public int getNoOfChildren()
   {
      return noOfChildren;
   }

   /**
    * Sets the no of children.
    *
    * @param noOfChildren the noOfChildren to set
    */
   public void setNoOfChildren(final int noOfChildren)
   {
      this.noOfChildren = noOfChildren;
   }

   /**
    * Gets the no of infants.
    *
    * @return the noOfInfants
    */
   public int getNoOfInfants()
   {
      return noOfInfants;
   }

   /**
    * Sets the no of infants.
    *
    * @param noOfInfants the noOfInfants to set
    */
   public void setNoOfInfants(final int noOfInfants)
   {
      this.noOfInfants = noOfInfants;
   }

   /**
    * Gets the room allocation image.
    *
    * @return the roomAllocationImage
    */
   public String getRoomAllocationImage()
   {
      return roomAllocationImage;
   }

   /**
    * Sets the room allocation image.
    *
    * @param roomAllocationImage the roomAllocationImage to set
    */
   public void setRoomAllocationImage(final String roomAllocationImage)
   {
      this.roomAllocationImage = roomAllocationImage;
   }

   /**
    * Gets the room code.
    *
    * @return the roomCode
    */
   public String getRoomCode()
   {
      return roomCode;
   }

   /**
    * Sets the room code.
    *
    * @param roomCode the roomCode to set
    */
   public void setRoomCode(final String roomCode)
   {
      this.roomCode = roomCode;
   }

   /**
    * Gets the selling code.
    *
    * @return the selling code
    */
   public String getSellingCode()
   {
      return sellingCode;
   }

   /**
    * Sets the selling code.
    *
    * @param sellingCode the new selling code
    */
   public void setSellingCode(final String sellingCode)
   {
      this.sellingCode = sellingCode;
   }

   /**
    * Gets the package id.
    *
    * @return the package id
    */
   public String getPackageId()
   {
      return packageId;
   }

   /**
    * Sets the package id.
    *
    * @param packageId the new package id
    */
   public void setPackageId(final String packageId)
   {
      this.packageId = packageId;
   }

   /**
    * Gets the room image.
    *
    * @return the room image
    */
   public List<RoomImage> getRoomImage()
   {
      return roomImage;
   }

   /**
    * Sets the room image.
    *
    * @param roomImage the new room image
    */
   public void setRoomImage(final List<RoomImage> roomImage)
   {
      this.roomImage = roomImage;
   }

   /**
    * Gets the min occupancy.
    *
    * @return the min occupancy
    */
   public int getMinOccupancy()
   {
      return minOccupancy;
   }

   /**
    * Sets the min occupancy.
    *
    * @param minOccupancy the new min occupancy
    */
   public void setMinOccupancy(final int minOccupancy)
   {
      this.minOccupancy = minOccupancy;
   }

   /**
    * Gets the max occupancy.
    *
    * @return the max occupancy
    */
   public int getMaxOccupancy()
   {
      return maxOccupancy;
   }

   /**
    * Sets the max occupancy.
    *
    * @param maxOccupancy the new max occupancy
    */
   public void setMaxOccupancy(final int maxOccupancy)
   {
      this.maxOccupancy = maxOccupancy;
   }

   /**
    * Gets the room name.
    *
    * @return the room name
    */
   public String getRoomName()
   {
      return roomName;
   }

   /**
    * Sets the room name.
    *
    * @param roomName the new room name
    */
   public void setRoomName(final String roomName)
   {
      this.roomName = roomName;
   }

   /**
    * Gets the room features.
    *
    * @return the room features
    */
   public List<String> getRoomFeatures()
   {
      return roomFeatures;
   }

   /**
    * Sets the room features.
    *
    * @param roomFeatures the new room features
    */
   public void setRoomFeatures(final List<String> roomFeatures)
   {
      this.roomFeatures = roomFeatures;
   }

   /**
    * Gets the errata.
    *
    * @return the errata
    */
   public String getErrata()
   {
      return errata;
   }

   /**
    * Sets the errata.
    *
    * @param errata the new errata
    */
   public void setErrata(final String errata)
   {
      this.errata = errata;
   }

   /**
    * Checks if is limited availability.
    *
    * @return true, if is limited availability
    */
   public boolean isLimitedAvailability()
   {
      return limitedAvailability;
   }

   /**
    * Sets the limited availability.
    *
    * @param limitedAvailability the new limited availability
    */
   public void setLimitedAvailability(final boolean limitedAvailability)
   {
      this.limitedAvailability = limitedAvailability;
   }

   /**
    * Checks if is selected.
    *
    * @return true, if is selected
    */
   public boolean isSelected()
   {
      return selected;
   }

   /**
    * Sets the selected.
    *
    * @param selected the new selected
    */
   public void setSelected(final boolean selected)
   {
      this.selected = selected;
   }

   /**
    * Gets the per person price.
    *
    * @return the per person price
    */
   public BigDecimal getPerPersonPrice()
   {
      return perPersonPrice;
   }

   /**
    * Sets the per person price.
    *
    * @param perPersonPrice the new per person price
    */
   public void setPerPersonPrice(final BigDecimal perPersonPrice)
   {
      this.perPersonPrice = perPersonPrice;
   }

   /**
    * Gets the difference price.
    *
    * @return the difference price
    */
   public String getDifferencePrice()
   {
      return this.differencePrice;
   }

   /**
    * Sets the difference price.
    *
    * @param differencePrice the new difference price
    */
   public void setDifferencePrice(final String differencePrice)
   {
      this.differencePrice = differencePrice;
   }

   /**
    * Checks if is should display.
    *
    * @return true, if is should display
    */
   public boolean isShouldDisplay()
   {
      return shouldDisplay;
   }

   /**
    * Sets the should display.
    *
    * @param shouldDisplay the new should display
    */
   public void setShouldDisplay(final boolean shouldDisplay)
   {
      this.shouldDisplay = shouldDisplay;
   }

   /**
    * Checks if is default room.
    *
    * @return true, if is default room
    */
   public boolean isDefaultRoom()
   {
      return defaultRoom;
   }

   /**
    * Sets the default room.
    *
    * @param defaultRoom the new default room
    */
   public void setDefaultRoom(final boolean defaultRoom)
   {
      this.defaultRoom = defaultRoom;
   }

   /**
    * Gets the list of board basis.
    *
    * @return the list of board basis
    */
   public List<BoardBasisViewData> getListOfBoardBasis()
   {
      return listOfBoardBasis;
   }

   /**
    * Sets the list of board basis.
    *
    * @param listOfBoardBasis the new list of board basis
    */
   public void setListOfBoardBasis(final List<BoardBasisViewData> listOfBoardBasis)
   {
      this.listOfBoardBasis = listOfBoardBasis;
   }

   /**
    * Gets the room type.
    *
    * @return the roomType
    */
   public String getRoomType()
   {
      return roomType;
   }

   /**
    * Sets the room type.
    *
    * @param roomType the roomType to set
    */
   public void setRoomType(final String roomType)
   {
      this.roomType = roomType;
   }

   /**
    * Gets the room title.
    *
    * @return the roomTitle
    */
   public String getRoomTitle()
   {
      return roomTitle;
   }

   /**
    * Sets the room title.
    *
    * @param roomTitle the roomTitle to set
    */
   public void setRoomTitle(final String roomTitle)
   {
      this.roomTitle = roomTitle;
   }

   /**
    * Gets the short description.
    *
    * @return the shortDescription
    */
   public String getShortDescription()
   {
      return shortDescription;
   }

   /**
    * Sets the short description.
    *
    * @param shortDescription the shortDescription to set
    */
   public void setShortDescription(final String shortDescription)
   {
      this.shortDescription = shortDescription;
   }

   /**
    * Gets the child ages.
    *
    * @return the childAges
    */
   public List<Integer> getChildAges()
   {
      return childAges;
   }

   /**
    * Sets the child ages.
    *
    * @param childAges the childAges to set
    */
   public void setChildAges(final List<Integer> childAges)
   {
      this.childAges = childAges;
   }

   /**
    * Gets the limited availability text.
    *
    * @return the limitedAvailabilityText
    */
   public String getLimitedAvailabilityText()
   {
      return limitedAvailabilityText;
   }

   /**
    * Sets the limited availability text.
    *
    * @param limitedAvailabilityText the limitedAvailabilityText to set
    */
   public void setLimitedAvailabilityText(final String limitedAvailabilityText)
   {
      this.limitedAvailabilityText = limitedAvailabilityText;
   }

   /**
    * Gets the total children count.
    *
    * @return the totalChildrenCount
    */
   public int getTotalChildrenCount()
   {
      return totalChildrenCount;
   }

   /**
    * Sets the total children count.
    *
    * @param totalChildrenCount the totalChildrenCount to set
    */
   public void setTotalChildrenCount(final int totalChildrenCount)
   {
      this.totalChildrenCount = totalChildrenCount;
   }

   /**
    * Gets the room warning message
    *
    * @return the roomWarningMessage
    */
   public String getRoomWarningMessage()
   {
      return roomWarningMessage;
   }

   /**
    * Sets the room warning message
    *
    * @param roomWarningMessage the roomWarningMessage to set
    */
   public void setRoomWarningMessage(final String roomWarningMessage)
   {
      this.roomWarningMessage = roomWarningMessage;
   }

   /**
    * @return the currencyAppendedPerPersonPrice
    */
   public String getCurrencyAppendedPerPersonPrice()
   {
      return currencyAppendedPerPersonPrice;
   }

   /**
    * @param currencyAppendedPerPersonPrice the currencyAppendedPerPersonPrice to set
    */
   public void setCurrencyAppendedPerPersonPrice(final String currencyAppendedPerPersonPrice)
   {
      this.currencyAppendedPerPersonPrice = currencyAppendedPerPersonPrice;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.web.view.data.HasFeatures#getFeatureCodesAndValues()
    */
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

   public String getFeatureValue(final String featureCode)
   {
      final List<Object> featurelist = featureCodesAndValues.get(featureCode);
      if (featureCodesAndValues.containsKey(featureCode) && CollectionUtils.isNotEmpty(featurelist))
      {
         return featurelist.get(0).toString();
      }
      return StringUtils.EMPTY;
   }

   public List<String> getRoomFeatureasList(final String featureCode)
   {
      final List<Object> uspObjects = featureCodesAndValues.get(featureCode);

      final List<String> usps = new ArrayList<String>();
      if (uspObjects != null)
      {
         for (final Object usp : uspObjects)
         {
            usps.add(usp.toString());
         }
      }
      return usps;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.web.view.data.HasFeatures#putFeatureValue(java.lang .String,
    * java.util.List)
    */
   @Override
   public void putFeatureValue(final String featureCode, final List<Object> featureValues)
   {

      // Do nothing because an overridden method.
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.web.view.data.HasFeatures#getFeatureValues(java. lang.String)
    */
   @Override
   public List<Object> getFeatureValues(final String featureCode)
   {
      return Collections.emptyList();
   }

}
