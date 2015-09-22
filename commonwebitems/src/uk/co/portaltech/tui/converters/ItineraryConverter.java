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

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.model.ItineraryLeg;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;

/**
 * @author narendra.bm
 *
 */
public class ItineraryConverter extends AbstractPopulatingConverter<ItemModel, ItineraryLeg>
{

   private static final String EDITORIAL_CONTENT = "editorialContent";

   private static final String INTRODUCTION = "introduction";

   private static final String ATTRACTION = "Attraction";

   private static final String EXCURSION = "Excursion";

   private static final String ACCOMMODATION = "Accommodation";

   private static final String NAME = "name";

   private static final String NON_BOOKABLE_CODE = "P00067";

   private static List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[] {
      NAME, INTRODUCTION, EDITORIAL_CONTENT, NON_BOOKABLE_CODE }));

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   @Resource
   private TUIUrlResolver<ItemModel> attractionUrlResolver;

   @Resource
   private FeatureService featureService;

   @Override
   public ItineraryLeg createTarget()
   {
      return new ItineraryLeg();
   }

   @Override
   public void populate(final ItemModel source, final ItineraryLeg target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");
      final String brand = null;

      target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
         featureDescriptorList, source, new Date(), brand));
      target.setTitle(target.getFeatureValue(NAME));
      if (ACCOMMODATION.equals(source.getItemtype()) || EXCURSION.equals(source.getItemtype()))
      {
         final ProductModel prod = (ProductModel) source;
         target.setCode(prod.getCode());
         target.setName(prod.getName());
         target.setNonBookable(StringUtils.isNotEmpty(target.getFeatureValue(NON_BOOKABLE_CODE))
            ? true : false);
         if (ACCOMMODATION.equals(source.getItemtype()))
         {
            target.setUrl(tuiProductUrlResolver.resolve(prod));
            target.setDescription(target.getFeatureValue(INTRODUCTION));
         }
         else
         {
            target.setUrl(attractionUrlResolver.resolve(source));
            target.setDescription(target.getFeatureValue(EDITORIAL_CONTENT));
         }
      }
      else if (ATTRACTION.equals(source.getItemtype()))
      {
         final AttractionModel attraction = (AttractionModel) source;
         target.setCode(attraction.getCode());
         target.setName(attraction.getName());
         target.setUrl(attractionUrlResolver.resolve(attraction));
         target.setDescription(target.getFeatureValue(EDITORIAL_CONTENT));
         target.setNonBookable(StringUtils.isNotEmpty(target.getFeatureValue(NON_BOOKABLE_CODE))
            ? true : false);
      }

      super.populate(source, target);
   }
}
