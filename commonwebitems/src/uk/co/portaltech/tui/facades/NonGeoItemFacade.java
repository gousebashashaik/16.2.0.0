/**
 *
 */
package uk.co.portaltech.tui.facades;

import de.hybris.platform.cms2.model.contents.CMSItemModel;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.components.model.SocialMediaLinksComponentModel;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;
import uk.co.portaltech.tui.web.view.data.DestinationGuideCollectionViewData;
import uk.co.portaltech.tui.web.view.data.DreamLinerPromotionViewData;
import uk.co.portaltech.tui.web.view.data.GroupsTabbingComponentViewData;
import uk.co.portaltech.tui.web.view.data.KidsClubComponentData;
import uk.co.portaltech.tui.web.view.data.ParallaxProductViewData;
import uk.co.portaltech.tui.web.view.data.ProductEditorialComponentViewData;
import uk.co.portaltech.tui.web.view.data.ProductEditorialImageViewData;
import uk.co.portaltech.tui.web.view.data.ProductPageComponentViewData;
import uk.co.portaltech.tui.web.view.data.SlideTesterEditorialViewData;
import uk.co.portaltech.tui.web.view.data.SocialMediaLinksComponentData;
import uk.co.portaltech.tui.web.view.data.StraplineViewData;

public interface NonGeoItemFacade
{

   List<ProductPageComponentViewData> getEditorialData(List<Map<String, String>> listofMapsData,
      CMSItemModel component);

   ProductEditorialImageViewData getProductEditorialImageData(
      List<Map<String, String>> listofMapsData, CMSItemModel component);

   ParallaxProductViewData getParallaxProductData(List<Map<String, String>> listofMapsData,
      CMSItemModel component);

   SocialMediaLinksComponentData getSocialMedia(final List<Map<String, String>> listofMapsData,
      SocialMediaLinksComponentModel socialMediaLinksComponentModel, String relativeurl);

   Map<String, List<KidsClubComponentData>> getkidsclubData(List<Map<String, String>> itemMap,
      String component);

   GroupsTabbingComponentViewData getTabbingComponentViewData(
      final List<Map<String, String>> listofMapsData);

   List<SlideTesterEditorialViewData> getSlideTesterData(List<Map<String, String>> listofMapsData);

   List<BenefitViewData> getProductBenefitsData(List<Map<String, String>> benefitsData);

   StraplineViewData getStraplineData(List<Map<String, String>> strapLineData);

   StraplineViewData getDisneyIntro();

   List<ProductPageComponentViewData> getEditorialDataForProductHeader(
      List<Map<String, String>> listofMapsData, CMSItemModel component);

   DreamLinerPromotionViewData getPromotionData(final List<Map<String, String>> listofMapsData);

   ProductEditorialComponentViewData getProductEditorialComponentData(
      List<Map<String, String>> itemMap);

   ProductPageComponentViewData getCollectionDiffProdData(final Map<String, String> mapData,
      final String componentWidth);

   List<ProductPageComponentViewData> getCollectionDiffProdData(
      List<Map<String, String>> listofMapsData, String componentWidth);

   DestinationGuideCollectionViewData getCollectionProductRangeData(
      List<Map<String, String>> listofMapsData);
}
