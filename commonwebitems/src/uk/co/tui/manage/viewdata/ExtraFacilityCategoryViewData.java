/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.ArrayList;
import java.util.List;

import uk.co.tui.book.domain.lite.ExtrasRequestType;


/**
 * @author sunil.bd
 *
 */
public class ExtraFacilityCategoryViewData
{

    private String extraFacilityCategoryCode;

    private String inventoryCode;

    private String superCategoryCode;

    private String aliasSuperCategoryCode;

    private String description;

    private List<ExtraFacilityViewData> extraFacilityViewData = new ArrayList<ExtraFacilityViewData>();

    private String extraFacilityGroup;

    private ExtrasRequestType extrasRequestType;

    private boolean isWelfare;

    /**
     * @return the extraFacilityCategoryCode
     */
    public String getExtraFacilityCategoryCode()
    {
        return extraFacilityCategoryCode;
    }

    /**
     * @param extraFacilityCategoryCode
     *           the extraFacilityCategoryCode to set
     */
    public void setExtraFacilityCategoryCode(final String extraFacilityCategoryCode)
    {
        this.extraFacilityCategoryCode = extraFacilityCategoryCode;
    }

    /**
     * @return the isWelfare
     */
    public boolean isWelfare()
    {
        return isWelfare;
    }

    /**
     * @param isWelfare
     *           the isWelfare to set
     */
    public void setWelfare(final boolean isWelfare)
    {
        this.isWelfare = isWelfare;
    }


    /**
     * @return the inventoryCode
     */
    public String getInventoryCode()
    {
        return inventoryCode;
    }

    /**
     * @param inventoryCode
     *           the inventoryCode to set
     */
    public void setInventoryCode(final String inventoryCode)
    {
        this.inventoryCode = inventoryCode;
    }

    /**
     * @return the superCategoryCode
     */
    public String getSuperCategoryCode()
    {
        return superCategoryCode;
    }

    /**
     * @param superCategoryCode
     *           the superCategoryCode to set
     */
    public void setSuperCategoryCode(final String superCategoryCode)
    {
        this.superCategoryCode = superCategoryCode;
    }

    /**
     * @return the aliasSuperCategoryCode
     */
    public String getAliasSuperCategoryCode()
    {
        return aliasSuperCategoryCode;
    }

    /**
     * @param aliasSuperCategoryCode
     *           the aliasSuperCategoryCode to set
     */
    public void setAliasSuperCategoryCode(final String aliasSuperCategoryCode)
    {
        this.aliasSuperCategoryCode = aliasSuperCategoryCode;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *           the description to set
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }


    /**
     * @return the extrasRequestType
     */
    public ExtrasRequestType getExtrasRequestType()
    {
        return extrasRequestType;
    }

    /**
     * @param extrasRequestType
     *           the extrasRequestType to set
     */
    public void setExtrasRequestType(final ExtrasRequestType extrasRequestType)
    {
        this.extrasRequestType = extrasRequestType;
    }

    /**
     * @return the extraFacilityGroup
     */
    public String getExtraFacilityGroup()
    {
        return extraFacilityGroup;
    }

    /**
     * @param extraFacilityGroup
     *           the extraFacilityGroup to set
     */
    public void setExtraFacilityGroup(final String extraFacilityGroup)
    {
        this.extraFacilityGroup = extraFacilityGroup;
    }

    /**
     * @return the extraFacilityViewData
     */
    public List<ExtraFacilityViewData> getExtraFacilityViewData() {
        return extraFacilityViewData;
    }

    /**
     * @param extraFacilityViewData the extraFacilityViewData to set
     */
    public void setExtraFacilityViewData(List<ExtraFacilityViewData> extraFacilityViewData) {
        this.extraFacilityViewData = extraFacilityViewData;
    }


}
