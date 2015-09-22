/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.services.ExcursionPriceService;
import uk.co.portaltech.travel.services.ExcursionService;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.converters.AttractionConverter;
import uk.co.portaltech.tui.converters.ExcursionOption;
import uk.co.portaltech.tui.facades.impl.DefaultExcursionFacade;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;
import uk.co.portaltech.tui.web.view.data.ViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.PriceAndAvailabilityWrapper;


/**
 * @author sunil.bd
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultExcursionFacadeTest {

	@InjectMocks
	private final DefaultExcursionFacade facade = new DefaultExcursionFacade();

	@Mock
	BigDecimal lowestPrice;
	
	@Mock
	BigDecimal lowestChildPrice;

	@Mock
	private ExcursionService excursionService;
	
	@Mock
	private ExcursionModel excursionModel4;
	
	private ExcursionModel excursionModel, excursionModel1, excursionModel2,
			excursionModel3;
	@Mock
	private ItemModel itemModel;
	
	@Mock
	private LocationModel locationModel;
	
	@Mock
	private ConfigurablePopulator<ExcursionModel, ExcursionViewData, ExcursionOption> excursionConfiguredPopulator;
	
	@Mock
	private ExcursionOption excursionOption;
	
	@Mock
	private CMSSiteService cmsSiteService;
	
	@Mock
	private CatalogVersionModel catalogVersion;

	private ExcursionViewData excursionViewData, excursionViewData1;
	
	@Mock
	private List<ExcursionViewData> excursionViewDataList;
	
	@Mock
	private AttractionService attractionService;
	
	@Mock
	private AttractionViewData attractionViewData;
	
	@Mock
	private Converter excursionConverter;
	
	@Mock
	private PriceAndAvailabilityWrapper priceAndAvailabilityWrapper,priceAndAvailabilityWrapper1,priceAndAvailabilityWrapper2;

	@Mock
	private CollectionUtils collectionUtils;
	
	@Mock
	private ExcursionPriceModel excursionPriceModel,excursionPriceModel1;
	
	@Mock
	private ExcursionPriceService excursionPriceService;
	
	@Mock
	private ConfigurablePopulator<ExcursionModel, ExcursionViewData, ExcursionOption> configurablePopulator;
	
	@Mock
	private AttractionConverter attractionConverter;
	
	@Mock
	private CatalogVersionModel catalogVersionModel;
	
	private List<CommentModel> commentModels;
	
	private CommentModel commentModel;
	
	List<ExcursionViewData> excursions = new ArrayList<ExcursionViewData>();
	
	private Set<ExcursionPriceModel> prices;

	
	private static final String EXCURSION_CODE = "1111";
	private static final String EXCURSION_CODE1 = "2222";
	private static final String EXCURSION_CODE2 = "992713";
	private static final String EXCURSION_DESC = "Good for Kids";
	private static final String EXCURSION_NAME = "Excursion";

	/*
	 * The method that initializes the commons objects
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		excursionModel = new ExcursionModel();
		excursionModel.setCode("980352");

		excursionModel1 = new ExcursionModel();
		excursionModel1.setCode("990791");

		prices = new HashSet<ExcursionPriceModel>();
		excursionPriceModel = new ExcursionPriceModel();
		excursionPriceModel.setAdultPrice(new BigDecimal(1000));
		excursionPriceModel.setCode("1111");
		excursionPriceModel.setExcursion(excursionModel2);
		excursionPriceModel.setCurrency("Rupees");
		excursionPriceModel.setTicketName("Travel Ticket");
		prices.add(excursionPriceModel);
		excursionModel3 = new ExcursionModel();
		excursionModel3.setExcursionPrices(prices);
		excursionViewData = new ExcursionViewData();
		excursionViewData.setChildPrice("500");
		excursionViewData.setCode("1111");
		excursionViewData.setName("Excursion");
		priceAndAvailabilityWrapper = new PriceAndAvailabilityWrapper();
		priceAndAvailabilityWrapper.setExcursion(excursionViewData);
		commentModels = new ArrayList<CommentModel>();
		commentModel = new CommentModel();
		commentModel.setCode(EXCURSION_CODE2);

		commentModels.add(commentModel);

		excursions = new ArrayList<ExcursionViewData>();

		excursionViewData = new ExcursionViewData();

		excursionViewData.setCode("980352");
		excursionViewData
				.setBookingUrl("http://excursions.firstchoice.co.uk/excursions-details/ESP"
						+ "/Spain/000260/Balearic-Islands/980352/Palma-City-Sightseeing.aspx#cost");
		excursionViewData.setChildAgeMax("16");
		excursionViewData.setChildAgeMin("8");
		excursionViewData.setName("Palma City Sightseeing");
		excursionViewData
				.setDescription("From its atmospheric old town to the leafy boulevards of its modern shopping district,"
						+ "				 Palma is one of those got-it-all cities. Today, you’ll get to know Majorca’s "
						+ "		capital from the comfort of our sightseeing bus. Best of all, "
						+ "you can hop on and off to explore the bits you fancy on foot. You’ll see the city’s "
						+ "impressive Gothic cathedral, which casts a shadow over the harbour. You’ll tick off the stylish waterfront, "
						+ "with its glossy yachts and open-air restaurants. And you’ll check out the main shopping boulevards, where the local ladies do lunch in designer sunglasses. The city’s showpiece, though, is its cobbled Medieval quarter. Hop off the bus here to discover tiny treasure-trove jewellery and antique shops, and sunny squares where tapas joints advertise their specials on outdoor chalk boards. "
						+ "				When you need to refuel, stop for a coffee and an ensaimada – "
						+ "						a delicious local pastry – at one of the cubbyhole patisseries.");
		excursionViewData
				.setUrl("/holiday/attraction/Alcudia/Palma-City-Sightseeing-980352");
		excursionViewData.setFromPrice("16.00");

		excursionViewData1 = new ExcursionViewData();
		excursionViewData1.setCode("990791");
		excursionViewData1
				.setBookingUrl("http://excursions.firstchoice.co.uk/excursions-details/ESP/Spain/001685"
						+ "/Canary-Islands/990791/Siam-Park.aspx#cost");
		excursionViewData1.setChildAgeMax("11");
		excursionViewData1.setChildAgeMin("3");
		excursionViewData1.setName("Siam Park");
		excursionViewData1
				.setDescription("Hold tight - you’re in for one rooting-tooting, chute-shooting day at Siam Park. This waterpark is one of the biggest in Europe and bursting with exciting rides and beaches set against an exotic Thai backdrop. With over 30 attractions, there’s something for all the family, whether you just want to chill out or launch yourself into the pulse-racing rides. Hold your breath as you hurtle down the near-vertical Tower of Power. Defy gravity as you raft through the jaws of The Dragon into a fast-spinning cone. Or catch the surf at the Wave Palace, raft the Mai Thai river or unleash the tots on The Lost City. And when you need a breather, you can relax on the white sands of Siam Beach, shop at the Floating Market or watch the cheeky sealions. Add teahouses, bars and restaurants and you’ve got yourself a perfect day trip to add to your holiday scrapbook.");
		excursionViewData1
				.setUrl("/holiday/attraction/Costa-Adeje/Siam-Park-990791");
		excursionViewData1.setFromPrice("33.00");

		excursions.add(excursionViewData);
		excursions.add(excursionViewData1);

	}

	public ExcursionModel dummydataforExcursionModel() {

		excursionModel = new ExcursionModel();
		excursionModel.setCode("1111");
		excursionModel.setChangeDescription("changedesc");

		excursionModel.setXmlcontent("xmlconetnt");

		return excursionModel;

	}

	private CatalogVersionModel getCatalogVersion() {

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("Online");
		return catalog;

	}

	public LocationModel dummyForLocationModel() {
		LocationModel locationModel = new LocationModel();
		locationModel.setCode(EXCURSION_CODE1);
		locationModel.setDescription(EXCURSION_DESC);
		locationModel.setName(EXCURSION_NAME);
		return locationModel;
	}

	public List<ExcursionViewData> dummyForViewData() {

		List<ExcursionViewData> excursionViewDataList = new ArrayList<ExcursionViewData>();
		excursionViewData.setFromPrice("Price");
		excursionViewData.setDescription("DESCRIPTION");
		excursionViewData.setName("Name1");

		return excursionViewDataList;

	}

	public ExcursionViewData dummyDataForExcursionViewData() {
		ExcursionViewData excursionViewData = new ExcursionViewData();
		excursionViewData.setCode(EXCURSION_CODE2);
		excursionViewData.setDescription("Description");

		excursionViewData.setChildPrice("19.99");
		excursionViewData.setName("Seville 1 Day");
		return excursionViewData;

	}

	public AttractionViewData dummyForAttractionViewData() {
		AttractionViewData attractionViewData = new AttractionViewData();
		attractionViewData.setCode(EXCURSION_CODE2);
		attractionViewData.setName(EXCURSION_NAME);
		return attractionViewData;
	}

	public List<ResultData> duumyDataForResultData() {

		List<ResultData> resulDataList = new ArrayList<ResultData>();
		ResultData resultData;

		resultData = new ResultData();

		resultData.setCode("980352");
		resultData.setName("Palma City Sightseeing");
		resultData.setPK("8796169142273");
		resultData.setPriceFrom("16.00");
		resultData.setSearchResultSubtype("EXR");
		resultData.setTypeOfResult("");

		resulDataList.add(resultData);
		return resulDataList;

	}

	private LocationModel getLocationModel() {
		LocationModel locationModel = new LocationModel();
		locationModel.setCode(EXCURSION_CODE2);
		return locationModel;
	}

	private ItemModel getItemModel() {

		ItemModel itemModel = new ItemModel();
		itemModel.setComments(commentModels);
		return itemModel;
	}

	@Test
	public void testGetExcursionUspsByCode() {
		CatalogVersionModel catalog = getCatalogVersion();

		ExcursionViewData excursionData = dummyDataForExcursionViewData();

		Mockito.when(cmsSiteService.getCurrentCatalogVersion()).thenReturn(
				catalog);

		Mockito.when(
				excursionService.getExcursionForCode(EXCURSION_CODE2, catalog))
				.thenReturn(excursionModel);
		if (excursionModel != null) {

			Mockito.when(excursionConverter.convert(excursionModel))
					.thenReturn(excursionData);
			excursionConfiguredPopulator.populate(excursionModel,
					excursionData, Arrays.asList(ExcursionOption.USPS));
			verify(excursionConfiguredPopulator).populate(excursionModel,
					excursionData, Arrays.asList(ExcursionOption.USPS));
		}

		final ExcursionViewData expectedResult = facade
				.getExcursionUspsByCode(EXCURSION_CODE2);

		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult.getCode(), is(excursionData.getCode()));
		assertThat(expectedResult.getName(), is(excursionData.getName()));

	}

	@Test
	public void testGetExcursionUspsByCodeForNull() {
		String EXCURSION_CODE_NULL = null;

		Mockito.when(cmsSiteService.getCurrentCatalogVersion())
				.thenReturn(null);
		Mockito.when(excursionService.getExcursionForCode(null, null))
				.thenReturn(excursionModel);
		final ExcursionViewData expectedResult = facade
				.getExcursionUspsByCode(EXCURSION_CODE_NULL);
		final ExcursionViewData expectedResult1 = facade.getExcursionUspsByCode("");
		assertNull(expectedResult);
		assertNull(expectedResult1);
	}

	@Test
	public void testGetRestrictionInfo() {

		final CatalogVersionModel catalog = getCatalogVersion();
		ExcursionViewData excursionData = dummyDataForExcursionViewData();
		Mockito.when(cmsSiteService.getCurrentCatalogVersion()).thenReturn(
				catalog);

		Mockito.when(
				excursionService.getExcursionForCode(EXCURSION_CODE, catalog))
				.thenReturn(excursionModel);
		assertNotNull(excursionModel);
		if (excursionModel != null) {

			Mockito.when(excursionConverter.convert(excursionModel))
					.thenReturn(excursionData);
			excursionConfiguredPopulator.populate(excursionModel,
					excursionData, Arrays.asList(ExcursionOption.RESTRICTION));
			verify(excursionConfiguredPopulator).populate(excursionModel,
					excursionData, Arrays.asList(ExcursionOption.RESTRICTION));		
		}
		
		
		final ExcursionViewData expectedResult = facade
				.getRestrictionInfo(EXCURSION_CODE);

		assertThat(expectedResult, is(notNullValue()));
		Mockito.verify(excursionConverter).convert(excursionModel);
		assertThat(expectedResult, is(excursionData));
		assertThat(expectedResult.getName(), is(excursionData.getName()));

	}

	@Test
	public void testGetRestrictionInfoForNull() {

		ExcursionViewData excursionData = dummyDataForExcursionViewData();
		Mockito.when(cmsSiteService.getCurrentCatalogVersion())
				.thenReturn(null);

		Mockito.when(excursionService.getExcursionForCode(null, null))
				.thenReturn(excursionModel);
		assertNotNull(excursionModel);
		Mockito.when(excursionConverter.convert(excursionModel)).thenReturn(
				excursionData);
		excursionConfiguredPopulator.populate(excursionModel, excursionData,
				Arrays.asList(ExcursionOption.RESTRICTION));

		final ExcursionViewData expectedResult = facade
				.getRestrictionInfo(null);
		final ExcursionViewData expectedResult1 = facade.getRestrictionInfo("");

		assertNotNull(expectedResult);
		assertNull(expectedResult1);

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetExcursionsWithEndecaData() {

		final CatalogVersionModel catalog = getCatalogVersion();

		List<ResultData> list = duumyDataForResultData();
		ResultData resultData = list.get(0);

		String codeFromEndeca = resultData.getCode();
		try {
			Mockito.when(
					excursionService.getExcursionForCode(codeFromEndeca,
							catalog)).thenReturn(excursionModel);

		} catch (ModelNotFoundException ex) {
			ex.printStackTrace();
		}

		assertNotNull(excursionModel);

		Mockito.when(excursionConverter.convert(excursionModel)).thenReturn(
				excursionViewData);
		excursionConfiguredPopulator.populate(excursionModel,
				excursionViewData, Arrays.asList(ExcursionOption.BASIC,
						ExcursionOption.PRIMARY_IMAGE));
		List<ExcursionViewData> expectedResult = facade
				.getExcursionsWithEndecaData(list);

		assertNotNull(expectedResult);

	}

	@Test
	public void testGetExcursionsWithEndecaDataForNull() {
		List<ExcursionViewData> excursions = new ArrayList<ExcursionViewData>();
		final CatalogVersionModel catalog = getCatalogVersion();
		ExcursionViewData excursionData = dummyDataForExcursionViewData();
		List<ResultData> list = new ArrayList<ResultData>();
		list.add(null);

		String codeFromEndeca = null;

		try {
			Mockito.when(
					excursionService.getExcursionForCode(codeFromEndeca,
							catalog)).thenReturn(excursionModel2);

		} catch (ModelNotFoundException ex) {
			ex.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (excursionModel2 != null) {
			Mockito.when(excursionConverter.convert(excursionModel2))
					.thenReturn(excursionData);

			excursionConfiguredPopulator.populate(excursionModel2,
					excursionData, Arrays.asList(ExcursionOption.BASIC,
							ExcursionOption.PRIMARY_IMAGE));

			excursionData.setFromPrice("");
			excursions.add(excursionData);

		}

		List<ExcursionViewData> expectedResult = null;
		try {
			expectedResult = facade.getExcursionsWithEndecaData(list);
			assertNull(expectedResult);
		} catch (NullPointerException nullPointerException) {
			nullPointerException.toString();
		}

	}

	@Test
	public void testGetExcursionNameComponentData() {

		final String attractionCode = "1212";
		AttractionViewData attractionViewData = Mockito
				.mock(AttractionViewData.class);
		ItemModel itemModel = Mockito.mock(ItemModel.class);
		excursionViewData = Mockito.mock(ExcursionViewData.class);
		AttractionModel attractionModel = Mockito.mock(AttractionModel.class);
		Mockito.when(
				attractionService.getAttractionForCode(attractionCode,
						cmsSiteService.getCurrentCatalogVersion())).thenReturn(
				itemModel);

		Mockito.when(excursionConverter.convert(excursionModel)).thenReturn(
				excursionViewData);
		
		Mockito.when(attractionConverter.convert(attractionModel)).thenReturn(
				attractionViewData);

		final ViewData expectedViewData = facade
				.getExcursionNameComponentData(attractionCode);

		assertNull(expectedViewData);

		assertThat(excursionViewData, is(ViewData.class));

		assertThat(attractionViewData, is(ViewData.class));
	}

	@Test
	public void testGetExcursionNameComponentDataForNull() {

		String attractionCode = null;
		CatalogVersionModel catalog = getCatalogVersion();
		ItemModel itemModel = Mockito.mock(ItemModel.class);
		
		try {
			Mockito.when(
					attractionService.getAttractionForCode(attractionCode,
							catalog)).thenReturn(null);
			assertNotNull(itemModel);
		} catch (NullPointerException nullPointerException) {

			nullPointerException.printStackTrace();
		}

		final ViewData expectedViewData = facade
				.getExcursionNameComponentData(attractionCode);
		final ViewData expectedViewData1 = facade.getExcursionNameComponentData("");
		assertNull(expectedViewData);
		assertNull(expectedViewData1);

	}

	@Test
	public void testGetLowsetPriceExcursionForLocation() {

		final CatalogVersionModel catalog = getCatalogVersion();

		ExcursionViewData dummyExcursionViewData = dummyDataForExcursionViewData();
		LocationModel locationMod = getLocationModel();
		ItemModel itemMod = getItemModel();

		Mockito.when(excursionModel4.getExcursionPrices()).thenReturn(prices);

		excursionConfiguredPopulator.populate(excursionModel2,
				dummyExcursionViewData, Arrays.asList(ExcursionOption.BASIC,
						ExcursionOption.PRIMARY_IMAGE));
		Mockito.when(
				excursionPriceService.getExcursionPrice(EXCURSION_CODE2,
						catalog)).thenReturn(excursionPriceModel);

		excursionConfiguredPopulator.populate(excursionModel2,
				dummyExcursionViewData,
				Arrays.asList(ExcursionOption.EXCURSIONPRICE));

		PriceAndAvailabilityWrapper expectedResult = facade
				.getLowsetPriceExcursionForLocation(locationMod, itemMod);

		verify(excursionConfiguredPopulator).populate(
				excursionModel2,
				dummyExcursionViewData,
				Arrays.asList(ExcursionOption.BASIC,
						ExcursionOption.PRIMARY_IMAGE));
		verify(excursionConfiguredPopulator).populate(excursionModel2,
				dummyExcursionViewData,
				Arrays.asList(ExcursionOption.EXCURSIONPRICE));
		
		assertThat(expectedResult, is(notNullValue()));
		assertThat(expectedResult, is(PriceAndAvailabilityWrapper.class));

	}

	@Test
	public void testGetLowsetPriceExcursionForLocationForNull() {
		final String code = null;
		BigDecimal lowestPrice = new BigDecimal(-1000);

		BigDecimal lowestChildPrice = new BigDecimal(-500);
		Mockito.when(excursionModel4.getExcursionPrices()).thenReturn(prices);
		Mockito.when(excursionPriceModel1.getAdultPrice()).thenReturn(
				lowestPrice);
		Mockito.when(excursionPriceModel1.getChildPrice()).thenReturn(
				lowestChildPrice);
		Mockito.when(excursionConverter.convert(excursionModel)).thenReturn(
				excursionViewData);
		Mockito.when(
				excursionPriceService.getExcursionPrice(code,
						catalogVersionModel)).thenReturn(excursionPriceModel);
		PriceAndAvailabilityWrapper expectedResult = facade.getLowsetPriceExcursionForLocation(null, null);
		
		assertNotNull(expectedResult);

	}
}
