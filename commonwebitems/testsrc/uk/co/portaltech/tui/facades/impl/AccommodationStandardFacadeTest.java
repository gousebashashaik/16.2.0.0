/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.components.model.AccommodationStandardComponentModel;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.facades.impl.AccommodationStandardFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;


/**
 * @author gaurav.b
 * 
 */
@UnitTest
public class AccommodationStandardFacadeTest
{
	private AccommodationStandardFacade accommodationStandardFacade;
	private AccommodationStandardComponentModel standardComponentModel;
	private SearchFacade searchFacade;
	private SearchResultData searchResultData;
	private ResultData resultData;
	private AccommodationFacade accomodationFacade;
	private AccommodationViewData accommodationViewData;

	private static final String LOCATION_CODE = "123456";
	private static final String PAGE_TYPE = "pageType";
	private static final String SEO_PAGE_TYPE = "seoPageType";
	private static final int NUMBER_SIX = 6;
	private static final String BUDGET_HOTEL = "Budget";
	private static final String STANDARD_HOTEL = "Standard";
	private static final String SUPERIOR_HOTEL = "Superior";
	private static final char PADCHAR = '0';
	private static final String CODE = "345678";
	private static final String PRICE = "990.0";


	@Before
	public void setUp()
	{
		accommodationStandardFacade = new AccommodationStandardFacade();
		standardComponentModel = mock(AccommodationStandardComponentModel.class);
		searchFacade = mock(SearchFacade.class);
		searchResultData = mock(SearchResultData.class);
		resultData = mock(ResultData.class);
		accomodationFacade = mock(AccommodationFacade.class);
		accommodationViewData = mock(AccommodationViewData.class);
		accommodationStandardFacade.setSearchFacade(searchFacade);
		accommodationStandardFacade.setAccomodationFacade(accomodationFacade);
	}

	@Test
	public void testGetAccommodationStandardDataForBudgetHotel()
	{
		given(searchFacade.getAccommodationStandardData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, standardComponentModel))
				.willReturn(searchResultData);
		given(searchResultData.getResults()).willReturn(new ArrayList()
		{
			{
				add(resultData);
			}
		});
		given(resultData.getCode()).willReturn(CODE);
		given(accomodationFacade.getAccommodationEditorialInfo(StringUtils.leftPad(resultData.getCode(), NUMBER_SIX, PADCHAR)))
				.willReturn(accommodationViewData);
		given(resultData.getHotelType()).willReturn(BUDGET_HOTEL);
		given(resultData.getPriceFrom()).willReturn(PRICE);

		accommodationStandardFacade.getAccommodationStandardData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, standardComponentModel);
	}

	@Test
	public void testGetAccommodationStandardDataForStandardHotel()
	{
		given(searchFacade.getAccommodationStandardData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, standardComponentModel))
				.willReturn(searchResultData);
		given(searchResultData.getResults()).willReturn(new ArrayList()
		{
			{
				add(resultData);
			}
		});
		given(resultData.getCode()).willReturn(CODE);
		given(accomodationFacade.getAccommodationEditorialInfo(StringUtils.leftPad(resultData.getCode(), NUMBER_SIX, PADCHAR)))
				.willReturn(accommodationViewData);
		given(resultData.getHotelType()).willReturn(STANDARD_HOTEL);
		given(resultData.getPriceFrom()).willReturn(PRICE);

		accommodationStandardFacade.getAccommodationStandardData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, standardComponentModel);
	}

	@Test
	public void testGetAccommodationStandardDataForSuperiorHotel()
	{
		given(searchFacade.getAccommodationStandardData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, standardComponentModel))
				.willReturn(searchResultData);
		given(searchResultData.getResults()).willReturn(new ArrayList()
		{
			{
				add(resultData);
			}
		});
		given(resultData.getCode()).willReturn(CODE);
		given(accomodationFacade.getAccommodationEditorialInfo(StringUtils.leftPad(resultData.getCode(), NUMBER_SIX, PADCHAR)))
				.willReturn(accommodationViewData);
		given(resultData.getHotelType()).willReturn(SUPERIOR_HOTEL);
		given(resultData.getPriceFrom()).willReturn(PRICE);

		accommodationStandardFacade.getAccommodationStandardData(LOCATION_CODE, PAGE_TYPE, SEO_PAGE_TYPE, standardComponentModel);
	}


}
