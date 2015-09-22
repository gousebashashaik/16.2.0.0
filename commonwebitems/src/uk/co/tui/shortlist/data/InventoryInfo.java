/**
 *
 */
package uk.co.tui.shortlist.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author akhileshvarma.d
 *
 */
public class InventoryInfo
{

   private String productCode = StringUtils.EMPTY;

   private String subProductCode = StringUtils.EMPTY;

   private String primeSubProductCode = StringUtils.EMPTY;

   private String sellingCode = StringUtils.EMPTY;

   private String tracsPackageId = StringUtils.EMPTY;

   private String prom = StringUtils.EMPTY;

   private String atcomId = StringUtils.EMPTY;

   /**
    * @return the productCode
    */
   public String getProductCode()
   {
      return productCode;
   }

   /**
    * @param productCode the productCode to set
    */
   public void setProductCode(final String productCode)
   {
      this.productCode = productCode;
   }

   /**
    * @return the subProductCode
    */
   public String getSubProductCode()
   {
      return subProductCode;
   }

   /**
    * @param subProductCode the subProductCode to set
    */
   public void setSubProductCode(final String subProductCode)
   {
      this.subProductCode = subProductCode;
   }

   /**
    * @return the primeSubProductCode
    */
   public String getPrimeSubProductCode()
   {
      return primeSubProductCode;
   }

   /**
    * @param primeSubProductCode the primeSubProductCode to set
    */
   public void setPrimeSubProductCode(final String primeSubProductCode)
   {
      this.primeSubProductCode = primeSubProductCode;
   }

   /**
    * @return the sellingCode
    */
   public String getSellingCode()
   {
      return sellingCode;
   }

   /**
    * @param sellingCode the sellingCode to set
    */
   public void setSellingCode(final String sellingCode)
   {
      this.sellingCode = sellingCode;
   }

   /**
    * @return the tracsPackageId
    */
   public String getTracsPackageId()
   {
      return tracsPackageId;
   }

   /**
    * @param tracsPackageId the tracsPackageId to set
    */
   public void setTracsPackageId(final String tracsPackageId)
   {
      this.tracsPackageId = tracsPackageId;
   }

   /**
    * @return the prom
    */
   public String getProm()
   {
      return prom;
   }

   /**
    * @param prom the prom to set
    */
   public void setProm(final String prom)
   {
      this.prom = prom;
   }

   /**
    * @return the atcomId
    */
   public String getAtcomId()
   {
      return atcomId;
   }

   /**
    * @param atcomId the atcomId to set
    */
   public void setAtcomId(final String atcomId)
   {
      this.atcomId = atcomId;
   }

}
