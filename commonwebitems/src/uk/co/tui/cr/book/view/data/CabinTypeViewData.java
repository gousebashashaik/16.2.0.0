/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;


/**
 * @author ramkishore.p
 *
 */
public class CabinTypeViewData
{

   /** The list of cabin view data. */
   private List<CabinViewData> listOfCabinViewData;

   private String cabinType;

   /**
    * Gets the list of cabin view data.
    *
    * @return the list of cabin view data
    */
   public List<CabinViewData> getListOfCabinViewData()
   {
      return listOfCabinViewData;
   }

   /**
    * Sets the list of cabin view data.
    *
    * @param cabins
    *           the new list of cabin view data
    */
   public void setListOfCabinViewData(final List<CabinViewData> cabins)
   {
      this.listOfCabinViewData = cabins;
   }

   public String getCabinType()
   {
      return cabinType;
   }

   public void setCabinType(final String cabinType)
   {
      this.cabinType = cabinType;
   }

}
