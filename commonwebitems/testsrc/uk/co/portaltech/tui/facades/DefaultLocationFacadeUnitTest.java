/**
 *
 */
package uk.co.portaltech.tui.facades;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.converters.CountryViewDataConverter;
import uk.co.portaltech.tui.converters.DestinationConverter;
import uk.co.portaltech.tui.facades.impl.DefaultLocationFacade;
import uk.co.portaltech.tui.model.CategoryItemsListModel;
import uk.co.portaltech.tui.resolvers.TuiCategoryUrlResolver;
import uk.co.portaltech.tui.web.view.data.CountryViewData;
import uk.co.portaltech.tui.web.view.data.DestinationData;

/**
 * @author sunilkumar.sahu
 *
 */
@UnitTest
public class DefaultLocationFacadeUnitTest {
	@InjectMocks
	private final DefaultLocationFacade locationFacade = new DefaultLocationFacade();

	@Mock
	private MainStreamTravelLocationService mstravelLocationService;
	@Mock
	private DestinationConverter destinationConverter;
	@Mock
	private CountryViewDataConverter countryViewDataConverter;
	@Mock
	private TuiCategoryUrlResolver tuiCategoryUrlResolver;
	@Mock
	private DefaultLocationFacade locationFacade1;
	@Mock
	private CategoryItemsListModel category;

	private DestinationData destinationData1, destinationData2,
			destinationData3;
	private LocationModel locationModel1, locationModel2, locationModel3;

	private static final String country1 = "IND";

	private static final String region1 = "Goa";
	private static final String destination1 = "South Goa";
	private final List<String> airports = new ArrayList<String>();
	private final List<String> dates = new ArrayList<String>();
	private final List<String> releventBrands = new ArrayList<String>();

	private final NewSearchPanelComponentModel searchPanel = new NewSearchPanelComponentModel();

	@Before
	public void setUp() {

		initMocks(this);

		destinationData1 = new DestinationData();
		destinationData1.setType(LocationType.COUNTRY.getCode());
		destinationData1.setName("India");

		destinationData2 = new DestinationData();
		destinationData2.setType(LocationType.REGION.getCode());
		destinationData2.setName("Goa");

		destinationData3 = new DestinationData();
		destinationData3.setType(LocationType.DESTINATION.getCode());
		destinationData3.setName("South Goa");

		locationModel1 = new LocationModel();
		locationModel1.setCode(country1);
		locationModel1.setType(LocationType.COUNTRY);

		locationModel2 = new LocationModel();
		locationModel2.setCode(region1);
		locationModel2.setType(LocationType.REGION);

		locationModel3 = new LocationModel();
		locationModel3.setCode(destination1);
		locationModel3.setType(LocationType.DESTINATION);
		// dummy airports
		airports.add("LGW");
		airports.add("LTN");
		// dummy dates
		dates.add("12-08-2014");
		dates.add("17-08-2014");
		// dummy brands
		releventBrands.add("TH");
		releventBrands.add("TH_FC");



	}

	public DestinationData getDummyDestinationData() {
		List<DestinationData> subCategories = new ArrayList<DestinationData>();
		subCategories.add(destinationData2);
		subCategories.add(destinationData3);

		DestinationData destinationData = new DestinationData();
		destinationData.setName("India");
		destinationData.setType(LocationType.COUNTRY.getCode());
		destinationData.setChildren(subCategories);

		return destinationData;
	}

	@Test
	public void shouldgetAllCountryModels() {
		List<LocationModel> modelList = new ArrayList<LocationModel>();
		modelList.add(locationModel1);

		when(mstravelLocationService.getAllCountries(null)).thenReturn(modelList);

		DestinationData dummyDestData = getDummyDestinationData();
		when(destinationConverter.convert(locationModel1)).thenReturn(
				dummyDestData);

		final List<DestinationData> expectedResult = locationFacade
				.getAllCountries(null,null);

		assertThat(expectedResult, is(notNullValue()));
		Mockito.verify(mstravelLocationService).getAllCountries(null);
		Mockito.verify(destinationConverter).convert(locationModel1);
		assertThat(expectedResult.get(0), is(dummyDestData));
		assertThat(expectedResult.get(0).getName(), is("India"));
		assertThat(expectedResult.get(0).getChildren().get(0).getName(),
				is("Goa"));
	}

