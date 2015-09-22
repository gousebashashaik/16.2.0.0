/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import static uk.co.portaltech.commons.DateUtils.format;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;


public class DeepLinkRequestTestData {

	/**  minNoOfChild to test.*/
		 private static final int MIN_NO_OF_CHILD = 8;

		   /** Holds the value for maxChildAge.*/

		 private static final int MAX_CHILD_AGE = 17;

		   /** Holds the value for minNoOfAdult.*/

		 private static final int MIN_NO_OF_ADULT = 2;

		   /** Holds the value for minChildAge.*/

		 private static final int MIN_CHILD_AGE = 0;

		   /** Holds the value for maxNoOfChild.*/

		 private static final int MAX_NO_OF_CHILD = 8;

		   /** Holds the value for maxNoOfAdult.*/

		 private static final int MAX_NO_OF_ADULT = 9;

		 private static final int INFANT_AGE=2;
		 private static final int CHILD_AGE=2;
		 private static final int MAX_PAX_CON=9;


	  @SuppressWarnings({ "unused", "boxing" })
	   public SearchPanelComponentModel createDummyFacilityModel()
	   {
		  SearchPanelComponentModel   searchPanelComponentModel = new SearchPanelComponentModel();

	      searchPanelComponentModel.setMaxChildAge(MAX_CHILD_AGE);
	      searchPanelComponentModel.setMinNoOfChild(MIN_NO_OF_CHILD);
	      searchPanelComponentModel.setMinNoOfAdult(MIN_NO_OF_ADULT);
	      searchPanelComponentModel.setMinChildAge(MIN_CHILD_AGE);
	      searchPanelComponentModel.setMaxNoOfChild(MAX_NO_OF_CHILD);
	      searchPanelComponentModel.setMaxNoOfAdult(MAX_NO_OF_ADULT);
	      searchPanelComponentModel.setInfantAge(INFANT_AGE);
	      searchPanelComponentModel.setMaxPaxConfig(MAX_PAX_CON);

	      return searchPanelComponentModel;
	   }

	  public DeepLinkRequestData searchRequestData(){
		  DeepLinkRequestData deepLinkRequestData = new DeepLinkRequestData();
		  return deepLinkRequestData;
	  }

	  public DeepLinkRequestData searchRequestDataWithoutWhen(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();

		  deepLinkRequestData.setNoOfAdults(2);
		  return deepLinkRequestData;

	  }
	  public DeepLinkRequestData searchRequestDataWithEmptyWhen(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();
		  deepLinkRequestData.setWhen(null);
		  deepLinkRequestData.setNoOfAdults(2);
		  return deepLinkRequestData;

	  }

	  public DeepLinkRequestData searchRequestDataWithInvalidWhen(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();
		  deepLinkRequestData.setWhen("20-12-1203");
		  deepLinkRequestData.setNoOfAdults(2);
		  return deepLinkRequestData;

	  }

	  public DeepLinkRequestData searchRequestDataWithWhenLessThanTodayDate(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();
		  deepLinkRequestData.setWhen("12-01-2013");
		  deepLinkRequestData.setNoOfAdults(2);
		  return deepLinkRequestData;

	  }
	  
	  public DeepLinkRequestData searchRequestDataWithTamperedDataInWhen(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();
		  deepLinkRequestData.setWhen("2012-12-12<script>alert('hi');</script>");
		  deepLinkRequestData.setNoOfAdults(2);
		  return deepLinkRequestData;

	  }

