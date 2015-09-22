/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import uk.co.portaltech.tui.components.model.ABTestComponentModel;
import uk.co.portaltech.tui.web.view.data.ABTestViewData;

/**
 * @author s.consolino
 *
 */
public class ABTestDataPopulatorFromABTestComponent implements Populator<ABTestComponentModel, ABTestViewData> {

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(ABTestComponentModel source, ABTestViewData target) throws ConversionException {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");
        target.setAbTestComponentUid(source.getUid());
        target.setAbTestComponentPk(source.getPk().toString());
        target.setAbTestComponentName(source.getName());
        if (source.getScope() != null) {
            target.setAbTestComponentScope(source.getScope().toString());
        }
    }
}
