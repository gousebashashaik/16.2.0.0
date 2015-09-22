/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author Manju.ts
 *
 */
public class MainFilterRequest
{

   private SliderRequest budgettotal;

   private SliderRequest budgetpp;

   private SliderRequest temperature;

   private SliderRequest fcRating;

   private List<FilterRequest> departurePoints;

   private List<FilterRequest> inslots;

   private List<FilterRequest> outslots;

   private List<FilterRequest> features;

   private List<FilterRequest> holidayType;

   private List<FilterRequest> bestfor;

   // accommtype adding
   private List<FilterRequest> accommodationType;

   // accommtype adding

   private List<FilterRequest> boardBasis;

   private List<FilterRequest> holidayOperator;

   private List<FilterRequest> destinations;

   private List<FilterRequest> collections;

   private List<FilterRequest> departureAirport;

   private List<FilterRequest> departurePorts;

   private List<FilterRequest> cruiseDestinations;

   private List<FilterRequest> cruiseFeatures;

   private List<FilterRequest> cruiseStayDestinations;

   private List<FilterRequest> cruiseShips;

   private List<FilterRequest> allInclusive;

   private List<FilterRequest> sailingDate;

   private SliderRequest tripadvisorrating;

   /**
    * Default constructor required by Jackson
    */
   public MainFilterRequest()
   {

   }

   /**
    * @return the collections
    */
   public List<FilterRequest> getCollections()
   {
      return collections;
   }

   /**
    * @param collections the collections to set
    */
   public void setCollections(final List<FilterRequest> collections)
   {
      this.collections = collections;
   }

   /**
    * @return the budgettotal
    */

   public SliderRequest getBudgettotal()
   {
      return budgettotal;
   }

   /**
    * @param budgettotal the budgettotal to set
    */
   public void setBudgettotal(final SliderRequest budgettotal)
   {
      this.budgettotal = budgettotal;
   }

   /**
    * @return the temperature
    */
   public SliderRequest getTemperature()
   {
      return temperature;
   }

   /**
    * @param temperature the temperature to set
    */
   public void setTemperature(final SliderRequest temperature)
   {
      this.temperature = temperature;
   }

   /**
    * @return the accommodationType
    */
   public List<FilterRequest> getAccommodationType()
   {
      return accommodationType;
   }

   /**
    * @param accommodationType the accommodationType to set
    */
   public void setAccommodationType(final List<FilterRequest> accommodationType)
   {
      this.accommodationType = accommodationType;
   }

   /**
    * @return the fcRating
    */
   public SliderRequest getFcRating()
   {
      return fcRating;
   }

   /**
    * @param fcRating the fcRating to set
    */
   public void setFcRating(final SliderRequest fcRating)
   {
      this.fcRating = fcRating;
   }

   /**
    * @return the tripadvisorrating
    */
   public SliderRequest getTripadvisorrating()
   {
      return tripadvisorrating;
   }

   /**
    * @param tripadvisorrating the tripadvisorrating to set
    */
   public void setTripadvisorrating(final SliderRequest tripadvisorrating)
   {
      this.tripadvisorrating = tripadvisorrating;
   }

   /**
    * @return the departurePoints
    */
   public List<FilterRequest> getDeparturePoints()
   {
      return departurePoints;
   }

   /**
    * @param departurePoints the departurePoints to set
    */
   public void setDeparturePoints(final List<FilterRequest> departurePoints)
   {
      this.departurePoints = departurePoints;
   }

   /**
    * @return the inslots
    */
   public List<FilterRequest> getInslots()
   {
      return inslots;
   }

   /**
    * @param inslots the inslot to set
    */
   public void setInslot(final List<FilterRequest> inslots)
   {
      this.inslots = inslots;
   }

   /**
    * @return the outslots
    */
   public List<FilterRequest> getOutslots()
   {
      return outslots;
   }

   /**
    * @param outslot the outslots to set
    */
   public void setOutslot(final List<FilterRequest> outslots)
   {
      this.outslots = outslots;
   }

   /**
    * @return the features
    */
   public List<FilterRequest> getFeatures()
   {
      return features;
   }

   /**
    * @param features the features to set
    */
   public void setFeatures(final List<FilterRequest> features)
   {
      this.features = features;
   }

   /**
    * @return the holidayType
    */
   public List<FilterRequest> getHolidayType()
   {
      return holidayType;
   }

   /**
    * @param holidayType the holidayType to set
    */
   public void setHolidayType(final List<FilterRequest> holidayType)
   {
      this.holidayType = holidayType;
   }

   /**
    * @return the bestfor
    */
   public List<FilterRequest> getBestfor()
   {
      return bestfor;
   }

