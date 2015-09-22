/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author pts
 *
 */
public class AccommodationAttractionExcursionConverter extends
   AbstractPopulatingConverter<ItemModel, AccommodationViewData>
{

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   @Resource
   private TUIUrlResolver<AttractionModel> attractionUrlResolver;

   @Resource
   private FeatureService featureService;

   @Resource
   private BrandUtils brandUtils;

   @Override
   public AccommodationViewData createTarget()
   {
      return new AccommodationViewData();
   }

   @Override
   public void populate(final ItemModel source, final AccommodationViewData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");
      String brand = null;

      if ("Accommodation".equals(source.getItemtype()) || "Excursion".equals(source.getItemtype()))
      {
         final ProductModel prod = (ProductModel) source;
         target.setCode(prod.getCode());
         target.setName(prod.getName());
         target.setUrl(tuiProductUrlResolver.resolve(prod));
         if ("Accommodation".equals(source.getItemtype()))
         {
            brand = brandUtils.getFeatureServiceBrand(((AccommodationModel) source).getBrands());
         }

      }
      else if ("Attraction".equals(source.getItemtype()))
      {
         final AttractionModel attraction = (AttractionModel) source;
         target.setCode(attraction.getCode());
         target.setName(attraction.getName());
         target.setUrl(attractionUrlResolver.resolve(attraction));
      }

      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { "name" }));
      target.putFeatureCodesAndValues(featureService.getValuesForFeatures(featureDescriptorList,
         source, new Date(), brand));

      super.populate(source, target);
   }
}
