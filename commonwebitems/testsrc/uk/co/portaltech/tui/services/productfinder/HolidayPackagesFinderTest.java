/**
 * 
 */
package uk.co.portaltech.tui.services.productfinder;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.thirdparty.endeca.HolidaySearchContext;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;





public class HolidayPackagesFinderTest
{

	@Mock
	private ConfigurationService configurationService;
	private SearchResultsRequestData searchRequest;
	@Mock
	private Configuration configuration;
	@Mock
	private TuiUtilityService tuiUtilityService;




	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		given(configurationService.getConfiguration()).willReturn(configuration);
		given(
				configuration.getString(tuiUtilityService.getSiteBrand() + CommonwebitemsConstants.DOT
						+ CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015")).willReturn("01-11-2014");

	}


	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.web.search.impl.SearchCriteriaEnrichmentService#populateAtcomSwitchDate(uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)}
	 * .
	 */
	@Test
	public void testSameDateDepartureAsAtcomSwithDate()
	{
		given(configurationService.getConfiguration()).willReturn(configuration);
		
		given(
				configuration.getString(tuiUtilityService.getSiteBrand() + CommonwebitemsConstants.DOT
						+ CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015")).willReturn("01-11-2014");
		final HolidayPackagesFinder hf = new HolidayPackagesFinder(configurationService);
		searchRequest = new SearchResultsRequestData();
		searchRequest.setDepartureDate("01-11-2014");
		searchRequest.setFlexibleDays(7);

		final HolidaySearchContext context = new HolidaySearchContext();
		hf.populateAtcomSwitchDate(context, searchRequest);

		assertEquals(context.getUntil(), null);
		assertEquals(context.getWhen(), "01-11-2014");


	}

	@Test
	public void testDepartureAfterAtcomSwithDate()
	{
		given(configurationService.getConfiguration()).willReturn(configuration);
		
		given(
				configuration.getString(tuiUtilityService.getSiteBrand() + CommonwebitemsConstants.DOT
						+ CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015")).willReturn("01-11-2014");
		final HolidayPackagesFinder hf = new HolidayPackagesFinder(configurationService);
		searchRequest = new SearchResultsRequestData();
		searchRequest.setDepartureDate("05-11-2014");
		searchRequest.setFlexibleDays(7);

		final HolidaySearchContext context = new HolidaySearchContext();
		hf.populateAtcomSwitchDate(context, searchRequest);

		assertEquals(context.getUntil(), null);
		assertEquals(context.getWhen(), "01-11-2014");

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.web.search.impl.SearchCriteriaEnrichmentService#populateAtcomSwitchDate(uk.co.portaltech.tui.web.view.data.SearchResultsRequestData)}
	 * .
	 */
	@Test
	public void testDepartureBeforeAtcomSwithDate()
	{
		given(configurationService.getConfiguration()).willReturn(configuration);
		
		given(
				configuration.getString(tuiUtilityService.getSiteBrand() + CommonwebitemsConstants.DOT
						+ CommonwebitemsConstants.TRACS_END_DATE_PROPERTY, "01-11-2015")).willReturn("01-11-2014");
		final HolidayPackagesFinder hf = new HolidayPackagesFinder(configurationService);
		searchRequest = new SearchResultsRequestData();
		searchRequest.setDepartureDate("28-10-2014");
		searchRequest.setFlexibleDays(7);

		final HolidaySearchContext context = new HolidaySearchContext();
		hf.populateAtcomSwitchDate(context, searchRequest);


		assertEquals(context.getUntil(), "31-10-2014");
		assertEquals(context.getWhen(), null);
	}




}
