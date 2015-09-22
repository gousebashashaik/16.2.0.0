
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.DateSelectionValue;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.wrapper.SearchResultDateSelectionViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultsViewData;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;




public class DateSelectorResultsPopulatorTest
{

	
	@InjectMocks
	private DateSelectorResultsPopulator datePopulator= new DateSelectorResultsPopulator();

	@Mock
    private ViewSelector viewSelector;
	
	@Mock
    private SessionService  sessionService;


	
	private SearchResultsRequestData  requestWithFlexibility,requestWithOutFlexibility;

	private  DateSelectionValue  dateSelectionValue1,dateSelectionValue2,dateSelectionValue3,dateSelectionValue4,dateSelectionValue5,dateSelectionValue6;
	private SearchResultsViewData searchResultsViewData;
	private EndecaSearchResult endecaResult;
	private SearchResultDateSelectionViewData dateSelectionViewData,dateSelectionViewData1;
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final String DATE1 = "20-12-2013";
	private static final String DATE2 = "21-12-2013";
	private static final String DATE3 = "22-12-2013";
	private static final String DATE4 = "23-12-2013";
	private static final String DATE5 = "24-12-2013";
	private static final String DATE6 = "25-12-2013";

	private static final String WHEN = "19-12-2013";
	private static final String UNTIL = "22-12-2013";

	private static final String FUTURE_WHEN = "01-01-2014";
	private static final int FLEXIBLEDAYS = 7;

	private List<DateSelectionValue>listofdates=null;
	@SuppressWarnings("boxing")
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		
		requestWithFlexibility=new SearchResultsRequestData();
		requestWithFlexibility.setFlexibility(true);
		requestWithFlexibility.setWhen(WHEN);
		requestWithFlexibility.setUntil("");
		requestWithFlexibility.setFlexibleDays(FLEXIBLEDAYS);

		requestWithOutFlexibility=new SearchResultsRequestData();
		requestWithOutFlexibility.setFlexibility(false);
		requestWithOutFlexibility.setWhen(WHEN);
		requestWithOutFlexibility.setUntil(UNTIL);

		
		dateSelectionViewData1 = new SearchResultDateSelectionViewData();
        dateSelectionValue1=new DateSelectionValue();
		dateSelectionValue2=new DateSelectionValue();
		dateSelectionValue3=new DateSelectionValue();
		dateSelectionValue4=new DateSelectionValue();
		dateSelectionValue5=new DateSelectionValue();
		dateSelectionValue6=new DateSelectionValue();
        dateSelectionValue1.setDate(DateUtils.toDateTime(DATE1, DATE_FORMAT));
		dateSelectionValue1.setAvailable("true");
        dateSelectionValue2.setDate(DateUtils.toDateTime(DATE2, DATE_FORMAT));
		dateSelectionValue2.setAvailable("true");
		dateSelectionValue3.setDate(DateUtils.toDateTime(DATE3, DATE_FORMAT));
		dateSelectionValue3.setAvailable("false");
		dateSelectionValue4.setDate(DateUtils.toDateTime(DATE4, DATE_FORMAT));
		dateSelectionValue4.setAvailable("false");
		dateSelectionValue5.setDate(DateUtils.toDateTime(DATE5, DATE_FORMAT));
		dateSelectionValue5.setAvailable("true");
		dateSelectionValue6.setDate(DateUtils.toDateTime(DATE6, DATE_FORMAT));
		dateSelectionValue6.setAvailable("true");

	    listofdates=new ArrayList<DateSelectionValue>();
		listofdates.add(dateSelectionValue1);
		listofdates.add(dateSelectionValue2);
		listofdates.add(dateSelectionValue3);
		listofdates.add(dateSelectionValue4);
		listofdates.add(dateSelectionValue5);
		listofdates.add(dateSelectionValue6);

		endecaResult = new EndecaSearchResult();
		searchResultsViewData = new SearchResultsViewData();
		endecaResult.setDateSelectorDates(listofdates);


	}

//	*//**
//	 * Test method for {@link uk.co.portaltech.tui.populators.DateSelectorResultsPopulator#populate(uk.co.portaltech.tui.populators.DatePopulaterMediater, uk.co.portaltech.tui.web.view.data.SearchResultsViewData)}.
//	 *//*
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate()
	{
		Mockito.when(viewSelector.checkIsMobile()).thenReturn(true);
		Mockito.when(sessionService.getAttribute(Mockito.anyString())).thenReturn("off");
		
		datePopulator.populate(endecaResult, searchResultsViewData);
		dateSelectionViewData=searchResultsViewData.getAvailableDates();
		assertThat(dateSelectionViewData.getAvailableValues(),is(notNullValue()));
		assertThat(dateSelectionViewData.getAvailableValues().contains(DATE1), is(false));
		assertThat(dateSelectionViewData.getAvailableValues().contains(DATE3), is(false));
	}


	/*@Test
	public void testPopulateMaxValueWhenFlexibility()
	{

		datePopulator.populateMaxValue(requestWithFlexibility, dateSelectionViewData1);
		assertThat(dateSelectionViewData1.getMaxValue(),is(notNullValue()));
		assertThat(dateSelectionViewData1.getMaxValue(),is("26-12-2013"));

	}*/

	/*@Test
	public void testPopulateMaxValueWhenNoFlexibility()
	{

		datePopulator.populateMaxValue(requestWithOutFlexibility, dateSelectionViewData1);
		assertThat(dateSelectionViewData1.getMaxValue(),is(notNullValue()));
		assertThat(dateSelectionViewData1.getMaxValue(),is(UNTIL));
	}*/
/*	@Test
	public void testPopulateMinValueWhenNoFlexibility()
	{
		datePopulator.populateMinValue(requestWithOutFlexibility, dateSelectionViewData1);
		assertThat(dateSelectionViewData1.getMinValue(),is(notNullValue()));
		assertThat(dateSelectionViewData1.getMinValue(),is(WHEN));
	}
	//When is todays date
	@Test
	public void testPopulateMinValueWhenFlexibility()
	{
		datePopulator.populateMinValue(requestWithFlexibility, dateSelectionViewData1);
		assertThat(dateSelectionViewData1.getMinValue(),is(notNullValue()));
		assertThat(dateSelectionViewData1.getMinValue(),is("19-12-2013"));
	}

	//When is future date
	@Test
	public void testPopulateMinValueWhenFutureDate()
	{
		requestWithFlexibility.setWhen(FUTURE_WHEN);
		datePopulator.populateMinValue(requestWithFlexibility, dateSelectionViewData1);
		assertThat(dateSelectionViewData1.getMinValue(),is(notNullValue()));
		assertThat(dateSelectionViewData1.getMinValue(),is("25-12-2013"));
	}*/


}
