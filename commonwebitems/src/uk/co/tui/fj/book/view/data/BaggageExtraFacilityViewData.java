/**
 *
 */
package uk.co.tui.fj.book.view.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author madhumathi.m
 *
 *         This class holds all the view data related to baggage.
 */
public class BaggageExtraFacilityViewData extends ExtraFacilityViewData {

    /**
     * Holds the party Relevant View Data.
     */
    private int maxWeightPerPiece;

    /**
     * Holds the party Relevant View Data.
     */
    private int maxPiecePerPerson;

    /**
     * Holds the party Relevant View Data.
     */
    private int infantWeight;

    /**
     * Holds the party Relevant View Data.
     */
    private int baseWeight;

    /** The default infant baggage weight description. */
    private String infantBaggageWeightDescription = StringUtils.EMPTY;

    /** The default infant baggage weight description. */
    private String infantBaggageWeightSelection = "free";

    /**
     * Indicates Select All button of baggage option
     */
    private boolean totalSelected;

    /**
     * @return the maxWeightPerPiece
     */
    public int getMaxWeightPerPiece() {
        return maxWeightPerPiece;
    }

    /**
     * @param maxWeightPerPiece
     *            the maxWeightPerPiece to set
     */
    public void setMaxWeightPerPiece(int maxWeightPerPiece) {
        this.maxWeightPerPiece = maxWeightPerPiece;
    }

    /**
     * @return the maxPiecePerPerson
     */
    public int getMaxPiecePerPerson() {
        return maxPiecePerPerson;
    }

    /**
     * @param maxPiecePerPerson
     *            the maxPiecePerPerson to set
     */
    public void setMaxPiecePerPerson(int maxPiecePerPerson) {
        this.maxPiecePerPerson = maxPiecePerPerson;
    }

    /**
     * @return the infantWeight
     */
    public int getInfantWeight() {
        return infantWeight;
    }

    /**
     * @param infantWeight
     *            the infantWeight to set
     */
    public void setInfantWeight(int infantWeight) {
        this.infantWeight = infantWeight;
    }

    /**
     * @return the baseWeight
     */
    public int getBaseWeight() {
        return baseWeight;
    }

    /**
     * @param baseWeight
     *            the baseWeight to set
     */
    public void setBaseWeight(int baseWeight) {
        this.baseWeight = baseWeight;
    }

    /**
     * @return the infantBaggageWeightDescription
     */
    public String getInfantBaggageWeightDescription() {
        return infantBaggageWeightDescription;
    }

    /**
     * @param infantBaggageWeightDescription
     *            the infantBaggageWeightDescription to set
     */
    public void setInfantBaggageWeightDescription(
            String infantBaggageWeightDescription) {
        this.infantBaggageWeightDescription = infantBaggageWeightDescription;
    }

    /**
     * @return the infantBaggageWeightSelection
     */
    public String getInfantBaggageWeightSelection() {
        return infantBaggageWeightSelection;
    }

    /**
     * @param infantBaggageWeightSelection
     *            the infantBaggageWeightSelection to set
     */
    public void setInfantBaggageWeightSelection(
            String infantBaggageWeightSelection) {
        this.infantBaggageWeightSelection = infantBaggageWeightSelection;
    }

    /**
     * @return the totalSelected
     */
    public boolean isTotalSelected() {
        return totalSelected;
    }

    /**
     * @param totalSelected the totalSelected to set
     */
    public void setTotalSelected(boolean totalSelected) {
        this.totalSelected = totalSelected;
    }

}
