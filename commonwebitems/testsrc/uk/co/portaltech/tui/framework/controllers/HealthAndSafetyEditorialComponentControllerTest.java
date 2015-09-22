/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author niranjani.r
 * 
 */
public class HealthAndSafetyEditorialComponentControllerTest
{

	@Mock
	private LocationFacade facade;

	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private CategoryService categoryService;
	@Mock
	private CatalogVersionModel model;
	@Mock
	private LocationService tuiLocationService;

	@Mock
	private SessionService sessionService;

	private final LocationModel location = new LocationModel();
	private final LocationModel location1 = new LocationModel();
	private final LocationData locationData = new LocationData();

	private List<String> brandPks = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
		brandPks = brandDetails.getRelevantBrands();

		MockitoAnnotations.initMocks(this);
		final CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		model.setCatalog(catalog);
		getDummyCountryLocationModel();
		getDummySubLocationModel();
		getDummyLocationDataForCountry();


	}

	private LocationModel getDummyCountryLocationModel()
	{
		location.setCode("ESP");
		location.setCatalogVersion(model);
		location.setType(LocationType.COUNTRY);
		return location;

	}

	private LocationModel getDummySubLocationModel()
	{
		location1.setCode("001");
		location1.setCatalogVersion(model);
		location1.setType(LocationType.REGION);
		return location1;

	}

	private LocationData getDummyLocationDataForCountry()
	{
		locationData.setCode("ESP");
		locationData.setName("SPAIN");
		final List<Object> passportAndVisaInfo = new ArrayList<Object>();
		passportAndVisaInfo.add("healthAndSafety Information for spain ");
		final Map<String, List<Object>> featureValues = new HashMap<String, List<Object>>();
		featureValues.put("healthAndSafety", passportAndVisaInfo);
		locationData.putFeatureCodesAndValues(featureValues);
		return locationData;

	}


	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.framework.controllers.PassportsAndVisasComponentController#viewPassportsAndVisasComponent()}
	 * .
	 */
	@Test
	public void testViewPassportsAndVisasComponentForCountry()
	{
		when((categoryService.getCategoryForCode("ESP"))).thenReturn(location);
		when(tuiLocationService.getLocationForItem(location, LocationType.COUNTRY, brandPks)).thenReturn(location);
		when(facade.getHealthAndSafetyEditorial(location.getCode())).thenReturn(locationData);
		assertThat(locationData.getFeatureCodesAndValues().get("healthAndSafety").get(0).toString(),
				is("healthAndSafety Information for spain "));

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.framework.controllers.PassportsAndVisasComponentController#viewPassportsAndVisasComponent()}
	 * .
	 */
	@Test
	public void testViewPassportsAndVisasComponentForSubCategories()
	{
		when((categoryService.getCategoryForCode("001"))).thenReturn(location1);
		when(tuiLocationService.getLocationForItem(location1, LocationType.COUNTRY, brandPks)).thenReturn(location1);
		when(facade.getHealthAndSafetyEditorial(location1.getCode())).thenReturn(locationData);
		assertThat(locationData.getFeatureCodesAndValues().get("healthAndSafety").get(0).toString(),
				is("healthAndSafety Information for spain "));

	}

}
