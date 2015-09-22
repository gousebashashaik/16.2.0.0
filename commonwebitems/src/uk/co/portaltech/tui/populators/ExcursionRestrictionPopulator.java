/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.ExcursionViewData;

/**
 * @author abi
 *
 */
public class ExcursionRestrictionPopulator implements Populator<ExcursionModel, ExcursionViewData> {


    @Resource
  private  FeatureService                         featureService;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(ExcursionModel sourceModel, ExcursionViewData targetData) throws ConversionException {

        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        targetData.putFeatureCodesAndValues(featureService.getValuesForFeatures(Arrays.asList(new String[] {"restrictionInfo"}), sourceModel, new Date(),null));

    }

}
