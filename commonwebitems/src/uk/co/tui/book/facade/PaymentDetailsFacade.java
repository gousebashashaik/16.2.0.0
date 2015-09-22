/**
 *
 */
package uk.co.tui.book.facade;

import java.util.Map;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.formbean.PassengerDetailsFormBean;
import uk.co.tui.book.view.data.SummaryPanelViewData;

/**
 * This class is used to perform the operations required to for payment page.
 *
 * @author madhumathi.m
 *
 */
public interface PaymentDetailsFacade {

    /**
     * Populate package data.
     *
     * @param formBean
     *            the form bean
     * @param packageModel
     *            the package model
     * @return the inclusive package model
     */
    BasePackage populatePackageData(
            PassengerDetailsFormBean formBean,
            BasePackage packageModel);

    /**
     * Populate link details.
     *
     * @param viewData
     *            the view data
     * @param hostPrefix
     *            the host
     * @return Map<String, String>
     */
    Map<String, String> populateBreadcrumbUrls(
            SummaryPanelViewData viewData, String hostPrefix);

    /**
     * Update deposit component.
     *
     * @param packageModel
     *            the package model
     */
    void updateDepositComponents(BasePackage packageModel);


}
