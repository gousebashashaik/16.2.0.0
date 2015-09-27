/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.ContentItemModel;
import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.travel.services.nongeo.NonGeoItemService;
import uk.co.portaltech.tui.components.model.SocialMediaLinksComponentModel;
import uk.co.portaltech.tui.components.model.SocialMediaModel;
import uk.co.portaltech.tui.facades.NonGeoItemFacade;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;
import uk.co.portaltech.tui.web.view.data.DestinationGuideCollectionViewData;
import uk.co.portaltech.tui.web.view.data.DreamLinerPromotionViewData;
import uk.co.portaltech.tui.web.view.data.GroupsTabbingComponentViewData;
import uk.co.portaltech.tui.web.view.data.ImageSectionViewData;
import uk.co.portaltech.tui.web.view.data.KidsClubComponentData;
import uk.co.portaltech.tui.web.view.data.ParallaxProductCollectionData;
import uk.co.portaltech.tui.web.view.data.ParallaxProductViewData;
import uk.co.portaltech.tui.web.view.data.ProductEditorialComponentViewData;
import uk.co.portaltech.tui.web.view.data.ProductEditorialImageViewData;
import uk.co.portaltech.tui.web.view.data.ProductPageComponentViewData;
import uk.co.portaltech.tui.web.view.data.SlideTesterEditorialViewData;
import uk.co.portaltech.tui.web.view.data.SocialMediaLinksComponentData;
import uk.co.portaltech.tui.web.view.data.SocialMediaViewData;
import uk.co.portaltech.tui.web.view.data.StraplineViewData;
import uk.co.portaltech.tui.web.view.data.TabSectionViewData;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author venkataharish.k
 *
 */
public class NonGeoItemFacadeImpl implements NonGeoItemFacade
{

   /**
     *
     */
   private static final String SPACE = " ";

   /**
     *
     */
   private static final String NUMBER_REGEX = "\\d+";

   /**
     *
     */
   private static final String EMPTY_STRING = "";

   /**
     *
     */
   private static final String UNDERSCORE = "_";

   @Resource
   private NonGeoItemService nonGeoItemService;

   @Resource
   private ConfigurationService configurationService;

   private static final String HEADING = "heading";

   private static final String DESCRIPTION = "description";

   private static final String DISPLAYNAME = "display_name";

   private static final String INTRO = "intro";

   private static final String TABBINGCOMPONENT = "TabbingComponent";

   private static final TUILogUtils LOG = new TUILogUtils("NonGeoItemFacadeImpl");

   private static final String MAIN_SECTION = "mainsection";

   private static final String XLARGE = "xlarge";

   private static final String LARGE = "large";

   private static final String MEDIUM = "medium";

   private static final String SMALL = "small";

   private static final String JPG = ".jpg";

   private static final String HOMEPAGE_HALF_HALF = "homepage_half_half";

   private static final String HOMEPAGE_HALF = "homepage_half";

   private static final String DESCRIPTION1 = "description1";

   private static final String DESCRIPTION2 = "description2";

   private static final String STRAPLINE = "strapline";

   private static final String TITLE = "title";

   private static final String ZERO = "0";

   private static final String ONE = "1";

   private static final String TWO = "2";

   private static final String THREE = "3";

   private static final String FOUR = "4";

   private static final String FIVE = "5";

   private static final String SIX = "6";

   private static final String SEVEN = "7";

   private static final String EIGHT = "8";

   private static final String NINE = "9";

   private static final String TEN = "10";

   private static final int NUMBER_THREE = 3;

   private static final int NUMBER_FOUR = 4;

   private static final String SMR_ID = "WF_COM_SMR";

   private static final String SEN_ID = "WF_COM_327_SENSATORI";

   private static final String COU_ID = "WF_COM_004_COU";

   private static final String FAM_ID = "WF_COM_006_FAM";

   private static final String WED_ID = "WF_COM_007_WED";

   private static final int VALUE_TWO = 2;

   private static List<String> componentsListForXtraLargeImage = new ArrayList<String>();

   static
   {
      componentsListForXtraLargeImage.add("WF_COM_005_FAM");
      componentsListForXtraLargeImage.add("WF_COM_SMR");
      componentsListForXtraLargeImage.add("WF_COM_005_COU");
      componentsListForXtraLargeImage.add("WF_COM_0005_WED");
      componentsListForXtraLargeImage.add("WF_COM_005_FLI");
      componentsListForXtraLargeImage.add("WF_COM_331_SENSATORI");
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.NonGeoItemFacade#getCollectionDiffProdData (java.util.List, java.lang.String)
    */
   @Override
   public List<ProductPageComponentViewData> getCollectionDiffProdData(final List<Map<String, String>> listofMapsData,
         final String componentWidth)
   {
      final List<ProductPageComponentViewData> viewDataList = new ArrayList<ProductPageComponentViewData>();
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      ProductPageComponentViewData editorialViewData = null;
      final Map<String, String> imageUrlsMap = new HashMap<String, String>();

      String tracCode = null;
      String keyCode = null;

      for (final Map<String, String> map : listofMapsData)
      {

         editorialViewData = new ProductPageComponentViewData();
         tracCode = map.get("0");
         editorialViewData.setTitle(tracCode.toLowerCase());

         keyCode = tracCode + UNDERSCORE + map.get("1");
         editorialViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));
         imageUrlPopulationForCollectionDiffProdData(componentWidth, contentValueEpicMap, editorialViewData, imageUrlsMap,
               keyCode);

         keyCode = tracCode + "_" + map.get("2");
         editorialViewData.setDescription(getValue(contentValueEpicMap, keyCode));

         viewDataList.add(editorialViewData);
      }

