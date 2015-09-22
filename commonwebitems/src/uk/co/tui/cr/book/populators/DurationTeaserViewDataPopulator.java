/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.cr.book.view.data.teasers.DurationTeaserViewData;

/**
 * @author samantha.gd
 *
 */
public class DurationTeaserViewDataPopulator
        implements
            Populator<BasePackage, DurationTeaserViewData> {

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final BasePackage source,
            final DurationTeaserViewData target) {
        target.setDuration(source.getDuration().intValue());
        target.setPackageId(source.getId());
        target.setPerPersonPrice(source.getPrice().getRate().getAmount());
        target.setTotalPrice(source.getPrice().getAmount().getAmount());
    }

}
