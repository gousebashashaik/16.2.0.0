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
package uk.co.tui.fo.book.facade;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.fo.book.view.data.ExtraOptionsViewData;

/**
 * Facade that is used in the room and extra options page.
 *
 * @author anithamani.s
 *
 */
public interface PackageExtraFacilityFacade {
    /**
     * Method to render the package extras Populate all view
     *
     * @param extraOptionsViewData
     *            The extra options data
     */
    void renderPackageExtras(ExtraOptionsViewData extraOptionsViewData);

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
    ExtraOptionsViewData updateProductForDonationOptions(String extraCategory,
            String code);

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
    ExtraOptionsViewData updateProductForTransferExtras(String extraCategory,
       String transferCode, boolean isExtraSelected);

    /**
     * Method is to populate ExtraOptionsContainerView,ExtraOptionsPackageView,
     * ExtraOptionsSummaryView
     *
     * @param packageModel
     *            Packagemodel
     * @param extraOptionsViewData
     *            The extra option view data
     */
    void populateExtraOptionsAllView(BasePackage packageModel,
            ExtraOptionsViewData extraOptionsViewData);

    /**
     * Populate extra options static content view data. *
     *
     * @param extraOptionsViewData
     *            the extra options view data
     */
    void populateExtraOptionsStaticContentViewData(
            ExtraOptionsViewData extraOptionsViewData);

}
