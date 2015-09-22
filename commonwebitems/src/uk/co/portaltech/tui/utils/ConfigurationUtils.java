/**
 *
 */
package uk.co.portaltech.tui.utils;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.services.TuiUtilityService;



/**
 * @author shyamaprasada.vs
 *
 *         This utility class reads the search configurations from the property file and returns as Map
 */

public class ConfigurationUtils
{

   private ConfigurationService configurationService;

   private final Map searchConfiguration = new HashMap<String, String>();


   private final Map searchResultsConfiguration = new HashMap<String, String>();

   private final Map customerAccountConfigurations = new HashMap<String, String>();

   private TUIConfigService tuiConfigService;

   private TuiUtilityService tuiUtilityService;

   private static final Map<String, String> BOARDBASISTOOLTIP = new HashMap<String, String>();

   /**
     *
     */
   public ConfigurationUtils()
   {

   }

   public Map<String, String> getSearchConfiguration()
   {
      if (searchConfiguration.isEmpty())
      {
         setSearchConfigurations();
      }
      return searchConfiguration;
   }

   public Map<String, String> getSearchResultsConfiguration()
   {
      setSearchResultsConfiguration();
      setTUIConfiguration();
      return searchResultsConfiguration;
   }

   private void setSearchResultsConfiguration()
   {
      searchResultsConfiguration.put("defaultView",
            configurationService.getConfiguration().getString("holiday.results.defaultView", "List"));
      if (!StringUtils.equalsIgnoreCase(tuiUtilityService.getSiteBrand(), "FJ"))
      {
         searchResultsConfiguration.put("shortlistPageUrl",
               tuiConfigService.getConfigValue(tuiUtilityService.getSiteBrand() + ".shortlistPageUrl", ""));
      }
   }

   private void setTUIConfiguration()
   {
      searchResultsConfiguration.put("maxShortlist", tuiConfigService.getConfigValue("holiday.results.maxShortlist", "10"));
   }


   private void setSearchConfigurations()
   {

      searchConfiguration.put("maxNoOfAdult",
            configurationService.getConfiguration().getString("holiday.search.maxNoOfAdult", "9"));
      searchConfiguration.put("minNoOfAdult",
            configurationService.getConfiguration().getString("holiday.search.minNoOfAdult", "1"));
      searchConfiguration.put("maxNoOfChild",
            configurationService.getConfiguration().getString("holiday.search.maxNoOfChild", "9"));
      searchConfiguration.put("minNoOfChild",
            configurationService.getConfiguration().getString("holiday.search.minNoOfChild", "0"));
      searchConfiguration.put("maxChildAge", configurationService.getConfiguration()
            .getString("holiday.search.maxChildAge", "12"));
      searchConfiguration
            .put("minChildAge", configurationService.getConfiguration().getString("holiday.search.minChildAge", "0"));
      searchConfiguration.put("infantAge", configurationService.getConfiguration().getString("holiday.search.infantAge", "3"));
      searchConfiguration.put("maxAirportsSelectable",
            configurationService.getConfiguration().getString("holiday.search.maxAirportsSelectable", "6"));
      searchConfiguration.put("persistedSearchPeriod",
            configurationService.getConfiguration().getString("holiday.search.persistedSearchPeriod", "30"));
      searchConfiguration.put("offset", configurationService.getConfiguration().getString("holiday.search.offset", "10"));
      searchConfiguration.put("first", configurationService.getConfiguration().getString("holiday.search.first", "0"));
      searchConfiguration.put("last", configurationService.getConfiguration().getString("holiday.search.last", "10"));
      searchConfiguration.put("flexibleDays",
            configurationService.getConfiguration().getString("holiday.search.flexibleDays", "7"));
      searchConfiguration.put("autocompleteDuration",
            configurationService.getConfiguration().getString("holiday.search.autocompleteDuration", "5"));
      searchConfiguration.put("maxCountriesViewable",
            configurationService.getConfiguration().getString("holiday.search.maxCountriesViewable", "4"));
      searchConfiguration.put("maxDestinationsViewable",
            configurationService.getConfiguration().getString("holiday.search.maxDestinationsViewable", "4"));
      searchConfiguration.put("maxHotelsViewable",
            configurationService.getConfiguration().getString("holiday.search.maxHotelsViewable", "4"));
      searchConfiguration.put("maxProdRangeViewable",
            configurationService.getConfiguration().getString("holiday.search.maxProdRangeViewable", "4"));

   }

