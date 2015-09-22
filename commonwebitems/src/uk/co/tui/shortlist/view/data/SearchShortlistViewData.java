/**
 *
 */
package uk.co.tui.shortlist.view.data;

/**
 * @author shwetha.rb
 *
 */
public class SearchShortlistViewData
{
   private boolean resFlag;

   private String packageId;

   /**
    * @return the packageId
    */

   public String getPackageId()
   {
      return packageId;
   }

   /**
    * @param packageId the packageId to set
    */

   public void setPackageId(final String packageId)
   {
      this.packageId = packageId;
   }

   /**
    * @return the resFlag
    */
   public boolean isResFlag()
   {
      return resFlag;
   }

   /**
    * @param resFlag the resFlag to set
    */
   public void setResFlag(final boolean resFlag)
   {
      this.resFlag = resFlag;
   }

}
