/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The Class ExtraFacilityCategoryViewData.
 *
 * @author munisekhar.k
 */
public class ExtraFacilityCategoryViewData
{

   /** The extra facility category code. */
   private String extraFacilityCategoryCode = StringUtils.EMPTY;

   /** The multi select. */
    private boolean multiSelect;

   /** The extra facility view data. */
   private List<ExtraFacilityViewData> extraFacilityViewData = Collections.emptyList();

   /** The extra facility group code. */
   private String extraFacilityGroupCode = StringUtils.EMPTY;

    /** The disable extra facility option available. */
    private boolean disableExtraFacilityOptionAvailable;

    /** The no transfer opted. */
    private boolean noTransferOpted;

    /**
     * Holds the code of Selected column of extra facility of baggage option
     */
    private String totalSelectedCode;

    /**
     * Holds the total weight of selected baggage.
     */
    private int totalSelectedWeight;

   /** The extras to passenger relation. */
   private List<ExtrasToPassengerRelationViewData> extrasToPassengerMapping = Collections.emptyList();

   /** The insurance view data. */
   private List<InsuranceViewData> insuranceViewData = new ArrayList<InsuranceViewData>();

   private String superCategoryCode = StringUtils.EMPTY;
   private String aliasSuperCategoryCode = StringUtils.EMPTY;
   private boolean display ;
   private String displayName = StringUtils.EMPTY;
   private String currencyAppendedCategoryTotalPrice;
   private int selectedQuantity;
   private String description = StringUtils.EMPTY;
   private Map<Object, Object> carHireContentMap = new HashMap<Object, Object>();

   /** The flag to help UI guys to decide whether an extra category is available to display or not.*/
   private boolean available;

   private boolean selected;

   private ExtraFacilityContentViewData extraContent;

   private int minAdultCountRequiredForExtra = 1;
/**
    * Gets the extra facility category code.
    *
    * @return the extraFacilityCategoryCode
    */
   public String getExtraFacilityCategoryCode()
   {
      return extraFacilityCategoryCode;
   }

   /**
    * Sets the extra facility category code.
    *
    * @param extraFacilityCategoryCode the extraFacilityCategoryCode to set
    */
   public void setExtraFacilityCategoryCode(String extraFacilityCategoryCode)
   {
      this.extraFacilityCategoryCode = extraFacilityCategoryCode;
   }

   /**
    * Checks if is multi select.
    *
    * @return the multiSelect
    */
   public boolean isMultiSelect()
   {
      return multiSelect;
   }

   /**
    * Sets the multi select.
    *
    * @param multiSelect the multiSelect to set
    */
   public void setMultiSelect(boolean multiSelect)
   {
      this.multiSelect = multiSelect;
   }

   /**
    * Gets the extra facility view data.
    *
    * @return the extraFacilityViewData
    */
   public List<ExtraFacilityViewData> getExtraFacilityViewData()
   {
      if (CollectionUtils.isEmpty(extraFacilityViewData))
      {
         extraFacilityViewData = new ArrayList<ExtraFacilityViewData>();
      }
      return extraFacilityViewData;
   }

   /**
    * @return the extraFacilityGroupCode
    */
   public String getExtraFacilityGroupCode()
   {
      return extraFacilityGroupCode;
   }

   /**
    * @param extraFacilityGroupCode the extraFacilityGroupCode to set
    */
   public void setExtraFacilityGroupCode(String extraFacilityGroupCode)
   {
      this.extraFacilityGroupCode = extraFacilityGroupCode;
   }

   /**
    * @param extraFacilityViewData the extraFacilityViewData to set
    */
   public void setExtraFacilityViewData(List<ExtraFacilityViewData> extraFacilityViewData)
   {
      this.extraFacilityViewData = extraFacilityViewData;
   }

   /**
    * Checks if an option available to disable the extra facility options.
    *
    * @return the disableExtraFacilityOptionAvailable
    */
   public boolean isDisableExtraFacilityOptionAvailable()
   {
      return disableExtraFacilityOptionAvailable;
   }

   /**
    * Sets an option to disable the extra facility options.
    *
    * @param disableExtraFacilityOptionAvailable
    */
   public void setDisableExtraFacilityOptionAvailable(boolean disableExtraFacilityOptionAvailable)
   {
      this.disableExtraFacilityOptionAvailable = disableExtraFacilityOptionAvailable;
   }

