/**
 *
 */
package uk.co.portaltech.tui.services;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.DurationHaulItemModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.portaltech.tui.web.view.data.AirportData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
/**
 * @author deepakkumar.k
 *
 */
@IntegrationTest
public class DurationPriorityRuleServiceTest extends ServicelayerTransactionalTest
{/*
	@Resource
	private DurationPriorityRuleService durationPriorityRuleService ;
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
    private SearchResultsRequestData srd1,srd2,srd3,srd4;
	private HolidaySearchContext  context1,context2,context3,context4;
	private CatalogModel catalogModel;
	private CatalogVersionModel catalogVersion;
	private LocationModel destination1,destination2,country,region,resort,prioritycountry,priorityregion,prioritydestination;
	private AirportModel airportmodel1,airportmodel2,airportmodel3;
    private DurationHaulItemModel shorthaulmodel,longhaulmodel;
    private static final int SEVEN=7;
    private static final int FOURTEEN=14;

	*//**
	 * Test method for {@link uk.co.portaltech.tui.services.DurationPriorityRuleService#applyDurationPriorityRules(uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext, uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)}.
	 *//*

@SuppressWarnings("javadoc")
@Before
public void setUp()
{

             setUpCatalogVersion();
             departureAirportForAll();
             priorityCountries();
             modelSetForShortHaul();
             duratonHaulModelForShortHaul();
             modelSetForLongHaul();
             duratonHaulModelForLongtHaul();
             inputForDefaulCondition();
             inputForPriorityCountries();
             inputForShortHaul();
             inputForLongHaul();


}

	*//**
	 * input parameter for long haul
	 *//*
	public void inputForLongHaul() {
               context4= new HolidaySearchContext();
               srd4 = new SearchResultsRequestData();
               List<AirportData> airports4 = new ArrayList<AirportData>();
               AirportData a11 = new AirportData();
               a11.setId("LTN");
               a11.setName("LONDON GATWICK");
               a11.setAvailable(true);
               airports4.add(a11);
               srd4.setAirports(airports4);

                     List<UnitData> units4 = new ArrayList<UnitData>();
                     UnitData u44 = new UnitData("000924", "Majoraca", "Destination");
                     units4.add(u44);
                     srd4.setUnits(units4);
	}




	*//**
	 * input parameter for short haul
	 *//*
  public void inputForShortHaul() {
               context3= new HolidaySearchContext();
               srd3 = new SearchResultsRequestData();
               List<AirportData> airports3 = new ArrayList<AirportData>();
               AirportData a1 = new AirportData();
               a1.setId("LTN");
               a1.setName("LONDON LUTON");
               a1.setAvailable(true);
               airports3.add(a1);
               srd3.setAirports(airports3);
                         List<UnitData> units3 = new ArrayList<UnitData>();
                         UnitData u12 = new UnitData("000923", "Rhodes_Destination", "Destination");
                         units3.add(u12);
                         srd3.setUnits(units3);
	}


	*//**
	 * input parameter for priority country.
	 *//*
	public void inputForPriorityCountries() {
		       context2= new HolidaySearchContext();
		       srd2 = new SearchResultsRequestData();
		       List<AirportData> airports2 = new ArrayList<AirportData>();
		                  List<UnitData> units2 = new ArrayList<UnitData>();
		                  UnitData u3 = new UnitData("GRC", "Greece", "Country");
		                  units2.add(u3);
		                  srd2.setAirports(airports2);
		                  srd2.setUnits(units2);
	}

	*//**
	 * input parameter for default condition.
	 *//*
	public void inputForDefaulCondition() {
		     context1= new HolidaySearchContext();
		     srd1 = new SearchResultsRequestData();
		     List<AirportData> airports1 = new ArrayList<AirportData>();
		     srd1.setAirports(airports1);
		     List<UnitData> units1 = new ArrayList<UnitData>();
		     srd1.setUnits(units1);
	}

	*//**
	 * duration haul item model   for long haul.
	 *//*
	public void duratonHaulModelForLongtHaul() {
		longhaulmodel= modelService.create(DurationHaulItemModel.class);
		longhaulmodel.setArrival("000924");
		longhaulmodel.setDeparture("LTN");
		longhaulmodel.setHaul("LH");
		longhaulmodel.setCode("bcd");
		longhaulmodel.setCatalogVersion(catalogVersion);
	    modelService.save(longhaulmodel);
	}

	*//**
	 * setting model in model service  for LONG haul.
	 *//*
	public void modelSetForLongHaul() {



   airportmodel3 = modelService.create(AirportModel.class);
   airportmodel3.setCode("000924");
   airportmodel3.setName("Majoraca");
   airportmodel3.setCatalogVersion(catalogVersion);
   modelService.save(airportmodel3);

 Collection<AirportModel>airportforlonghaul= new ArrayList<AirportModel>();
 airportforlonghaul.add(airportmodel3);

		     destination2 = modelService.create(LocationModel.class);
		   	 destination2.setCode("000924");
		   	 destination2.setType(LocationType.DESTINATION);
		   	 destination2.setName("Majoraca");
		     destination2.setAirports(airportforlonghaul);

		            List<CategoryModel> resorts1= new ArrayList<CategoryModel>();
		            resorts1.add(resort);
                    List<CategoryModel> regions1= new ArrayList<CategoryModel>();
		            regions1.add(region);

                    List<CategoryModel> countries1= new ArrayList<CategoryModel>();
		            countries1.add(country);


		   destination2.setCategories(resorts1);
		   destination2.setSupercategories(regions1);
		   destination2.setSupercategories(countries1);
		   destination2.setCatalogVersion(catalogVersion);
		   modelService.save(destination2);

	}

	*//**
	 * DurationHaulItemModel for short haul.
	 *//*
	public void duratonHaulModelForShortHaul() {
		   shorthaulmodel=  modelService.create(DurationHaulItemModel.class);
		   shorthaulmodel.setArrival("000923");
		   shorthaulmodel.setDeparture("LTN");
		   shorthaulmodel.setHaul("SH");
		   shorthaulmodel.setCode("abc");
		   shorthaulmodel.setCatalogVersion(catalogVersion);
		   modelService.save(shorthaulmodel);
	}

	*//**
	 * setting model in model service  for short haul.
	 *//*
	public void modelSetForShortHaul() {
		airportmodel2 = modelService.create(AirportModel.class);
	    airportmodel2.setCode("000923");//
	    airportmodel2.setName("Rhodes_Destination");
	    airportmodel2.setCatalogVersion(catalogVersion);
	    modelService.save(airportmodel2);
	    Collection<AirportModel>airportforshorthaul= new ArrayList<AirportModel>();
		airportforshorthaul.add(airportmodel2);

     country = modelService.create(LocationModel.class);
 	 country.setCode("ESP");
 	 country.setType(LocationType.COUNTRY);
     country.setName("spain");
     country.setCatalogVersion(catalogVersion);

     region = modelService.create(LocationModel.class);
 	 region.setCode("L24960");
 	 region.setType(LocationType.REGION);
 	 region.setName("South Aegean Islands");
 	 region.setCatalogVersion(catalogVersion);

 	    destination1 = modelService.create(LocationModel.class);
 	   	destination1.setCode("000923");
 	   	destination1.setType(LocationType.DESTINATION);
 	   	destination1.setName("Rhodes_Destination");
 	   	destination1.setAirports(airportforshorthaul);

	 resort= modelService.create(LocationModel.class);
	 resort.setCode("000927");
	 resort.setType(LocationType.RESORT);
      resort.setName("Kalithea");
	 resort.setCatalogVersion(catalogVersion);


    List<CategoryModel> resorts= new ArrayList<CategoryModel>();
    resorts.add(resort);

      List<CategoryModel> regions= new ArrayList<CategoryModel>();
	  regions.add(region);
      modelService.save(region);

	    List<CategoryModel> countries= new ArrayList<CategoryModel>();
	    countries.add(country);
	    modelService.save(country);

	  destination1.setCategories(resorts);
	  destination1.setSupercategories(regions);
	  destination1.setSupercategories(countries);
	  destination1.setCatalogVersion(catalogVersion);
	  modelService.save(destination1);
	}

	*//**
	 * setting model in model service  for priority country.
	 *//*
	public void priorityCountries() {
		 prioritycountry = modelService.create(LocationModel.class);
		 prioritycountry.setCode("GRC");
		 prioritycountry.setType(LocationType.COUNTRY);
		 prioritycountry.setName("Greece");
		 prioritycountry.setCatalogVersion(catalogVersion);
		 priorityregion = modelService.create(LocationModel.class);
		 priorityregion.setCode("L24961");
		 priorityregion.setType(LocationType.REGION);
		 priorityregion.setName("South Aegean Island");
		 priorityregion.setCatalogVersion(catalogVersion);

		 prioritydestination = modelService.create(LocationModel.class);
		 prioritydestination.setCode("000926");
		 prioritydestination.setType(LocationType.DESTINATION);
		 prioritydestination.setName("Rhodes");


		 List<CategoryModel> priorityregions= new ArrayList<CategoryModel>();
		 priorityregions.add(priorityregion);
		 modelService.save(priorityregion);

		 List<CategoryModel> prioritycountries= new ArrayList<CategoryModel>();
		 prioritycountries.add(prioritycountry);
		 modelService.save(prioritycountry);


		  prioritydestination.setSupercategories(priorityregions);
		  prioritydestination.setSupercategories(prioritycountries);
		  prioritydestination.setCatalogVersion(catalogVersion);
		  modelService.save(prioritydestination);
	}

	*//**
	 * Departure Airport for all condition.
	 *//*
	public void departureAirportForAll() {
		airportmodel1 = modelService.create(AirportModel.class);
		airportmodel1.setCode("LTN");
		airportmodel1.setName("LONDON GATWICK");
		airportmodel1.setCatalogVersion(catalogVersion);
		modelService.save(airportmodel1);
	}

	*//**
	 * setting Catalog version.
	 *//*
	public void setUpCatalogVersion() {
		catalogModel = new CatalogModel();
		catalogModel.setId("fc_catalog");
		catalogVersion = new CatalogVersionModel();
		catalogVersion.setVersion("Online");
		modelService.save(catalogModel);
		catalogVersion.setCatalog(catalogModel);
		modelService.save(catalogVersion);
	}


	*//**
	 * testing DurationPriorityRules for default condition.
	 *//*
@SuppressWarnings("boxing")
@Test
	public void testApplyDurationPriorityRulesForDefault()
   {
		durationPriorityRuleService.applyDurationPriorityRules( context1, srd1);
		assertEquals("7,14,10,11", context1.getDurationPriorityOrder());
		assertEquals(SEVEN , context1.getDuration());

	}
*//**
 * testing DurationPriorityRules for priority country.
 *//*
	@SuppressWarnings("boxing")
@Test
	public void testApplyDurationPriorityRulesForPriorityCountries()
   {
		durationPriorityRuleService.applyDurationPriorityRules( context2, srd2);
		assertEquals("14,21,10,7", context2.getDurationPriorityOrder());
		assertEquals(FOURTEEN , context2.getDuration());

	}
*//**
 * testing DurationPriorityRules for Short Haul .
 *//*
	@SuppressWarnings("boxing")
@Test
	public void testApplyDurationPriorityRulesforShortHaul()
   {
		durationPriorityRuleService.applyDurationPriorityRules( context3, srd3);
		assertEquals("7,14,21,10", context3.getDurationPriorityOrder());
		assertEquals(SEVEN , context3.getDuration());

	}

/**
 * testing DurationPriorityRules for Long Haul.

	@SuppressWarnings("boxing")
@Test
	public void testApplyDurationPriorityRulesForLongHaul()
   {
		durationPriorityRuleService.applyDurationPriorityRules( context4, srd4);
		assertEquals("14,21,10,7", context4.getDurationPriorityOrder());
		assertEquals(FOURTEEN , context4.getDuration());

	}
*/
}
