/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author premkumar.nd
 *
 */

/*
 * RoomViewData should have the details from Domian object + PIM data(room occupancy details)
 */

public class RoomViewData
{

   private String roomNo;

   private String roomDesc;

   private List<String> facList;

   private boolean infIncOcc;

   private String boardingCode;

   private boolean included;

   /** The currency appended price. */
   private String currencyAppendedPrice = StringUtils.EMPTY;

   private int quantity;

   private String roomCode;

   private List<String> altBoardingCode;

   private List<PassengerViewData> passengerViewDataList;

   private List<PriceViewData> priceList;

   private String roomType;

   private String roomTitle;

   private Map<String, Integer> occupancy;

   private String[] usps;

   private String description;

   private String shortDescription;

   private String[] upgrade;

   private String accommodationRoomsUrl;

   private Map<String, List<Object>> featureCodesAndValues;

   private List<String> sharedUsps;

   private List<String> varyingUsps;

   private List<MediaViewData> galleryImages;

   private uk.co.portaltech.tui.web.view.data.MediaViewData thumbnail;

   private static final int PRIME = 31;

   /**
    * @return the priceList
    */
   public List<PriceViewData> getPriceList()
   {
      return priceList;
   }

   /**
    * @param priceList the priceList to set
    */
   public void setPriceList(final List<PriceViewData> priceList)
   {
      this.priceList = priceList;
   }

   /**
    * @return the galleryImages
    */
   public List<MediaViewData> getGalleryImages()
   {
      return galleryImages;
   }

   /**
    * @param galleryImages the galleryImages to set
    */
   public void setGalleryImages(final List<MediaViewData> galleryImages)
   {
      this.galleryImages = galleryImages;
   }

   /**
    * @return the roomNo
    */
   public String getRoomNo()
   {
      return roomNo;
   }

   /**
    * @param roomNo the roomNo to set
    */
   public void setRoomNo(final String roomNo)
   {
      this.roomNo = roomNo;
   }

   /**
    * @return the roomDesc
    */
   public String getRoomDesc()
   {
      return roomDesc;
   }

   /**
    * @param roomDesc the roomDesc to set
    */
   public void setRoomDesc(final String roomDesc)
   {
      this.roomDesc = roomDesc;
   }

   /**
    * @return the facList
    */
   public List<String> getFacList()
   {
      return facList;
   }

   /**
    * @param facList the facList to set
    */
   public void setFacList(final List<String> facList)
   {
      this.facList = facList;
   }

   /**
    * @return the infIncOcc
    */
   public boolean isInfIncOcc()
   {
      return infIncOcc;
   }

   /**
    * @param infIncOcc the infIncOcc to set
    */
   public void setInfIncOcc(final boolean infIncOcc)
   {
      this.infIncOcc = infIncOcc;
   }

   /**
    * @return the boardingCode
    */
   public String getBoardingCode()
   {
      return boardingCode;
   }

   /**
    * @param boardingCode the boardingCode to set
    */
   public void setBoardingCode(final String boardingCode)
   {
      this.boardingCode = boardingCode;
   }

   /**
    * @return the altBoardingCode
    */
   public List<String> getAltBoardingCode()
   {
      return altBoardingCode;
   }

   /**
    * @param altBoardingCode the altBoardingCode to set
    */
   public void setAltBoardingCode(final List<String> altBoardingCode)
   {
      this.altBoardingCode = altBoardingCode;
   }

   /**
    * @return the passengerViewDataList
    */
   public List<PassengerViewData> getPassengerViewDataList()
   {
      return passengerViewDataList;
   }

   /**
    * @param passengerViewDataList the passengerViewDataList to set
    */
   public void setPassengerViewDataList(final List<PassengerViewData> passengerViewDataList)
   {
      this.passengerViewDataList = passengerViewDataList;
   }

   /**
    * @return the roomType
    */
   public String getRoomType()
   {
      return roomType;
   }

   /**
    * @param roomType the roomType to set
    */
   public void setRoomType(final String roomType)
   {
      this.roomType = roomType;
   }

   /**
    * @return the roomTitle
    */
   public String getRoomTitle()
   {
      return roomTitle;
   }

   /**
    * @param roomTitle the roomTitle to set
    */
   public void setRoomTitle(final String roomTitle)
   {
      this.roomTitle = roomTitle;
   }

   /**
    * @return the occupancy
    */
   public Map<String, Integer> getOccupancy()
   {
      return occupancy;
   }

   /**
    * @param occupancy the occupancy to set
    */
   public void setOccupancy(final Map<String, Integer> occupancy)
   {
      this.occupancy = occupancy;
   }

