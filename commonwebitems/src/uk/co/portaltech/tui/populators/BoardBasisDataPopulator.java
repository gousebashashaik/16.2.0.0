/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.BoardBasisContentModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.BoardBasisData;


/**
 * @author abir.p
 *
 */
public class BoardBasisDataPopulator implements Populator<BoardBasisContentModel, BoardBasisData>
{

    private final List<String> boardBasisFeatureList = Arrays.asList(new String[]
    { "breakfast_included", "lunch_included", "dinner_included", "drinks_included", "snacks_included", "meals_description",
            "drinks_description", "snacks_description" });


    @Resource
    private FeatureService featureService;

    private static final Logger LOG = Logger.getLogger(BoardBasisDataPopulator.class);

    /**
     * @param sourceModel
     * @param targetData
     * @description This method populates a BoardaBasisData object using the data of BoardBasisModel..
     */
    @Override
    public void populate(final BoardBasisContentModel sourceModel, final BoardBasisData targetData) throws ConversionException
    {

        LOG.debug("Populating board basis data");
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");
        targetData.setBoardbasisCode(sourceModel.getCode());
        targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(boardBasisFeatureList, sourceModel, new Date(),
                null));
        targetData.setName(sourceModel.getName());
    }

}
