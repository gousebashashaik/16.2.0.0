/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

/**
 * @author laxmibai.p
 *
 */
public final class DeepLinkRequestData extends SearchRequestData
{

   private List<AirportData> airports;

   private List<UnitData> units;

   private String until;

   private String when;

   private boolean flexibility;

   private int noOfAdults;

   private int noOfSeniors;

   /** noOfChildren = infant+ children sent by UI */
   private int noOfChildren;

   /** comma serperated age */
   private List<Integer> childrenAge;

   private int flexibleDays;

   private int first;

   private int last;

   private int duration;

   /** calculated by hybris based on childrenAge */
   private int infantCount;

   /** calculated by hybris based on noOfChildren - infantCount */
   private int childCount;

   private String packageId;

   /**
    * holds the searchRequestType "sort" or "filterPanel"
    */
   private String searchRequestType;

   private boolean appRequest;

   private boolean iscapeRequest;

   private boolean flightOptions;

   private String selectedBoardBasis;

   private String multiSelect;

   private List<RoomAllocation> rooms = new ArrayList<RoomAllocation>();

   // Should have the duration code to be used for mobile.
   private int modifiedDuration;

   private String startDate;

   private String enddate;

   private String smerchDuration;

   private String productType;

   private String searchType;

   private String pdd;

   private String brandType;

   /**
    * @return the pdd
    */
   public String getPdd()
   {
      return pdd;
   }

   /**
    * @param pdd the pdd to set
    */
   public void setPdd(final String pdd)
   {
      this.pdd = pdd;
   }

   /**
    * @return the productType
    */
   public String getProductType()
   {
      return productType;
   }

   /**
    * @param productType the productType to set
    */
   public void setProductType(final String productType)
   {
      this.productType = productType;
   }

   /**
    * @return the rooms
    */
   public List<RoomAllocation> getRooms()
   {
      return rooms;
   }

   /**
    * @param rooms the rooms to set
    */
   public void setRooms(final List<RoomAllocation> rooms)
   {
      this.rooms = rooms;
   }

   /**
    * @return the brandType
    */
   public String getBrandType()
   {
      return brandType;
   }

   /**
    * @param brandType the brandType to set
    */
   public void setBrandType(final String brandType)
   {
      this.brandType = brandType;
   }

   /**
    * @return the multiSelect
    */
   public String getMultiSelect()
   {
      return multiSelect;
   }

   /**
    * @param multiSelect the multiSelect to set
    */
   public void setMultiSelect(final String multiSelect)
   {
      this.multiSelect = multiSelect;
   }

   /**
    * @return the searchRequestType
    */
   public String getSearchRequestType()
   {
      return searchRequestType;
   }

   /**
    * @param searchRequestType the searchRequestType to set
    */
   public void setSearchRequestType(final String searchRequestType)
   {
      this.searchRequestType = searchRequestType;
   }

   /**
    * @return the childCount
    */
   public int getChildCount()
   {
      return childCount;
   }

   /**
    * @param childCount the childCount to set
    */
   public void setChildCount(final int childCount)
   {
      this.childCount = childCount;
   }

   /**
    * @return the childrenAge
    */
   public List<Integer> getChildrenAge()
   {
      return childrenAge;
   }

   /**
    * @param childrenAge the childrenAge to set
    */
   public void setChildrenAge(final List<Integer> childrenAge)
   {
      this.childrenAge = childrenAge;
   }

   /**
    * @return the packageId
    */
   public String getPackageId()
   {
      return packageId;
   }

   /**
    * @param packageId the packageId to set
    */
   public void setPackageId(final String packageId)
   {
      this.packageId = packageId;
   }

   /**
    * @return the airports
    */
   public List<AirportData> getAirports()
   {
      return airports;
   }

   /**
    * @param airports the airports to set
    */
   public void setAirports(final List<AirportData> airports)
   {
      this.airports = airports;
   }

   /**
    * @return the units
    */
   public List<UnitData> getUnits()
   {
      return units;
   }

   /**
    * @param units the units to set
    */
   public void setUnits(final List<UnitData> units)
   {
      this.units = units;
   }

   /**
    * @return the until
    */
   public String getUntil()
   {
      return until;
   }

   /**
    * @param until the until to set
    */
   public void setUntil(final String until)
   {
      this.until = until;
   }

   /**
    * @return the when
    */
   public String getWhen()
   {
      return when;
   }

   /**
    * @param when the when to set
    */
   public void setWhen(final String when)
   {
      this.when = when;
   }

   /**
    * @return the flexibility
    */
   public boolean isFlexibility()
   {
      return flexibility;
   }

