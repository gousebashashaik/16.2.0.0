/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;

/**
 * @author l.furrer
 *
 */
public class AttractionUspsPopulator implements Populator<AttractionModel, AttractionViewData> {

    @Resource
    private FeatureService featureService;

    @Override
    public void populate(AttractionModel sourceModel, AttractionViewData targetData) throws ConversionException {

        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");


        List<Object> uspsObject = featureService.getFeatureValues("usps", sourceModel, new Date(), null);
        List<String> usps = new ArrayList<String>();
        for (Object usp : uspsObject)
        {
            usps.add((String) usp);
        }

        targetData.setUsps(usps);

    }

}
