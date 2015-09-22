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
public class DeepLinkRequestViewData extends SearchRequestData
{

   private List<AirportData> airports = new ArrayList<AirportData>();

   private List<UnitData> units = new ArrayList<UnitData>();

   private String until;

   private String when;

   private boolean flexibility;

   private int noOfAdults;

   private int noOfSeniors;

   /** noOfChildren = infant+ children sent by UI */
   private int noOfChildren;

   /** comma serperated age */
   private List<Integer> childrenAge = new ArrayList();

   private List<RoomAllocation> rooms = new ArrayList<RoomAllocation>();

   // Should have the duration code to be used for mobile.
   private int modifiedDuration;

   private String multiSelect;

   private String productType;

   private String searchType;

   private String pdd;

   private int flexibleDays;

   private int first;

   private int last;

   private int duration;

   /** calculated by hybris based on childrenAge */
   private int infantCount;

   private String groupId;

   private List<ErrorData> errors = new ArrayList<ErrorData>();

   private List<String> availableDates = new ArrayList<String>();

   private String searchRequestType;

   private boolean iscapeRequest;

   private String siteBrand;

   private boolean flightOptions;

   private String selectedBoardBasis;

   private String brandType;

   private String packageId;

   // is accom details page
   private boolean accomDetails;

   private String startdate;

   private String endDate;

   private List<String> availableMonths;

   private String smerchDuration;

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
    * @return the siteBrand
    */
   public String getSiteBrand()
   {
      return siteBrand;
   }

   /**
    * @param siteBrand the siteBrand to set
    */
   public void setSiteBrand(final String siteBrand)
   {
      this.siteBrand = siteBrand;
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
    * @return the availableDates
    */
   public List<String> getAvailableDates()
   {
      return availableDates;
   }

   /**
    * @param availableDates the availableDates to set
    */
   public void setAvailableDates(final List<String> availableDates)
   {
      this.availableDates = availableDates;
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
    * @return the errors
    */
   public List<ErrorData> getErrors()
   {
      return errors;
   }

   /**
    * @param errors the errors to set
    */
   public void setErrors(final List<ErrorData> errors)
   {
      this.errors = errors;
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
    * @return the groupId
    */
   public String getGroupId()
   {
      return groupId;
   }

   /**
    * @param groupId the groupId to set
    */
   public void setGroupId(final String groupId)
   {
      this.groupId = groupId;
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
    * @return the accomDetails
    */
   public boolean isAccomDetails()
   {
      return accomDetails;
   }

   /**
    * @param accomDetails the accomDetails to set
    */
   public void setAccomDetails(final boolean accomDetails)
   {
      this.accomDetails = accomDetails;
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
    * @return the startdate
    */
   public String getStartdate()
   {
      return startdate;
   }

   /**
    * @param startdate the startdate to set
    */
   public void setStartdate(final String startdate)
   {
      this.startdate = startdate;
   }

   /**
    * @return the endDate
    */
   public String getEndDate()
   {
      return endDate;
   }

   /**
    * @param endDate the endDate to set
    */
   public void setEndDate(final String endDate)
   {
      this.endDate = endDate;
   }

   /**
    * @return the availableMonths
    */
   public List<String> getAvailableMonths()
   {
      return availableMonths;
   }

   /**
    * @param availableMonths the availableMonths to set
    */
   public void setAvailableMonths(final List<String> availableMonths)
   {
      this.availableMonths = availableMonths;
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