   public String getBoardBasisToolTip(final String boardBasisCode)
   {
      if (!BOARDBASISTOOLTIP.containsKey(boardBasisCode))
      {
         BOARDBASISTOOLTIP.put(boardBasisCode,
               configurationService.getConfiguration().getString("boardBasis.code." + boardBasisCode));
      }

      return BOARDBASISTOOLTIP.get(boardBasisCode);
   }

   /**
    * @return the configurationService
    */
   public ConfigurationService getConfigurationService()
   {
      return configurationService;
   }

   /**
    * @param configurationService
    *           the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }

   /**
    * @return the tuiConfigService
    */
   public TUIConfigService getTuiConfigService()
   {
      return tuiConfigService;
   }

   /**
    * @param tuiConfigService
    *           the tuiConfigService to set
    */
   public void setTuiConfigService(final TUIConfigService tuiConfigService)
   {
      this.tuiConfigService = tuiConfigService;
   }

   public String getMobileContext()
   {
      return tuiConfigService.getConfigValue("mobileContext", "true");
   }

   public String getMoovwebSwitchFC()
   {
      return configurationService.getConfiguration().getString("moovweb.redirection.switch.fc", "on");
   }

   public String getMoovwebSwitchTH()
   {
      return configurationService.getConfiguration().getString("moovweb.redirection.switch.th", "on");
   }

   /**
    * @return the tuiUtilityService
    */
   public TuiUtilityService getTuiUtilityService()
   {
      return tuiUtilityService;
   }

   public String getFlexibleDurationForSmerch()
   {
      return tuiConfigService.getConfigValue("flexibleDurationsForSmerch", "15");
   }

   /**
    * @param tuiUtilityService
    *           the tuiUtilityService to set
    */
   public void setTuiUtilityService(final TuiUtilityService tuiUtilityService)
   {
      this.tuiUtilityService = tuiUtilityService;
   }

   public Map<String, String> getCustomerAccountConfigurations()
   {
      if (customerAccountConfigurations.isEmpty())
      {
         setCustomerAccountConfigurations();
      }
      return customerAccountConfigurations;
   }

   private void setCustomerAccountConfigurations()
   {
      customerAccountConfigurations.put("customeraccount.confignTimeLimit.OnActivationEmail", "14");
      customerAccountConfigurations.put("customeraccount.emailTemplate", "account activation");
      customerAccountConfigurations.put("customeraccount.from_email", "customeraccount@thomson.co.uk");
      customerAccountConfigurations.put("customeraccount.noOfAccounts.sameEmailId", "5");
      customerAccountConfigurations.put("customeraccount.subjectLine", "welcome to tui");
      customerAccountConfigurations.put("holiday.results.maxShortlist", "10");
      customerAccountConfigurations.put("holiday.search.airportfuzzyPercentage", "0.4");
      customerAccountConfigurations.put("holiday.search.unitfuzzyPercentage", "0.9");
      customerAccountConfigurations.put("iscapeBusinessContextId", "36");
      customerAccountConfigurations.put("shortlistPageUrl",
            "http://www.firstchoice.co.uk/fcsun/page/shortlist/shortlist.page?requestFrom=phoenix");
      customerAccountConfigurations.put("singleAccommodation.search", "true");
      customerAccountConfigurations.put("sourceSytemId", "TRACS_FC44");

   }
}
