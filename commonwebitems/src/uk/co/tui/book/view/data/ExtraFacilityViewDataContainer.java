/**
 *
 */
package uk.co.tui.book.view.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.CollectionUtils;

/**
 * The Class ExtraFacilityViewContainer.
 *
 * @author munisekhar.k
 */
public class ExtraFacilityViewDataContainer {

    /** Hold the seat options. */
    private ExtraFacilityCategoryViewData seatOptions;

    /** Hold the baggage options. */
    private ExtraFacilityCategoryViewData baggageOptions;

    /** Hold the meal options. */
    private ExtraFacilityCategoryViewData mealOptions;

    /** Hold the donation options. */
    private ExtraFacilityCategoryViewData donationOptions;

    /** Hold the latecheckout options. */
    private ExtraFacilityCategoryViewData lateCheckOut;

    /** Hold the Transfer options. */
    private ExtraFacilityCategoryViewData transferOptions;

    /** Holds the Infant options. */
    private ExtraFacilityCategoryViewData infantOptions;

    /** Holds the Stage Academy options. */
    private ExtraFacilityCategoryViewData stageOptions;

    /** Holds the Swim Academy options. */
    private ExtraFacilityCategoryViewData swimOptions;

    /** Holds the Creche options. */
    private ExtraFacilityCategoryViewData crecheOptions;

    /** Holds the FootBall/Soccer options. */
    private ExtraFacilityCategoryViewData soccerOptions;

    /** Holds the excursion options. */
    private List<ExtraFacilityCategoryViewData> excursionOptions;

    /** Holds the attraction options. */
    private Map<String, List<ExtraFacilityCategoryViewData>> attractionOptions;

    /** Holds the carHireUpgrade options. */
    private List<ExtraFacilityCategoryViewData> carHireUpgrade;

    /** Holds the carHireUpgradeExtras options. */
    private ExtraFacilityCategoryViewData carHireExtras;

    /**
     * Gets the seat options.
     *
     * @return the seatOptions
     */
    public ExtraFacilityCategoryViewData getSeatOptions() {
        if (this.seatOptions == null) {
            this.seatOptions = new ExtraFacilityCategoryViewData();
        }
        return seatOptions;
    }

    /**
     * Sets the seat options.
     *
     * @param seatOptions
     *            the seatOptions to set
     */
    public void setSeatOptions(final ExtraFacilityCategoryViewData seatOptions) {
        this.seatOptions = seatOptions;
    }

    /**
     * Gets the baggage options.
     *
     * @return the baggageOptions
     */
    public ExtraFacilityCategoryViewData getBaggageOptions() {
        if (this.baggageOptions == null) {
            this.baggageOptions = new ExtraFacilityCategoryViewData();
        }
        return baggageOptions;
    }

    /**
     * Sets the baggage options.
     *
     * @param baggageOptions
     *            the baggageOptions to set
     */
    public void setBaggageOptions(
            final ExtraFacilityCategoryViewData baggageOptions) {
        this.baggageOptions = baggageOptions;
    }

    /**
     * Gets the meal options.
     *
     * @return the mealOptions
     */
    public ExtraFacilityCategoryViewData getMealOptions() {
        if (this.mealOptions == null) {
            this.mealOptions = new ExtraFacilityCategoryViewData();
        }
        return mealOptions;
    }

    /**
     * Sets the meal options.
     *
     * @param mealOptions
     *            the mealOptions to set
     */
    public void setMealOptions(final ExtraFacilityCategoryViewData mealOptions) {
        this.mealOptions = mealOptions;
    }

    /**
     * Gets the late check out.
     *
     * @return the lateCheckOut
     */
    public ExtraFacilityCategoryViewData getLateCheckOut() {
        if (this.lateCheckOut == null) {
            this.lateCheckOut = new ExtraFacilityCategoryViewData();
        }
        return lateCheckOut;
    }

    /**
     * Sets the late check out.
     *
     * @param lateCheckOut
     *            the lateCheckOut to set
     */
    public void setLateCheckOut(final ExtraFacilityCategoryViewData lateCheckOut) {
        this.lateCheckOut = lateCheckOut;
    }

    /**
     * @return the donationOptions
     */
    public ExtraFacilityCategoryViewData getDonationOptions() {
        if (this.donationOptions == null) {
            this.donationOptions = new ExtraFacilityCategoryViewData();
        }
        return donationOptions;
    }

    /**
     * @param donationOptions
     *            the donationOptions to set
     */
    public void setDonationOptions(
            final ExtraFacilityCategoryViewData donationOptions) {
        this.donationOptions = donationOptions;
    }