	@Test
	public void shouldgetChildCategories() {
		List<CategoryModel> subDests = new ArrayList<CategoryModel>();
		locationModel3.setCategories(null);
		subDests.add(locationModel3);

		List<CategoryModel> subRegions = new ArrayList<CategoryModel>();
		locationModel2.setCategories(subDests);
		subRegions.add(locationModel2);

		LocationModel loc1 = new LocationModel();
		loc1.setCode("IND");
		MediaModel md = new MediaModel();
		md.setURL("pic001");
		loc1.setThumbnail(md);
		loc1.setCategories(subRegions);
		LocationModel spyLoc1 = Mockito.spy(loc1);
		String url = "path";

		List<DestinationData> subDestns = new ArrayList<DestinationData>();
		DestinationData destinationData1 = new DestinationData();
		destinationData1.setName("South Goa");
		destinationData1.setAvailable(true);
		destinationData1.setType(LocationType.DESTINATION.getCode());
		subDestns.add(destinationData1);

		List<DestinationData> subRegns = new ArrayList<DestinationData>();
		DestinationData regionData1 = new DestinationData();
		regionData1.setName("Goa");
		regionData1.setAvailable(true);
		regionData1.setType(LocationType.REGION.getCode());
		regionData1.setChildren(subDestns);
		subRegns.add(regionData1);

		LocationModel spyRegLoc1 = Mockito.spy(locationModel2);
		CountryViewData countryViewData = new CountryViewData();
		countryViewData.setId("IND");
		countryViewData.setName("India");
		countryViewData.setType(LocationType.COUNTRY);
		countryViewData.setChildren(subRegns);

		when(mstravelLocationService.getCountry("IND",null)).thenReturn(
				loc1);
		when(tuiCategoryUrlResolver.resolve(loc1)).thenReturn(url);
		when(countryViewDataConverter.convert(loc1)).thenReturn(
				countryViewData);
		when(spyLoc1.getCategories()).thenReturn(subRegions);

		when(destinationConverter.convert((LocationModel) subRegions.get(0)))
				.thenReturn(regionData1);
		when(spyRegLoc1.getCategories()).thenReturn(subDests);
		when(destinationConverter.convert((LocationModel) subDests.get(0)))
		.thenReturn(destinationData1);

		CountryViewData resultCountryViewData = locationFacade.getChildCategories("IND",null);

		assertThat(resultCountryViewData, is(notNullValue()));
		assertThat((resultCountryViewData.getId()), is("IND"));
		assertThat((resultCountryViewData.getName()), is("India"));
		assertThat((resultCountryViewData.getUrl()), is("path"));

		assertThat(resultCountryViewData.getChildren(), is(notNullValue()));
		assertThat(resultCountryViewData.getChildren().get(0).getName(),is("Goa"));
		assertThat(resultCountryViewData.getChildren().get(0).getType(),is(LocationType.REGION.getCode()));
		assertThat(resultCountryViewData.getChildren().get(0).getChildren().get(0).getName(),is("South Goa"));
		assertThat(resultCountryViewData.getChildren().get(0).getChildren().get(0).getType(),is(LocationType.DESTINATION.getCode()));

	}

	@Test
	public void shouldgetChildCategoriesIfNoRegions() {

		List<CategoryModel> subList = new ArrayList<CategoryModel>();
		locationModel3.setCategories(null);
		subList.add(locationModel3);

		LocationModel loc1 = new LocationModel();
		loc1.setCode("IND");
		MediaModel md = new MediaModel();
		md.setURL("pic001");
		loc1.setThumbnail(md);
		loc1.setCategories(subList);

		String url = "path";

		CountryViewData resultCountryViewData = new CountryViewData();

		DestinationData resultDestinationData = new DestinationData();
		resultDestinationData.setName("South Goa");
		resultDestinationData.setAvailable(true);
		resultDestinationData.setType(LocationType.DESTINATION.getCode());
		resultDestinationData.setChildren(null);

		when(mstravelLocationService.getCountry("IND",null)).thenReturn(
				loc1);

		when(tuiCategoryUrlResolver.resolve(loc1)).thenReturn(url);
		when(countryViewDataConverter.convert(loc1)).thenReturn(
				resultCountryViewData);
		when(destinationConverter.convert(loc1)).thenReturn(
				resultDestinationData);
		assertThat(locationFacade.getChildCategories("IND",null), is(notNullValue()));

		assertThat(resultDestinationData.getName(), is(notNullValue()));
		assertThat((resultDestinationData.getName()), is("South Goa"));
		assertThat((resultDestinationData.getType()),
				is(LocationType.DESTINATION.getCode()));

	}