	  public DeepLinkRequestData searchRequestDataWithPartyComposition(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();

		  deepLinkRequestData.setWhen(validDate());
		  deepLinkRequestData.setNoOfAdults(19);
		  deepLinkRequestData.setNoOfChildren(19);
		  deepLinkRequestData.setNoOfSeniors(9);
		 
		  return deepLinkRequestData;

	  }
	  public DeepLinkRequestData searchRequestDataWithPartyCompositionForMaxAdults(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();

		  deepLinkRequestData.setWhen(validDate());
		  deepLinkRequestData.setNoOfAdults(10);
		
		  return deepLinkRequestData;

	  }
	  public DeepLinkRequestData searchRequestDataWithPartyCompositionForMaxChildren(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();

		  deepLinkRequestData.setWhen(validDate());
		  deepLinkRequestData.setNoOfAdults(2);
		  deepLinkRequestData.setNoOfChildren(10);
		
		  return deepLinkRequestData;

	  }
	  @SuppressWarnings("boxing")
	public DeepLinkRequestData searchRequestDataWithPartyCompositionForInfants(){
		  DeepLinkRequestData deepLinkRequestData = searchRequestDataWithAirportAndUnits();

		  deepLinkRequestData.setWhen(validDate());
		  deepLinkRequestData.setNoOfAdults(1);
		  deepLinkRequestData.setNoOfChildren(2);
		  List<Integer> childAge = new ArrayList<Integer>();
		  childAge.add(CHILD_AGE);
		  childAge.add(1);
		  deepLinkRequestData.setChildrenAge(childAge);
		  return deepLinkRequestData;

	  }




	private DeepLinkRequestData searchRequestDataWithAirportAndUnits() {
		DeepLinkRequestData deepLinkRequestData =  searchRequestData();
		deepLinkRequestData.setAirports(poupluateAirportData());
		deepLinkRequestData.setUnits(populateUnitData());
		return deepLinkRequestData;
	}


	  private String validDate(){

		  LocalDate localdate =new LocalDate();
		return ( format(localdate.plusDays(2))) ;
	  }

	  public  List<AirportData> poupluateAirportData()
	 {
		 Collection<String> group =   new ArrayList<String>();
		 group.add(new String("SE"));
		 group.add(new String("LN"));
		 AirportData ad= new AirportData("LGW", "LONDON GATWICK") ;
		  List<AirportData> lists= new ArrayList();
		   group =   new ArrayList<String>();
			 group.add(new String("LUT"));
			 group.add(new String("STN"));

			 AirportData  ad2= new AirportData("LN", "ANY LONDON") ;

			 lists.add((ad));
		  lists.add((ad2));
		 return (lists);

	 }

	  public List<UnitData> populateUnitData(){

		 UnitData  ud1 = new UnitData("ESP", "Spain", "COUNTRY");
		 UnitData  ud2 = new UnitData("004965", "Eurosalou Hotel  Spa", "HOTEL");
		  List<UnitData> lists= new ArrayList();
		  lists.add((ud1));
		  lists.add((ud2));
		 return (lists);

	 }
	  public DeepLinkRequestData populateAirportRoute()
	  {
		  DeepLinkRequestData deepLinkRequestData =  searchRequestData();
	  		 AirportData ad= new AirportData("LGW", "LONDON GATWICK") ;
	  		 UnitData  ud2 = new UnitData("000127", "Menorca", "DESTINATION");
	  		 List<UnitData> units= new ArrayList();
	  		units.add(ud2);
	  		
	  		 List<AirportData> airports= new ArrayList();
	  		airports.add(ad);
	  		
	  		deepLinkRequestData.setAirports(airports);
	  		deepLinkRequestData.setUnits(units);
	  		return deepLinkRequestData;
	  }
	  
	  public DeepLinkRequestData populateAirportToUnitRoute()
	  {
		  DeepLinkRequestData deepLinkRequestData =  searchRequestData();
		  AirportData  ad2= new AirportData("SC", "ANY SCOTLAND") ;
	  		 UnitData  ud2 = new UnitData("IND", "India", "COUNTRY");
	  		 List<UnitData> units= new ArrayList();
	  		units.add(ud2);
	  		
	  		 List<AirportData> airports= new ArrayList();
	  		airports.add(ad2);
	  		
	  		deepLinkRequestData.setAirports(airports);
	  		deepLinkRequestData.setUnits(units);
	  		return deepLinkRequestData;
	  }
	  
