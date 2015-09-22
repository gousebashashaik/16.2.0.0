/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author extcs5
 *
 */
public class DestinationMainData
{

   private String value;

   private String name;

   private String id;

   private boolean selected;

   private String noAccommodations;

   private String categoryCode = StringUtils.EMPTY;

   private String noItineraries;

   private List<DestinationMainData> children;

   /**
    * @return the noItineraries
    */
   public String getNoItineraries()
   {
      return noItineraries;
   }

   /**
    * @param noItineraries the noItineraries to set
    */
   public void setNoItineraries(final String noItineraries)
   {
      this.noItineraries = noItineraries;
   }

   /**
    * @return the children
    */
   public List<DestinationMainData> getChildren()
   {
      return children;
   }

   /**
    * @param children the children to set
    */
   public void setChildren(final List<DestinationMainData> children)
   {
      this.children = children;
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
    * @return the noAccommodations
    */
   public String getNoAccommodations()
   {
      return noAccommodations;
   }

   /**
    * @param noAccommodations the noAccommodations to set
    */
   public void setNoAccommodations(final String noAccommodations)
   {
      this.noAccommodations = noAccommodations;
   }

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

   @Override
   public boolean equals(final Object rhs)
   {
      if (this == rhs)
      {
         return true;
      }
      if (rhs == null || getClass() != rhs.getClass())
      {
         return false;
      }

      final DestinationMainData unit = (DestinationMainData) rhs;
      return this.id.equals(unit.id);
   }

   @Override
   public int hashCode()
   {
      return id.hashCode();
   }

}
