/**
 *
 */
package uk.co.portaltech.tui.model;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.travel.model.FilterComponentModel;

public class FilterSectionComponent implements Comparable<FilterSectionComponent>
{

   private String uid;

   private String name;

   private Integer position;

   private List<FilterComponentModel> filterComponent = new ArrayList<FilterComponentModel>();

   private static final int PRIME = 31;

   /**
    * @return the uid
    */
   public String getUid()
   {
      return uid;
   }

   /**
    * @param uid the uid to set
    */
   public void setUid(final String uid)
   {
      this.uid = uid;
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
    * @return the position
    */
   public Integer getPosition()
   {
      return position;
   }

   /**
    * @param position the position to set
    */
   public void setPosition(final Integer position)
   {
      this.position = position;
   }

   /**
    * @return the filterComponent
    */
   public List<FilterComponentModel> getFilterComponent()
   {
      return filterComponent;
   }

   /**
    * @param filterComponent the filterComponent to set
    */
   public void setFilterComponent(final List<FilterComponentModel> filterComponent)
   {
      this.filterComponent = filterComponent;
   }

   /**
    * Sorting Logic
    */
   @SuppressWarnings("boxing")
   @Override
   public int compareTo(final FilterSectionComponent o)
   {

      if (position != null && o.getPosition() != null)
      {
         return position - o.getPosition();
      }
      return -1;

   }

   /**
    * @return integer
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {

      int result = 1;
      result = PRIME * result + ((position == null) ? 0 : position.hashCode());
      return result;
   }

   /**
    * @param obj
    * @return boolean
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      return positionEquals(obj);
   }

   /**
    * @param obj
    * @return boolean
    */
   private boolean positionEquals(final Object obj)
   {
      final FilterSectionComponent other = (FilterSectionComponent) obj;
      if (position == null)
      {
         if (other.position != null)
         {
            return false;
         }
      }
      else if (!position.equals(other.position))
      {
         return false;
      }
      return true;
   }
}
