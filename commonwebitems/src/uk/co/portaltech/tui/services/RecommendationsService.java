/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.Resource;

import uk.co.portaltech.tui.converters.BaynoteRESTConverter;
import uk.co.portaltech.tui.web.view.data.RecommendationsData;
import uk.co.tui.async.logging.TUILogUtils;

public class RecommendationsService
{

   @Resource
   private BaynoteRESTConverter baynoteRESTConverter;

   /** The configuration service. */
   @Resource
   private ConfigurationService configurationService;

   private static final TUILogUtils LOG = new TUILogUtils("RecommendationsService");

   /**
    * @return the configurationService
    */
   public ConfigurationService getConfigurationService()
   {
      return configurationService;
   }

   /**
    * @param configurationService the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }

   public RecommendationsData getRecommendations(final String pageType, final String brand)
   {
      RecommendationsData recommendationsData = new RecommendationsData();
      String baynoteStub = null;
      LOG.debug("Calling StubResponse");
      // Thread Created for Performance Testing
      final Thread stubThread = new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               String threadSleepTime = null;
               threadSleepTime =
                  configurationService.getConfiguration().getString(
                     "recommendations.baynote.stubs.thread.sleepTime");
               final long sleepTime = Long.parseLong(threadSleepTime);
               LOG.debug("Start Thread: " + System.currentTimeMillis());
               Thread.sleep(sleepTime);
               LOG.debug("End Thread: " + System.currentTimeMillis());
            }
            catch (final InterruptedException e)
            {
               LOG.info("StubThread is Interrupted: " + e.getMessage());
            }
         }

      };

      final Thread t1 = new Thread(stubThread);
      t1.start();

      try
      {
         baynoteStub = getStubXMLFile(brand, pageType);
      }
      catch (final IOException e)
      {
         LOG.debug("IOException while calling baynoteStub", e);
      }

      try
      {
         recommendationsData = baynoteRESTConverter.convert(baynoteStub, pageType);
      }
      catch (final Exception exp)
      {
         LOG.error("Exception While Calling Converting Baynote Response:" + exp.getMessage());
      }

      return recommendationsData;

   }

   public String getStubXMLFile(final String brand, final String pageType) throws IOException
   {

      File stubXmlFile = null;
      BufferedReader br = null;
      final StringBuilder sbXmlResponse = new StringBuilder();
      String line = null;
      String baynoteStubPath = null;
      baynoteStubPath =
         configurationService.getConfiguration().getString("recommendations.baynote.stubs.path");
      final StringBuilder stubPath = new StringBuilder(Config.getString("HYBRIS_BIN_DIR", ""));
      stubPath.append("/");
      stubPath.append(baynoteStubPath);
      LOG.debug("brand: " + brand + " :  pageType: " + pageType);

      if ("TH".equalsIgnoreCase(brand) && "HomeRec".equalsIgnoreCase(pageType))
      {
         stubPath.append("/tuifeeds/resources/tui/baynoteStubs/TH_HomePageRecommendations.xml");
         stubXmlFile = new File(stubPath.toString());

      }
      else if ("TH".equalsIgnoreCase(brand) && "AccomodationRec".equalsIgnoreCase(pageType))
      {
         stubPath.append("/tuifeeds/resources/tui/baynoteStubs/TH_BrowseFlowRecommendations.xml");
         stubXmlFile = new File(stubPath.toString());

      }
      else if ("FC".equalsIgnoreCase(brand) && "HomeRec".equalsIgnoreCase(pageType))
      {

         stubPath.append("/tuifeeds/resources/tui/baynoteStubs/FC_HomePageRecommendations.xml");
         stubXmlFile = new File(stubPath.toString());

      }
      else if ("FC".equalsIgnoreCase(brand) && "AccomodationRec".equalsIgnoreCase(pageType))
      {
         stubPath.append("/tuifeeds/resources/tui/baynoteStubs/FC_BrowseFlowRecommendations.xml");
         stubXmlFile = new File(stubPath.toString());
      }

      try
      {
         br = new BufferedReader(new FileReader(stubXmlFile));
         line = br.readLine();

         while (line != null)
         {
            sbXmlResponse.append(line);
            sbXmlResponse.append("\n");
            line = br.readLine();
         }

      }
      catch (final IOException e)
      {
         LOG.error("IOException while calling getStubXMLFile: ", e);
      }
      finally
      {
         if (br != null)
         {
            br.close();
         }
      }

      return sbXmlResponse.toString();
   }

}