	@Test
	public void shouldgetChildCategoriesIfNoDestinations() {

		List<CategoryModel> subList = new ArrayList<CategoryModel>();
		locationModel2.setCategories(null);
		subList.add(locationModel2);

		LocationModel loc1 = new LocationModel();
		loc1.setCode("IND");
		MediaModel md = new MediaModel();
		md.setURL("pic001");
		loc1.setThumbnail(md);
		loc1.setCategories(subList);
		LocationModel spyLoc1 = Mockito.spy(loc1);
		String url = "path";

		CountryViewData resultCountryViewData = new CountryViewData();
		DestinationData dummyDestinationData = new DestinationData();

		DestinationData resultDestinationData = new DestinationData();
		resultDestinationData.setName("Goa");
		resultDestinationData.setAvailable(true);
		resultDestinationData.setType(LocationType.REGION.getCode());
		resultDestinationData.setChildren(null);

		when(mstravelLocationService.getCountry("IND",null)).thenReturn(
				loc1);
		when(tuiCategoryUrlResolver.resolve(loc1)).thenReturn(url);
		when(countryViewDataConverter.convert(loc1)).thenReturn(
				resultCountryViewData);
		when(destinationConverter.convert(loc1)).thenReturn(
				dummyDestinationData);
		when(spyLoc1.getCategories()).thenReturn(subList);

		when(destinationConverter.convert((LocationModel) subList.get(0)))
				.thenReturn(resultDestinationData);

		assertThat(locationFacade.getChildCategories("IND",null), is(notNullValue()));

		assertThat(resultDestinationData.getName(), is(notNullValue()));
		assertThat((resultDestinationData.getName()), is("Goa"));
		assertThat((resultDestinationData.getType()), is(LocationType.REGION.getCode()));

	}

/*	@Test
	public void testMostPopularDataForNull()
	{
		locationFacade.fetchDestinationGuideForSearchB(null, airports, dates, releventBrands, "true");
	}

	@Test
	public void testMostPopularDataForEmptySearchPanel()
	{
		locationFacade.fetchDestinationGuideForSearchB(searchPanel, airports, dates, releventBrands, "true");
	}*/

	/*
	 * @Test void testDestinationGuideWithCollectionData() { final List<ProductRangeModel> besHolidayTypes = new
	 * ArrayList<ProductRangeModel>(); final ProductRangeModel productRange1 = new ProductRangeModel(); final
	 * ProductRangeModel productRange2 = new ProductRangeModel();
	 * 
	 * productRange1.setCode("FHV"); }
	 */

	@Test
	public void testDestinationGuideWithOutMostPopularValue()
	{

		//dummyLocationModelList
		final LocationModel location1 = new LocationModel();
		location1.setCode("ESP");
		final LocationModel location2 = new LocationModel();
		location2.setCode("EGY");
		final List<LocationModel> locationList = new ArrayList<LocationModel>();
		locationList.add(location1);
		locationList.add(location2);
		searchPanel.setBestforbeaches(locationList);
		searchPanel.setBestforfamily(locationList);
		final CategoryItemsListModel categoryModel1 = new CategoryItemsListModel();
		final CategoryItemsListModel categoryModel2 = new CategoryItemsListModel();
		final List<String> mostPopularDestinationList = new ArrayList<String>();
		mostPopularDestinationList.add("ESP");
		mostPopularDestinationList.add("CYP");
		final List<String> holidayTypeList = new ArrayList<String>();
		holidayTypeList.add("FHV");
		holidayTypeList.add("FSO");
		final List<ProductRangeModel> besHolidayTypes = new ArrayList<ProductRangeModel>();
		final ProductRangeModel productRange1 = new ProductRangeModel();
		final ProductRangeModel productRange2 = new ProductRangeModel();
		productRange1.setCode("FHV");
		productRange2.setCode("FSO");
		besHolidayTypes.add(productRange1);
		besHolidayTypes.add(productRange2);
		categoryModel1.setItemCodes(mostPopularDestinationList);
		categoryModel2.setItemCodes(holidayTypeList);
		searchPanel.setMostpopulardestinations(categoryModel1);
		searchPanel.setHolidaytypes(categoryModel2);
		Assert.assertNotNull(searchPanel.getHolidaytypes());
		Assert.assertNotNull(searchPanel.getBestforfamily());
		Assert.assertNotNull(searchPanel.getBestforbeaches());
		Assert.assertNotNull(searchPanel.getMostpopulardestinations());
		Assert.assertNull(searchPanel.getMostPopularList());
	}
}