    /**
     * Gets the transfer options.
     *
     * @return the transferOptions
     */
    public ExtraFacilityCategoryViewData getTransferOptions() {
        if (this.transferOptions == null) {
            this.transferOptions = new ExtraFacilityCategoryViewData();
        }
        return transferOptions;
    }

    /**
     * Sets the transfer options.
     *
     * @param transferOptions
     *            the transferOptions to set
     */
    public void setTransferOptions(
            final ExtraFacilityCategoryViewData transferOptions) {
        this.transferOptions = transferOptions;
    }

    /**
     * Gets the infantOptions
     */
    public ExtraFacilityCategoryViewData getInfantOptions() {
        if (this.infantOptions == null) {
            this.infantOptions = new ExtraFacilityCategoryViewData();
        }
        return infantOptions;
    }

    /**
     * Sets the infantOptions
     */
    public void setInfantOptions(ExtraFacilityCategoryViewData infantOptions) {
        this.infantOptions = infantOptions;
    }

    /**
     * @return the stageOptions
     */
    public ExtraFacilityCategoryViewData getStageOptions() {

        if (this.stageOptions == null) {
            this.stageOptions = new ExtraFacilityCategoryViewData();
        }
        return stageOptions;
    }

    /**
     * @param stageOptions
     *            the stageOptions to set
     */
    public void setStageOptions(ExtraFacilityCategoryViewData stageOptions) {
        this.stageOptions = stageOptions;
    }

    /**
     * @return the swimOptions
     */
    public ExtraFacilityCategoryViewData getSwimOptions() {

        if (this.swimOptions == null) {
            this.swimOptions = new ExtraFacilityCategoryViewData();
        }
        return swimOptions;
    }

    /**
     * @param swimOptions
     *            the swimOptions to set
     */
    public void setSwimOptions(ExtraFacilityCategoryViewData swimOptions) {
        this.swimOptions = swimOptions;
    }

    /**
     * @return the crecheOptions
     */
    public ExtraFacilityCategoryViewData getCrecheOptions() {

        if (this.crecheOptions == null) {
            this.crecheOptions = new ExtraFacilityCategoryViewData();
        }
        return crecheOptions;
    }

    /**
     * @param crecheOptions
     *            the crecheOptions to set
     */
    public void setCrecheOptions(ExtraFacilityCategoryViewData crecheOptions) {
        this.crecheOptions = crecheOptions;
    }

    /**
     * @return the soccerOptions
     */
    public ExtraFacilityCategoryViewData getSoccerOptions() {

        if (this.soccerOptions == null) {
            this.soccerOptions = new ExtraFacilityCategoryViewData();
        }
        return soccerOptions;
    }

    /**
     * @param soccerOptions
     *            the soccerOptions to set
     */
    public void setSoccerOptions(ExtraFacilityCategoryViewData soccerOptions) {
        this.soccerOptions = soccerOptions;
    }

    /**
     * @return the excursionOptions
     */
    public List<ExtraFacilityCategoryViewData> getExcursionOptions() {

        if (CollectionUtils.isEmpty(this.excursionOptions)) {
            this.excursionOptions = new ArrayList<ExtraFacilityCategoryViewData>();
        }
        return excursionOptions;
    }

    /**
     * @param excursionOptions
     *            the excursionOptions to set
     */
    public void setExcursionOptions(
            List<ExtraFacilityCategoryViewData> excursionOptions) {
        this.excursionOptions = excursionOptions;
    }

    /**
     * @return the carHireUpgrade
     */
    public List<ExtraFacilityCategoryViewData> getCarHireUpgrade() {
        return carHireUpgrade;
    }

    /**
     * @param carHireUpgrade
     *            the carHireUpgrade to set
     */
    public void setCarHireUpgrade(
            List<ExtraFacilityCategoryViewData> carHireUpgrade) {
        this.carHireUpgrade = carHireUpgrade;
    }

    /**
     * @return the carHireExtras
     */
    public ExtraFacilityCategoryViewData getCarHireExtras() {
        return carHireExtras;
    }

    /**
     * @param carHireExtras
     *            the carHireExtras to set
     */
    public void setCarHireExtras(ExtraFacilityCategoryViewData carHireExtras) {
        this.carHireExtras = carHireExtras;
    }

    /**
     * @return the attractionOptions
     */
    public Map<String, List<ExtraFacilityCategoryViewData>> getAttractionOptions() {
        if (CollectionUtils.isEmpty(this.attractionOptions)) {
            this.attractionOptions = new TreeMap<String, List<ExtraFacilityCategoryViewData>>();
        }
        return attractionOptions;
    }

    public void setAttractionOptions(
            Map<String, List<ExtraFacilityCategoryViewData>> attractionOptions) {
        this.attractionOptions = attractionOptions;
    }

}
