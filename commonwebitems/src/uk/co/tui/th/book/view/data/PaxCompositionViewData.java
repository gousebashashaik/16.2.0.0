/**
 *
 */
package uk.co.tui.th.book.view.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class holds party composition related view data.
 *
 * @author madhumathi.m
 *
 */
public class PaxCompositionViewData
{
   /**
    * Represents number of adults in the party.
    */
   private int noOfAdults;

   /**
    * Represents number of children in the party.
    */
   private int noOfChildren;

   /**
    * Represents number of infants in the party.
    */
   private int noOfInfants;

   /**
    * Represents number of seniors in the party.
    */
   private int noOfSeniors;

   /**
    * Represents number of seniors in the party.
    */
   private List<Integer> childAges;

   /**
    * Represents the string of all passengers in the party.
    */
   private String paxComposition = StringUtils.EMPTY;

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
    * @return the noOfInfants
    */
   public int getNoOfInfants()
   {
      return noOfInfants;
   }

   /**
    * @param noOfInfants the noOfInfants to set
    */
   public void setNoOfInfants(final int noOfInfants)
   {
      this.noOfInfants = noOfInfants;
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
    * Return the string representation of the object.
    *
    * @return the string representation of the object.
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return ToStringBuilder.reflectionToString(this);
   }

   /**
    * @return the childAges
    */
   public List<Integer> getChildAges()
   {
      if (CollectionUtils.isEmpty(this.childAges))
      {
         this.childAges = new ArrayList<Integer>();
      }
      return childAges;
   }

   /**
    * @param childAges the childAges to set
    */
   public void setChildAges(final List<Integer> childAges)
   {
      this.childAges = childAges;
   }

   /**
    * @return the paxComposition
    */
   public String getPaxComposition()
   {
      return paxComposition;
   }

   /**
    * @param paxComposition the paxComposition to set
    */
   public void setPaxComposition(final String paxComposition)
   {
      this.paxComposition = paxComposition;
   }

}
