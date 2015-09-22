/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import uk.co.portaltech.tui.web.view.data.wrapper.SearchResultDateSelectionViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

public class SearchResultsRequestData extends SearchRequestData implements Cloneable
{

   private List<AirportData> airports = new ArrayList<AirportData>();

   private List<AirportData> sourceAirportList = new ArrayList<AirportData>();

   private List<UnitData> units = new ArrayList<UnitData>();

   // this is added as part of flight options page
   private List<UnitData> primaryUnits = new ArrayList<UnitData>();

   private static final int NUM_PRIME_31 = 31;

   private static final int NUM_PRIME_17 = 17;

   private String until;

   private String when;

   private boolean flexibility;

   private int noOfAdults;

   private int noOfSeniors;

   /** noOfChildren = infant+ children sent by UI */
   private int noOfChildren;

   /** children age sent by UI */
   private List<Integer> childrenAge = new ArrayList<Integer>();

   private int flexibleDays;

   private int first;

   private int last;

   private int duration;

   /** calculated by hybris based on childrenAge */
   private int infantCount;

   /** This attribute is added to hold actual departure date */
   private String departureDate;

   private String defaultDuration;

   /** calculated by hybris based on noOfChildren - infantCount */
   private int childCount;

   // Main filter
   private MainFilterRequest filters;

   /**
    * holds the searchRequestType "sort" or "filterPanel"
    */
   private String searchRequestType;

   private List<RoomAllocation> rooms = new ArrayList<RoomAllocation>();

   private boolean singleAccomSearch;

   private List<Integer> childAges = new ArrayList<Integer>();

   private boolean iscapeRequest;

   private boolean flightOptions;

   private String selectedBoardBasis;

   private String brandType;

   private String packageId;

   private String hashValue;

   private String searchType;

   private String smerchDuration;

   private String pdd;

   private int updatedNoOfAdults;

   private int updatedNoOfChildren;

   private int updatedAge;

   private List<Integer> updatedChildrenAges = new ArrayList<Integer>();

   private boolean posibilityOfDuplicateUnit;

   // Should have the duration code to be used for mobile.
   private int modifiedDuration;

   private List<String> airportList;

   private String singleAirport;

   private Map<String, String> recomData;

   private List<String> unitsList;

   private String priceView;

   private SearchResultDurationViewData durationSelection;

   private DestinationOptionData backToDestinationOptionData;

   private boolean dateSliderFilter;

   private String newUntil;

   private String newWhen;

   private boolean anyWhereSearch;

   private int shortlistFirst;

   private int shortlistOffset;

   // is accom details page
   private boolean accomDetails;

   private SearchResultDateSelectionViewData availableDates =
      new SearchResultDateSelectionViewData();

   /**
    * @return the updatedNoOfAdults
    */
   public int getUpdatedNoOfAdults()
   {
      return updatedNoOfAdults;
   }

   /**
    * @param updatedNoOfAdults the updatedNoOfAdults to set
    */
   public void setUpdatedNoOfAdults(final int updatedNoOfAdults)
   {
      this.updatedNoOfAdults = updatedNoOfAdults;
   }

   /**
    * @return the updatedNoOfChildren
    */
   public int getUpdatedNoOfChildren()
   {
      return updatedNoOfChildren;
   }

   /**
    * @param updatedNoOfChildren the updatedNoOfChildren to set
    */
   public void setUpdatedNoOfChildren(final int updatedNoOfChildren)
   {
      this.updatedNoOfChildren = updatedNoOfChildren;
   }

   /**
    * @return the updatedAge
    */
   public int getUpdatedAge()
   {
      return updatedAge;
   }

   /**
    * @param updatedAge the updatedAge to set
    */
   public void setUpdatedAge(final int updatedAge)
   {
      this.updatedAge = updatedAge;
   }

   /**
    * @return the updatedChildrenAges
    */
   public List<Integer> getUpdatedChildrenAges()
   {
      return updatedChildrenAges;
   }

   /**
    * @param updatedChildrenAges the updatedChildrenAges to set
    */
   public void setUpdatedChildrenAges(final List<Integer> updatedChildrenAges)
   {
      this.updatedChildrenAges = updatedChildrenAges;
   }

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
    * @return the hashValue
    */
   public String getHashValue()
   {
      return hashValue;
   }

   /**
    * @param hashValue the hashValue to set
    */
   public void setHashValue(final String hashValue)
   {
      this.hashValue = hashValue;
   }

