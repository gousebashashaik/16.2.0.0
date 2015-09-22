/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;

/**
 * @author pts
 *
 */
public class AttractionBasicPopulator implements Populator<AttractionModel, AttractionViewData> {

    @Resource
    private FeatureService featureService;

    @Override
    public void populate(AttractionModel sourceModel, AttractionViewData targetData) throws ConversionException {

        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");



        if (sourceModel!=null) {
            targetData.setCode(sourceModel.getCode());
            targetData.setName(sourceModel.getName());
            targetData.setType(sourceModel.getAttractionType().getCode());

            List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[] { "name","availability","editorialContent","itemRank","awards","tags","bestFor","extraCharge"}));
            targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, sourceModel, new Date(), null));
        }

    }

}
