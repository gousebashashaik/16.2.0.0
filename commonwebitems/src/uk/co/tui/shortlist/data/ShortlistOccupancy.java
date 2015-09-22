/**
 *
 */
package uk.co.tui.shortlist.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistOccupancy
{
   private int adults;

   private int children;

   private int infant;

   private int seniors;

   private List<ShortlistPaxDetails> paxDetail = new ArrayList<ShortlistPaxDetails>();

   /**
    * @return the adults
    */
   public int getAdults()
   {
      return adults;
   }

   /**
    * @param adults the adults to set
    */
   public void setAdults(final int adults)
   {
      this.adults = adults;
   }

   /**
    * @return the children
    */
   public int getChildren()
   {
      return children;
   }

   /**
    * @param children the children to set
    */
   public void setChildren(final int children)
   {
      this.children = children;
   }

   /**
    * @return the infant
    */
   public int getInfant()
   {
      return infant;
   }

   /**
    * @param infant the infant to set
    */
   public void setInfant(final int infant)
   {
      this.infant = infant;
   }

   /**
    * @return the seniors
    */
   public int getSeniors()
   {
      return seniors;
   }

   /**
    * @param seniors the seniors to set
    */
   public void setSeniors(final int seniors)
   {
      this.seniors = seniors;
   }

   /**
    * @return the paxDetail
    */
   public List<ShortlistPaxDetails> getPaxDetail()
   {
      return paxDetail;
   }

   /**
    * @param paxDetail the paxDetail to set
    */
   public void setPaxDetail(final List<ShortlistPaxDetails> paxDetail)
   {
      this.paxDetail = paxDetail;
   }

}
