/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.travel.services.config.impl.TUIConfigServiceImpl;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.impl.DefaultHolidayFacade;
import uk.co.portaltech.tui.services.DroolsPriorityProviderService;
import uk.co.portaltech.tui.services.TuiPageService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.TuiUtilityServiceImpl;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsLocationData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsResortData;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.services.data.WorldCareFundRuleOutputData;


/**
 * @author gaurav.b
 * 
 */
@UnitTest
public class DefaultHolidayFacadeTest
{
	private DefaultHolidayFacade defaultHolidayFacade;

	private static final String BRAND_TYPE = "TH";
	private static final String ISCAPE_BUSINESS_CONTEXT_ID = ".iscapeBusinessContextId";
	private DroolsPriorityProviderService droolsPriorityProviderService;
	private TuiUtilityService           tuiUtilityService;
	private TuiPageService tuiPageService;
	private ComponentFacade componentFacade;
	private SearchPanelComponentModel componentModel;


	private SearchResultsRequestData searchResultsRequestData;
	private BookFlowAccommodationViewData bookFlowAccommodationViewData;
	private SearchResultViewData searchResultViewData;
	private TUIConfigService tuiConfigService;
	private SearchResultFlightViewData searchResultFlightViewData;
	private SearchResultFlightDetailViewData flightDetailViewData;
	private SearchResultAccomodationViewData accomodationViewData;
	private SearchResultPriceViewData priceViewData;
	private WorldCareFundRuleOutputData careFundRuleOutputData;
	private SearchResultsLocationData locationData;
	private SearchResultsResortData resortData;

	@Before
	public void setUp()
	{
		defaultHolidayFacade = new DefaultHolidayFacade();
		searchResultsRequestData = mock(SearchResultsRequestData.class);
		searchResultViewData = mock(SearchResultViewData.class);
		bookFlowAccommodationViewData = mock(BookFlowAccommodationViewData.class);
		tuiConfigService = mock(TUIConfigServiceImpl.class);
		searchResultFlightViewData = mock(SearchResultFlightViewData.class);
		accomodationViewData = mock(SearchResultAccomodationViewData.class);
		priceViewData = mock(SearchResultPriceViewData.class);
		droolsPriorityProviderService = mock(DroolsPriorityProviderService.class);
		careFundRuleOutputData = mock(WorldCareFundRuleOutputData.class);
		locationData = mock(SearchResultsLocationData.class);
		resortData = mock(SearchResultsResortData.class);
		flightDetailViewData = mock(SearchResultFlightDetailViewData.class);
		tuiUtilityService = mock(TuiUtilityServiceImpl.class);
		tuiPageService = mock(TuiPageService.class);
		componentFacade = mock(ComponentFacade.class);
		componentModel = mock(SearchPanelComponentModel.class);
		defaultHolidayFacade.setTuiConfigService(tuiConfigService);
		defaultHolidayFacade.setDroolsPriorityProviderService(droolsPriorityProviderService);
		defaultHolidayFacade.setTuiUtilityService(tuiUtilityService);

		defaultHolidayFacade.setComponentFacade(componentFacade);


	}

	@Test
	public void testPopulateShortlistedHoliday()
	{
		given(bookFlowAccommodationViewData.getPackageData()).willReturn(searchResultViewData);
		given(searchResultViewData.getBrandType()).willReturn(BRAND_TYPE);
		given(tuiConfigService.getConfigValue(BRAND_TYPE + ISCAPE_BUSINESS_CONTEXT_ID)).willReturn("1");
		given(searchResultViewData.getItinerary()).willReturn(searchResultFlightViewData);
		given(searchResultViewData.getItinerary().getOutbounds()).willReturn(Collections.EMPTY_LIST);
		given(searchResultViewData.getAccommodation()).willReturn(accomodationViewData);
		given(searchResultViewData.getAccommodation().getAccomType()).willReturn("HT");
		given(searchResultViewData.getAccommodation().getUrl()).willReturn("URL");
		given(searchResultViewData.getItinerary().getDepartureDate()).willReturn("28-05-2014");
		given(searchResultViewData.getPrice()).willReturn(priceViewData);
		given(droolsPriorityProviderService.getWorldCareFundCharges()).willReturn(careFundRuleOutputData);
		given(careFundRuleOutputData.getAdultCharge()).willReturn("1000.0");
		given(bookFlowAccommodationViewData.getSearchRequestData()).willReturn(searchResultsRequestData);
		given(bookFlowAccommodationViewData.getSearchRequestData().getNoOfAdults()).willReturn(2);
		given(bookFlowAccommodationViewData.getSearchRequestData().getNoOfSeniors()).willReturn(1);
		given(bookFlowAccommodationViewData.getSearchRequestData().getChildAges()).willReturn(new ArrayList<Integer>(){{add(1);add(2);}});
		given(accomodationViewData.getCode()).willReturn("1212");
		given(accomodationViewData.getName()).willReturn("Flamingo");
		given(accomodationViewData.getRooms()).willReturn(Collections.EMPTY_LIST);
		given(accomodationViewData.getLocation()).willReturn(locationData);
		given(accomodationViewData.getLocation().getResort()).willReturn(resortData);
		given(accomodationViewData.getLocation().getResort().getCode()).willReturn("111");
		given(searchResultFlightViewData.getInbounds()).willReturn(new ArrayList<SearchResultFlightDetailViewData>(){{add(flightDetailViewData);}});
		given(flightDetailViewData.getArrivalAirportCode()).willReturn("100");
		given(searchResultFlightViewData.getDepartureAirport()).willReturn("100");
		given(priceViewData.getLowDeposit()).willReturn("990.0");
		given(priceViewData.getPerPerson()).willReturn("900.0");
		given(priceViewData.getTotalParty()).willReturn("1800.0");
		given(priceViewData.getDiscount()).willReturn("100.0");
		given(tuiUtilityService.getSiteBrand()).willReturn(BRAND_TYPE);
		try{
		given(tuiPageService.resolveABTestcode("newsp_abtest")).willReturn("search_panelabtest");
		}catch (CMSItemNotFoundException e) {
			e.printStackTrace();
		}
		try{
		given(componentFacade.getSearchPanelComponent()).willReturn(componentModel);
		}
		catch (SearchResultsBusinessException e) {
			e.printStackTrace();
		}

		defaultHolidayFacade.populateShortlistedHoliday(searchResultsRequestData, bookFlowAccommodationViewData, BRAND_TYPE);
	}
}