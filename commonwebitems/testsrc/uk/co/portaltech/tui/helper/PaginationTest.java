/**
 * 
 */
package uk.co.portaltech.tui.helper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author vivek.vk
 *
 */
public class PaginationTest{
	
	/**
	 * 
	 */
	private static final int TWENTY_THREE = 23;

	private EndecaSearchResult searchResult  = new EndecaSearchResult();
	
	private Pagination paginator = new Pagination();
	
	private SearchResultsRequestData searchReqData = new SearchResultsRequestData();

	/**
	 * 
	 */
	
	@Before
	public void setUp() throws Exception {
		init();
	}
	
	private void init() 
	{
		List<Holiday> holidays = new ArrayList<Holiday>();
		for(int i=0;i<TWENTY_THREE;i++)
		{		
			Holiday holiday = new Holiday();
			holiday.setPackageId(""+i);
			holidays.add(holiday);
		}			
		searchResult.setHolidays(holidays);
	}
	

	@SuppressWarnings("boxing")
	@Test
	public void testPaginationFirstPage()
	{	
		
		
		searchReqData.setFirst(1);
		searchReqData.setOffset(10);
		List<Holiday> pagedHolidays = paginator.paginateResults(searchResult, searchReqData);
		Assert.assertEquals(pagedHolidays.size(), 10);
	}
	
	@SuppressWarnings("boxing")
	@Test
	public void testPaginationLastPage()
	{	
		
		
		searchReqData.setFirst(3);
		searchReqData.setOffset(10);
		List<Holiday> pagedHolidays = paginator.paginateResults(searchResult, searchReqData);
		Assert.assertEquals(pagedHolidays.size(), 3);
	}
	
	@Test
	public void testPageableLogicUsingPages()
	{
		Pageable pages = new Pageable<Holiday>(searchResult.getHolidays());
		pages.setPage(2);
		Assert.assertEquals(pages.getListForPage().size(),10);
		pages.setPage(3);
		Assert.assertEquals(pages.getListForPage().size(),3);
	}

	@Test
	public void testPageableLogicUsingPageLimits()
	{
		Pageable pages = new Pageable<Holiday>(searchResult.getHolidays());
		Assert.assertEquals(pages.listByIndex(20, 30).size(),3);
		Assert.assertEquals(pages.listByIndex(1, 10).size(),9);
		Assert.assertEquals(pages.listByIndex(11, 20).size(),9);
		Assert.assertEquals(pages.listByIndex(0, 10).size(),10);
	}
	
	@SuppressWarnings("boxing")
	@Test
	public void testOffsetPageableLogicUsingPageLimits()
	{
		Pageable pages = new Pageable<Holiday>(searchResult.getHolidays(), 23);
		pages.setPage(1);
		Assert.assertEquals(pages.getListForPage().size(),23);
	}
	
	@Test
	public void testPageableLogic()
	{
		List<Holiday> holidays = searchResult.getHolidays();
		for(int i=TWENTY_THREE;i<55;i++)
		{		
			Holiday holiday = new Holiday();
			holiday.setPackageId(""+i);
			holidays.add(holiday);
		}			
		searchResult.setHolidays(holidays);		
		Assert.assertEquals(searchResult.getHolidays().size(),55);
		Pageable pages = new Pageable<Holiday>(searchResult.getHolidays());
		pages.setPageSize(5);
		Assert.assertEquals(pages.getMaxPageRange(), 11);		
	}
	
}
