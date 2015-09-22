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
public class RoomAllocation
{

   private int id;

   private int noOfAdults;

   private int noOfSeniors;

   private int noOfChildren;

   private int infantCount;

   /** children age sent by UI */
   private List<Integer> childrenAge = new ArrayList<Integer>();

   /**
    * Default constructor required by Jackson
    */
   public RoomAllocation()
   {

   }

   /**
    * @return the id
    */
   public int getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(final int id)
   {
      this.id = id;
   }

   /**
    * @return the noOfAdults
    */
   public int getNoOfAdults()
   {
      return noOfAdults;
   }

   /**
    * @param noOfAdults the noOfAdults to set
    */
   public void setNoOfAdults(final int noOfAdults)
   {
      this.noOfAdults = noOfAdults;
   }

   /**
    * @return the noOfSeniors
    */
   public int getNoOfSeniors()
   {
      return noOfSeniors;
   }

   /**
    * @param noOfSeniors the noOfSeniors to set
    */
   public void setNoOfSeniors(final int noOfSeniors)
   {
      this.noOfSeniors = noOfSeniors;
   }

   /**
    * @return the noOfChildren
    */
   public int getNoOfChildren()
   {
      return noOfChildren;
   }

   /**
    * @param noOfChildren the noOfChildren to set
    */
   public void setNoOfChildren(final int noOfChildren)
   {
      this.noOfChildren = noOfChildren;
   }

   /**
    * @return the childrenAge
    */
   public List<Integer> getChildrenAge()
   {
      return childrenAge;
   }

   /**
    * @param childrenAge the childrenAge to set
    */
   public void setChildrenAge(final List<Integer> childrenAge)
   {
      this.childrenAge = childrenAge;
   }

   /**
    * @return the infantCount
    */
   public int getInfantCount()
   {
      return infantCount;
   }

   /**
    * @param infantCount the infantCount to set
    */
   public void setInfantCount(final int infantCount)
   {
      this.infantCount = infantCount;
   }

}
