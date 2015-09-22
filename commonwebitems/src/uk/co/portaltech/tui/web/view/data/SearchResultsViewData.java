/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.web.view.data.wrapper.SearchResultDateSelectionViewData;

public class SearchResultsViewData
{

   private SingleAccomSearchResultViewData singleAccomViewData;

   private boolean singleAccomSearch;

   private SearchResultDurationViewData durationSelection;

   private SearchResultDateSelectionViewData availableDates =
      new SearchResultDateSelectionViewData();

   private FilterPanel filterPanel;

   private Map<String, String> sortBy = new HashMap<String, String>();

   private List<SearchResultViewData> holidays = new ArrayList<SearchResultViewData>();

   private String searchSource;

   private int endecaResultsCount;

   private int prioritizedDuration;

   // Added to get duration selected for mobile
   private int duration;

   private String siteBrand;

   private boolean aniteSwitch;

   private boolean isRecsEngineSearch;

   private boolean shortlistEnabled;
   
   /**
	 * @return the shortlistEnabled
	 */
	public boolean isShortlistEnabled()
	{
		return shortlistEnabled;
	}

	/**
	 * @param shortlistEnabled the shortlistEnabled to set
	 */
	public void setShortlistEnabled(boolean shortlistEnabled)
	{
		this.shortlistEnabled = shortlistEnabled;
	}

	/**
    * @return the isRecsEngineSearch
    */
   public boolean isRecsEngineSearch()
   {
      return isRecsEngineSearch;
   }

   /**
    * @param isRecsEngineSearch the isRecsEngineSearch to set
    */
   public void setRecsEngineSearch(final boolean isRecsEngineSearch)
   {
      this.isRecsEngineSearch = isRecsEngineSearch;
   }

   /**
    * @return the siteBrand
    */
   public String getSiteBrand()
   {
      return siteBrand;
   }

   /**
    * @param siteBrand the siteBrand to set
    */
   public void setSiteBrand(final String siteBrand)
   {
      this.siteBrand = siteBrand;
   }

   /**
    * @return the filterPanel
    */
   public FilterPanel getFilterPanel()
   {
      return filterPanel;
   }

   /**
    * @param filterPanel the filterPanel to set
    */
   public void setFilterPanel(final FilterPanel filterPanel)
   {
      this.filterPanel = filterPanel;
   }

   /**
    * @return the availableDates
    */
   public SearchResultDateSelectionViewData getAvailableDates()
   {
      return availableDates;
   }

   /**
    * @param availableDates the availableDates to set
    */
   public void setAvailableDates(final SearchResultDateSelectionViewData availableDates)
   {
      this.availableDates = availableDates;
   }

   /**
    * @return the durationSelection
    */
   public SearchResultDurationViewData getDurationSelection()
   {
      return durationSelection;
   }

   /**
    * @param durationSelection the durationSelection to set
    */
   public void setDurationSelection(final SearchResultDurationViewData durationSelection)
   {
      this.durationSelection = durationSelection;
   }

   /**
    * @return the searchResults
    */
   public List<SearchResultViewData> getHolidays()
   {
      return holidays;
   }

   /**
    * @param searchResults the searchResults to set
    */
   public void setHolidays(final List<SearchResultViewData> holidays)
   {
      this.holidays = holidays;
   }

   /**
    * @return the sortBy
    */
   public Map<String, String> getSortBy()
   {
      return sortBy;
   }

   /**
    * @param sortBy the sortBy to set
    */
   public void setSortBy(final Map<String, String> sortBy)
   {
      this.sortBy = sortBy;
   }

   /**
    * @return the searchSource
    */
   public String getSearchSource()
   {
      return searchSource;
   }

   /**
    * @param searchSource the searchSource to set
    */
   public void setSearchSource(final String searchSource)
   {
      this.searchSource = searchSource;
   }

   /**
    * @return the endecaResultsCount
    */
   public int getEndecaResultsCount()
   {
      return endecaResultsCount;
   }

   /**
    * @param endecaResultsCount the endecaResultsCount to set
    */
   public void setEndecaResultsCount(final int endecaResultsCount)
   {
      this.endecaResultsCount = endecaResultsCount;
   }

   /**
    * @return the prioritizedDuration
    */
   public int getPrioritizedDuration()
   {
      return prioritizedDuration;
   }

   /**
    * @param prioritizedDuration the prioritizedDuration to set
    */
   public void setPrioritizedDuration(final int prioritizedDuration)
   {
      this.prioritizedDuration = prioritizedDuration;
   }

   /**
    * @return the singleAccomViewData
    */
   public SingleAccomSearchResultViewData getSingleAccomViewData()
   {
      return singleAccomViewData;
   }

   /**
    * @param singleAccomViewData the singleAccomViewData to set
    */
   public void setSingleAccomViewData(final SingleAccomSearchResultViewData singleAccomViewData)
   {
      this.singleAccomViewData = singleAccomViewData;
   }

   /**
    * @return the singleAccomSearch
    */
   public boolean isSingleAccomSearch()
   {
      return singleAccomSearch;
   }

   /**
    * @param singleAccomSearch the singleAccomSearch to set
    */
   public void setSingleAccomSearch(final boolean singleAccomSearch)
   {
      this.singleAccomSearch = singleAccomSearch;
   }

   /**
    * @return the duration
    */
   public int getDuration()
   {
      return duration;
   }

   /**
    * @param duration the duration to set
    */
   public void setDuration(final int duration)
   {
      this.duration = duration;
   }

   /**
    * @return the aniteSwitch
    */
   public boolean isAniteSwitch()
   {
      return aniteSwitch;
   }

   /**
    * @param aniteSwitch the aniteSwitch to set
    */
   public void setAniteSwitch(final boolean aniteSwitch)
   {
      this.aniteSwitch = aniteSwitch;
   }
}
