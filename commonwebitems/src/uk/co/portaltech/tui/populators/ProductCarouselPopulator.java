/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;

/**
 * @author omonikhide
 *
 */
public class ProductCarouselPopulator
        implements
            Populator<List<ResultData>, List<ProductRangeViewData>> {

    @Override
    public void populate(final List<ResultData> source,
            final List<ProductRangeViewData> target) throws ConversionException {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");
    }

}
