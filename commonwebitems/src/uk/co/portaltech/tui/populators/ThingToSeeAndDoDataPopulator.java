/**
 * 
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.TabCarouselData;
import uk.co.portaltech.tui.web.view.data.ThingsToSeeAndDoEditorialData;

/**
 * @author niranjani.r
 * 
 */
public class ThingToSeeAndDoDataPopulator implements
   Populator<LocationData, List<ThingsToSeeAndDoEditorialData>>
{

   private static final String BEACHES = "BEACHES";

   private static final String BEACHTITLE1 = "beaches1Title";

   private static final String BEACHBODY1 = "beaches1Body";

   private static final String BEACHTITLE2 = "beaches2Title";

   private static final String BEACHBODY2 = "beaches2Body";

   private static final String BEACHTITLE3 = "beaches3Title";

   private static final String BEACHBODY3 = "beaches3Body";

   private static final String BARGAINBUYS = " Bargain Buys";

   private static final String BARGAIN = "bargain";

   private static final String MID = "Mid-Range Buys";

   private static final String MIDRANGE = "midRange";

   private static final String DESIGNERBUYS = "Designer Buys";

   private static final String DESIGNER = "designer";

   private static final String SHOPPING = "SHOPPING";

   private static final String LAIDEVE = "Laid-back evenings";

   private static final String LAID = "nightLifeLaidBack";

   private static final String LIVELY = "Lively evenings";

   private static final String NIGHTLIVELY = "nightLifeLively";

   private static final String NIGHTLIFE = "NIGHTLIFE";

   private static final String FOODTITLE1 = "foodDrinkIntro1Title";

   private static final String FOODBODY1 = "foodDrinkIntro1Body";

   private static final String FOODTITLE2 = "foodDrinkIntro2Title";

   private static final String FOODBODY2 = "foodDrinkIntro2Body";

   private static final String FOODTITLE3 = "foodDrinkIntro3Title";

   private static final String FOODBODY3 = "foodDrinkIntro3Body";

   private static final String FOODTITLE4 = "foodDrinkIntro4Title";

   private static final String FOODBODY4 = "foodDrinkIntro4Body";

   private static final String FOODTITLE5 = "foodDrinkIntro5Title";

   private static final String FOODBODY5 = "foodDrinkIntro5Body";

   private static final String FOODANDDRINK = "FOOD & DRINK";

   /*
    * Populates the tab data from feature codes and values.
    */
   @Override
   public void populate(final LocationData source, final List<ThingsToSeeAndDoEditorialData> target)
      throws ConversionException
   {
      final TabCarouselData beachTab1Data = new TabCarouselData();
      final TabCarouselData beachTab2Data = new TabCarouselData();
      final TabCarouselData beachTab3Data = new TabCarouselData();
      final TabCarouselData designerData = new TabCarouselData();
      final TabCarouselData midRangeData = new TabCarouselData();
      final TabCarouselData bargainData = new TabCarouselData();
      final TabCarouselData laidbackData = new TabCarouselData();
      final TabCarouselData foodDrinkIntro1 = new TabCarouselData();
      final TabCarouselData foodDrinkIntro2 = new TabCarouselData();
      final TabCarouselData foodDrinkIntro3 = new TabCarouselData();
      final TabCarouselData foodDrinkIntro4 = new TabCarouselData();
      final TabCarouselData foodDrinkIntro5 = new TabCarouselData();

      final TabCarouselData livelyData = new TabCarouselData();
      final List<TabCarouselData> beachData = new ArrayList<TabCarouselData>();
      final List<TabCarouselData> shoppingData = new ArrayList<TabCarouselData>();
      final List<TabCarouselData> nightLifeData = new ArrayList<TabCarouselData>();
      final List<TabCarouselData> foodAndDrinkData = new ArrayList<TabCarouselData>();

      final ThingsToSeeAndDoEditorialData beachTabData = new ThingsToSeeAndDoEditorialData();
      final ThingsToSeeAndDoEditorialData ShoppingTabData = new ThingsToSeeAndDoEditorialData();
      final ThingsToSeeAndDoEditorialData nightLifeTabData = new ThingsToSeeAndDoEditorialData();
      final ThingsToSeeAndDoEditorialData foodAndDrinkTabData = new ThingsToSeeAndDoEditorialData();

      beachTab1Data.setTitle(checkForNull(source.getFeatureCodesAndValues().get(BEACHTITLE1)));
      beachTab1Data.setDescription(checkForNull(source.getFeatureCodesAndValues().get(BEACHBODY1)));
      beachTab2Data.setTitle(checkForNull(source.getFeatureCodesAndValues().get(BEACHTITLE2)));
      beachTab2Data.setDescription(checkForNull(source.getFeatureCodesAndValues().get(BEACHBODY2)));
      beachTab3Data.setTitle(checkForNull(source.getFeatureCodesAndValues().get(BEACHTITLE3)));
      beachTab3Data.setDescription(checkForNull(source.getFeatureCodesAndValues().get(BEACHBODY3)));
      beachData.add(beachTab1Data);
      beachData.add(beachTab2Data);
      beachData.add(beachTab3Data);
      beachTabData.setTabName(BEACHES);
      beachTabData.setTabData(beachData);

      designerData.setTitle(BARGAINBUYS);
      designerData.setDescription(checkForNull(source.getFeatureCodesAndValues().get(BARGAIN)));
      midRangeData.setTitle(MID);
      midRangeData.setDescription(checkForNull(source.getFeatureCodesAndValues().get(MIDRANGE)));
      bargainData.setTitle(DESIGNERBUYS);
      bargainData.setDescription(checkForNull(source.getFeatureCodesAndValues().get(DESIGNER)));
      shoppingData.add(bargainData);
      shoppingData.add(midRangeData);
      shoppingData.add(designerData);
      ShoppingTabData.setTabName(SHOPPING);
      ShoppingTabData.setTabData(shoppingData);

      laidbackData.setTitle(LAIDEVE);
      laidbackData.setDescription(checkForNull(source.getFeatureCodesAndValues().get(LAID)));
      livelyData.setTitle(LIVELY);
      livelyData.setDescription(checkForNull(source.getFeatureCodesAndValues().get(NIGHTLIVELY)));
      nightLifeData.add(laidbackData);
      nightLifeData.add(livelyData);
      nightLifeTabData.setTabName(NIGHTLIFE);
      nightLifeTabData.setTabData(nightLifeData);

      foodDrinkIntro1.setTitle(checkForNull(source.getFeatureCodesAndValues().get(FOODTITLE1)));
      foodDrinkIntro2.setTitle(checkForNull(source.getFeatureCodesAndValues().get(FOODTITLE2)));
      foodDrinkIntro3.setTitle(checkForNull(source.getFeatureCodesAndValues().get(FOODTITLE3)));
      foodDrinkIntro4.setTitle(checkForNull(source.getFeatureCodesAndValues().get(FOODTITLE4)));
      foodDrinkIntro5.setTitle(checkForNull(source.getFeatureCodesAndValues().get(FOODTITLE5)));

      foodDrinkIntro1
         .setDescription(checkForNull(source.getFeatureCodesAndValues().get(FOODBODY1)));
      foodDrinkIntro2
         .setDescription(checkForNull(source.getFeatureCodesAndValues().get(FOODBODY2)));
      foodDrinkIntro3
         .setDescription(checkForNull(source.getFeatureCodesAndValues().get(FOODBODY3)));
      foodDrinkIntro4
         .setDescription(checkForNull(source.getFeatureCodesAndValues().get(FOODBODY4)));
      foodDrinkIntro5
         .setDescription(checkForNull(source.getFeatureCodesAndValues().get(FOODBODY5)));

      foodAndDrinkData.add(foodDrinkIntro1);
      foodAndDrinkData.add(foodDrinkIntro2);
      foodAndDrinkData.add(foodDrinkIntro3);
      foodAndDrinkData.add(foodDrinkIntro4);
      foodAndDrinkData.add(foodDrinkIntro5);

      foodAndDrinkTabData.setTabName(FOODANDDRINK);
      foodAndDrinkTabData.setTabData(foodAndDrinkData);

      target.add(beachTabData);
      target.add(ShoppingTabData);
      target.add(nightLifeTabData);
      target.add(foodAndDrinkTabData);

   }

   /**
    * @param value
    * @return value or empty string.
    */
   private String checkForNull(final List<Object> value)
   {
      if (value != null && (value.size() > 0) && StringUtils.isNotEmpty(value.get(0).toString()))
      {
         return value.get(0).toString();
      }
      return StringUtils.EMPTY;

   }

}
