/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.mockito.Mockito.when;
import static uk.co.portaltech.commons.StringUtil.split;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.commons.DateRangeProviderUtil;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.impl.DefaultLocationFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.CountryViewData;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.DestinationGuideViewData;
import uk.co.portaltech.tui.web.view.data.SuggestionViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

import com.enterprisedt.util.debug.Logger;


/**
 * @author veena.pn
 * 
 */
public class HolidayFinderComponentControllerTest
{

	@Mock
	private AccommodationFacade accomodationFacade;

	@Mock
	private DefaultLocationFacade locationFacade;

	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private CategoryService categoryService;

	@Mock
	private SessionService sessionService;

	@Mock
	private TuiUtilityService tuiUtilityService;

	
	private final TUILogUtils LOG = new TUILogUtils("HolidayFinderComponentControllerTest");


	private HolidayFinderComponentModel holidayFinderComponentModel;
	private CountryViewData countryViewData;

	private DestinationData destinationData;
	private DestinationData destinationData2;
	private SuggestionViewData suggestionViewData;
	private SuggestionViewData suggestionViewData2;

	private DestinationGuideViewData destinationGuideViewData;

	@Mock
	private CatalogVersionModel model;
	@Mock
	private CatalogModel catalogmodel;

	ArrayList<String> airports = new ArrayList<String>();
	List<DestinationData> destinationdataList = new ArrayList<DestinationData>();
	List<SuggestionViewData> suggestionViewDataList = new ArrayList<SuggestionViewData>();

	String country;
	boolean isflexible;
	String dates;

	private List<String> brandPks = null;


	@Before
	public void setUp() throws Exception
	{

		final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
		brandPks = brandDetails.getRelevantBrands();

		MockitoAnnotations.initMocks(this);
		final CatalogVersionModel model = new CatalogVersionModel();
		final CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		model.setCatalog(catalogmodel);

		airports.add("LN");
		country = "ESP";
		isflexible = true;
		dates = "8-1-2014";

		holidayFinderComponentModel = new HolidayFinderComponentModel();
		destinationData = new DestinationData();
		destinationData2 = new DestinationData();
		countryViewData = new CountryViewData();
		suggestionViewData = new SuggestionViewData();
		suggestionViewData2 = new SuggestionViewData();

		destinationGuideViewData = new DestinationGuideViewData();
		dummyDestinationData();
		getDummyHolidayFinderComponentModel();
		dummyCountryViewData();
		dummySuggestionViewData();
		dummyDestinationGuideViewData();

	}

	private CountryViewData dummyCountryViewData()

	{
		countryViewData.setId("id");
		countryViewData.setAvailable(true);
		countryViewData.setName("name");
		countryViewData.setType(LocationType.COUNTRY);
		countryViewData.setUrl("url");
		return countryViewData;
	}

	private List<DestinationData> dummyDestinationData()

	{
		destinationData.setAvailable(true);
		destinationData.setId("id");
		destinationData.setName("destinationname");
		destinationData.setType("destinationtype");

		destinationData2.setAvailable(true);
		destinationData2.setId("id123");
		destinationData2.setName("destinationname2");
		destinationData2.setType("destinationtype2");

		destinationdataList.add(destinationData);
		destinationdataList.add(destinationData2);
		return destinationdataList;
	}

	private DestinationGuideViewData dummyDestinationGuideViewData()

	{
		destinationGuideViewData.setDestinationlist(destinationdataList);
		destinationGuideViewData.setSuggestions(suggestionViewDataList);


		return destinationGuideViewData;

	}

	private List<SuggestionViewData> dummySuggestionViewData()

	{
		suggestionViewData.setId("suggid");
		suggestionViewData.setInspireImage("inspireImage");
		suggestionViewData.setInspireURL("inspireURL");
		suggestionViewData.setName("name");
		suggestionViewData.setType("type");

		suggestionViewData2.setId("sgid2");
		suggestionViewData2.setInspireImage("inspireImage2");
		suggestionViewData2.setInspireText("inspireText2");
		suggestionViewData2.setInspireURL("inspireURL2");
		suggestionViewData2.setName("name2");
		suggestionViewDataList.add(suggestionViewData);
		suggestionViewDataList.add(suggestionViewData2);
		return suggestionViewDataList;
	}

