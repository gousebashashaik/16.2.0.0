/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.portaltech.travel.services.productrange.MainstreamProductRangeService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.GenericContentViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.util.CatalogUtil;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author l.furrer
 *
 */
public class DefaultAccommodationBasicPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   private static final TUILogUtils LOG = new TUILogUtils("DefaultAccommodationBasicPopulator");

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private FeatureService featureService;

   private Populator<ProductRangeModel, ProductRangeViewData> productRangeBasicPopulator;

   @Resource
   private MainstreamProductRangeService productRangeService;

   @Resource
   private CatalogVersionService catalogVersionService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private Populator<ContentValueModel, GenericContentViewData> genericContentPopulator;

   @Resource
   private GenericContentService genericContentService;

   @Resource
   private CatalogUtil catalogUtil;

   @Resource
   private TypeService typeService;

   private static final String[] NAME_SEARCH_ITEM = { "name_search_item" };

   @Required
   public void setProductRangeBasicPopulator(
      final Populator<ProductRangeModel, ProductRangeViewData> productRangeBasicPopulator)
   {
      this.productRangeBasicPopulator = productRangeBasicPopulator;
   }

   @Override
   public void populate(final AccommodationModel source, final AccommodationViewData target)
      throws ConversionException
   {
      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");

      target.setCode(source.getCode());
      target.setAccommodationType(source.getType().getCode());
      if (source.getReviewRating() != null)
      {
         target.setReviewRating(source.getReviewRating().toString());
      }
      try
      {
         final String featureServiceBrand = brandUtils.getFeatureServiceBrand(source.getBrands());
         target.putFeatureCodesAndValues(featureService.getValuesForFeatures(
            Arrays.asList(new String[] { "name", "introduction", "tRating",
               "falcon_product_override" }), source, new Date(), featureServiceBrand));
         target.setBrands(source.getBrands());

         Collection<ProductRangeModel> productRangeModels = null;
         final List<Object> featureValues = target.getFeatureValues("falcon_product_override");
         if (CollectionUtils.isEmpty(featureValues)
            || (CollectionUtils.isNotEmpty(featureValues) && !StringUtils.equalsIgnoreCase(
               "ZZZZZZ", featureValues.get(0).toString())))
         {
            productRangeModels =
               productRangeService.getProductRangesForProductByBrand(
                  source,
                  Arrays.asList(typeService.getEnumerationValue("BrandType", featureServiceBrand)
                     .getPk().toString()), getActiveCatalogversion());
         }

         if (CollectionUtils.isNotEmpty(productRangeModels))
         {
            final List<ProductRangeViewData> productRanges = new ArrayList<ProductRangeViewData>();
            List<ContentValueModel> contentValueModels = null;
            for (final ProductRangeModel productRangeModel : productRangeModels)
            {
               if (!("Yes".equalsIgnoreCase(getNameSearchItemValue(productRangeModel.getCode()))))
               {
                  contentValueModels =
                     genericContentService.getContentValue(productRangeModel.getCode(),
                        Arrays.asList("attribute_type_code"));
                  if ((CollectionUtils.isEmpty(featureValues)
                     && CollectionUtils.isNotEmpty(contentValueModels) && StringUtils
                        .isNotEmpty(featureService.getFirstFeatureValueAsString(contentValueModels
                           .get(0).getValue().toString(), source, new Date(), featureServiceBrand)))
                     || getFeatureValue(featureValues, contentValueModels))
                  {
                     final ProductRangeViewData productRange = new ProductRangeViewData();
                     productRangeBasicPopulator.populate(productRangeModel, productRange);
                     productRanges.add(productRange);
                  }
               }
               target.setProductRanges(productRanges);
            }
         }
      }
      catch (final UnknownIdentifierException uie)
      {
         LOG.error("Featue Descriptor not found", uie);
      }
   }

   /**
    * @param featureValues
    * @param contentValueModels
    * @return boolean
    */
   private boolean getFeatureValue(final List<Object> featureValues,
      final List<ContentValueModel> contentValueModels)
   {
      return (featureValues != null)
         && (featureValues.get(0).toString().equalsIgnoreCase(contentValueModels.get(0).getValue()
            .toString()));
   }

   /**
    * This method returns active product Catalogversion
    *
    * @return CatalogVersionModel
    */
   private CatalogVersionModel getActiveCatalogversion()
   {
      final CatalogModel catalogModel =
         catalogUtil.getDefaultProductCatalogForCMSSiteId(tuiUtilityService.getSiteBrand());
      return catalogVersionService.getCatalogVersion(catalogModel.getId(), "Online");
   }

   private String getNameSearchItemValue(final String code)
   {

      List<ContentValueModel> contentValueModels = null;
      GenericContentViewData contentViewData = null;
      String isNamesearchitem = "";
      contentValueModels =
         genericContentService.getContentValue(code, Arrays.asList(NAME_SEARCH_ITEM));
      if (contentValueModels != null && CollectionUtils.isNotEmpty(contentValueModels))
      {
         contentViewData = new GenericContentViewData();
         // populate ContentValueModel's value.
         genericContentPopulator.populate(contentValueModels.get(0), contentViewData);
         isNamesearchitem = contentViewData.getContent();
      }

      return isNamesearchitem;
   }

}
