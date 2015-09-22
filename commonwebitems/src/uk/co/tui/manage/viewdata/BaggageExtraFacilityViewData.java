/**
 *
 */
package uk.co.tui.manage.viewdata;




/**
 * @author sunil.bd
 *
 */
public class BaggageExtraFacilityViewData extends ExtraFacilityViewData
{

    private Integer baggagePieces;

    private Integer baggageWeight;

    private BaggageExtraFacilityRestrictionsViewData baggageExtraFacilityRestrictions;

    /**
     * @return the baggageExtraFacilityRestrictions
     */
    public BaggageExtraFacilityRestrictionsViewData getBaggageExtraFacilityRestrictions()
    {
        return baggageExtraFacilityRestrictions;
    }

    /**
     * @param baggageExtraFacilityRestrictions
     *           the baggageExtraFacilityRestrictions to set
     */
    public void setBaggageExtraFacilityRestrictions(final BaggageExtraFacilityRestrictionsViewData baggageExtraFacilityRestrictions)
    {
        this.baggageExtraFacilityRestrictions = baggageExtraFacilityRestrictions;
    }

    /**
     * @return the baggagePieces
     */
    public Integer getBaggagePieces()
    {
        return baggagePieces;
    }

    /**
     * @param baggagePieces
     *           the baggagePieces to set
     */
    public void setBaggagePieces(final Integer baggagePieces)
    {
        this.baggagePieces = baggagePieces;
    }

    /**
     * @return the baggageWeight
     */
    public Integer getBaggageWeight()
    {
        return baggageWeight;
    }

    /**
     * @param baggageWeight
     *           the baggageWeight to set
     */
    public void setBaggageWeight(final Integer baggageWeight)
    {
        this.baggageWeight = baggageWeight;
    }

}
