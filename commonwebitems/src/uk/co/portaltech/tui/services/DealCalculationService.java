/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;

/**
 * @author veena.pn
 *
 */
public class DealCalculationService
{
   private static final int TWO = 2;

   private static final int HUNDRED = 100;

   private static final String GREATDEALHOLIDAYS = "greatDealHolidays";

   private static final String TH_GREATDEAL_SWITCH = "TH.GreatDealSwitch";

   private static final String FC_GREATDEAL_SWITCH = "FC.GreatDealSwitch";

   private static final TUIConfigService TUI_CONFIG_SERVICE = (TUIConfigService) Registry
      .getApplicationContext().getBean("tuiConfigService");

   @Resource
   private SessionService sessionService;

   public void calulateDiscountPercentage(final SearchResultViewData searchResultViewData,
      final String discountPercentage, final String discountPerPerson,
      final Map<String, Integer> greatDealMap)

   {
      String greatDealSwitch = "OFF";
      greatDealSwitch = getGreatDealSwitch(searchResultViewData);
      if ("ON".equalsIgnoreCase(greatDealSwitch))
      {
         final Double val = Double.valueOf(searchResultViewData.getPrice().getPerPerson());
         final BigDecimal discountPP =
            new BigDecimal(searchResultViewData.getPrice().getDiscountPP());
         final BigDecimal totalprice = BigDecimal.valueOf(val).add(discountPP);
         final BigDecimal discountPer =
            (discountPP.divide(totalprice, TWO, java.math.RoundingMode.FLOOR))
               .multiply(new BigDecimal(HUNDRED));
         final BigDecimal value = BigDecimal.valueOf(Long.valueOf(discountPercentage));
         if (discountPer.compareTo(value) >= 0
            && discountPP.intValue() >= Integer.valueOf(discountPerPerson))
         {
            greatDealMap.put(searchResultViewData.getPackageId(), discountPer.intValue());

         }

      }
   }

   /**
    * @param searchResultViewData
    * @return
    */
   private String getGreatDealSwitch(final SearchResultViewData searchResultViewData)
   {
      String greatDealSwitch;
      if ("T".equalsIgnoreCase(searchResultViewData.getBrandType()))
      {
         greatDealSwitch = TUI_CONFIG_SERVICE.getConfigValue(TH_GREATDEAL_SWITCH);
      }
      else
      {
         greatDealSwitch = TUI_CONFIG_SERVICE.getConfigValue(FC_GREATDEAL_SWITCH);
      }
      return greatDealSwitch;
   }

   private String getGreatDealSwitchs(final List<SearchResultViewData> searchResultViewData)
   {
      String greatDealSwitch = null;

      for (final SearchResultViewData data : searchResultViewData)
      {
         if ("T".equalsIgnoreCase(data.getBrandType()))
         {
            greatDealSwitch = TUI_CONFIG_SERVICE.getConfigValue(TH_GREATDEAL_SWITCH);
         }
         else
         {
            greatDealSwitch = TUI_CONFIG_SERVICE.getConfigValue(FC_GREATDEAL_SWITCH);
         }
         break;
      }
      return greatDealSwitch;
   }

   public void showGrealDeal(final List<SearchResultViewData> searchResultViewData,
      final String discountedHolidayPerPage, final Map<String, Integer> greatDealMap)

   {
      String greatDealSwitch = "OFF";
      greatDealSwitch = getGreatDealSwitchs(searchResultViewData);
      if ("ON".equalsIgnoreCase(greatDealSwitch))
      {

         showDeal(searchResultViewData, discountedHolidayPerPage, greatDealMap);
      }

   }

   /**
    * @param searchResultViewData
    * @param discountedHolidayPerPage
    * @param greatDealMap
    */
   private void showDeal(final List<SearchResultViewData> searchResultViewData,
      final String discountedHolidayPerPage, final Map<String, Integer> greatDealMap)
   {
      sessionService.removeAttribute(GREATDEALHOLIDAYS);
      final List<Map.Entry<String, Integer>> greatDealHolidays =
         sortGreatDeal(discountedHolidayPerPage, greatDealMap);
      final List<Map.Entry<String, Integer>> greatDealHolidaysfromsession =
         sessionService.getAttribute(GREATDEALHOLIDAYS);
      chcekForSession(greatDealHolidays, greatDealHolidaysfromsession);
      for (final Map.Entry<String, Integer> entry1 : greatDealHolidays)
      {
         for (final SearchResultViewData result : searchResultViewData)
         {
            if (entry1.getKey().equalsIgnoreCase(result.getPackageId()))
            {
               result.setGreatDeal(true);
               result.setGreatDealPercentage(entry1.getValue());

               break;
            }

         }

      }
   }

   /**
    * @param greatDealHolidays
    * @param greatDealHolidaysfromsession
    */
   private void chcekForSession(final List<Map.Entry<String, Integer>> greatDealHolidays,
      final List<Map.Entry<String, Integer>> greatDealHolidaysfromsession)
   {
      if (CollectionUtils.isEmpty(greatDealHolidaysfromsession))
      {
         sessionService.setAttribute(GREATDEALHOLIDAYS, greatDealHolidays);
      }
      else
      {
         greatDealHolidays.addAll(greatDealHolidaysfromsession);
         sessionService.setAttribute(GREATDEALHOLIDAYS, greatDealHolidays);

      }
   }

   /**
    * @param discountedHolidayPerPage
    * @param greatDealMap
    * @return
    */
   private List<Map.Entry<String, Integer>> sortGreatDeal(final String discountedHolidayPerPage,
      final Map<String, Integer> greatDealMap)
   {
      final List<Map.Entry<String, Integer>> list =
         new LinkedList<Map.Entry<String, Integer>>(greatDealMap.entrySet());
      Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
      {
         public int compare(final Map.Entry<String, Integer> o1, final Map.Entry<String, Integer> o2)
         {
            return (o2.getValue()).compareTo(o1.getValue());
         }
      });
      List<Map.Entry<String, Integer>> greatDealHolidays = list;
      if (list.size() > Integer.valueOf(discountedHolidayPerPage))
      {
         greatDealHolidays = list.subList(0, Integer.valueOf(discountedHolidayPerPage));
      }
      return greatDealHolidays;
   }

   public void getGreatDealForAccom(final BookFlowAccommodationViewData accomData)
   {

      final List<Map.Entry<String, Integer>> greatDealHolidays =
         sessionService.getAttribute(GREATDEALHOLIDAYS);
      if (CollectionUtils.isNotEmpty(greatDealHolidays))
      {
         for (final Map.Entry<String, Integer> entry1 : greatDealHolidays)
         {
            if (entry1.getKey().equalsIgnoreCase(accomData.getPackageData().getPackageId()))
            {
               accomData.getPackageData().setGreatDeal(true);
               accomData.getPackageData().setGreatDealPercentage(entry1.getValue());
               break;

            }
         }
      }
   }
}