	  	public DeepLinkRequestData deepLinkRequestDataWithValidAirportRoutes()
		{
	  		 DeepLinkRequestData deepLinkRequestData = populateAirportRoute();
	  		deepLinkRequestData.setWhen("09-04-2013");
	  		return deepLinkRequestData;
	  		
	  		
		}
		
		public DeepLinkRequestData deepLinkRequestDataWithInValidAirportRoutes()
		{
			 DeepLinkRequestData deepLinkRequestData = populateAirportRoute();
		  		deepLinkRequestData.setWhen("08-04-2013");
		  		return deepLinkRequestData;
		}
		
		public DeepLinkRequestData deepLinkRequestDataWithInValidAirportToUnitRoute()
		{
			 DeepLinkRequestData deepLinkRequestData = populateAirportToUnitRoute();
		  		deepLinkRequestData.setWhen("09-04-2013");
		  		return deepLinkRequestData;
		}
		
		public DeepLinkRequestData deepLinkRequestDataWithValidAirportToUnitRoute()
		{
			 DeepLinkRequestData deepLinkRequestData =  searchRequestData();
			  AirportData  ad2= new AirportData("SC", "ANY SCOTLAND") ;
		  		 UnitData  ud2 = new UnitData("000127", "Menorca", "DESTINATION");
		  		 List<UnitData> units= new ArrayList();
		  		units.add(ud2);
		  		
		  		 List<AirportData> airports= new ArrayList();
		  		airports.add(ad2);
		  		
		  		deepLinkRequestData.setAirports(airports);
		  		deepLinkRequestData.setUnits(units);
		  		deepLinkRequestData.setWhen("15-05-2013");
		  		return deepLinkRequestData;
		}
		
		public DeepLinkRequestData deepLinkRequestDataWithValidUnitToDateRoutes()
		{
			 DeepLinkRequestData deepLinkRequestData =  searchRequestData();
		  		 UnitData  ud2 = new UnitData("000242", "Ibiza", "DESTINATION");
		  		 List<UnitData> units= new ArrayList();
		  		units.add(ud2);
		  		
		  		 List<AirportData> airports= new ArrayList();
		  		
		  		deepLinkRequestData.setAirports(airports);
		  		deepLinkRequestData.setUnits(units);
		  		deepLinkRequestData.setWhen("29-06-2013");
		  		return deepLinkRequestData;
		}
		
		public DeepLinkRequestData deepLinkRequestDataWithInValidUnitToDateRoutes()
		{
			DeepLinkRequestData deepLinkRequestData =  searchRequestData();
	  		 UnitData  ud2 = new UnitData("000242", "Ibiza", "DESTINATION");
	  		 List<UnitData> units= new ArrayList();
	  		units.add(ud2);
	  		
	  		 List<AirportData> airports= new ArrayList();
	  		
	  		deepLinkRequestData.setAirports(airports);
	  		deepLinkRequestData.setUnits(units);
	  		deepLinkRequestData.setWhen("25-06-2013");
	  		return deepLinkRequestData;
		}
		
		public DeepLinkRequestData deepLinkRequestDataWithValidUnitToAirportRoutes()
		{
			 DeepLinkRequestData deepLinkRequestData =  searchRequestData();
		  		 UnitData  ud2 = new UnitData("0001982", "Ibiza", "HOTEL");
		  		 AirportData ad= new AirportData("SC", "ANY SCOTLAND") ;
		  		 List<UnitData> units= new ArrayList();
		  		units.add(ud2);
		  		
		  		 List<AirportData> airports= new ArrayList();
		  		airports.add(ad);
		  		
		  		deepLinkRequestData.setAirports(airports);
		  		deepLinkRequestData.setUnits(units);
		  		deepLinkRequestData.setWhen("10-05-2013");
		  		return deepLinkRequestData;
		}
		
		
		
	

}