   /**
    * @return the usps
    */
   public String[] getUsps()
   {
      if (usps == null)
      {
         return new String[0];
      }
      return Arrays.copyOf(usps, this.usps.length);
   }

   /**
    * @param usps the usps to set
    */
   public void setUsps(final String[] newUsps)
   {
      if (newUsps == null)
      {
         this.usps = new String[0];
      }
      else
      {
         this.usps = Arrays.copyOf(newUsps, newUsps.length);
      }

   }

   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(final String description)
   {
      this.description = description;
   }

   /**
    * @return the shortDescription
    */
   public String getShortDescription()
   {
      return shortDescription;
   }

   /**
    * @param shortDescription the shortDescription to set
    */
   public void setShortDescription(final String shortDescription)
   {
      this.shortDescription = shortDescription;
   }

   /**
    * @return the upgrade
    */
   public String[] getUpgrade()
   {
      if (upgrade == null)
      {
         return new String[0];
      }
      return Arrays.copyOf(upgrade, upgrade.length);
   }

   /**
    * @param newUpgrade the upgrade to set
    */
   public void setUpgrade(final String[] newUpgrade)
   {
      if (newUpgrade == null)
      {
         this.upgrade = new String[0];
      }
      else
      {
         this.upgrade = Arrays.copyOf(newUpgrade, newUpgrade.length);
      }
   }

   /**
    * @return the accommodationRoomsUrl
    */
   public String getAccommodationRoomsUrl()
   {
      return accommodationRoomsUrl;
   }

   /**
    * @param accommodationRoomsUrl the accommodationRoomsUrl to set
    */
   public void setAccommodationRoomsUrl(final String accommodationRoomsUrl)
   {
      this.accommodationRoomsUrl = accommodationRoomsUrl;
   }

   /**
    * @return the sharedUsps
    */
   public List<String> getSharedUsps()
   {
      return sharedUsps;
   }

   /**
    * @param sharedUsps the sharedUsps to set
    */
   public void setSharedUsps(final List<String> sharedUsps)
   {
      this.sharedUsps = sharedUsps;
   }

   /**
    * @return the varyingUsps
    */
   public List<String> getVaryingUsps()
   {
      return varyingUsps;
   }

   /**
    * @param varyingUsps the varyingUsps to set
    */
   public void setVaryingUsps(final List<String> varyingUsps)
   {
      this.varyingUsps = varyingUsps;
   }

   /**
    * @return the featureCodesAndValues
    */
   public Map<String, List<Object>> getFeatureCodesAndValues()
   {
      return featureCodesAndValues;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {

      int result = 1;
      result = PRIME * result + ((roomDesc == null) ? 0 : roomDesc.hashCode());
      result = PRIME * result + ((roomNo == null) ? 0 : roomNo.hashCode());
      return result;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final RoomViewData other = (RoomViewData) obj;
      if (roomDesc == null)
      {
         if (other.roomDesc != null)
         {
            return false;
         }
      }
      else if (!roomDesc.equals(other.roomDesc))
      {
         return false;
      }
      if (roomNo == null)
      {
         if (other.roomNo != null)
         {
            return false;
         }
      }
      else if (!roomNo.equals(other.roomNo))
      {
         return false;
      }
      return true;
   }

   /**
    * @return the thumbnail
    */
   public uk.co.portaltech.tui.web.view.data.MediaViewData getThumbnail()
   {
      return thumbnail;
   }

   /**
    * @param thumbnail the thumbnail to set
    */
   public void setThumbnail(final uk.co.portaltech.tui.web.view.data.MediaViewData thumbnail)
   {
      this.thumbnail = thumbnail;
   }

   /**
    * @return the quantity
    */
   public int getQuantity()
   {
      return quantity;
   }

   /**
    * @param quantity the quantity to set
    */
   public void setQuantity(final int quantity)
   {
      this.quantity = quantity;
   }

   /**
    * @return the roomCode
    */
   public String getRoomCode()
   {
      return roomCode;
   }

   /**
    * @param roomCode the roomCode to set
    */
   public void setRoomCode(final String roomCode)
   {
      this.roomCode = roomCode;
   }

   /**
    * @return the included
    */
   public boolean isIncluded()
   {
      return included;
   }

   /**
    * @param included the included to set
    */
   public void setIncluded(final boolean included)
   {
      this.included = included;
   }

   /**
    * @return the currencyAppendedPrice
    */
   public String getCurrencyAppendedPrice()
   {
      return currencyAppendedPrice;
   }

   /**
    * @param currencyAppendedPrice the currencyAppendedPrice to set
    */
   public void setCurrencyAppendedPrice(final String currencyAppendedPrice)
   {
      this.currencyAppendedPrice = currencyAppendedPrice;
   }
}
