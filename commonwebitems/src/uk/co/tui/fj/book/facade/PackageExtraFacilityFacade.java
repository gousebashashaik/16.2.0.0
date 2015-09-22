/*
 * Copyright (C)2006 TUI UK Ltd
 *
 * TUI UK Ltd,
 * Columbus House,
 * Westwood Way,
 * Westwood Business Park,
 * Coventry,
 * United Kingdom
 * CV4 8TT
 *
 * Telephone - (024)76282828
 *
 * All rights reserved - The copyright notice above does not evidence
 * any actual or intended publication of this source code.
 *
 * $RCSfile:   ExtraOptionsPageFacade.java$
 *
 * $Revision:   $
 *
 * $Date:   Jul 1, 2013$
 *
 * Author: anithamani.s
 *
 *
 * $Log:   $
 */
package uk.co.tui.fj.book.facade;

import java.util.List;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.fj.book.data.AncillaryCriteria;
import uk.co.tui.fj.book.data.InfantActivityCriteria;
import uk.co.tui.fj.book.data.KidsActivityCriteria;
import uk.co.tui.fj.book.view.data.ExtraOptionsViewData;
import uk.co.tui.fj.book.view.data.RoomAndBoardOptionsPageViewData;


/**
 * Facade that is used in the room and extra options page.
 *
 * @author anithamani.s
 *
 */
public interface PackageExtraFacilityFacade
{
    /**
     * Method to render the package extras Populate all view
     *
     * @param extraOptionsViewData
     *            The extra options data
     */
    void renderPackageExtras(ExtraOptionsViewData extraOptionsViewData);

    /**
     * The method to render the accomodation extras Populate all view
     *
     * @param roomOptionsViewData
     *            The viewdat for room option
     */
    void renderAccomExtras(RoomAndBoardOptionsPageViewData roomOptionsViewData);

    /**
     * The method to update donation extras to passenger,populate
     * ExtraOptionsAllView,saving the package model after updating
     *
     * @param extraCategory
     *            The extra facility category code
     * @param code
     *            The donationFacilityCode
     * @return Returns Extra option view data
     */
    ExtraOptionsViewData updateProductForDonationOptions(String extraCategory, String code);

    /**
     * Method to remove ExtraCategoryFromPackage if code is empty,update the
     * quantity , calculate price, update the packagemodel Populate AllViews of
     * RoomOptions,saving the package model
     *
     * @param extraCategory
     *            The extra facility categorymodel code
     * @param extraCode
     *            The extrafacility code for Late checkout
     * @param quantity
     *            The quantity of rooms selected selected by the user
     * @return returns the RoomAndBoardOptionsPageViewData
     */
    RoomAndBoardOptionsPageViewData updateProductForLateCheckOut(String extraCategory, String extraCode, String quantity);

    /**
     * Method to removeExtraCategoryFromPackage
     * RemoveExtraSuperCategoryFromPackage,update transfer extra facility
     * quantity,calculate price ,update the packagemodel Populate all
     * view,saving the package
     *
     * @param extraCategory
     *            The extra facility categorymodel code
     * @param transferCode
     *            The extrafacility code for transfer
     * @return The ExtraOptionsViewData
     */
    ExtraOptionsViewData updateProductForTransferExtras(String extraCategory, String transferCode);

    /**
     * This method updateProductForInfantOptions this method also Adding
     * InfantEquipments Update the packagemodel,populate all view,saving the
     * package
     *
     * @param infantExtraCriteria
     *            The criteria for Infant extras
     * @return Returns ExtraOptionsViewData
     */
    ExtraOptionsViewData updateProductForInfantOptions(List<InfantActivityCriteria> infantExtraCriteria);

    /**
     * This method updates the Stage Academy Extras. Update the package model
     * ,populate all view,saving the package
     *
     * @param data
     *            list of KidsActivityCriteria
     * @return ExtraOptionsViewData
     */
    ExtraOptionsViewData updateProductForKidsActivity(List<KidsActivityCriteria> data);

    /**
     * Method updates SwimExtra facility Update the package model ,populate all
     * view,saving the package
     *
     * @param data
     *            List of KidsActivityCriteria
     * @return ExtraOptionsViewData
     */
    ExtraOptionsViewData updateProductForSwimActivity(List<KidsActivityCriteria> data);

    /**
     * Method removes product for KidsActivity, Update the package model
     * ,populate all view,saving the package
     *
     * @param data
     *            list of KidsActivityCriteria
     * @return ExtraOptionsViewData
     */
    ExtraOptionsViewData removeProductForKidsActivity(List<KidsActivityCriteria> data);

    /**
     * Method updates product for excursions ,updateAncilliary Update the
     * package model ,populate all view,saving the package
     *
     * @param criteria
     *            the list of AncillaryCriteria
     * @return ExtraOptionsViewData
     */
    ExtraOptionsViewData updateProductForExcursions(List<AncillaryCriteria> criteria);

    /**
     * This method update the package model for the attraction Updates
     * ExtraFacility selected quantity for child Update the package model
     * ,populate all view,saving the package
     *
     * @param criteria
     *            the list of AncillaryCriteria
     * @return ExtraOptionsViewData
     */
    ExtraOptionsViewData updateProductForAttractions(List<AncillaryCriteria> criteria);

    /**
     * Method to updateProductForCarHireUpgradeOptions This method does removal
     * of DefaultTransferCategories,updateStoreTransferExtras,adding selected
     * CarHireExtraFacilty To PackageModel Populate all view,saving the package
     *
     * @param code
     *            the extra facility code
     * @return ExtraOptionsViewData
     */
    ExtraOptionsViewData updateProductForCarHireUpgradeOptions(String code);

    /**
     * Method to compare two packages This method does
     * removePromotionalCode,swapping the packages,populate alert view
     * data,populate total cost alert
     *
     * @param extraOptionsViewData
     *            The selected extra option view data
     */
    void doPackageComparison(ExtraOptionsViewData extraOptionsViewData);

    /**
     * Method is to populate ExtraOptionsContainerView,ExtraOptionsPackageView,
     * ExtraOptionsSummaryView
     *
     * @param packageModel
     *            Packagemodel
     * @param extraOptionsViewData
     *            The extra option view data
     */
    void populateExtraOptionsAllView(BasePackage packageModel, ExtraOptionsViewData extraOptionsViewData);

    /**
     * Populate extra options static content view data. *
     *
     * @param extraOptionsViewData
     *            the extra options view data
     */
    void populateExtraOptionsStaticContentViewData(ExtraOptionsViewData extraOptionsViewData);
}
