/**
 *
 */
package uk.co.tui.shortlist.view.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

// TODO: Auto-generated Javadoc
/**
 * This class holds the Flights related view data.
 *
 * @author Sravani
 *
 */
public class SavedHolidayFlightViewData
{
   /**
    * Holds the duration of the flight.
    */
   private int duration;

   /** Holds the collection of Flight Sectors for Outbound. */
   private List<LegViewData> outboundSectors;

   /** Holds the collection of Flight Sectors for Inbound. */
   private List<LegViewData> inboundSectors;

   /** The highlights. */
   private List<String> highlights;

   /** Holds the whether Flight type */
   private boolean inboundDreamLiner;

   /** Holds the whether Flight type */
   private boolean outboundDreamLiner;

   /** Holds the difference of flexible days */
   private int departureDateDiff;

   /** Holds the difference of flexible days */
   private int arrivalDateDiff;

   /**
    * @return the inboundDreamLiner
    */
   public boolean isInboundDreamLiner()
   {
      return inboundDreamLiner;
   }

   /**
    * @param inboundDreamLiner the inboundDreamLiner to set
    */
   public void setInboundDreamLiner(final boolean inboundDreamLiner)
   {
      this.inboundDreamLiner = inboundDreamLiner;
   }

   /**
    * @return the outboundDreamLiner
    */
   public boolean isOutboundDreamLiner()
   {
      return outboundDreamLiner;
   }

   /**
    * @param outboundDreamLiner the outboundDreamLiner to set
    */
   public void setOutboundDreamLiner(final boolean outboundDreamLiner)
   {
      this.outboundDreamLiner = outboundDreamLiner;
   }

   /**
    * @return the arrivalDateDiff
    */
   public int getArrivalDateDiff()
   {
      return arrivalDateDiff;
   }

   /**
    * @param arrivalDateDiff the arrivalDateDiff to set
    */
   public void setArrivalDateDiff(final int arrivalDateDiff)
   {
      this.arrivalDateDiff = arrivalDateDiff;
   }

   /**
    * @param outboundSectors the outboundSectors to set
    */
   public void setOutboundSectors(final List<LegViewData> outboundSectors)
   {
      this.outboundSectors = outboundSectors;
   }

   /**
    * @param inboundSectors the inboundSectors to set
    */
   public void setInboundSectors(final List<LegViewData> inboundSectors)
   {
      this.inboundSectors = inboundSectors;
   }

   /**
    * @param highlights the highlights to set
    */
   public void setHighlights(final List<String> highlights)
   {
      this.highlights = highlights;
   }

   /**
    * @return the departureDateDiff
    */
   public int getDepartureDateDiff()
   {
      return departureDateDiff;
   }

   /**
    * @param departureDateDiff the departureDateDiff to set
    */
   public void setDepartureDateDiff(final int departureDateDiff)
   {
      this.departureDateDiff = departureDateDiff;
   }

   /**
    * Gets the duration.
    *
    * @return the duration
    */
   public int getDuration()
   {
      return this.duration;
   }

   /**
    * Sets the duration.
    *
    * @param duration the duration to set
    */
   public void setDuration(final int duration)
   {
      this.duration = duration;
   }

   /**
    * Gets the outbound sectors.
    *
    * @return the outboundSectors
    */
   public List<LegViewData> getOutboundSectors()
   {
      if (CollectionUtils.isEmpty(this.outboundSectors))
      {
         this.outboundSectors = new ArrayList<LegViewData>();
      }
      return this.outboundSectors;
   }

   /**
    * Gets the inbound sectors.
    *
    * @return the inboundSectors
    */
   public List<LegViewData> getInboundSectors()
   {
      if (CollectionUtils.isEmpty(this.inboundSectors))
      {
         this.inboundSectors = new ArrayList<LegViewData>();
      }
      return this.inboundSectors;
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
    * Gets the highlights.
    *
    * @return the highlights
    */
   public List<String> getHighlights()
   {
      if (CollectionUtils.isEmpty(this.highlights))
      {
         this.highlights = new ArrayList<String>();
      }
      return this.highlights;
   }
}
