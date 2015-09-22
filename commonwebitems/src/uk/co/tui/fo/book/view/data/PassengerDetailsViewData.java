/**
 *
 */
package uk.co.tui.fo.book.view.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.util.CollectionUtils;

import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.book.view.data.CommunicationPreferencesViewData;
import uk.co.tui.book.view.data.CustomerSignInViewData;

/**
 * This class holds all the data required for the population of the view data in the
 * PassengerDetails page.
 *
 * @author madhumathi.m
 *
 */
public class PassengerDetailsViewData
{
   /**
    * Holds the data specific to summary panel.
    */
   private SummaryPanelViewData summaryViewData;

   /**
    * Holds the Package Relevant View Data.
    */
   private PackageViewData packageViewData;

   /**
    * Holds the Package Relevant View Data.
    */
   private InfantNotYetBornViewData infantNotYetBornViewData;

   /**
    * Holds the passengers insurance details
    */
   private PassengerInsuranceViewData passengerInsuranceViewData;

   /**
    * The passenger details static content view data.
    */
   private PassengerDetailsStaticContentViewData passengerDetailsStaticContentViewData;

   /**
    * Holds Alert list
    */
   private List<AlertViewData> alertMessages;

   private PromotionalCodeViewData promotionalCodeViewData;

   /** The data protection view data. */
   private DataProtectionViewData dataProtectionViewData;

   private CommunicationPreferencesViewData communicationPreferencesViewData;

   /**
    * To inform front end whether infobook request is success or not valStatusFlag="0": infobook
    * request is success valStatusFlag="1": infobook request is success ,insurance failed go to
    * extras page valStatusFlag="2": infobook failed, go to accommodation page
    */
   private String valStatusFlag;

   private CustomerSignInViewData customerSignInViewData;

   /** The package type. */
   private String packageType;

   private PageResponse pageResponse;

   /** The inventory type. */
   private String inventoryType;

   /** The min promo code length. */
   private int minPromoCodeLength;

   /** The max promo code length. */
   private int maxPromoCodeLength;

   /** The personal promo code length. */
   private int personalPromoCodeLength;

   private Map<String, String> countryCodes;

   /** The session time interval. */
   private int sessionTimePeriod;

   /**
    * @return the packageType
    */
   public String getPackageType()
   {
      return packageType;
   }

   /**
    * @param packageType the packageType to set
    */
   public void setPackageType(final String packageType)
   {
      this.packageType = packageType;
   }

   /**
    * @return the valStatusFlag
    */
   public String getValStatusFlag()
   {
      return valStatusFlag;
   }

   /**
    * @param valStatusFlag the valStatusFlag to set
    */
   public void setValStatusFlag(final String valStatusFlag)
   {
      this.valStatusFlag = valStatusFlag;
   }

   /**
    * @return the infantNotYetBornViewData
    */
   public InfantNotYetBornViewData getInfantNotYetBornViewData()
   {
      return infantNotYetBornViewData;
   }

   /**
    * @param infantNotYetBornViewData the infantNotYetBornViewData to set
    */
   public void setInfantNotYetBornViewData(final InfantNotYetBornViewData infantNotYetBornViewData)
   {
      this.infantNotYetBornViewData = infantNotYetBornViewData;
   }

   /**
    * @return the packageViewData
    */
   public PackageViewData getPackageViewData()
   {
      if (this.packageViewData == null)
      {
         this.packageViewData = new PackageViewData();
      }
      return this.packageViewData;
   }

