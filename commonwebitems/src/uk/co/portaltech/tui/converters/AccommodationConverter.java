package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author pts
 *
 */
public class AccommodationConverter extends
   AbstractPopulatingConverter<AccommodationModel, AccommodationViewData>
{

   @Resource
   private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;

   @Resource
   private FeatureService featureService;

   @Resource
   private BrandUtils brandUtils;

   private Populator<AccommodationModel, AccommodationViewData> productBasicPopulator;

   @Required
   public void setProductBasicPopulator(
      final Populator<AccommodationModel, AccommodationViewData> productBasicPopulator)
   {
      this.productBasicPopulator = productBasicPopulator;
   }

   @Override
   public AccommodationViewData createTarget()
   {
      return new AccommodationViewData();
   }

   @Override
   public void populate(final AccommodationModel source, final AccommodationViewData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      productBasicPopulator.populate(source, target);

      target.setCode(source.getCode());
      final List<String> featureDescriptorList =
         new ArrayList(Arrays.asList(new String[] { "name", "strapline" }));

      final String brand = brandUtils.getFeatureServiceBrand(source.getBrands());

      target.putFeatureCodesAndValues(featureService.getValuesForFeatures(featureDescriptorList,
         source, new Date(), brand));
      target.setName(source.getName());
      target.setAccommodationType(source.getType().toString());
      target.setBrand(brand);
      target.setUrl(tuiProductUrlResolver.resolve(source));
      super.populate(source, target);
   }
}