   /**
    * @return the newUntil
    */
   public String getnewUntil()
   {
      return newUntil;
   }

   /**
    * @param newUntil the newUntil to set
    */
   public void setnewUntil(final String newUntil)
   {
      this.newUntil = newUntil;
   }

   /**
    * @return the newWhen
    */
   public String getnewWhen()
   {
      return newWhen;
   }

   /**
    * @param newWhen the newWhen to set
    */
   public void setnewWhen(final String newWhen)
   {
      this.newWhen = newWhen;
   }

   /**
    * @return the dateSliderFilter
    */
   public boolean isDateSliderFilter()
   {
      return dateSliderFilter;
   }

   /**
    * @param dateSliderFilter the dateSliderFilter to set
    */
   public void setDateSliderFilter(final boolean dateSliderFilter)
   {
      this.dateSliderFilter = dateSliderFilter;
   }

   /**
    * @return the backToDestinationOptionData
    */
   public DestinationOptionData getBackToDestinationOptionData()
   {
      return backToDestinationOptionData;
   }

   /**
    * @param backToDestinationOptionData the backToDestinationOptionData to set
    */
   public void setBackToDestinationOptionData(
      final DestinationOptionData backToDestinationOptionData)
   {
      this.backToDestinationOptionData = backToDestinationOptionData;
   }

   /**
    * @return the availableDates
    */
   public SearchResultDateSelectionViewData getAvailableDates()
   {
      return availableDates;
   }

   /**
    * @param availableDates the availableDates to set
    */
   public void setAvailableDates(final SearchResultDateSelectionViewData availableDates)
   {
      this.availableDates = availableDates;
   }

   /**
    * @return the durationSelection
    */
   public SearchResultDurationViewData getDurationSelection()
   {
      return durationSelection;
   }

   /**
    * @param durationSelection the durationSelection to set
    */
   public void setDurationSelection(final SearchResultDurationViewData durationSelection)
   {
      this.durationSelection = durationSelection;
   }

   /**
    * @return the priceView
    */
   public String getPriceView()
   {
      return priceView;
   }

   /**
    * @param priceView the priceView to set
    */
   public void setPriceView(final String priceView)
   {
      this.priceView = priceView;
   }

   /**
    * @return the unitsList
    */
   public List<String> getUnitsList()
   {
      return unitsList;
   }

   /**
    * @param unitsList the unitsList to set
    */
   public void setUnitsList(final List<String> unitsList)
   {
      this.unitsList = unitsList;
   }

   /**
    * @return the recomData
    */
   public Map<String, String> getRecomData()
   {
      return recomData;
   }

   /**
    * @param recomData the recomData to set
    */
   public void setRecomData(final Map<String, String> recomData)
   {
      this.recomData = recomData;
   }

   /**
    * @return the singleAirport
    */
   public String getSingleAirport()
   {
      return singleAirport;
   }

   /**
    * @param singleAirport the singleAirport to set
    */
   public void setSingleAirport(final String singleAirport)
   {
      this.singleAirport = singleAirport;
   }

   /**
    * @return the airportList
    */
   public List<String> getAirportList()
   {
      return airportList;
   }

