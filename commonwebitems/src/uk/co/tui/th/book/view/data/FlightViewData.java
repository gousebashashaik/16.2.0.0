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
 * This class holds the Flights related view data.
 *
 * @author madhumathi.m
 *
 */
public class FlightViewData
{
   /**
    * Holds the duration of the flight.
    */
   private int duration;

   /** Holds the collection of Flight Sectors for Outbound. */
   private List<Leg> outboundSectors;

   /** Holds the collection of Flight Sectors for Inbound. */
   private List<Leg> inboundSectors;

   /** The highlights. */
   private List<String> highlights;

   /** The duration summary. */
   private String durationSummary = StringUtils.EMPTY;

   private String haulType = StringUtils.EMPTY;

   private boolean isInboundThirdParty;

   private boolean isOutBoundThirdParty;

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
   public List<Leg> getOutboundSectors()
   {
      if (CollectionUtils.isEmpty(this.outboundSectors))
      {
         this.outboundSectors = new ArrayList<Leg>();
      }
      return this.outboundSectors;
   }

   /**
    * Gets the inbound sectors.
    *
    * @return the inboundSectors
    */
   public List<Leg> getInboundSectors()
   {
      if (CollectionUtils.isEmpty(this.inboundSectors))
      {
         this.inboundSectors = new ArrayList<Leg>();
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

   /**
    * @return the durationSummary
    */
   public String getDurationSummary()
   {
      return durationSummary;
   }

   /**
    * @param durationSummary the durationSummary to set
    */
   public void setDurationSummary(final String durationSummary)
   {
      this.durationSummary = durationSummary;
   }

   /**
    * @return the haulType
    */
   public String getHaulType()
   {
      return haulType;
   }

   /**
    * @param haulType the haulType to set
    */
   public void setHaulType(final String haulType)
   {
      this.haulType = haulType;
   }

   /**
    * @return the isInboundThirdParty
    */
   public boolean isInboundThirdParty()
   {
      return isInboundThirdParty;
   }

   /**
    * @param isInboundThirdParty the isInboundThirdParty to set
    */
   public void setInboundThirdParty(final boolean isInboundThirdParty)
   {
      this.isInboundThirdParty = isInboundThirdParty;
   }

   /**
    * @return the isOutBoundThirdParty
    */
   public boolean isOutBoundThirdParty()
   {
      return isOutBoundThirdParty;
   }

   /**
    * @param isOutBoundThirdParty the isOutBoundThirdParty to set
    */
   public void setOutBoundThirdParty(final boolean isOutBoundThirdParty)
   {
      this.isOutBoundThirdParty = isOutBoundThirdParty;
   }

}
