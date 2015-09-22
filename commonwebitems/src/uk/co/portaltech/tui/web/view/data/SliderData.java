/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author manju.ts
 *
 */
public class SliderData
{

   private String id;

   private String name;

   private List<?> range;

   private int steps;

   private List<?> values;

   private List<?> limit;

   private String trackType;

   private String code;

   private String maxValue;

   private boolean repaint;

   private String type;

   /**
    * @return the type
    */
   public String getType()
   {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(final String type)
   {
      this.type = type;
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
    * @return the range
    */
   public List<?> getRange()
   {
      return range;
   }

   /**
    * @param range the range to set
    */
   public void setRange(final List<?> range)
   {
      this.range = range;
   }

   /**
    * @return the steps
    */
   public int getSteps()
   {
      return steps;
   }

   /**
    * @param steps the steps to set
    */
   public void setSteps(final int steps)
   {
      this.steps = steps;
   }

   /**
    * @return the values
    */
   public List<?> getValues()
   {
      return values;
   }

   /**
    * @param values the values to set
    */
   public void setValues(final List<?> values)
   {
      this.values = values;
   }

   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(final String id)
   {
      this.id = id;
   }

   /**
    * @return the limit
    */
   public List<?> getLimit()
   {
      return limit;
   }

   /**
    * @param limit the limit to set
    */
   public void setLimit(final List<?> limit)
   {
      this.limit = limit;
   }

   /**
    * @return the tracType
    */
   public String getTrackType()
   {
      return trackType;
   }

   /**
    * @param trackType the tracType to set
    */
   public void setTrackType(final String trackType)
   {
      this.trackType = trackType;
   }

   /**
    * @return the code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * @param code the code to set
    */
   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @return the maxValue
    */
   public String getMaxValue()
   {
      return maxValue;
   }

   /**
    * @param maxValue the maxValue to set
    */
   public void setMaxValue(final String maxValue)
   {
      this.maxValue = maxValue;
   }

   /**
    * @return the repaint
    */
   public boolean isRepaint()
   {
      return repaint;
   }

   /**
    * @param repaint the repaint to set
    */
   public void setRepaint(final boolean repaint)
   {
      this.repaint = repaint;
   }

}
