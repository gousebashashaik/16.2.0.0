/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author Manju.ts
 *
 */
public class FilterRequest
{

   private String value;

   private String parent;

   private String categoryCode;

   private String id;

   private int noAccommodations;

   private String name;

   private boolean selected;

   private boolean disabled;

   private boolean countShowing;

   private int noItineraries;

   private String sortBy;

   // This is added to map the object local storage
   private List<String> availableDates;

   /**
    * @return the value
    */
   public String getValue()
   {
      return value;
   }

   /**
    * @param value the value to set
    */
   public void setValue(final String value)
   {
      this.value = value;
   }

   /**
    * @return the parent
    */
   public String getParent()
   {
      return parent;
   }

   /**
    * @param parent the parent to set
    */
   public void setParent(final String parent)
   {
      this.parent = parent;
   }

   /**
    * @return the categoryCode
    */
   public String getCategoryCode()
   {
      return categoryCode;
   }

   /**
    * @param categoryCode the categoryCode to set
    */
   public void setCategoryCode(final String categoryCode)
   {
      this.categoryCode = categoryCode;
   }

   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(final String id)
   {
      this.id = id;
   }

   /**
    * @return the noAccommodations
    */
   public int getNoAccommodations()
   {
      return noAccommodations;
   }

   /**
    * @param noAccommodations the noAccommodations to set
    */
   public void setNoAccommodations(final int noAccommodations)
   {
      this.noAccommodations = noAccommodations;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * @return the selected
    */
   public boolean isSelected()
   {
      return selected;
   }

   /**
    * @param selected the selected to set
    */
   public void setSelected(final boolean selected)
   {
      this.selected = selected;
   }

   /**
    * @return the disabled
    */
   public boolean isDisabled()
   {
      return disabled;
   }

   /**
    * @param disabled the disabled to set
    */
   public void setDisabled(final boolean disabled)
   {
      this.disabled = disabled;
   }

   /**
    * @return the countShowing
    */
   public boolean isCountShowing()
   {
      return countShowing;
   }

   /**
    * @param countShowing the countShowing to set
    */
   public void setCountShowing(final boolean countShowing)
   {
      this.countShowing = countShowing;
   }

   /**
    * @return the noItineraries
    */
   public int getNoItineraries()
   {
      return noItineraries;
   }

   /**
    * @param noItineraries the noItineraries to set
    */
   public void setNoItineraries(final int noItineraries)
   {
      this.noItineraries = noItineraries;
   }

   /**
    * @return the sortBy
    */
   public String getSortBy()
   {
      return sortBy;
   }

   /**
    * @param sortBy the sortBy to set
    */
   public void setSortBy(final String sortBy)
   {
      this.sortBy = sortBy;
   }

   /**
    * @return the availableDates
    */
   public List<String> getAvailableDates()
   {
      return availableDates;
   }

   /**
    * @param availableDates the availableDates to set
    */
   public void setAvailableDates(final List<String> availableDates)
   {
      this.availableDates = availableDates;
   }
}
