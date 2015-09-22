/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author extcs5
 *
 */
public class CommonData
{

   private int noItineraries;

   private List<CommonData> children = new ArrayList<CommonData>();

   private int noAccommodations;

   private String name;

   private String value;

   private boolean selected;

   private String id;

   private String parentCode;

   private String categoryCode;

   private String tooltip;

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
    * @return the childs
    */
   public List<CommonData> getChildren()
   {
      return children;
   }

   /**
    * @param childs the childs to set
    */
   public void setChilds(final List<CommonData> children)
   {
      this.children = children;
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
    * @return the parentCode
    */
   public String getParentCode()
   {
      return parentCode;
   }

   /**
    * @param parentCode the parentCode to set
    */
   public void setParentCode(final String parentCode)
   {
      this.parentCode = parentCode;
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
    * @return the tooltip
    */
   public String getTooltip()
   {
      return tooltip;
   }

   /**
    * @param tooltip the tooltip to set
    */
   public void setTooltip(final String tooltip)
   {
      this.tooltip = tooltip;
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

}
