/**
 *
 */
package uk.co.tui.fj.book.validator;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.th.book.view.data.ExtraFacilityViewData;

/**
 * @author sunilkumar.sahu
 *
 */
public interface Validator {

    /**
     * Validator.
     *
     * @param packageModel
     *            the package model
     * @return true, if successful
     */
    boolean validate(BasePackage packageModel);

    /**
     * Validator.
     *
     * @param extraViewData
     *            the extra view data
     * @param quantity
     *            the quantity
     * @return true, if successful
     */
    boolean validate(ExtraFacilityViewData extraViewData, int quantity);

}
