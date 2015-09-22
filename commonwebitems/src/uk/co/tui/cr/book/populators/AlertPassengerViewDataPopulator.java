/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.cr.book.view.data.AlertViewData;

/**
 * @author uday.g
 *
 */
public class AlertPassengerViewDataPopulator
        implements
            Populator<Feedback, AlertViewData> {

    /** The message property reader. */
    @Resource
    private PropertyReader messagePropertyReader;

    /**
     * Method to populate alert view data
     *
     * @param source
     *            The feedback messages from anite
     * @param target
     *            The AlertViewData object
     *
     */
    public void populate(final Feedback source, final AlertViewData target)
            throws ConversionException {
        target.setMessageText(messagePropertyReader.substitute(
                "Infant_Notyet_Born_Text", null));
        target.setAlertLevel("defalut");
    }

}
