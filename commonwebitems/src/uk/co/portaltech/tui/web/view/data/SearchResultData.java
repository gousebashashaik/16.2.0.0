/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author omonikhide
 *
 */
import java.util.List;

import uk.co.portaltech.travel.model.results.EndecaSearchResult;
import uk.co.portaltech.travel.thirdparty.endeca.HolidayPackageItem;
import uk.co.portaltech.travel.thirdparty.endeca.PaginationData;
import uk.co.portaltech.travel.thirdparty.endeca.QuickSearchAutoSuggestItem;
import uk.co.portaltech.travel.thirdparty.endeca.SortOption;
import uk.co.portaltech.travel.thirdparty.endeca.TabResult;

public class SearchResultData<T>
{

   private List<T> results;

   private PaginationData pagination;

   private List<SortOption> sortOptions;

   private QuickSearchAutoSuggestItem quickSearchAutoSuggestItem;

   private String heading;

   private HolidayPackageItem holidayPackageItem;

   private EndecaSearchResult searchResult;

   private List<TabResult> tabResults;

   /**
    * @return the holidayPackageItem
    */
   public HolidayPackageItem getHolidayPackageItem()
   {
      return holidayPackageItem;
   }

   /**
    * @param holidayPackageItem the holidayPackageItem to set
    */
   public void setHolidayPackageItem(final HolidayPackageItem holidayPackageItem)
   {
      this.holidayPackageItem = holidayPackageItem;
   }

   public List<T> getResults()
   {
      return results;
   }

   public void setResults(final List<T> results)
   {
      this.results = results;
   }

   public PaginationData getPagination()
   {
      return pagination;
   }

   /**
    * @return the sortOptions
    */
   public List<SortOption> getSortOptions()
   {
      return sortOptions;
   }

   /**
    * @param sortOptions the sortOptions to set
    */
   public void setSortOptions(final List<SortOption> sortOptions)
   {
      this.sortOptions = sortOptions;
   }

   public void setPagination(final PaginationData pagination)
   {
      this.pagination = pagination;
   }

   /**
    * @return the quickSearchAutoSuggestItem
    */
   public QuickSearchAutoSuggestItem getQuickSearchAutoSuggestItem()
   {
      return quickSearchAutoSuggestItem;
   }

   /**
    * @param quickSearchAutoSuggestItem the quickSearchAutoSuggestItem to set
    */
   public void setQuickSearchAutoSuggestItem(
      final QuickSearchAutoSuggestItem quickSearchAutoSuggestItem)
   {
      this.quickSearchAutoSuggestItem = quickSearchAutoSuggestItem;
   }

   /**
    * @return the heading
    */
   public String getHeading()
   {
      return heading;
   }

   /**
    * @param heading the heading to set
    */
   public void setHeading(final String heading)
   {
      this.heading = heading;
   }

   /**
    * @return the tabResults
    */
   public List<TabResult> getTabResults()
   {
      return tabResults;
   }

   /**
    * @param tabResults the tabResults to set
    */
   public void setTabResults(final List<TabResult> tabResults)
   {
      this.tabResults = tabResults;
   }

   /**
    * @return the searchResult
    */
   public EndecaSearchResult getSearchResult()
   {
      return searchResult;
   }

   /**
    * @param searchResult the searchResult to set
    */
   public void setSearchResult(final EndecaSearchResult searchResult)
   {
      this.searchResult = searchResult;
   }

}
