/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.ProductRangeService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author omonikhide
 *
 */
public class ProductRangeCategoryCarouselOverviewPopulator implements
        Populator<ProductRangeCategoryModel, ProductRangeCategoryViewData>
{
    @Resource
    private ProductRangeService productRangeService;

    @Resource
    private MediaDataPopulator mediaDataPopulator;

    @Resource
    private BrandUtils brandUtils;


    @Resource
    private TUIUrlResolver<ProductRangeModel> tuiCategoryModelUrlResolver;

    @Resource
    private CMSSiteService cmsSiteService;

    @Resource
    private FeatureService featureService;

    @Resource
    private SessionService sessionService;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final ProductRangeCategoryModel source, final ProductRangeCategoryViewData target)
            throws ConversionException
    {
        final List<CategoryModel> categories = source.getCategories();
        final List<ProductRangeViewData> productRange = new ArrayList<ProductRangeViewData>();
        final List<BenefitViewData> benefits = new ArrayList<BenefitViewData>();
        BenefitViewData benefitData = null;
        MediaViewData mediaData = null;
        ProductRangeViewData productRangeViewData = null;
        final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
        final List<String> brandPKs = brandDetails.getRelevantBrands();
        for (final CategoryModel model : categories)
        {
            if (model instanceof ProductRangeModel)
            {
                final ProductRangeModel prModel = (ProductRangeModel) model;
                productRangeViewData = new ProductRangeViewData();
                productRangeViewData.setCode(prModel.getCode());
                if (prModel.getPicture() != null)
                {
                    mediaData = new MediaViewData();
                    mediaDataPopulator.populate(prModel.getPicture(), mediaData);
                    productRangeViewData.setPicture(mediaData);
                }
                productRangeViewData.setUrl(tuiCategoryModelUrlResolver.resolve(prModel));
                final List<BenefitModel> productRangeBenefits = productRangeService.getProductRangeBenefits(prModel.getCode(),
                        cmsSiteService.getCurrentCatalogVersion(), brandPKs);
                if (productRangeBenefits != null && !productRangeBenefits.isEmpty())
                {
                    for (final BenefitModel benefitModel : productRangeBenefits)
                    {
                        benefitData = new BenefitViewData();
                        benefitData.setName(featureService
                                .getFeatureValues("name", benefitModel, new Date(),
                                        brandUtils.getFeatureServiceBrand(benefitModel.getBrands())).get(0).toString());
                        benefitData.setDescription(featureService
                                .getFeatureValues("description", benefitModel, new Date(),
                                        brandUtils.getFeatureServiceBrand(benefitModel.getBrands())).get(0).toString());
                        benefits.add(benefitData);
                    }
                    productRangeViewData.setBenefits(benefits);
                }
                productRange.add(productRangeViewData);
                target.setProductRanges(productRange);
            }
        }

    }
}