   /**
    * @param bestfor the bestfor to set
    */
   public void setBestfor(final List<FilterRequest> bestfor)
   {
      this.bestfor = bestfor;
   }

   /**
    * @return the destinations
    */
   public List<FilterRequest> getDestinations()
   {
      return destinations;
   }

   /**
    * @param destinations the destinations to set
    */
   public void setDestinations(final List<FilterRequest> destinations)
   {
      this.destinations = destinations;
   }

   /**
    * @return the budgetpp
    */
   public SliderRequest getBudgetpp()
   {
      return budgetpp;
   }

   /**
    * @param budgetpp the budgetpp to set
    */
   public void setBudgetpp(final SliderRequest budgetpp)
   {
      this.budgetpp = budgetpp;
   }

   /**
    * @return the boardBasis
    */
   public List<FilterRequest> getBoardBasis()
   {
      return boardBasis;
   }

   /**
    * @param boardBasis the boardBasis to set
    */
   public void setBoardBasis(final List<FilterRequest> boardBasis)
   {
      this.boardBasis = boardBasis;
   }

   /**
    * @return the holidayOperator
    */
   public List<FilterRequest> getHolidayOperator()
   {
      return holidayOperator;
   }

   /**
    * @param holidayOperator the holidayOperator to set
    */
   public void setHolidayOperator(final List<FilterRequest> holidayOperator)
   {
      this.holidayOperator = holidayOperator;
   }

   /**
    * @return the departureAirport
    */
   public List<FilterRequest> getDepartureAirport()
   {
      return departureAirport;
   }

   /**
    * @param departureAirport the departureAirport to set
    */
   public void setDepartureAirport(final List<FilterRequest> departureAirport)
   {
      this.departureAirport = departureAirport;
   }

   /**
    * @return the departurePorts
    */
   public List<FilterRequest> getDeparturePorts()
   {
      return departurePorts;
   }

   /**
    * @param departurePorts the departurePorts to set
    */
   public void setDeparturePorts(final List<FilterRequest> departurePorts)
   {
      this.departurePorts = departurePorts;
   }

   /**
    * @return the cruiseDestinations
    */
   public List<FilterRequest> getCruiseDestinations()
   {
      return cruiseDestinations;
   }

   /**
    * @param cruiseDestinations the cruiseDestinations to set
    */
   public void setCruiseDestinations(final List<FilterRequest> cruiseDestinations)
   {
      this.cruiseDestinations = cruiseDestinations;
   }

   /**
    * @return the cruiseFeatures
    */
   public List<FilterRequest> getCruiseFeatures()
   {
      return cruiseFeatures;
   }

   /**
    * @param cruiseFeatures the cruiseFeatures to set
    */
   public void setCruiseFeatures(final List<FilterRequest> cruiseFeatures)
   {
      this.cruiseFeatures = cruiseFeatures;
   }

   /**
    * @return the cruiseStayDestinations
    */
   public List<FilterRequest> getCruiseStayDestinations()
   {
      return cruiseStayDestinations;
   }

   /**
    * @param cruiseStayDestinations the cruiseStayDestinations to set
    */
   public void setCruiseStayDestinations(final List<FilterRequest> cruiseStayDestinations)
   {
      this.cruiseStayDestinations = cruiseStayDestinations;
   }

   /**
    * @return the cruiseShips
    */
   public List<FilterRequest> getCruiseShips()
   {
      return cruiseShips;
   }

   /**
    * @param cruiseShips the cruiseShips to set
    */
   public void setCruiseShips(final List<FilterRequest> cruiseShips)
   {
      this.cruiseShips = cruiseShips;
   }

   /**
    * @param inslots the inslots to set
    */
   public void setInslots(final List<FilterRequest> inslots)
   {
      this.inslots = inslots;
   }

   /**
    * @param outslots the outslots to set
    */
   public void setOutslots(final List<FilterRequest> outslots)
   {
      this.outslots = outslots;
   }

   /**
    * @return the allInclusive
    */
   public List<FilterRequest> getAllInclusive()
   {
      return allInclusive;
   }

   /**
    * @param allInclusive the allInclusive to set
    */
   public void setAllInclusive(final List<FilterRequest> allInclusive)
   {
      this.allInclusive = allInclusive;
   }

   /**
    * @return the sailingDate
    */
   public List<FilterRequest> getSailingDate()
   {
      return sailingDate;
   }

   /**
    * @param sailingDate the sailingDate to set
    */
   public void setSailingDate(final List<FilterRequest> sailingDate)
   {
      this.sailingDate = sailingDate;
   }

}
