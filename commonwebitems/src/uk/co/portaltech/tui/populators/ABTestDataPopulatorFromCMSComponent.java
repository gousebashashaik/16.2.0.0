/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import uk.co.portaltech.tui.web.view.data.ABTestViewData;

/**
 * @author s.consolino
 *
 */
public class ABTestDataPopulatorFromCMSComponent implements Populator<SimpleCMSComponentModel, ABTestViewData> {

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(SimpleCMSComponentModel source, ABTestViewData target) throws ConversionException {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");
        target.setCmsComponentName(source.getName());
        target.setCmsComponentPk(source.getPk().toString());
        target.setCmsComponentUid(source.getUid());
        target.setCmsComponentItemtype(source.getItemtype());
    }
}