   /**
    * @param airportList the airportList to set
    */
   public void setAirportList(final List<String> airportList)
   {
      this.airportList = airportList;
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
    * @return the childAges
    */
   public List<Integer> getChildAges()
   {
      return childAges;
   }

   /**
    * @param childAges the childAges to set
    */
   public void setChildAges(final List<Integer> childAges)
   {
      this.childAges = childAges;
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

   // Filter panel request

   /**
    * @return the filters
    */
   public MainFilterRequest getFilters()
   {
      return filters;
   }

   /**
    * @param filters the filters to set
    */
   public void setFilters(final MainFilterRequest filters)
   {
      this.filters = filters;
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
    * @return the singleAccomSearch
    */
   public boolean isSingleAccomSearch()
   {
      return singleAccomSearch;
   }

   /**
    * @param singleAccomSearch the singleAccomSearch to set
    */
   public void setSingleAccomSearch(final boolean singleAccomSearch)
   {
      this.singleAccomSearch = singleAccomSearch;
   }

   @Override
   public int hashCode()
   {

      return generateHashCode();
   }

   /**
    *
    * * @return
    */
   private int generateHashCode()
   {
      final HashCodeBuilder hashBuilder = new HashCodeBuilder();

      hashBuilder.append(this.first);
      hashBuilder.append(this.duration);
      hashBuilder.append(this.flexibility);
      hashBuilder.append(this.until);
      hashBuilder.append(this.when);
      hashBuilder.append(this.noOfAdults);
      hashBuilder.append(this.noOfSeniors);
      hashBuilder.append(this.infantCount);

      if (CollectionUtils.isNotEmpty(getUnits()))
      {
         for (final UnitData unit : getUnits())
         {
            if (unit != null && StringUtils.isNotBlank(unit.getId()))
            {
               hashBuilder.append(unit.getId());
            }
         }
      }

      if (CollectionUtils.isNotEmpty(this.airports))
      {
         for (final AirportData airportData : this.airports)
         {
            if (airportData != null && StringUtils.isNotBlank(airportData.getId()))
            {
               hashBuilder.append(airportData.getId());
               if (airportData.getChildren() != null)
               {
                  for (final String childCode : airportData.getChildren())
                  {
                     if (StringUtils.isNotBlank(childCode))
                     {
                        hashBuilder.append(childCode);
                     }
                  }
               }
            }
         }
      }
      if (CollectionUtils.isNotEmpty(this.childrenAge))
      {
         for (final Integer childAge : this.childrenAge)
         {
            if (childAge != null)
            {
               hashBuilder.append(childAge.intValue());
            }
         }
      }

      if (this.rooms != null && CollectionUtils.isNotEmpty(this.rooms))
      {
         hashBuilder.append(rooms.size());
      }

      // Generating unique based on prime number combination
      return NUM_PRIME_31 * NUM_PRIME_17 + hashBuilder.toHashCode();
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
    * @return the defaultDuration
    */
   public String getDefaultDuration()
   {
      return defaultDuration;
   }

   /**
    * @param defaultDuration the defaultDuration to set
    */
   public void setDefaultDuration(final String defaultDuration)
   {
      this.defaultDuration = defaultDuration;
   }

   /**
    * @return the primaryUnits
    */
   public List<UnitData> getPrimaryUnits()
   {
      return primaryUnits;
   }

   /**
    * @param primaryUnits the primaryUnits to set
    */
   public void setPrimaryUnits(final List<UnitData> primaryUnits)
   {
      this.primaryUnits = primaryUnits;
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
    * @return the posibilityOfDuplicateUnit
    */
   public boolean isPosibilityOfDuplicateUnit()
   {
      return posibilityOfDuplicateUnit;
   }

   /**
    * @param posibilityOfDuplicateUnit the posibilityOfDuplicateUnit to set
    */
   public void setPosibilityOfDuplicateUnit(final boolean posibilityOfDuplicateUnit)
   {
      this.posibilityOfDuplicateUnit = posibilityOfDuplicateUnit;
   }

   /**
    * @return the sourceAirportList
    */
   public List<AirportData> getSourceAirportList()
   {
      return sourceAirportList;
   }

   /**
    * @param sourceAirportList the sourceAirportList to set
    */
   public void setSourceAirportList(final List<AirportData> sourceAirportList)
   {
      this.sourceAirportList = sourceAirportList;
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

   @Override
   public Object clone() throws CloneNotSupportedException
   {
      return super.clone();
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
    * @return the anyWhereSearch
    */
   public boolean isAnyWhereSearch()
   {
      return anyWhereSearch;
   }

   /**
    * @param anyWhereSearch the anyWhereSearch to set
    */
   public void setAnyWhereSearch(final boolean anyWhereSearch)
   {
      this.anyWhereSearch = anyWhereSearch;
   }

   /**
    * @return the shortlistFirst
    */
   public int getShortlistFirst()
   {
      return shortlistFirst;
   }

   /**
    * @param shortlistFirst the shortlistFirst to set
    */
   public void setShortlistFirst(final int shortlistFirst)
   {
      this.shortlistFirst = shortlistFirst;
   }

   /**
    * @return the shortlistOffset
    */
   public int getShortlistOffset()
   {
      return shortlistOffset;
   }

   /**
    * @param shortlistOffset the shortlistOffset to set
    */
   public void setShortlistOffset(final int shortlistOffset)
   {
      this.shortlistOffset = shortlistOffset;
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

   /**
    * @return the smerchDuration
    */
   public String getSmerchDuration()
   {
      return smerchDuration;
   }

   @Override
   public boolean equals(final Object obj)
   {
      return super.equals(obj);
   }

   /**
    * @param smerchDuration the smerchDuration to set
    */
   public void setSmerchDuration(final String smerchDuration)
   {
      this.smerchDuration = smerchDuration;
   }

}