   /**
    * @param flexibility the flexibility to set
    */
   public void setFlexibility(final boolean flexibility)
   {
      this.flexibility = flexibility;
   }

   /**
    * @return the noOfAdults
    */
   public int getNoOfAdults()
   {
      return noOfAdults;
   }

   /**
    * @param noOfAdults the noOfAdults to set
    */
   public void setNoOfAdults(final int noOfAdults)
   {
      this.noOfAdults = noOfAdults;
   }

   /**
    * @return the noOfSeniors
    */
   public int getNoOfSeniors()
   {
      return noOfSeniors;
   }

   /**
    * @param noOfSeniors the noOfSeniors to set
    */
   public void setNoOfSeniors(final int noOfSeniors)
   {
      this.noOfSeniors = noOfSeniors;
   }

   /**
    * @return the noOfChildren
    */
   public int getNoOfChildren()
   {
      return noOfChildren;
   }

   /**
    * @param noOfChildren the noOfChildren to set
    */
   public void setNoOfChildren(final int noOfChildren)
   {
      this.noOfChildren = noOfChildren;
   }

   /**
    * @return the childrenAge
    *
    *         /**
    * @return the flexibleDays
    */
   public int getFlexibleDays()
   {
      return flexibleDays;
   }

   /**
    * @param flexibleDays the flexibleDays to set
    */
   public void setFlexibleDays(final int flexibleDays)
   {
      this.flexibleDays = flexibleDays;
   }

   /**
    * @return the first
    */
   public int getFirst()
   {
      return first;
   }

   /**
    * @param first the first to set
    */
   public void setFirst(final int first)
   {
      this.first = first;
   }

   /**
    * @return the last
    */
   public int getLast()
   {
      return last;
   }

   /**
    * @param last the last to set
    */
   public void setLast(final int last)
   {
      this.last = last;
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
    * @return the infantCount
    */
   public int getInfantCount()
   {
      return infantCount;
   }

   /**
    * @param infantCount the infantCount to set
    */
   public void setInfantCount(final int infantCount)
   {
      this.infantCount = infantCount;
   }

   /**
    * @return the appRequest
    */
   public boolean isAppRequest()
   {
      return appRequest;
   }

   /**
    * @param appRequest the appRequest to set
    */
   public void setAppRequest(final boolean appRequest)
   {
      this.appRequest = appRequest;
   }

   /**
    * @return the iscapeRequest
    */
   public boolean isIscapeRequest()
   {
      return iscapeRequest;
   }

   /**
    * @param iscapeRequest the iscapeRequest to set
    */
   public void setIscapeRequest(final boolean iscapeRequest)
   {
      this.iscapeRequest = iscapeRequest;
   }

   /**
    * @return the flightOptions
    */
   public boolean isFlightOptions()
   {
      return flightOptions;
   }

   /**
    * @param flightOptions the flightOptions to set
    */
   public void setFlightOptions(final boolean flightOptions)
   {
      this.flightOptions = flightOptions;
   }

   /**
    * @return the selectedBoardBasis
    */
   public String getSelectedBoardBasis()
   {
      return selectedBoardBasis;
   }

   /**
    * @param selectedBoardBasis the selectedBoardBasis to set
    */
   public void setSelectedBoardBasis(final String selectedBoardBasis)
   {
      this.selectedBoardBasis = selectedBoardBasis;
   }

   /**
    * @return the modifiedDuration
    */
   public int getModifiedDuration()
   {
      return modifiedDuration;
   }

   /**
    * @param modifiedDuration the modifiedDuration to set
    */
   public void setModifiedDuration(final int modifiedDuration)
   {
      this.modifiedDuration = modifiedDuration;
   }

   /**
    * @return the startDate
    */
   public String getStartDate()
   {
      return startDate;
   }

   /**
    * @param startDate the startDate to set
    */
   public void setStartDate(final String startDate)
   {
      this.startDate = startDate;
   }

   /**
    * @return the enddate
    */
   public String getEnddate()
   {
      return enddate;
   }

   /**
    * @param enddate the enddate to set
    */
   public void setEnddate(final String enddate)
   {
      this.enddate = enddate;
   }

   /**
    * @return the smerchDuration
    */
   public String getSmerchDuration()
   {
      return smerchDuration;
   }

   /**
    * @param smerchDuration the smerchDuration to set
    */
   public void setSmerchDuration(final String smerchDuration)
   {
      this.smerchDuration = smerchDuration;
   }

   /**
    * @return the searchType
    */
   public String getSearchType()
   {
      return searchType;
   }

   /**
    * @param searchType the searchType to set
    */
   public void setSearchType(final String searchType)
   {
      this.searchType = searchType;
   }

}
