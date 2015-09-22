/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author extcs5
 *
 */
public class FilterData
{

   private String name;

   private List<?> filters;

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
    * @return the filters
    */
   public List<?> getFilters()
   {
      return filters;
   }

   /**
    * @param filters the filters to set
    */
   public void setFilters(final List<?> filters)
   {
      this.filters = filters;
   }

}
