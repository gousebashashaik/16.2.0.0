/**
 *
 */
package uk.co.tui.shortlist.view.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author Sravani
 *
 */
public class SavedHolidayInsuranceViewData
{

   /** The code. */
   private String code = StringUtils.EMPTY;

   /** The description. */
   private String description = StringUtils.EMPTY;

   /** The from adult price. */
   private String frmAdtPrice = StringUtils.EMPTY;

   /** The from child price. */
   private String frmChdPrice = StringUtils.EMPTY;

   /** The price. */
   private String price;

   /** The frm price. */
   private String frmPrice;

   /** The price with ew. */
   private String priceWithEW;

   /** The insurance type. */
   private boolean tandcAccepted;

   /** The selected. */
   private boolean selected;

   /** The currency appendedFrm price. */
   private String currencyAppendedFrmPrice = StringUtils.EMPTY;

   /** The pax composition. */
   private String paxComposition = StringUtils.EMPTY;

   /** The currency frm price. */
   private String currencyFrmPrice = StringUtils.EMPTY;

   /** The ins removed. */
   private boolean insRemoved;

   /** The quantity. */
   private int quantity;

   private String totalPrice;

   /** The over age message. */
   private String overAgeMessage = StringUtils.EMPTY;

   /** The description. */
   private String descriptionSummary = StringUtils.EMPTY;

   /**
    * @return the totalPrice
    */
   public String getTotalPrice()
   {
      return totalPrice;
   }

   /**
    * @param totalPrice the totalPrice to set
    */
   public void setTotalPrice(final String totalPrice)
   {
      this.totalPrice = totalPrice;
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
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(final String description)
   {
      this.description = description;
   }

   /**
    * @return the frmAdtPrice
    */
   public String getFrmAdtPrice()
   {
      return frmAdtPrice;
   }

   /**
    * @param frmAdtPrice the frmAdtPrice to set
    */
   public void setFrmAdtPrice(final String frmAdtPrice)
   {
      this.frmAdtPrice = frmAdtPrice;
   }

   /**
    * @return the frmChdPrice
    */
   public String getFrmChdPrice()
   {
      return frmChdPrice;
   }

   /**
    * @param frmChdPrice the frmChdPrice to set
    */
   public void setFrmChdPrice(final String frmChdPrice)
   {
      this.frmChdPrice = frmChdPrice;
   }

   /**
    * @return the price
    */
   public String getPrice()
   {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(final String price)
   {
      this.price = price;
   }

   /**
    * @return the frmPrice
    */
   public String getFrmPrice()
   {
      return frmPrice;
   }

   /**
    * @param frmPrice the frmPrice to set
    */
   public void setFrmPrice(final String frmPrice)
   {
      this.frmPrice = frmPrice;
   }

   /**
    * @return the priceWithEW
    */
   public String getPriceWithEW()
   {
      return priceWithEW;
   }

   /**
    * @param priceWithEW the priceWithEW to set
    */
   public void setPriceWithEW(final String priceWithEW)
   {
      this.priceWithEW = priceWithEW;
   }

   /**
    * @return the tandcAccepted
    */
   public boolean isTandcAccepted()
   {
      return tandcAccepted;
   }

   /**
    * @param tandcAccepted the tandcAccepted to set
    */
   public void setTandcAccepted(final boolean tandcAccepted)
   {
      this.tandcAccepted = tandcAccepted;
   }

   /**
    * @return the selected
    */
   public boolean isSelected()
   {
      return selected;
   }

   /**
    * @param selected the selected to set
    */
   public void setSelected(final boolean selected)
   {
      this.selected = selected;
   }

   /**
    * @return the currencyAppendedFrmPrice
    */
   public String getCurrencyAppendedFrmPrice()
   {
      return currencyAppendedFrmPrice;
   }

   /**
    * @param currencyAppendedFrmPrice the currencyAppendedFrmPrice to set
    */
   public void setCurrencyAppendedFrmPrice(final String currencyAppendedFrmPrice)
   {
      this.currencyAppendedFrmPrice = currencyAppendedFrmPrice;
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

   /**
    * @return the currencyFrmPrice
    */
   public String getCurrencyFrmPrice()
   {
      return currencyFrmPrice;
   }

   /**
    * @param currencyFrmPrice the currencyFrmPrice to set
    */
   public void setCurrencyFrmPrice(final String currencyFrmPrice)
   {
      this.currencyFrmPrice = currencyFrmPrice;
   }

   /**
    * @return the insRemoved
    */
   public boolean isInsRemoved()
   {
      return insRemoved;
   }

   /**
    * @param insRemoved the insRemoved to set
    */
   public void setInsRemoved(final boolean insRemoved)
   {
      this.insRemoved = insRemoved;
   }

   /**
    * @return the quantity
    */
   public int getQuantity()
   {
      return quantity;
   }

   /**
    * @param quantity the quantity to set
    */
   public void setQuantity(final int quantity)
   {
      this.quantity = quantity;
   }

   /**
    * @return the overAgeMessage
    */
   public String getOverAgeMessage()
   {
      return overAgeMessage;
   }

   /**
    * @param overAgeMessage the overAgeMessage to set
    */
   public void setOverAgeMessage(final String overAgeMessage)
   {
      this.overAgeMessage = overAgeMessage;
   }

   /**
    * @return the descriptionSummary
    */
   public String getDescriptionSummary()
   {
      return descriptionSummary;
   }

   /**
    * @param descriptionSummary the descriptionSummary to set
    */
   public void setDescriptionSummary(final String descriptionSummary)
   {
      this.descriptionSummary = descriptionSummary;
   }
}