	@SuppressWarnings("boxing")
	private HolidayFinderComponentModel getDummyHolidayFinderComponentModel()
	{
		holidayFinderComponentModel.setCatalogVersion(model);
		holidayFinderComponentModel.setUid("301");
		holidayFinderComponentModel.setMaxChildAge(1);
		holidayFinderComponentModel.setMaxNoOfChild(3);
		holidayFinderComponentModel.setFlexibleDays(7);
		return holidayFinderComponentModel;
	}

	@SuppressWarnings("boxing")
	@Test
	public void testrenderResourceBoxComponent()
	{
		final String componentUID = "WF_COM_301";

		try
		{
			when((HolidayFinderComponentModel) componentFacade.getComponent(componentUID)).thenReturn(holidayFinderComponentModel);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}

		Assert.assertSame(1, holidayFinderComponentModel.getMaxChildAge());
		Assert.assertSame(3, holidayFinderComponentModel.getMaxNoOfChild());

	}

	@SuppressWarnings("boxing")
	@Test
	public void testGetDestinationGuide()
	{
		if (isflexible == true && StringUtils.isNotEmpty(dates))
		{
			when(
					locationFacade.availableRoutesForChild(airports, country,
							DateRangeProviderUtil.getAllvaliddate(dates, holidayFinderComponentModel.getFlexibleDays()), brandPks,
							tuiUtilityService.getSiteReleventBrandPks())).thenReturn(countryViewData);
		}
		else
		{
			when(
					locationFacade.availableRoutesForChild(airports, country, split(dates, ","), brandPks,
							tuiUtilityService.getSiteReleventBrandPks())).thenReturn(countryViewData);
		}



	}

	@SuppressWarnings("boxing")
	@Test
	public void testgetflexibility()
	{
		try
		{
			when(componentFacade.getHolidayFinderComponent()).thenReturn(holidayFinderComponentModel);
		}
		catch (final SearchResultsBusinessException e)
		{

			LOG.error("SearchResultsBusinessException " + e);
		}
		Assert.assertNotNull(holidayFinderComponentModel.getFlexibleDays());
		Assert.assertSame(7, holidayFinderComponentModel.getFlexibleDays());
	}

	@Test
	public void testfetchAllCountries()
	{

		when(locationFacade.getAllCountries(brandPks, null)).thenReturn(destinationdataList);

		Assert.assertNotNull(destinationdataList);

	}


	@SuppressWarnings("boxing")
	@Test
	public void testDestinationGuideViewData()
	{
		try
		{
			when((HolidayFinderComponentModel) componentFacade.getComponent("WF_COM_301")).thenReturn(holidayFinderComponentModel);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No Such Component Found " + e);

		}
		if (isflexible == true && StringUtils.isNotEmpty(dates))
		{
			when(
					locationFacade.fetchDestinationGuideForMobile(holidayFinderComponentModel, airports,
							DateRangeProviderUtil.getAllvaliddate(dates, holidayFinderComponentModel.getFlexibleDays()), brandPks))
					.thenReturn(destinationGuideViewData);
		}
		else
		{
			when(locationFacade.fetchDestinationGuideForMobile(holidayFinderComponentModel, airports, split(dates, ","), brandPks))
					.thenReturn(destinationGuideViewData);
		}

		when(locationFacade.getAllCountries(brandPks, null)).thenReturn(destinationdataList);
		for (final DestinationData destdata : destinationdataList)
		{
			destdata.setAvailable(true);
		}
		destinationGuideViewData.setDestinationlist(destinationdataList);
		Assert.assertNotNull(destinationGuideViewData);
		Assert.assertEquals(destinationdataList, destinationGuideViewData.getDestinationlist());
		Assert.assertEquals(suggestionViewDataList, destinationGuideViewData.getSuggestions());

	}


}
