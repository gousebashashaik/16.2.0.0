/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.productrange.MainstreamProductRangeService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.fj.book.view.data.AccommodationViewData;
import uk.co.tui.util.CatalogAndCategoryUtil;


/**
 * @author l.furrer
 *
 */
public class DefaultAccommodationBasicPopulator implements Populator<AccommodationModel, AccommodationViewData>
{

    private static final TUILogUtils LOG = new TUILogUtils("DefaultAccommodationBasicPopulator");

    @Resource
    private FeatureService featureService;

    private Populator<ProductRangeModel, ProductRangeViewData> productRangeBasicPopulator;

    /** The productRangeService. */
    @Resource
    private MainstreamProductRangeService productRangeService;

    /** The catalog and category util. */
    @Resource
    private CatalogAndCategoryUtil catalogAndCategoryUtil;

    /** The tui utility service. */
    @Resource
    private TuiUtilityService tuiUtilityService;

    @Resource
    private BrandUtils brandUtils;


    @Required
    public void setProductRangeBasicPopulator(final Populator<ProductRangeModel, ProductRangeViewData> productRangeBasicPopulator)
    {
        this.productRangeBasicPopulator = productRangeBasicPopulator;
    }

    @Override
    public void populate(final AccommodationModel source, final AccommodationViewData target) throws ConversionException
    {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");

        try
        {
            target.putFeatureCodesAndValues(featureService.getValuesForFeatures(Arrays.asList(new String[]
            { "name", "introduction", "tRating" }), source, new Date(), brandUtils.getFeatureServiceBrand(source.getBrands())));

            final Collection<ProductRangeModel> productRangeModels = productRangeService.getProductRangesForProductByBrand(source,
                    tuiUtilityService.getSiteReleventBrandPks(), catalogAndCategoryUtil.getActiveCatalogversion());
            if (productRangeModels != null)
            {
                final List<ProductRangeViewData> productRanges = new ArrayList<ProductRangeViewData>();

                for (final ProductRangeModel productRangeModel : productRangeModels)
                {
                    final ProductRangeViewData productRange = new ProductRangeViewData();
                    productRangeBasicPopulator.populate(productRangeModel, productRange);
                    productRanges.add(productRange);
                }
                target.setProductRanges(productRanges);
            }

        }
        catch (final UnknownIdentifierException uie)
        {
            LOG.error("Featue Descriptor not found", uie);
        }

    }

}