   /**
    * @param packageViewData the packageViewData to set
    */
   public void setPackageViewData(final PackageViewData packageViewData)
   {
      this.packageViewData = packageViewData;
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
    * @return the summaryViewData
    */
   public SummaryPanelViewData getSummaryViewData()
   {
      if (this.summaryViewData == null)
      {
         this.summaryViewData = new SummaryPanelViewData();
      }
      return summaryViewData;
   }

   /**
    * @param summaryViewData the summaryViewData to set
    */
   public void setSummaryViewData(final SummaryPanelViewData summaryViewData)
   {
      this.summaryViewData = summaryViewData;
   }

   /**
    * @return the alertMessages
    */
   public List<AlertViewData> getAlertMessages()
   {
      return alertMessages;
   }

   /**
    * @param alertMessages the alertMessages to set
    */
   public void setAlertMessages(final List<AlertViewData> alertMessages)
   {
      this.alertMessages = alertMessages;
   }

   /**
    * @return the sessionTimePeriod
    */
   public int getSessionTimePeriod()
   {
      return sessionTimePeriod;
   }

   /**
    * @param sessionTimePeriod the sessionTimePeriod to set
    */
   public void setSessionTimePeriod(final int sessionTimePeriod)
   {
      this.sessionTimePeriod = sessionTimePeriod;
   }

   /**
    * @return the passengerInsuranceViewData
    */
   public PassengerInsuranceViewData getPassengerInsuranceViewData()
   {
      return passengerInsuranceViewData;
   }

   /**
    * @param passengerInsuranceViewData the passengerInsuranceViewData to set
    */
   public void setPassengerInsuranceViewData(
      final PassengerInsuranceViewData passengerInsuranceViewData)
   {
      this.passengerInsuranceViewData = passengerInsuranceViewData;
   }

   /**
    * @return the promotionalCodeViewData
    */
   public PromotionalCodeViewData getPromotionalCodeViewData()
   {
      if (this.promotionalCodeViewData == null)
      {
         this.promotionalCodeViewData = new PromotionalCodeViewData();
      }
      return promotionalCodeViewData;
   }

   /**
    * @param promotionalCodeViewData the promotionalCodeViewData to set
    */
   public void setPromotionalCodeViewData(final PromotionalCodeViewData promotionalCodeViewData)
   {
      this.promotionalCodeViewData = promotionalCodeViewData;
   }

   /**
    * @return the passengerDetailsStaticContentViewData
    */
   public PassengerDetailsStaticContentViewData getPassengerDetailsStaticContentViewData()
   {
      return passengerDetailsStaticContentViewData;
   }

   /**
    * @param passengerDetailsStaticContentViewData the passengerDetailsStaticContentViewData to set
    */
   public void setPassengerDetailsStaticContentViewData(
      final PassengerDetailsStaticContentViewData passengerDetailsStaticContentViewData)
   {
      this.passengerDetailsStaticContentViewData = passengerDetailsStaticContentViewData;
   }

   /**
    * @return the communicationPreferencesViewData
    */
   public CommunicationPreferencesViewData getCommunicationPreferencesViewData()
   {
      return communicationPreferencesViewData;
   }

   /**
    * @param communicationPreferencesViewData the communicationPreferencesViewData to set
    */
   public void setCommunicationPreferencesViewData(
      final CommunicationPreferencesViewData communicationPreferencesViewData)
   {
      this.communicationPreferencesViewData = communicationPreferencesViewData;
   }

   /**
    * @return the dataProtectionViewData
    */
   public DataProtectionViewData getDataProtectionViewData()
   {
      return dataProtectionViewData;
   }

   /**
    * @param dataProtectionViewData the dataProtectionViewData to set
    */
   public void setDataProtectionViewData(final DataProtectionViewData dataProtectionViewData)
   {
      this.dataProtectionViewData = dataProtectionViewData;
   }

   /**
    * @return the customerSignInViewData
    */
   public CustomerSignInViewData getCustomerSignInViewData()
   {
      if (customerSignInViewData == null)
      {
         customerSignInViewData = new CustomerSignInViewData();
      }
      return customerSignInViewData;
   }

   /**
    * @param customerSignInViewData the customerSignInViewData to set
    */
   public void setCustomerSignInViewData(final CustomerSignInViewData customerSignInViewData)
   {
      this.customerSignInViewData = customerSignInViewData;
   }

   /**
    * @return the pageResponse
    */
   public PageResponse getPageResponse()
   {
      return pageResponse;
   }

   /**
    * @param pageResponse the pageResponse to set
    */
   public void setPageResponse(final PageResponse pageResponse)
   {
      this.pageResponse = pageResponse;
   }

   /**
    * @return the inventoryType
    */
   public String getInventoryType()
   {
      return inventoryType;
   }

   /**
    * @param inventoryType the inventoryType to set
    */
   public void setInventoryType(final String inventoryType)
   {
      this.inventoryType = inventoryType;
   }

   /**
    * @return the minPromoCodeLength
    */
   public int getMinPromoCodeLength()
   {
      return minPromoCodeLength;
   }

   /**
    * @param minPromoCodeLength the minPromoCodeLength to set
    */
   public void setMinPromoCodeLength(final int minPromoCodeLength)
   {
      this.minPromoCodeLength = minPromoCodeLength;
   }

   /**
    * @return the maxPromoCodeLength
    */
   public int getMaxPromoCodeLength()
   {
      return maxPromoCodeLength;
   }

   /**
    * @param maxPromoCodeLength the maxPromoCodeLength to set
    */
   public void setMaxPromoCodeLength(final int maxPromoCodeLength)
   {
      this.maxPromoCodeLength = maxPromoCodeLength;
   }

   /**
    * @return the personalPromoCodeLength
    */
   public int getPersonalPromoCodeLength()
   {
      return personalPromoCodeLength;
   }

   /**
    * @param personalPromoCodeLength the personalPromoCodeLength to set
    */
   public void setPersonalPromoCodeLength(final int personalPromoCodeLength)
   {
      this.personalPromoCodeLength = personalPromoCodeLength;
   }

   /**
    * @return the countryCodes
    */
   public Map<String, String> getCountryCodes()
   {
      return countryCodes;
   }

   /**
    * @param countryCodes the countryCodes to set
    */
   public void setCountryCodes(final Map<String, String> countryCodes)
   {
      if (CollectionUtils.isEmpty(this.countryCodes))
      {
         this.countryCodes = new HashMap<String, String>();
      }
      this.countryCodes = countryCodes;
   }

}