      return viewDataList;
   }

   /**
    * @param componentWidth
    * @param contentValueEpicMap
    * @param editorialViewData
    * @param imageUrlsMap
    * @param keyCode
    */
   private void imageUrlPopulationForCollectionDiffProdData(final String componentWidth,
         final Map<String, ContentValueModel> contentValueEpicMap, final ProductPageComponentViewData editorialViewData,
         final Map<String, String> imageUrlsMap, final String keyCode)
   {
      String imgUrl = "";
      String imgUrlSmall = "";
      if (HOMEPAGE_HALF_HALF.equals(componentWidth))
      {
         imgUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
      }
      else if (HOMEPAGE_HALF.equals(componentWidth))
      {
         imgUrl = getValue1(contentValueEpicMap, keyCode, LARGE);
      }
      else
      {
         imgUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);
         imgUrlSmall = getValue1(contentValueEpicMap, keyCode, SMALL);
         imageUrlsMap.put(MEDIUM, imgUrl);
         imageUrlsMap.put(SMALL, imgUrlSmall);
      }

      if (imgUrl != null)
      {
         editorialViewData.setImageUrl(imgUrl);
         editorialViewData.setImageUrlsMap(imageUrlsMap);
      }
   }

   @Override
   public List<ProductPageComponentViewData> getEditorialData(final List<Map<String, String>> listofMapsData,
         final CMSItemModel component)
   {

      final List<ProductPageComponentViewData> viewDataList = new ArrayList<ProductPageComponentViewData>();
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      final Map<String, ContentItemModel> contentItemEpicMap = nonGeoItemService.getContentValuesForVideo(epicCodeList);
      ProductPageComponentViewData editorialViewData = null;
      ProductPageComponentViewData editorialViewDatamain = null;

      String tracCode = null;
      String keyCode = null;
      for (final Map<String, String> map : listofMapsData)
      {
         editorialViewData = new ProductPageComponentViewData();
         tracCode = map.get("0");

         keyCode = tracCode + UNDERSCORE + map.get("1");
         editorialViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));

         if (map.containsValue(MAIN_SECTION))
         {
            final String imgUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
            if (imgUrl != null)
            {
               editorialViewData.setImageUrl(imgUrl);
            }
         }
         else if (StringUtils.equalsIgnoreCase(SEN_ID, component.getUid())
               || StringUtils.equalsIgnoreCase(COU_ID, component.getUid()) || checkForComponent(component))
         {

            final String imgUrl = getValue1(contentValueEpicMap, keyCode, LARGE);
            if (imgUrl != null)
            {
               editorialViewData.setImageUrl(imgUrl);
            }
         }
         else if (componentsListForXtraLargeImage.contains(component.getUid()))
         {
            String imageUrl = null;
            imageUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
            if (imageUrl != null)
            {
               editorialViewData.setImageUrl(imageUrl);
            }
         }
         else if ("WF_COM_331_SENSATORI".equalsIgnoreCase(component.getUid()))
         {
            final String imgUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
            if (imgUrl != null)
            {
               editorialViewData.setImageUrl(imgUrl);
            }
         }

         else if ("BrideAndGroomIncentives".equalsIgnoreCase(component.getName())
               || "WF_COM_005_WED".equalsIgnoreCase(component.getUid()))
         {

            String imageUrl = null;
            imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
            if (imageUrl != null)
            {
               editorialViewData.setImageUrl(imageUrl);
            }
         }

         else if ("ExtraSpeciualTouches".equalsIgnoreCase(component.getName())
               || "WF_COM_008_WED".equalsIgnoreCase(component.getUid()))
         {
            editorialViewData.setImageUrl(map.get("3"));

         }
         else if ("WF_COM_325_LH".equalsIgnoreCase(component.getUid()))
         {
            final String imgUrl = getValue1(contentValueEpicMap, keyCode, LARGE);
            if (imgUrl != null)
            {
               editorialViewData.setImageUrl(imgUrl);
            }

         }
         else
         {
            final String imgUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);
            if (imgUrl != null)
            {
               editorialViewData.setImageUrl(imgUrl);
            }
         }

         if ("WF_COM_0005_WED".equalsIgnoreCase(component.getUid()))
         {
            keyCode = tracCode + UNDERSCORE + map.get("3");
            editorialViewData.setBlog(getValue(contentValueEpicMap, keyCode));

            keyCode = tracCode + UNDERSCORE + map.get("4");
            editorialViewData.setBrochure(getValue(contentValueEpicMap, keyCode));

            keyCode = tracCode + UNDERSCORE + map.get("5");
            editorialViewData.setEmail(getValue(contentValueEpicMap, keyCode));

            keyCode = tracCode + UNDERSCORE + map.get("6");
            editorialViewData.setCallUsOn(getNumberDataFromString(getValue(contentValueEpicMap, keyCode)));

            keyCode = tracCode + UNDERSCORE + map.get("7");
            editorialViewData.setExtraDetails(getValue(contentValueEpicMap, keyCode));
         }
         checkForEconomyComp(component, contentValueEpicMap, editorialViewData, tracCode, map);

         if ("WF_COM_003_LONGHAUL".equalsIgnoreCase(component.getUid()))
         {
            editorialViewData.setUrl(map.get("4"));
            editorialViewData.setLinkText(map.get("1"));

         }

         keyCode = tracCode + UNDERSCORE + map.get("2");
         editorialViewData.setDescription(getValue(contentValueEpicMap, keyCode));

         if (editorialViewData.getImageUrl() == null)
         {
            if (map.containsValue(MAIN_SECTION))
            {
               final String imgUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
               if (imgUrl != null)
               {
                  editorialViewData.setImageUrl(imgUrl);
               }
            }
            else
            {
               final String imgUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);
               if (imgUrl != null)
               {
                  editorialViewData.setImageUrl(imgUrl);
               }
            }
         }

         if ("ProductHeaderComponent".equals(component.getItemtype()))
         {
            tracCode = map.get("0");

            final MediaModel media = getMedia(contentItemEpicMap, tracCode);
            if (media != null)
            {
               final String videoUrl = media.getURL();
               if (videoUrl != null)
               {
                  editorialViewData.setVideoUrl(videoUrl);
               }
            }

         }

         if (map.get("3") != null)
         {
            keyCode = tracCode + UNDERSCORE + map.get("3");
            editorialViewData.setSubTitle(getValue(contentValueEpicMap, keyCode));
            if (editorialViewData.getImageUrl() == null)
            {
               if (map.containsValue(MAIN_SECTION))
               {
                  final String imgUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
                  if (imgUrl != null)
                  {
                     editorialViewData.setImageUrl(imgUrl);
                  }
               }
               else
               {
                  final String imgUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);
                  if (imgUrl != null)
                  {
                     editorialViewData.setImageUrl(imgUrl);
                  }
               }
            }
         }

         if ("parent".equals(map.get(String.valueOf(map.size() - 1))))
         {

            editorialViewData.setSubImage(false);
            editorialViewDatamain = editorialViewData;
            viewDataList.add(editorialViewDatamain);

         }
         else
         {
            editorialViewData.setSubImage(true);
            viewDataList.add(editorialViewData);
         }

      }

      return viewDataList;
   }

   /**
    * @param component
    * @param contentValueEpicMap
    * @param editorialViewData
    * @param tracCode
    * @param map
    */
   private void checkForEconomyComp(final CMSItemModel component, final Map<String, ContentValueModel> contentValueEpicMap,
         final ProductPageComponentViewData editorialViewData, final String tracCode, final Map<String, String> map)
   {
      String keyCode;
      if ("WF_COM_LH_EC".equalsIgnoreCase(component.getUid()))
      {
         final Map<String, String> usps = new HashMap<String, String>();
         keyCode = tracCode + UNDERSCORE + map.get("4");
         usps.put(map.get("4"), getValue(contentValueEpicMap, keyCode));
         keyCode = tracCode + UNDERSCORE + map.get("5");
         usps.put(map.get("5"), getValue(contentValueEpicMap, keyCode));
         keyCode = tracCode + UNDERSCORE + map.get("6");
         usps.put(map.get("6"), getValue(contentValueEpicMap, keyCode));
         editorialViewData.setUsps(usps);
      }
   }

   @Override
   public Map<String, List<KidsClubComponentData>> getkidsclubData(final List<Map<String, String>> listofMapsData,
         final String component)
   {
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, true);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);

      Map<String, List<KidsClubComponentData>> tabMap = new LinkedHashMap<String, List<KidsClubComponentData>>();
      Map<String, List<KidsClubComponentData>> tabMapTemp = null;

      Map<String, String> tabNames = new HashMap();

      for (final Map<String, String> map : listofMapsData)
      {
         fetchKidsClubData(component, contentValueEpicMap, tabMap, tabNames, map);
      }

      tabMapTemp = new LinkedHashMap<String, List<KidsClubComponentData>>();

      for (final Map.Entry<String, List<KidsClubComponentData>> pp : tabMap.entrySet())
      {
         if (tabNames.get(pp.getKey()) != null)
         {
            tabMapTemp.put(tabNames.get(pp.getKey()), tabMap.get(pp.getKey()));
         }
         else if (MAIN_SECTION.equalsIgnoreCase(pp.getKey()))
         {
            tabMapTemp.put(pp.getKey(), pp.getValue());
         }
      }

      tabMap = null;
      tabNames = null;
      return tabMapTemp;
   }

   /**
    * @param component
    * @param contentValueEpicMap
    * @param tabMap
    * @param tabNames
    * @param map
    */
   private void fetchKidsClubData(final String component, final Map<String, ContentValueModel> contentValueEpicMap,
         final Map<String, List<KidsClubComponentData>> tabMap, final Map<String, String> tabNames, final Map<String, String> map)
   {
      KidsClubComponentData editorialViewData;
      String tracCode;
      String keyCode;

      String key;
      editorialViewData = new KidsClubComponentData();

      if ("tabTitle".equals(map.get(String.valueOf(map.size() - 1))))
      {
         key = map.get(String.valueOf(map.size() - VALUE_TWO));
         tracCode = map.get("0");
         keyCode = tracCode + UNDERSCORE + map.get("1");
         tabNames.put(key, getValue(contentValueEpicMap, keyCode));

      }
      else if (MAIN_SECTION.equals(map.get(String.valueOf(map.size() - 1))))
      {
         fetchKidsClubMainSectionData(contentValueEpicMap, tabMap, map, editorialViewData);
      }

      else
      {
         createViewDataForTabbing(component, contentValueEpicMap, tabMap, map, editorialViewData);
      }
   }

   /**
    * @param component
    * @param contentValueEpicMap
    * @param tabMap
    * @param map
    * @param editorialViewData
    */
   private void createViewDataForTabbing(final String component, final Map<String, ContentValueModel> contentValueEpicMap,
         final Map<String, List<KidsClubComponentData>> tabMap, final Map<String, String> map,
         final KidsClubComponentData editorialViewData)
   {
      if (TABBINGCOMPONENT.equals(component))
      {
         tabSectionViewdataCreationForKidsClub(contentValueEpicMap, editorialViewData, tabMap, map);
      }
      else
      {
         tabSectionViewdataCreationForFunAround(contentValueEpicMap, editorialViewData, tabMap, map);
      }
   }

   /**
    * @param contentValueEpicMap
    * @param tabMap
    * @param map
    * @param editorialViewData
    */
   private void fetchKidsClubMainSectionData(final Map<String, ContentValueModel> contentValueEpicMap,
         final Map<String, List<KidsClubComponentData>> tabMap, final Map<String, String> map,
         final KidsClubComponentData editorialViewData)
   {
      String tracCode;
      String keyCode;
      List<KidsClubComponentData> viewMapList;
      tracCode = map.get("0");
      viewMapList = new ArrayList<KidsClubComponentData>();

      if (map.get("1") != null)
      {
         keyCode = tracCode + UNDERSCORE + map.get("1");
         editorialViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));
         editorialViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, XLARGE));
      }

      if (map.get("2") != null)
      {

         keyCode = tracCode + UNDERSCORE + map.get("2");
         editorialViewData.setIntro(getValue(contentValueEpicMap, keyCode));
         if (editorialViewData.getImageUrl() == null)
         {
            editorialViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, XLARGE));
         }
      }

      if (map.get("3") != null && !map.get("3").endsWith(JPG) && (map.size() - 1) != NUMBER_THREE)
      {
         keyCode = tracCode + UNDERSCORE + map.get("3");
         editorialViewData.setDescription(getValue(contentValueEpicMap, keyCode));
         if (editorialViewData.getImageUrl() == null)
         {
            editorialViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, XLARGE));
         }
      }
      viewMapList.add(editorialViewData);

      tabMap.put(MAIN_SECTION, viewMapList);
   }

   /**
    * @param contentValueEpicMap
    * @param editorialViewData
    * @param tabMap
    * @param map
    */
   private void tabSectionViewdataCreationForKidsClub(final Map<String, ContentValueModel> contentValueEpicMap,
         final KidsClubComponentData editorialViewData, final Map<String, List<KidsClubComponentData>> tabMap,
         final Map<String, String> map)
   {
      String tracCode;
      String keyCode;
      List<KidsClubComponentData> viewMapList;
      String key;
      tracCode = map.get(ZERO);
      key = map.get(String.valueOf(map.size() - 1));

      viewMapList = fetchMapList(tabMap, key);

      if (map.get(TWO) != null)
      {
         setFeatureTitleForTabSectionViewdataCreationForKidsClub(contentValueEpicMap, editorialViewData, map, tracCode);
      }

      if (checkCond(map))
      {
         setFeatureIntroForTabSectionViewdataCreationForKidsClub(contentValueEpicMap, editorialViewData, map, tracCode);

      }

      if (checkCond1(map))
      {
         keyCode = tracCode + UNDERSCORE + map.get(FOUR);
         editorialViewData.setDescription(getValue(contentValueEpicMap, keyCode));
         if (editorialViewData.getImageUrl() == null)
         {
            editorialViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, MEDIUM));
         }
      }

      viewMapList.add(editorialViewData);

      tabMap.put(key, viewMapList);
   }

   /**
    * @param tabMap
    * @param key
    * @return
    */
   private List<KidsClubComponentData> fetchMapList(final Map<String, List<KidsClubComponentData>> tabMap, final String key)
   {
      List<KidsClubComponentData> viewMapList;
      if (tabMap.containsKey(key))
      {
         viewMapList = tabMap.get(key);
      }
      else
      {
         viewMapList = new ArrayList<KidsClubComponentData>();
      }
      return viewMapList;
   }

   /**
    * @param contentValueEpicMap
    * @param editorialViewData
    * @param map
    * @param tracCode
    */
   private void setFeatureIntroForTabSectionViewdataCreationForKidsClub(final Map<String, ContentValueModel> contentValueEpicMap,
         final KidsClubComponentData editorialViewData, final Map<String, String> map, final String tracCode)
   {
      String keyCode;
      keyCode = tracCode + UNDERSCORE + map.get(THREE);
      editorialViewData.setIntro(getValue(contentValueEpicMap, keyCode));
      if (editorialViewData.getImageUrl() == null)
      {
         editorialViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, MEDIUM));
      }
   }

   /**
    * @param map
    * @return
    */
   private boolean checkCond1(final Map<String, String> map)
   {
      return map.get(FOUR) != null && !map.get(FOUR).endsWith(JPG) && (map.size() - 1) != NUMBER_FOUR;
   }

   /**
    * @param map
    * @return
    */
   private boolean checkCond(final Map<String, String> map)
   {
      return map.get(THREE) != null && !map.get(THREE).endsWith(JPG) && (map.size() - 1) != NUMBER_THREE;
   }

   /**
    * @param contentValueEpicMap
    * @param editorialViewData
    * @param map
    * @param tracCode
    */
   private void setFeatureTitleForTabSectionViewdataCreationForKidsClub(final Map<String, ContentValueModel> contentValueEpicMap,
         final KidsClubComponentData editorialViewData, final Map<String, String> map, final String tracCode)
   {
      String keyCode;
      keyCode = tracCode + UNDERSCORE + map.get(TWO);
      editorialViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));
      editorialViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, MEDIUM));
   }

   /**
    * @param contentValueEpicMap
    * @param editorialViewData
    * @param tabMap
    * @param map
    */
   private void tabSectionViewdataCreationForFunAround(final Map<String, ContentValueModel> contentValueEpicMap,
         final KidsClubComponentData editorialViewData, final Map<String, List<KidsClubComponentData>> tabMap,
         final Map<String, String> map)
   {
      String tracCode;
      String keyCode;
      @SuppressWarnings("unused")
      List<KidsClubComponentData> viewMapList;
      String key;
      tracCode = map.get(ZERO);
      key = map.get(String.valueOf(map.size() - 1));

      viewMapList = fetchMapList(tabMap, key);

      if (map.get(ONE) != null)
      {
         keyCode = tracCode + UNDERSCORE + map.get(ONE);
         editorialViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));
         final String imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
         if (imageUrl != null)
         {
            editorialViewData.setImageUrl(imageUrl);
         }

      }
      if (map.get(TWO) != null)
      {
         keyCode = tracCode + UNDERSCORE + map.get(TWO);
         editorialViewData.setIntro(getValue(contentValueEpicMap, keyCode));
         final String imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
         if (imageUrl != null)
         {
            editorialViewData.setImageUrl(imageUrl);
         }
      }
      if (map.get(THREE) != null)
      {
         keyCode = tracCode + UNDERSCORE + map.get(THREE);
         editorialViewData.setStrapLine(getValue(contentValueEpicMap, keyCode));
         final String imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
         if (imageUrl != null)
         {
            editorialViewData.setImageUrl(imageUrl);
         }
      }

      viewMapList.add(editorialViewData);

      tabMap.put(key, viewMapList);
   }

   @Override
   public List<SlideTesterEditorialViewData> getSlideTesterData(final List<Map<String, String>> listofMapsData)
   {
      final List<SlideTesterEditorialViewData> viewDataList = new ArrayList<SlideTesterEditorialViewData>();
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      final Map<String, ContentItemModel> contentItemEpicMap = nonGeoItemService.getContentValuesForVideo(epicCodeList);
      SlideTesterEditorialViewData productViewData = null;

      String tracCode = null;
      String keyCode = null;
      for (final Map<String, String> map : listofMapsData)
      {
         productViewData = new SlideTesterEditorialViewData();
         tracCode = map.get("0");

         keyCode = tracCode + UNDERSCORE + map.get("1");
         productViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));
         productViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, XLARGE));

         keyCode = tracCode + UNDERSCORE + map.get("2");
         productViewData.setDescription(getValue(contentValueEpicMap, keyCode));
         if (productViewData.getImageUrl() == null)
         {
            productViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, XLARGE));
         }

         productViewData.setDescription(getValue(contentValueEpicMap, keyCode));
         keyCode = tracCode + UNDERSCORE + map.get("3");
         if (productViewData.getImageUrl() == null)
         {
            productViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, XLARGE));
         }

         productViewData.setSubTitle(getValue(contentValueEpicMap, keyCode));

         final String videoUrl = getMedia(contentItemEpicMap, tracCode).getURL();
         if (videoUrl != null)
         {
            productViewData.setVideo(videoUrl);
         }

      }
      viewDataList.add(productViewData);
      return viewDataList;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.NonGeoItemFacade#getProductBenefitsData(java.util.List)
    */
   @Override
   public List<BenefitViewData> getProductBenefitsData(final List<Map<String, String>> benefitsData)
   {
      final List<BenefitViewData> viewDataList = new ArrayList<BenefitViewData>();
      final List<String> epicCodeList = getTracsCodeList(benefitsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);

      BenefitViewData benefitViewData = null;

      String tracCode = null;
      String keyCode = null;
      for (final Map<String, String> map : benefitsData)
      {
         benefitViewData = new BenefitViewData();
         tracCode = map.get("0");

         keyCode = tracCode + UNDERSCORE + map.get("1");

         benefitViewData.setName(getValue(contentValueEpicMap, keyCode));
         keyCode = tracCode + UNDERSCORE + map.get("2");

         benefitViewData.setDescription(getValue(contentValueEpicMap, keyCode));
         benefitViewData.setThunbnailUrl(getMedia(map));

         viewDataList.add(benefitViewData);
      }

      return viewDataList;
   }

   private String getMedia(final Map<String, String> map)
   {
      String imageurl = null;
      if (map.size() > VALUE_TWO)
      {
         imageurl = map.get(String.valueOf(map.size() - VALUE_TWO));
         if (imageurl != null && (imageurl.endsWith(JPG) || imageurl.endsWith(".gif") || imageurl.endsWith(".png")))
         {
            return imageurl;
         }
      }
      return imageurl;
   }

   private List<String> getTracsCodeList(final List<Map<String, String>> listofMapsData, final boolean isKidsdata)
   {
      final List<String> tracsCodeList = new ArrayList<String>();

      for (final Map<String, String> map : listofMapsData)
      {
         tracsCodeList.add(map.get("0"));
         if (isKidsdata)
         {
            tracsCodeList.add(map.get("1"));
         }

      }

      return tracsCodeList;

   }

   /**
    * @param contentItemEpicMap
    * @param keyCode
    * @return
    */
   @SuppressWarnings(
   { "boxing", "javadoc" })
   private MediaModel getMedia(final Map<String, ContentItemModel> contentItemEpicMap, final String keyCode)
   {
      if (keyCode != null)
      {
         final Object obj = contentItemEpicMap.get(keyCode);
         if (obj != null)
         {
            final ContentItemModel contentItem = (ContentItemModel) obj;
            if (contentItem.getMedias() != null)
            {
               final List<MediaContainerModel> mediasContainers = contentItem.getMedias();
               List<MediaModel> medias = null;
               medias = checkMediaConstants(mediasContainers, medias);
               if (medias != null)
               {
                  for (final MediaModel media : medias)
                  {
                     if (!(media.getURL().isEmpty()))
                     {
                        return media;
                     }
                  }

               }
               else
               {
                  LOG.info("ContentValueModel does exist but value dont exiat " + keyCode);
                  return null;
               }
            }
         }

      }
      return null;
   }

   /**
    * @param mediasContainers
    * @param medias
    * @return
    */
   private List<MediaModel> checkMediaConstants(final List<MediaContainerModel> mediasContainers, final List<MediaModel> medias)
   {

      List<MediaModel> newmedia = medias;
      if (CollectionUtils.isNotEmpty(mediasContainers))
      {
         newmedia = (List<MediaModel>) mediasContainers.get(0).getMedias();
      }
      return newmedia;
   }

   private String getValue(final Map<String, ContentValueModel> contentValueEpicMap, final String keyCode)
   {
      if (keyCode != null)
      {
         final Object object = contentValueEpicMap.get(keyCode);
         if (object != null)
         {
            final ContentValueModel contentValueModel = (ContentValueModel) object;
            if (contentValueModel.getValue() != null)
            {
               return (String) contentValueModel.getValue();

            }
            return null;
         }
         else
         {
            LOG.info("ContentValueModel does exist but value doesn't exist " + keyCode);
         }

      }
      return null;
   }

   private String getValue1(final Map<String, ContentValueModel> contentValueEpicMap, final String keyCode, final String size)
   {
      if (keyCode != null && keyCode.indexOf("http") == -1)
      {
         final Object object = contentValueEpicMap.get(keyCode);
         if (object != null)
         {
            final ContentValueModel contentValueModel = (ContentValueModel) object;
            if (contentValueModel.getValue() != null)
            {
               final List<MediaContainerModel> mediasContainers = contentValueModel.getMedias();
               List<MediaModel> medias = null;
               medias = checkMediaConstants(mediasContainers, medias);
               if (medias != null)
               {
                  for (final MediaModel media : medias)
                  {
                     if (size.equals(media.getMediaFormat().getName()) && (media.getURL() != null) && (!media.getURL().isEmpty()))
                     {
                        return media.getURL();
                     }
                  }
               }
            }
            else
            {
               LOG.info("ContentValueModel does exist but value dont exiat " + keyCode);
               return null;
            }
         }
      }
      return null;
   }

   /*
    * @param StrapLine Data.
    *
    * Return the StraplineViewData having heading and description.
    */
   @Override
   public StraplineViewData getStraplineData(final List<Map<String, String>> strapLineData)
   {
      final List<String> epicCodeList = getTracsCodeList(strapLineData, false);
      final StraplineViewData viewData = new StraplineViewData();

      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);

      for (final Map<String, String> map : strapLineData)
      {
         if (HEADING.equals(map.get(String.valueOf(map.size() - 1))))
         {

            viewData.setTitle(getValueForKey(contentValueEpicMap, map));
         }
         if (DESCRIPTION.equals(map.get(String.valueOf(map.size() - 1))))
         {

            viewData.setDescription(getValueForKey(contentValueEpicMap, map));
         }
      }
      return viewData;
   }

   /**
    * @param contentValueEpicMap
    * @param map
    * @return content value.
    */
   private String getValueForKey(final Map<String, ContentValueModel> contentValueEpicMap, final Map<String, String> map)
   {
      final String tracCode = map.get("0");
      final String keyCode = tracCode + UNDERSCORE + map.get("1");
      return getValue(contentValueEpicMap, keyCode);

   }

   @Override
   public StraplineViewData getDisneyIntro()
   {

      final String epicCodeForDisneyMyMagic = configurationService.getConfiguration().getString("disneymymagic.epicode");
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService
            .getContentForEpicCode(epicCodeForDisneyMyMagic);

      final StraplineViewData straplineViewData = new StraplineViewData();
      straplineViewData.setTitle((String) contentValueEpicMap.get(DISPLAYNAME).getValue());
      straplineViewData.setDescription((String) contentValueEpicMap.get(INTRO).getValue());

      return straplineViewData;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.NonGeoItemFacade#getParallaxProductData(java.util.List,
    * de.hybris.platform.cms2.model.contents.CMSItemModel)
    */
   @Override
   public ParallaxProductViewData getParallaxProductData(final List<Map<String, String>> listofMapsData,
         final CMSItemModel component)
   {
      final List<ParallaxProductCollectionData> viewDataList = new ArrayList<ParallaxProductCollectionData>();
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      final ParallaxProductViewData parallaxProductViewData = new ParallaxProductViewData();
      ParallaxProductCollectionData editorialViewData = null;

      String tracCode = null;
      String keyCode = null;
      for (final Map<String, String> map : listofMapsData)
      {
         tracCode = map.get(ZERO);

         if (map.containsValue(HEADING) || map.containsValue(DESCRIPTION))
         {

            keyCode = tracCode + UNDERSCORE + map.get(ONE);
            if (HEADING.equalsIgnoreCase(map.get(TWO)))
            {
               parallaxProductViewData.setTitle(getValue(contentValueEpicMap, keyCode));
            }
            else if (DESCRIPTION.equalsIgnoreCase(map.get(TWO)))
            {
               parallaxProductViewData.setDescription(getValue(contentValueEpicMap, keyCode));
            }

         }
         else
         {
            editorialViewData = new ParallaxProductCollectionData();

            keyCode = tracCode + UNDERSCORE + map.get(ONE);
            // to set image title
            final String imageTitle = getValue(contentValueEpicMap, keyCode);
            if (imageTitle != null)
            {
               editorialViewData.setImagetitle(imageTitle);
               editorialViewData.setImageabsencetext(imageTitle);
            }

            // to set image description
            keyCode = tracCode + UNDERSCORE + map.get(TWO);
            final String imageDescription = getValue(contentValueEpicMap, keyCode);
            if (imageDescription != null)
            {
               editorialViewData.setImagedescription(imageDescription);
            }
            else
            {
               editorialViewData.setImagedescription(EMPTY_STRING);
            }

            // to set large image
            String largeImageUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
            if (largeImageUrl != null)
            {
               editorialViewData.setLimage(largeImageUrl);
            }

            // to set small image
            String smallImageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
            if (smallImageUrl != null)
            {
               editorialViewData.setSimage(smallImageUrl);
            }

            if (StringUtils.isEmpty(editorialViewData.getLimage()))
            {

               keyCode = tracCode + UNDERSCORE + map.get(ONE);
               largeImageUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);

               if (largeImageUrl != null)
               {
                  editorialViewData.setLimage(largeImageUrl);
               }

               smallImageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
               if (smallImageUrl != null)
               {
                  editorialViewData.setSimage(smallImageUrl);
               }
            }

            if (largeImageUrl != null || smallImageUrl != null)
            {
               viewDataList.add(editorialViewData);
            }

         }

      }
      parallaxProductViewData.setImageData(viewDataList);
      parallaxProductViewData.setNoofimages(viewDataList.size());
      return parallaxProductViewData;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.NonGeoItemFacade#getProductEditorialImageData(java.util.List,
    * de.hybris.platform.cms2.model.contents.CMSItemModel)
    */
   @Override
   public ProductEditorialImageViewData getProductEditorialImageData(final List<Map<String, String>> listofMapsData,
         final CMSItemModel component)
   {
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      final ProductEditorialImageViewData productEditorialImageViewData = new ProductEditorialImageViewData();

      String tracCode = null;
      String keyCode = null;
      for (final Map<String, String> map : listofMapsData)
      {
         tracCode = map.get(ZERO);
         keyCode = tracCode + UNDERSCORE + map.get(ONE);

         if (map.containsValue(MAIN_SECTION))
         {
            final String imgUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
            if (imgUrl != null)
            {
               productEditorialImageViewData.setImage(imgUrl);
            }
            else
            {
               productEditorialImageViewData.setImage(EMPTY_STRING);
            }
            if (STRAPLINE.equals(map.get(TWO)))
            {
               keyCode = tracCode + UNDERSCORE + map.get(TWO);
               productEditorialImageViewData.setDescription(getValue(contentValueEpicMap, keyCode));
               productEditorialImageViewData.setImage(getValue1(contentValueEpicMap, keyCode, XLARGE));
            }
            if (DISPLAYNAME.equals(map.get(THREE)))
            {
               keyCode = tracCode + UNDERSCORE + map.get(THREE);
               productEditorialImageViewData.setTitle(getValue(contentValueEpicMap, keyCode));
            }
         }
         else
         {

            if (DESCRIPTION1.equals(map.get(TWO)))
            {
               keyCode = tracCode + UNDERSCORE + map.get(ONE);
               productEditorialImageViewData.setPara1(getValue(contentValueEpicMap, keyCode));
            }
            else if (DESCRIPTION2.equals(map.get(TWO)))
            {
               keyCode = tracCode + UNDERSCORE + map.get(ONE);
               productEditorialImageViewData.setPara2(getValue(contentValueEpicMap, keyCode));
            }

         }

      }

      return productEditorialImageViewData;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.NonGeoItemFacade#getSocialMedia(java.util.List,
    * uk.co.portaltech.tui.components.model.SocialMediaLinksComponentModel, java.lang.String)
    */
   @Override
   public SocialMediaLinksComponentData getSocialMedia(final List<Map<String, String>> listofMapsData,
         final SocialMediaLinksComponentModel socialMediaLinksComponentModel, final String relativeurl)
   {
      final SocialMediaLinksComponentData socialMedia = new SocialMediaLinksComponentData();
      final List<SocialMediaViewData> socialMediaViewDataList = new ArrayList<SocialMediaViewData>();
      final List<SocialMediaModel> socialMediaList = socialMediaLinksComponentModel.getMediaList();
      SocialMediaViewData socialMediaViewData = null;

      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);

      String tracCode = null;
      String keyCode = null;
      for (final Map<String, String> map : listofMapsData)
      {
         tracCode = map.get(ZERO);
         keyCode = tracCode + UNDERSCORE + map.get(ONE);

         if (TITLE.equals(map.get(TWO)))
         {
            keyCode = tracCode + UNDERSCORE + map.get(ONE);

            socialMedia.setTitle(getValue(contentValueEpicMap, keyCode));
         }
         if (DESCRIPTION.equals(map.get(TWO)))
         {
            keyCode = tracCode + UNDERSCORE + map.get(ONE);
            socialMedia.setDescription(getValue(contentValueEpicMap, keyCode));
         }

      }

      for (final SocialMediaModel socialMediaModel : socialMediaList)
      {
         socialMediaViewData = new SocialMediaViewData();
         socialMediaViewData.setColor(socialMediaModel.getColor());
         socialMediaViewData.setIconUrl(socialMediaModel.getIconURL());
         final String mediaText = socialMediaModel.getMediaText().toLowerCase();
         socialMediaViewData.setMediaText(mediaText);
         socialMediaViewData.setOnHoverColor(socialMediaModel.getOnHoverColor());
         socialMediaViewData.setOnHoverText(socialMediaModel.getOnHoverText());
         socialMediaViewData.setMediaUrl(socialMediaModel.getMediaURL());
         socialMediaViewDataList.add(socialMediaViewData);
      }

      socialMedia.setSocialMediaViewDataList(socialMediaViewDataList);

      return socialMedia;

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.NonGeoItemFacade#getTabbingComponentViewData(java.util.List)
    */
   @Override
   public GroupsTabbingComponentViewData getTabbingComponentViewData(final List<Map<String, String>> listofMapsData)
   {
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, true);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);

      final GroupsTabbingComponentViewData groupsTabbingComponentViewData = new GroupsTabbingComponentViewData();

      final Map<String, TabSectionViewData> tabs = new LinkedHashMap<String, TabSectionViewData>();
      groupsTabbingComponentViewData.setTabs(tabs);

      String tracCode = null;
      String keyCode = null;

      for (final Map<String, String> map : listofMapsData)
      {
         final TabSectionViewData tabSectionViewData = new TabSectionViewData();
         final List<ImageSectionViewData> imageSectionViewDatas = new ArrayList<ImageSectionViewData>();

         tracCode = map.get(ZERO);
         String imageUrl = EMPTY_STRING;

         if ("mainsection".equals(map.get(String.valueOf(map.size() - 1))))
         {
            keyCode = tracCode + UNDERSCORE + map.get(ONE);
            groupsTabbingComponentViewData.setTitle(getValue(contentValueEpicMap, keyCode));
            keyCode = tracCode + UNDERSCORE + map.get(TWO);
            groupsTabbingComponentViewData.setDescription(getValue(contentValueEpicMap, keyCode));

         }
         else if ("tab1".equals(map.get(String.valueOf(map.size() - 1))))
         {
            final ImageSectionViewData imageSectionViewData1 = new ImageSectionViewData();
            final ImageSectionViewData imageSectionViewData2 = new ImageSectionViewData();

            keyCode = tracCode + UNDERSCORE + map.get(ONE);
            tabSectionViewData.setTabTitle(getValue(contentValueEpicMap, keyCode));
            keyCode = tracCode + UNDERSCORE + map.get(TWO);
            tabSectionViewData.setTabIntro(getValue(contentValueEpicMap, keyCode));

            keyCode = tracCode + UNDERSCORE + map.get(THREE);
            imageSectionViewData1.setImageTitle(getValue(contentValueEpicMap, keyCode));
            keyCode = tracCode + UNDERSCORE + map.get(FOUR);
            imageSectionViewData1.setImageIntro(getValue(contentValueEpicMap, keyCode));
            imageUrl = getValue1(contentValueEpicMap, keyCode, LARGE);
            if (imageUrl != null)
            {
               imageSectionViewData1.setImageUrl(imageUrl);
            }
            keyCode = tracCode + UNDERSCORE + map.get(FIVE);
            imageSectionViewData2.setImageTitle(getValue(contentValueEpicMap, keyCode));
            keyCode = tracCode + UNDERSCORE + map.get(SIX);
            imageSectionViewData2.setImageIntro(getValue(contentValueEpicMap, keyCode));
            imageUrl = getValue1(contentValueEpicMap, keyCode, LARGE);
            if (imageUrl != null)
            {
               imageSectionViewData2.setImageUrl(imageUrl);
            }
            imageSectionViewDatas.add(imageSectionViewData1);
            imageSectionViewDatas.add(imageSectionViewData2);

            tabSectionViewData.setImages(imageSectionViewDatas);

            groupsTabbingComponentViewData.getTabs().put("tab1", tabSectionViewData);

         }
         else if ("tab2".equals(map.get(String.valueOf(map.size() - 1))))
         {
            final ImageSectionViewData imageSectionViewData1 = new ImageSectionViewData();
            final ImageSectionViewData imageSectionViewData2 = new ImageSectionViewData();
            final ImageSectionViewData imageSectionViewData3 = new ImageSectionViewData();
            final ImageSectionViewData imageSectionViewData4 = new ImageSectionViewData();

            keyCode = tracCode + UNDERSCORE + map.get(ONE);
            tabSectionViewData.setTabTitle(getValue(contentValueEpicMap, keyCode));

            keyCode = tracCode + UNDERSCORE + map.get(TWO);
            tabSectionViewData.setTabIntro(getValue(contentValueEpicMap, keyCode));

            keyCode = tracCode + UNDERSCORE + map.get(THREE);
            imageSectionViewData1.setImageTitle(getValue(contentValueEpicMap, keyCode));
            imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
            if (imageUrl != null)
            {
               imageSectionViewData1.setImageUrl(imageUrl);
            }
            keyCode = tracCode + UNDERSCORE + map.get(FOUR);
            imageSectionViewData1.setImageIntro(getValue(contentValueEpicMap, keyCode));

            imageSectionViewDatas.add(imageSectionViewData1);

            keyCode = tracCode + UNDERSCORE + map.get(FIVE);
            imageSectionViewData2.setImageTitle(getValue(contentValueEpicMap, keyCode));
            imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
            if (imageUrl != null)
            {
               imageSectionViewData2.setImageUrl(imageUrl);
            }
            keyCode = tracCode + UNDERSCORE + map.get(SIX);
            imageSectionViewData2.setImageIntro(getValue(contentValueEpicMap, keyCode));

            imageSectionViewDatas.add(imageSectionViewData2);

            keyCode = tracCode + UNDERSCORE + map.get(SEVEN);
            imageSectionViewData3.setImageTitle(getValue(contentValueEpicMap, keyCode));
            imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
            if (imageUrl != null)
            {
               imageSectionViewData3.setImageUrl(imageUrl);
            }
            keyCode = tracCode + UNDERSCORE + map.get(EIGHT);
            imageSectionViewData3.setImageIntro(getValue(contentValueEpicMap, keyCode));

            imageSectionViewDatas.add(imageSectionViewData3);

            keyCode = tracCode + UNDERSCORE + map.get(NINE);
            imageSectionViewData4.setImageTitle(getValue(contentValueEpicMap, keyCode));
            imageUrl = getValue1(contentValueEpicMap, keyCode, SMALL);
            if (imageUrl != null)
            {
               imageSectionViewData4.setImageUrl(imageUrl);
            }
            keyCode = tracCode + UNDERSCORE + map.get(TEN);
            imageSectionViewData4.setImageIntro(getValue(contentValueEpicMap, keyCode));

            imageSectionViewDatas.add(imageSectionViewData4);

            tabSectionViewData.setImages(imageSectionViewDatas);

            groupsTabbingComponentViewData.getTabs().put("tab2", tabSectionViewData);

         }
         else if ("tab3".equals(map.get(String.valueOf(map.size() - 1))))
         {
            keyCode = tracCode + UNDERSCORE + map.get(ONE);
            tabSectionViewData.setTabTitle(getValue(contentValueEpicMap, keyCode));

            final ImageSectionViewData imageSectionViewData = new ImageSectionViewData();
            keyCode = tracCode + UNDERSCORE + map.get(TWO);
            imageSectionViewData.setImageIntro(getValue(contentValueEpicMap, keyCode));
            imageUrl = getValue1(contentValueEpicMap, keyCode, LARGE);
            if (imageUrl != null)
            {
               imageSectionViewData.setImageUrl(imageUrl);
            }

            imageSectionViewDatas.add(imageSectionViewData);

            tabSectionViewData.setImages(imageSectionViewDatas);

            groupsTabbingComponentViewData.getTabs().put("tab3", tabSectionViewData);

         }

      }

      return groupsTabbingComponentViewData;
   }

   @Override
   public List<ProductPageComponentViewData> getEditorialDataForProductHeader(final List<Map<String, String>> listofMapsData,
         final CMSItemModel component)
   {

      final List<ProductPageComponentViewData> viewDataList = new ArrayList<ProductPageComponentViewData>();
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, true);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      final Map<String, ContentItemModel> contentItemEpicMap = nonGeoItemService.getContentValuesForVideo(epicCodeList);
      ProductPageComponentViewData editorialViewData = null;

      String tracCode = null;
      String keyCode = null;
      String imageUrl = null;
      MediaModel mediaModel = null;

      final Map<String, String> mapDataForMainSection = listofMapsData.get(0);

      editorialViewData = new ProductPageComponentViewData();
      tracCode = mapDataForMainSection.get("0");

      // Mapping for Strap line
      keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("2");
      editorialViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));

      keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("3");
      editorialViewData.setSubTitle(getValue(contentValueEpicMap, keyCode));

      // Mapping for Display name
      keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("4");
      editorialViewData.setDescription(getValue(contentValueEpicMap, keyCode));

      // Mapping for Main Section Image

      if ("WF_COM_001_SMR".equalsIgnoreCase(component.getUid()))
      {
         keyCode = tracCode + "_" + mapDataForMainSection.get("2");
      }

      imageUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
      if (imageUrl != null)
      {
         editorialViewData.setImageUrl(imageUrl);
      }

      if ("WF_COM_001_WED".equalsIgnoreCase(component.getUid()) || "wedding_sidebar".equalsIgnoreCase(component.getUid())
            || "wedding_callToAction".equalsIgnoreCase(component.getUid()))
      {
         keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("5");
         editorialViewData.setBlog(getValue(contentValueEpicMap, keyCode));

         keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("6");
         editorialViewData.setBrochure(getValue(contentValueEpicMap, keyCode));

         keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("7");
         editorialViewData.setEmail(getValue(contentValueEpicMap, keyCode));

         keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("8");
         editorialViewData.setCallUsOn(getNumberDataFromString(getValue(contentValueEpicMap, keyCode)));

      }
      // Mapping for video

      mediaModel = getMedia(contentItemEpicMap, tracCode);
      if (null != mediaModel)
      {
         final String videoUrl = mediaModel.getCode().split(UNDERSCORE)[0];
         if (videoUrl != null)
         {
            editorialViewData.setVideoUrl(videoUrl);
         }
      }
      // Mapping for video thumb nail image
      if ("WF_COM_001_ROB".equalsIgnoreCase(component.getUid()) || "WF_COM_011_PLATINUM".equalsIgnoreCase(component.getUid())
            || "WF_COM_353_SAF".equalsIgnoreCase(component.getUid()) || "WF_COM_001_SMR".equalsIgnoreCase(component.getUid())
            || "WF_COM_001_SEN".equalsIgnoreCase(component.getUid()) || "WF_COM_001_COU".equalsIgnoreCase(component.getUid())
            || "WF_COM_001_FAM".equalsIgnoreCase(component.getUid()) || "WF_COM_001_WED".equalsIgnoreCase(component.getUid())
            || "WF_COM_001_GLD".equalsIgnoreCase(component.getUid()) || "WF_COM_001_VIL".equalsIgnoreCase(component.getUid()))
      {
         tracCode = mapDataForMainSection.get("0");
         keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("1");
      }
      else
      {
         tracCode = mapDataForMainSection.get("1");
         keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get("4");
      }

      imageUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);
      if (imageUrl != null)
      {
         editorialViewData.setThunbnailUrl(imageUrl);
      }

      viewDataList.add(editorialViewData);

      return viewDataList;
   }

   @Override
   public DreamLinerPromotionViewData getPromotionData(final List<Map<String, String>> listofMapsData)
   {

      final List<String> epicCodeList = getTracsCodeList(listofMapsData, true);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);

      final DreamLinerPromotionViewData dreamLinerPromotionViewData = new DreamLinerPromotionViewData();
      String tracCode = null;
      String keyCode = null;
      final List<String> bulletPoints = new ArrayList<String>();
      for (final Map<String, String> map : listofMapsData)
      {
         tracCode = map.get(ZERO);
         keyCode = tracCode + UNDERSCORE + map.get(ONE);
         if ("display_name".equals(map.get(String.valueOf(map.size() - 1))))
         {
            dreamLinerPromotionViewData.setDisplayName(getValue(contentValueEpicMap, keyCode));
         }
         else if ("strapline".equals(map.get(String.valueOf(map.size() - 1))))
         {
            dreamLinerPromotionViewData.setStrapline(getValue(contentValueEpicMap, keyCode));
         }
         else
         {
            bulletPoints.add(getValue(contentValueEpicMap, keyCode));
         }
         dreamLinerPromotionViewData.setPointsOnDreamLiner(bulletPoints);
      }

      return dreamLinerPromotionViewData;
   }

   @Override
   public ProductEditorialComponentViewData getProductEditorialComponentData(final List<Map<String, String>> listofMapsData)
   {
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      final ProductEditorialComponentViewData productEditorialComponentViewData = new ProductEditorialComponentViewData();

      final Map<String, String> mapDataForMainSection = listofMapsData.get(0);
      String tracCode = null;
      String keyCode = null;

      tracCode = mapDataForMainSection.get(ZERO);
      keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get(TWO);

      productEditorialComponentViewData.setTitle(getValue(contentValueEpicMap, keyCode));
      productEditorialComponentViewData.setImageUrl(getValue1(contentValueEpicMap, keyCode, LARGE));

      keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get(THREE);
      productEditorialComponentViewData.setDescription(getValue(contentValueEpicMap, keyCode));

      keyCode = tracCode + UNDERSCORE + mapDataForMainSection.get(FOUR);
      productEditorialComponentViewData.setButtonName(getValue(contentValueEpicMap, keyCode));

      return productEditorialComponentViewData;
   }

   private String getNumberDataFromString(final String callUs)
   {
      final StringBuilder value = new StringBuilder(EMPTY_STRING);
      if (StringUtils.isNotEmpty(callUs))
      {
         final Pattern pattern = Pattern.compile(NUMBER_REGEX);
         final Matcher matcher = pattern.matcher(callUs);
         while (matcher.find())
         {
            value.append(matcher.group()).append(SPACE);
         }
      }
      return value.toString();
   }

   /**
    * @param component
    * @return true or false
    */
   private boolean checkForComponent(final CMSItemModel component)
   {
      return StringUtils.equalsIgnoreCase(SMR_ID, component.getUid()) || StringUtils.equalsIgnoreCase(WED_ID, component.getUid())
            || StringUtils.equalsIgnoreCase(FAM_ID, component.getUid());
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.facades.NonGeoItemFacade#getCollectionDiffProdData (java.util.List, java.lang.String)
    */
   @Override
   public ProductPageComponentViewData getCollectionDiffProdData(final Map<String, String> mapData, final String componentWidth)
   {
      final List<String> epicCodeList = new ArrayList<String>();
      epicCodeList.add(mapData.get("0"));
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      ProductPageComponentViewData editorialViewData = null;
      final Map<String, String> imageUrlsMap = new HashMap<String, String>();

      String tracCode = null;
      String keyCode = null;

      editorialViewData = new ProductPageComponentViewData();
      tracCode = mapData.get("0");
      editorialViewData.setTitle(tracCode.toLowerCase());

      keyCode = tracCode + UNDERSCORE + mapData.get("1");
      editorialViewData.setFeatureTitle(getValue(contentValueEpicMap, keyCode));
      String imgUrl = EMPTY_STRING;
      String imgUrlSmall = EMPTY_STRING;
      if (HOMEPAGE_HALF_HALF.equals(componentWidth))
      {
         imgUrl = getValue1(contentValueEpicMap, keyCode, XLARGE);
      }
      else if (HOMEPAGE_HALF.equals(componentWidth))
      {
         imgUrl = getValue1(contentValueEpicMap, keyCode, LARGE);
      }
      else
      {
         imgUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);
         imgUrlSmall = getValue1(contentValueEpicMap, keyCode, SMALL);
         imageUrlsMap.put(MEDIUM, imgUrl);
         imageUrlsMap.put(SMALL, imgUrlSmall);
      }

      if (imgUrl != null)
      {
         editorialViewData.setImageUrl(imgUrl);
         editorialViewData.setImageUrlsMap(imageUrlsMap);
      }

      keyCode = tracCode + UNDERSCORE + mapData.get("2");
      editorialViewData.setDescription(getValue(contentValueEpicMap, keyCode));

      return editorialViewData;
   }

   @Override
   public DestinationGuideCollectionViewData getCollectionProductRangeData(final List<Map<String, String>> listofMapsData)
   {
      final List<String> epicCodeList = getTracsCodeList(listofMapsData, false);
      final Map<String, ContentValueModel> contentValueEpicMap = nonGeoItemService.getContentValues(epicCodeList);
      DestinationGuideCollectionViewData editorialViewData = null;

      String tracCode = null;
      String keyCode = null;

      final Map<String, String> map = listofMapsData.get(0);

      editorialViewData = new DestinationGuideCollectionViewData();
      tracCode = map.get("0");
      editorialViewData.setId(tracCode);

      keyCode = tracCode + "_" + map.get("1");
      editorialViewData.setName(getValue(contentValueEpicMap, keyCode));
      String imgUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);

      keyCode = tracCode + "_" + map.get("2");

      if (imgUrl == null)
      {
         imgUrl = getValue1(contentValueEpicMap, keyCode, MEDIUM);
      }
      editorialViewData.setCollectionImage(imgUrl);
      editorialViewData.setInspireText(getValue(contentValueEpicMap, keyCode));

      return editorialViewData;
   }
}
