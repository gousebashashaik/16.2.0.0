package uk.co.portaltech.tui.web.view.data;

import java.util.Set;
import java.util.TreeSet;

public class SearchResultDurationViewData
{

   private Set<Integer> defaultDisplay = new TreeSet<Integer>();

   private Integer activeDuration;

   private Set<Integer> moreChoices = new TreeSet<Integer>();

   public SearchResultDurationViewData()
   {
   }

   /**
    * @return the selected
    */
   public Integer getActiveDuration()
   {
      return activeDuration;
   }

   /**
    * @param selected the selected to set
    */
   public void setActiveDuration(final Integer activeDuration)
   {
      this.activeDuration = activeDuration;
   }

   /**
    * @return the defaultDisplay
    */
   public Set<Integer> getDefaultDisplay()
   {
      return defaultDisplay;
   }

   /**
    * @param defaultDisplay the defaultDisplay to set
    */
   public void setDefaultDisplay(final Set<Integer> defaultDisplay)
   {
      this.defaultDisplay = defaultDisplay;
   }

   /**
    * @return the moreChoices
    */
   public Set<Integer> getMoreChoices()
   {
      return moreChoices;
   }

   /**
    * @param moreChoices the moreChoices to set
    */
   public void setMoreChoices(final Set<Integer> moreChoices)
   {
      this.moreChoices = moreChoices;
   }

}