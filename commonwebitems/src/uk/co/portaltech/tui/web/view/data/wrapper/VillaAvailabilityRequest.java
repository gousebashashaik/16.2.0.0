/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import uk.co.portaltech.commons.DateUtils;

/**
 * @author arya.ap
 *
 *         1) Agreed format with UI. (Please append to this in case any attributes are added or
 *         there is a change in Format.)
 *
 *         { "availabilityRequest" : { "code" : "", (holds the accommodation code) "noOfChildren" :
 *         "", (children count) "noOfAdults" : "", (adult count) "requestType" : "", (Possible
 *         values are 'paginate' or 'search'.) "startDate" : "" (start date in 'dd-mm-yyyy' format.
 *         Eg: 21-03-2013) } }
 *
 */
public class VillaAvailabilityRequest
{

   private String code;

   private String noOfChildren;

   private String noOfAdults;

   private String requestType;

   private String startDate;

   private String direction;

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
    * @return the noOfAdults
    */
   public String getNoOfAdults()
   {
      return noOfAdults;
   }

   /**
    * @param noOfAdults the noOfAdults to set
    */
   public void setNoOfAdults(final String noOfAdults)
   {
      this.noOfAdults = noOfAdults;
   }

   /**
    * @return the noOfChildren
    */
   public String getNoOfChildren()
   {
      return noOfChildren;
   }

   /**
    * @param noOfChildren the noOfChildren to set
    */
   public void setNoOfChildren(final String noOfChildren)
   {
      this.noOfChildren = noOfChildren;
   }

   /**
    * @return the requestType
    */
   public String getRequestType()
   {
      return requestType;
   }

   /**
    * @param requestType the requestType to set
    */
   public void setRequestType(final String requestType)
   {
      this.requestType = requestType;
   }

   /**
    * @return the startDate
    */
   public DateTime getStartDate()
   {
      return StringUtils.isEmpty(startDate) ? new DateTime() : DateUtils.toDateTime(startDate,
         "dd-MM-yyyy");
   }

   /**
    * @param startDate the startDate to set
    */
   public void setStartDate(final String startDate)
   {
      this.startDate = startDate;
   }

   /**
    * @return the direction
    */
   public String getDirection()
   {
      return direction;
   }

   /**
    * @param direction the direction to set
    */
   public void setDirection(final String direction)
   {
      this.direction = direction;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {
      if (obj == null)
      {
         return false;
      }
      if (!(obj instanceof VillaAvailabilityRequest))
      {
         return false;
      }
      final VillaAvailabilityRequest request = (VillaAvailabilityRequest) obj;

      return new EqualsBuilder().append(this.getCode(), request.getCode())
         .append(this.getNoOfChildren(), request.getNoOfChildren())
         .append(this.getNoOfAdults(), request.getNoOfAdults())
         .append(this.getRequestType(), request.getRequestType())
         .append(DateUtils.format(this.getStartDate()), DateUtils.format(request.getStartDate()))
         .isEquals();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      return new HashCodeBuilder().append(this.getCode()).append(this.getNoOfChildren())
         .append(this.getNoOfAdults()).append(this.getRequestType())
         .append(DateUtils.format(this.getStartDate())).toHashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      final StringBuilder sb =
         new StringBuilder(this.getCode()).append(this.getNoOfChildren())
            .append(this.getNoOfAdults()).append(this.getRequestType())
            .append(DateUtils.format(this.getStartDate()));
      return sb.toString();
   }

}