    /**
     * @return the extrasToPassengerMapping
     */
    public List<ExtrasToPassengerRelationViewData> getExtrasToPassengerMapping() {
        if (CollectionUtils.isEmpty(extrasToPassengerMapping)) {
            extrasToPassengerMapping = new ArrayList<ExtrasToPassengerRelationViewData>();
        }
        return extrasToPassengerMapping;
    }


    /**
     * Checks if is no transfer opted.
     *
     * @return true, if is no transfer opted
     */
    public boolean isNoTransferOpted() {
        return noTransferOpted;
    }

    /**
     * Sets the no transfer opted.
     *
     * @param noTransferOpted the new no transfer opted
     */
    public void setNoTransferOpted(boolean noTransferOpted) {
        this.noTransferOpted = noTransferOpted;
    }

    /**
     * @return the insuranceViewData
     */
    public List<InsuranceViewData> getInsuranceViewData() {
        return insuranceViewData;
    }

    /**
     * @param insuranceViewData the insuranceViewData to set
     */
    public void setInsuranceViewData(List<InsuranceViewData> insuranceViewData) {
        this.insuranceViewData = insuranceViewData;
    }

    /**
     * @return the display
     */
    public boolean isDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(boolean display) {
        this.display = display;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the currencyAppendedCategoryTotalPrice
     */
    public String getCurrencyAppendedCategoryTotalPrice() {
        return currencyAppendedCategoryTotalPrice;
    }

    /**
     * @param currencyAppendedCategoryTotalPrice the currencyAppendedCategoryTotalPrice to set
     */
    public void setCurrencyAppendedCategoryTotalPrice(
            String currencyAppendedCategoryTotalPrice) {
        this.currencyAppendedCategoryTotalPrice = currencyAppendedCategoryTotalPrice;
    }

    /**
     * @return the superCategoryCode
     */
    public String getSuperCategoryCode() {
        return superCategoryCode;
    }

    /**
     * @param superCategoryCode the superCategoryCode to set
     */
    public void setSuperCategoryCode(String superCategoryCode) {
        this.superCategoryCode = superCategoryCode;
    }

    public String getAliasSuperCategoryCode() {
        return aliasSuperCategoryCode;
    }

    public void setAliasSuperCategoryCode(String aliasSuperCategoryCode) {
        this.aliasSuperCategoryCode = aliasSuperCategoryCode;
    }

    /**
     * @return the selectedQuantity
     */
    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    /**
     * @param selectedQuantity the selectedQuantity to set
     */
    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the minAdultCountRequiredForExtra
     */
    public int getMinAdultCountRequiredForExtra() {
        return minAdultCountRequiredForExtra;
    }

    /**
     * @param minAdultCountRequiredForExtra the minAdultCountRequiredForExtra to set
     */
    public void setMinAdultCountRequiredForExtra(int minAdultCountRequiredForExtra) {
        this.minAdultCountRequiredForExtra = minAdultCountRequiredForExtra;
    }

    /**
     * @return the extraContent
     */
    public ExtraFacilityContentViewData getExtraContent() {
        if(extraContent == null)
        {
            extraContent = new ExtraFacilityContentViewData();
        }
        return extraContent;
    }

    /**
     * @param extraContent the extraContent to set
     */
    public void setExtraContent(ExtraFacilityContentViewData extraContent) {
        this.extraContent = extraContent;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @return the totalSelectedCode
     */
    public String getTotalSelectedCode() {
        return totalSelectedCode;
    }

    /**
     * @param totalSelectedCode the totalSelectedCode to set
     */
    public void setTotalSelectedCode(String totalSelectedCode) {
        this.totalSelectedCode = totalSelectedCode;
    }

    /**
     * @return the totalSelectedWeight
     */
    public int getTotalSelectedWeight() {
        return totalSelectedWeight;
    }

    /**
     * @param totalSelectedWeight the totalSelectedWeight to set
     */
    public void setTotalSelectedWeight(int totalSelectedWeight) {
        this.totalSelectedWeight = totalSelectedWeight;
    }

    /**
     * @return the carHireContentMap
     */
    public Map<Object, Object> getCarHireContentMap() {
        if (MapUtils.isEmpty(carHireContentMap)) {
            carHireContentMap = new HashMap<Object, Object>();
        }
        return carHireContentMap;
    }

    /**
     * @param carHireContentMap the carHireContentMap to set
     */
    public void setCarHireContentMap(Map<Object, Object> carHireContentMap) {
        this.carHireContentMap = carHireContentMap;
    }

}
