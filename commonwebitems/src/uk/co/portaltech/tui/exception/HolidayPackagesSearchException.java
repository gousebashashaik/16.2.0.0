/**
 *
 */
package uk.co.portaltech.tui.exception;

import uk.co.tui.exception.TUIBusinessException;

/**
 * Will be thrown when any error encountered while searching for holiday.
 */
public class HolidayPackagesSearchException extends TUIBusinessException {

    /**
     * @param code
     */
    public HolidayPackagesSearchException(String code) {
        super(code);

    }


}
