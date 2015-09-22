/**
 *
 */
package uk.co.tui.fj.book.view.data;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class holds the airport related view data.
 *
 * @author madhumathi.m
 *
 */
public class Airport
{

   /** The airport code. */
   private String code = StringUtils.EMPTY;

   /** The airport name. */
   private String name = StringUtils.EMPTY;

   /**
    * @return the code
    */
   public String getCode()
   {
      return this.code;
   }

   /**
    * @param code the code to set
    */
   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * @param name the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
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
}
