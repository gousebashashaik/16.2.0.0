/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.services.FacilityService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.converters.FacilityConverter;
import uk.co.portaltech.tui.converters.FacilityOption;
import uk.co.portaltech.tui.facades.impl.DefaultFacilityFacade;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 * @author kavita.na
 * 
 */
public class DefaultFacilityFacadeTest {
	@InjectMocks
	private final DefaultFacilityFacade facilityFacade = new DefaultFacilityFacade();

	@Mock
	private FacilityService facilityService;

	@Mock
	private FacilityConverter facilityConverter;

	@Mock
	private CMSSiteService siteService;

	@Mock
	private SessionService sessionService;

	@Mock
	private ConfigurablePopulator<FacilityModel, FacilityViewData, FacilityOption> facilityConfiguredPopulator;

	private FacilityModel facilityModel, facilityModel1, facilityModel2;
	private static final String FACILITY_CODE = "260";
	private static final String FACILITY_CODE1 = "386";
	private static final String FACILITY_CODE2 = "496";
	private static final String FACILITY_NAME = "Barbecue restaurant";
	private static final String FACILITY_NAME1 = "Watersports";
	private static final String FACILITY_NAME2 = "Mini waterpark";
	private static final String FACILITY_TYPE = "Food and Drink";
	private static final String FACILITY_TYPE1 = "Sport and Activity";
	private static final String FACILITY_TYPE2 = "Pool";
	private static final String ACCOMMCODE = "007854";
	private FacilityViewData viewData1, viewData2;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

		facilityModel = new FacilityModel();
		facilityModel.setCode(FACILITY_CODE);

		facilityModel1 = new FacilityModel();
		facilityModel1.setCode(FACILITY_CODE1);

		facilityModel2 = new FacilityModel();
		facilityModel2.setCode(FACILITY_CODE2);

	}

	public FacilityViewData getDummyFacilityData() {

		FacilityViewData viewData = new FacilityViewData();
		viewData.setFacilityType(FACILITY_TYPE);
		viewData.setName(FACILITY_NAME);
		viewData.setDescription("Held 3 times a week on the beach. Just ask reception for the details.");
		return viewData;
	}

	public List<FacilityViewData> getTestData() {
		viewData1.setFacilityType(FACILITY_TYPE1);
		viewData1.setName(FACILITY_NAME1);

		viewData2.setName(FACILITY_NAME2);
		viewData2.setFacilityType(FACILITY_TYPE2);
		List<FacilityViewData> data = new ArrayList<FacilityViewData>();
		data.add(viewData1);
		data.add(viewData2);
		return data;

	}

	private CatalogVersionModel getCatalogVersion() {

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;

	}

	@Test
	public void testinGetFacility() {
		final CatalogVersionModel catalog = getCatalogVersion();
		FacilityViewData dummyLocData = getDummyFacilityData();
		BrandDetails brandDetails = new BrandDetails();
		List<String> brandPks = new ArrayList<String>();
		brandDetails.setRelevantBrands(brandPks);

		Mockito.when(sessionService.getAttribute(Mockito.anyString()))
				.thenReturn(brandDetails);
		Mockito.when(siteService.getCurrentCatalogVersion())
				.thenReturn(catalog);

		Mockito.when(
				facilityService
						.getFacilityForCode(FACILITY_CODE, catalog, null))
				.thenReturn(facilityModel);

		Mockito.when(facilityConverter.convert(facilityModel)).thenReturn(
				dummyLocData);
		final FacilityViewData expectedResult = facilityFacade
				.getFacility(FACILITY_CODE);

		assertNull(expectedResult);

		assertThat(FACILITY_NAME, is(dummyLocData.getName()));
		assertThat(FACILITY_TYPE, is(dummyLocData.getFacilityType()));
		assertThat(
				"Held 3 times a week on the beach. Just ask reception for the details.",
				is(dummyLocData.getDescription()));

	}

	@Test
	public void getFacilitiesByAccommodationAndType() {

		List<FacilityModel> facilitiesModel = new ArrayList<FacilityModel>();
		facilitiesModel.add(facilityModel);
		facilitiesModel.add(facilityModel1);
		facilitiesModel.add(facilityModel2);
		BrandDetails brandDetails = new BrandDetails();
		List<String> brandPks = new ArrayList<String>();
		brandDetails.setRelevantBrands(brandPks);
		FacilityViewData dummyLocData = getDummyFacilityData();

		final CatalogVersionModel catalog = getCatalogVersion();
		Mockito.when(sessionService.getAttribute(Mockito.anyString()))
		.thenReturn(brandDetails);
		Mockito.when(siteService.getCurrentCatalogVersion())
				.thenReturn(catalog);
		Mockito.when(
				facilityService.getFacilitiesForAccommodationAndType(
						ACCOMMCODE, FACILITY_TYPE, catalog, brandPks))
				.thenReturn(facilitiesModel);
		Mockito.when(facilityConverter.convert(facilityModel)).thenReturn(
				viewData1);
		Mockito.doNothing()
				.when(facilityConfiguredPopulator)
				.populate(Mockito.any(FacilityModel.class),
						Mockito.any(FacilityViewData.class),
						Mockito.anyCollection());
		List<FacilityViewData> expectedResult = facilityFacade
				.getFacilitiesByAccommodationAndType(ACCOMMCODE, FACILITY_TYPE1);
		assertThat(expectedResult, is(notNullValue()));

		assertThat(FACILITY_NAME, is(dummyLocData.getName()));
		assertThat(FACILITY_TYPE, is(dummyLocData.getFacilityType()));
		assertThat(
				"Held 3 times a week on the beach. Just ask reception for the details.",
				is(dummyLocData.getDescription()));

	}

}
