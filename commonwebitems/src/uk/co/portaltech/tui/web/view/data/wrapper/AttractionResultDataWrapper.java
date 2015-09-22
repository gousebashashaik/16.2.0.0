/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.travel.thirdparty.endeca.SortOption;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author Abi
 *
 */
public class AttractionResultDataWrapper
{

   private List<ExcursionViewData> excursions;

   private List<AttractionViewData> events;

   private List<AttractionViewData> sights;

   private List<SortOption> sortBy;

   private String topLocationName;

   private String selectedSortOption;

   /**
     *
     */
   public AttractionResultDataWrapper()
   {
      super();
      excursions = new ArrayList<ExcursionViewData>();
      events = new ArrayList<AttractionViewData>();
      sights = new ArrayList<AttractionViewData>();

   }

   /**
    * @return the excursions
    */
   public List<ExcursionViewData> getExcursions()
   {
      return excursions;
   }

   /**
    * @param excursions the excursions to set
    */
   public void setExcursions(final List<ExcursionViewData> excursions)
   {
      this.excursions = excursions;
   }

   /**
    * @return the sortBy
    */
   public List<SortOption> getSortBy()
   {
      return sortBy;
   }

   /**
    * @param sortBy the sortBy to set
    */
   public void setSortBy(final List<SortOption> sortBy)
   {
      this.sortBy = sortBy;
   }

   /**
    * @return the events
    */
   public List<AttractionViewData> getEvents()
   {
      return events;
   }

   /**
    * @param events the events to set
    */
   public void setEvents(final List<AttractionViewData> events)
   {
      this.events = events;
   }

   /**
    * @return the sights
    */
   public List<AttractionViewData> getSights()
   {
      return sights;
   }

   /**
    * @param sights the sights to set
    */
   public void setSights(final List<AttractionViewData> sights)
   {
      this.sights = sights;
   }

   /**
    * @return the topLocationName
    */
   public String getTopLocationName()
   {
      return topLocationName;
   }

   /**
    * @param topLocationName the topLocationName to set
    */
   public void setTopLocationName(final String topLocationName)
   {
      this.topLocationName = topLocationName;
   }

   /**
    * @return the selectedSortOption
    */
   public String getSelectedSortOption()
   {
      return selectedSortOption;
   }

   /**
    * @param selectedSortOption the selectedSortOption to set
    */
   public void setSelectedSortOption(final String selectedSortOption)
   {
      this.selectedSortOption = selectedSortOption;
   }

}
