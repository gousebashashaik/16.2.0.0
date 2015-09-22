/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.media.services.FacilityMediaService;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.FacilityTypeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.facility.FacilityTypeService;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author l.furrer
 *
 */
public class FacilityBasicPopulator implements Populator<FacilityModel, FacilityViewData>
{

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private FeatureService featureService;

   private static final String[] FEATURELIST = { "name", "description", "priority", "openingTimes",
      "isKey" };

   @Resource(name = "facilityType")
   private FacilityTypeService facilityTypeService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private FacilityMediaService facilityMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   /** The log. */
   private static final Logger LOG = Logger.getLogger(FacilityBasicPopulator.class);

   @Override
   public void populate(final FacilityModel sourceModel, final FacilityViewData targetData)
      throws ConversionException
   {

      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");
      String brand = null;
      if (CollectionUtils.isNotEmpty(sourceModel.getBrands()))
      {
         brand = brandUtils.getFeatureServiceBrand(sourceModel.getBrands());
      }

      final Map<String, List<Object>> valuesForFeatures =
         featureService.getValuesForFeatures(Arrays.asList(FEATURELIST), sourceModel, new Date(),
            brand);
      targetData.putFeatureCodesAndValues(valuesForFeatures);
      if (valuesForFeatures.get("name") != null)
      {
         targetData.setName((String) valuesForFeatures.get("name").get(0));
      }
      if (valuesForFeatures.get("description") != null)
      {
         targetData.setDescription((String) valuesForFeatures.get("description").get(0));
      }

      LOG.debug("Facility Type is " + sourceModel.getType());
      if (sourceModel.getType() != null)
      {
         targetData.setFacilityType(sourceModel.getType().getName());
      }

      final FacilityTypeModel facilityTypeModel =
         facilityTypeService.getFacilityTypeParent(cmsSiteService.getCurrentCatalogVersion(),
            sourceModel.getType().getCode());
      if (facilityTypeModel != null)
      {
         targetData.setParentFacilityType(facilityTypeModel.getCode());
         targetData.setParentFacilityTypeName(facilityTypeModel.getName());

      }

      final List<MediaViewData> mediaList = new ArrayList<MediaViewData>();
      mediaPopulatorLite.populate(facilityMediaService.getImageMedias(sourceModel), mediaList);
      targetData.getGalleryImages().addAll(mediaList);

      final MediaModel pdfMediaModel = sourceModel.getPdfMedia();
      if (pdfMediaModel != null)
      {
         targetData.setPdfMediaUrl(pdfMediaModel.getURL());
      }
   }
}
