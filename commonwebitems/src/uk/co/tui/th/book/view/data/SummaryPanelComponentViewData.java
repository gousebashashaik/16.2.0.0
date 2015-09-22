/**
 *
 */
package uk.co.tui.th.book.view.data;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author thyagaraju.e
 *
 */
public class SummaryPanelComponentViewData
{

   /** The expanded. */
   private String expanded;

   /** The name. */
   private String name;

   /** The summary panel urls view data. */
   private SummaryPanelUrlsViewData summaryPanelUrlsViewData;

   private static final int PRIME = 31;

   /**
    * Instantiates a new summary panel urls view data.
    *
    * @param expanded the expanded
    * @param name the name
    * @param summaryPanelUrlsViewData the summary panel urls view data
    */
   public SummaryPanelComponentViewData(final String expanded, final String name,
                                        final SummaryPanelUrlsViewData summaryPanelUrlsViewData)
   {
      this.expanded = expanded;
      this.name = name;
      this.summaryPanelUrlsViewData = summaryPanelUrlsViewData;
   }

   /**
    * @return the expanded
    */
   public String getExpanded()
   {
      return expanded;
   }

   /**
    * @param expanded the expanded to set
    */
   public void setExpanded(final String expanded)
   {
      this.expanded = expanded;
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
    * @return the summaryPanelUrlsViewData
    */
   public SummaryPanelUrlsViewData getSummaryPanelUrlsViewData()
   {
      return summaryPanelUrlsViewData;
   }

   /**
    * @param summaryPanelUrlsViewData the summaryPanelUrlsViewData to set
    */
   public void setSummaryPanelUrlsViewData(final SummaryPanelUrlsViewData summaryPanelUrlsViewData)
   {
      this.summaryPanelUrlsViewData = summaryPanelUrlsViewData;
   }

   @Override
   public int hashCode()
   {

      int result = 1;
      result = PRIME * result + ((expanded == null) ? 0 : expanded.hashCode());
      result = PRIME * result + ((name == null) ? 0 : name.hashCode());
      result =
         PRIME * result
            + ((summaryPanelUrlsViewData == null) ? 0 : summaryPanelUrlsViewData.hashCode());
      return result;
   }

   /*
    * Overridden equals method of the SummaryPanelComponentViewData instance using EqualsBuilder.
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {
      if (obj instanceof SummaryPanelComponentViewData)
      {
         final SummaryPanelComponentViewData other = (SummaryPanelComponentViewData) obj;
         return new EqualsBuilder().append(expanded, other.expanded).append(name, other.name)
            .append(summaryPanelUrlsViewData, other.summaryPanelUrlsViewData).isEquals();
      }
      else
      {
         return false;
      }
   }

}
