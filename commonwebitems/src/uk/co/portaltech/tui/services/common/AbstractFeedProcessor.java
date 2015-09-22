/**
 *
 */
package uk.co.portaltech.tui.services.common;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import uk.co.portaltech.travel.enums.Month;
import uk.co.portaltech.travel.enums.WeatherTypeName;
import uk.co.portaltech.travel.enums.WeatherUnit;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.model.WeatherModel;
import uk.co.portaltech.travel.model.WeatherTypeModel;
import uk.co.portaltech.travel.model.WeatherTypeValueModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.util.CatalogUtil;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import au.com.bytecode.opencsv.CSVReader;

import com.google.common.io.Files;

/**
 * @author Abi
 *
 */
public abstract class AbstractFeedProcessor
{

   private static final TUILogUtils LOG = new TUILogUtils("AbstractFeedProcessor");

   protected static final String LOCATIONQUERY =
      "SELECT {pk} FROM {Location} WHERE {code}=?code and {catalogVersion}=?catalogVersion";

   protected static final String ACCOMMODATIONQUERY =
      "SELECT {pk} FROM {Accommodation} WHERE {code}=?code and {catalogVersion}=?catalogVersion";

   protected static final String MEDIAMOGULQUERY =
      "SELECT {pk} FROM {MediaContainer} WHERE {mediaMogulId} = ?mediaMogulId AND {catalogVersion} = ?catalogVersion";

   protected static final String ALL_MEDIA_CONTAINERS_QUERY =
      "SELECT {pk} FROM {MediaContainer} WHERE {catalogVersion} = ?catalogVersion";

   protected List<String> notFeatures = new ArrayList<String>(Arrays.asList("code",
      "supercategories", "facilityType", "averageTempMonthly", "sunshineHoursMonthly",
      "rainfallMonthly", "itemType", "greatGrandParentCode", "grandParentCode",
      "productRangeCategory", "productRangeCode", "unitCode", "airportCode", "images", "subTypes",
      "tuiaptname", "airportName", "scope", "synonyms", "group", "ISSEARCHABLE", "CHARGETYPE",
      "dateRanges"));

   protected Logger logToFile;

   protected Logger imageLogger;

   private Map<String, MediaContainerModel> mediaContainers;

   private static final String DATADIR = "HYBRIS_DATA_DIR";

   public abstract void processData(String rootDir, String outputDir, String fileName);

   protected abstract Map<String, Integer> getPositionMap();

   protected CatalogVersionModel getImportCatalogVersion()
   {

      final CatalogVersionService catalogVersionService =
         Registry.getApplicationContext().getBean(CatalogVersionService.class);
      final CatalogUtil catalogUtil =
         (CatalogUtil) Registry.getApplicationContext().getBean("catalogUtil");
      String catalog = StringUtils.EMPTY;
      final CatalogModel catalogModel = catalogUtil.getNonActiveProductCatalogForDataRefresh();
      if (null != catalogModel)
      {
         catalog = catalogModel.getId();
      }
      return catalogVersionService.getCatalogVersion(catalog, "Online");
   }

   protected boolean createFeatureValuesSets(final ItemModel itemModel,
      final Map<String, String> line, final CatalogVersionModel catalog)
   {
      ItemModel item = itemModel;
      final FlexibleSearchService flexibleSearchService =
         Registry.getApplicationContext().getBean(FlexibleSearchService.class);
      final ModelService modelService =
         (ModelService) Registry.getApplicationContext().getBean("modelService");
      final FeatureService featureService =
         Registry.getApplicationContext().getBean(FeatureService.class);
      final BrandUtils brandUtils = Registry.getApplicationContext().getBean(BrandUtils.class);

      final Map<String, Integer> posMap = getPositionMap();
      final List<String> features = new ArrayList<String>();
      final Iterator<Entry<String, Integer>> itr = posMap.entrySet().iterator();
      boolean error = false;
      /*
       * Iterate over all the column definitions in posMap and build a list of features from it.
       * Features are all those columns that are not in the exclude list 'notFeatures'.
       */
      while (itr.hasNext())
      {
         final Entry<String, Integer> column = itr.next();
         if (!notFeatures.contains(column.getKey()))
         {
            features.add(column.getKey());
         }
      }

      // We remove any existing valueSets as we will be overriding them.
      final List<FeatureValueSetModel> existingValueSets =
         featureService.getFeatureValueSetsForItem(item);
      if (existingValueSets != null && !existingValueSets.isEmpty())
      {
         LOG.debug("Removing existing FeatureValueSets");
         modelService.removeAll(existingValueSets);
         LOG.debug("Finished removing existing FeatureValueSets");
      }

      final List<FeatureValueSetModel> itemValueSets = new ArrayList<FeatureValueSetModel>();
      LOG.debug("Creating feature values");
      for (final String featureCode : features)
      {
         String rawValue = line.get(featureCode);
         if (StringUtils.isNotBlank(rawValue))
         {
            rawValue = StringEscapeUtils.unescapeHtml(rawValue);
            /*
             * First we have to fetch the FeatureDescriptor. This must exist already, if not we skip
             * this feature.
             */
            FeatureDescriptorModel featureDescriptor = new FeatureDescriptorModel();
            featureDescriptor.setCatalogVersion(catalog);
            featureDescriptor.setCode(featureCode);
            try
            {
               featureDescriptor = flexibleSearchService.getModelByExample(featureDescriptor);
            }
            catch (final ModelNotFoundException e)
            {
               LOG.error(
                  "Unable to find an existing FeatureDescriptor with code '"
                     + featureDescriptor.getCode() + "' and CatalogVersion '"
                     + featureDescriptor.getCatalogVersion(), e);
               error = true;
               continue;
            }
            catch (final AmbiguousIdentifierException e)
            {
               LOG.error(
                  "More than one FeatureDescriptor was found with code '"
                     + featureDescriptor.getCode() + "' and CatalogVersion '"
                     + featureDescriptor.getCatalogVersion(), e);
               error = true;
               continue;
            }

            FeatureValueSetModel featureValueSet = featureService.newFeatureValueSet();
            featureValueSet.setFeatureDescriptor(featureDescriptor);
            item = featureService.addFeatureValueSetToItem(featureValueSet, item);

            if (itemModel instanceof LocationModel)
            {
               featureValueSet.setBrands(((LocationModel) itemModel).getBrands());
            }
            else if (itemModel instanceof FacilityModel)
            {
               featureValueSet.setBrands(((FacilityModel) itemModel).getBrands());
            }
            else if (itemModel instanceof AccommodationModel)
            {
               featureValueSet.setBrands(((AccommodationModel) itemModel).getBrands());
            }
            else if (itemModel instanceof AttractionModel)
            {
               featureValueSet.setBrands(brandUtils.getAllValidBrands());

            }

            modelService.save(featureValueSet);
            try
            {

               modelService.save(item);

            }
            catch (final ModelSavingException e)
            {

               LOG.error(
                  "There was an issue with some Location being added into the wrong place in the Geographical Hierarchy:",
                  e);
               logToFile
                  .error(
                     "There was an issue with some Location being added into the wrong place in the Geographical Hierarchy:",
                     e);
               error = true;
               continue;
            }

            /*
             * We split on the pipe ('|') character in order to handle multi-valued fields.
             */
            final String[] values = rawValue.split("\\|");

            for (final String value : values)
            {
               final FeatureValueModel featureValue = featureService.newFeatureValue(catalog);
               featureValue.setValue(value);
               featureValueSet =
                  featureService.addFeatureValueToFeatureValueSet(featureValue, featureValueSet);
               modelService.save(featureValue);
            }

            modelService.save(featureValueSet);
            itemValueSets.add(featureValueSet);
         }
      }
      LOG.debug("Finished creating feature values");
      item = featureService.setFeatureValueSetsForItem(item, itemValueSets);
      LOG.debug("Saving item");
      modelService.save(item);
      LOG.debug("Finished saving item");

      return error;
   }

   protected boolean handleImages(final ItemModel item, final Map<String, String> line,
      final CatalogVersionModel catalogVersion)
   {
      final String imagesString = line.get("images");
      final ModelService modelService =
         (ModelService) Registry.getApplicationContext().getBean("modelService");
      if (StringUtils.isNotEmpty(imagesString))
      {
         final Map<String, MediaContainerModel> mediaContainerModels =
            getMediaContainerMap(catalogVersion);
         final List<MediaContainerModel> medias = new ArrayList<MediaContainerModel>();
         LOG.debug("Images: " + imagesString);
         final String[] imageStrings = imagesString.split("\\|");
         for (String imageString : imageStrings)
         {
            imageString = imageString.trim();
            if (mediaContainerModels.containsKey(imageString))
            {
               medias.add(mediaContainerModels.get(imageString));
            }
            else
            {
               imageLogger.warn("No Media has been found for Media Mogul ID: '" + imageString
                  + "' on " + item.getItemtype() + " with code '"
                  + item.getAttributeProvider().getAttribute("code") + "'.");
               LOG.warn("No Media has been found for Media Mogul ID: '" + imageString + "' on "
                  + item.getItemtype() + " with code '"
                  + item.getAttributeProvider().getAttribute("code") + "'.");
            }

         }
         if (!medias.isEmpty())
         {
            final MediaService mediaService =
               (MediaService) Registry.getApplicationContext().getBean("mediaService");
            final MediaContainerService mediaContainerService =
               (MediaContainerService) Registry.getApplicationContext().getBean(
                  "mediaContainerService");
            final Class itemClass = item.getClass();
            final MediaFormatModel mainImageFormat = mediaService.getFormat("658x370");
            final MediaFormatModel thumbnailImageFormat = mediaService.getFormat("232x130");

            // // issues found on reflection- so we checked the item instance
            if (item instanceof AccommodationModel)
            {
               final AccommodationModel accommodation = (AccommodationModel) item;
               accommodation.setGalleryImages(medias);
               accommodation.setPicture(mediaContainerService.getMediaForFormat(medias.get(0),
                  mainImageFormat));
               accommodation.setThumbnail(mediaContainerService.getMediaForFormat(medias.get(0),
                  thumbnailImageFormat));
               for (final MediaContainerModel media : medias)
               {
                  media.setBrands(accommodation.getBrands());

                  modelService.save(media);
               }
            }
            else if (item instanceof RoomModel)
            {
               final RoomModel room = (RoomModel) item;
               room.setGalleryImages(medias);
               room.setPicture(mediaContainerService.getMediaForFormat(medias.get(0),
                  mainImageFormat));
               // No thumbnail on Room:
               // room.setThumbnail(mediaContainerService.getMediaForFormat(medias.get(0),

            }
            else if (item instanceof FacilityModel)
            {
               final FacilityModel facility = (FacilityModel) item;
               facility.setGalleryImages(medias);
               facility.setPicture(mediaContainerService.getMediaForFormat(medias.get(0),
                  mainImageFormat));
               // No thumbnail on Facility:
               // facility.setThumbnail(mediaContainerService.getMediaForFormat(medias.get(0),

               for (final MediaContainerModel media : medias)
               {
                  media.setBrands(facility.getBrands());

                  modelService.save(media);
               }

            }
            else if (item instanceof ExcursionModel)
            {
               final ExcursionModel excursion = (ExcursionModel) item;
               excursion.setGalleryImages(medias);
               excursion.setPicture(mediaContainerService.getMediaForFormat(medias.get(0),
                  mainImageFormat));
               excursion.setThumbnail(mediaContainerService.getMediaForFormat(medias.get(0),
                  thumbnailImageFormat));
            }
            else if (item instanceof AttractionModel)
            {
               final AttractionModel attraction = (AttractionModel) item;
               attraction.setGalleryImages(medias);
               attraction.setPicture(mediaContainerService.getMediaForFormat(medias.get(0),
                  mainImageFormat));
               attraction.setThumbnail(mediaContainerService.getMediaForFormat(medias.get(0),
                  thumbnailImageFormat));
            }
            else if (item instanceof LocationModel)
            {
               final LocationModel location = (LocationModel) item;
               location.setGalleryImages(medias);
               location.setPicture(mediaContainerService.getMediaForFormat(medias.get(0),
                  mainImageFormat));
               location.setThumbnail(mediaContainerService.getMediaForFormat(medias.get(0),
                  thumbnailImageFormat));

               for (final MediaContainerModel media : medias)
               {
                  media.setBrands(location.getBrands());

                  modelService.save(media);
               }

            }
            else if (item instanceof ProductRangeModel)
            {
               final ProductRangeModel prodRange = (ProductRangeModel) item;
               prodRange.setGalleryImages(medias);
               prodRange.setPicture(mediaContainerService.getMediaForFormat(medias.get(0),
                  mainImageFormat));
               prodRange.setThumbnail(mediaContainerService.getMediaForFormat(medias.get(0),
                  thumbnailImageFormat));

               for (final MediaContainerModel media : medias)
               {
                  media.setBrands(prodRange.getBrands());

                  modelService.save(media);
               }

            }
            else
            {
               try
               {
                  final Method setGalleryImages =
                     itemClass.getMethod("setGalleryImages", new Class[] { Collection.class });
                  setGalleryImages.invoke(item, new Object[] { medias });
                  final Method setPicture =
                     itemClass.getMethod("setPicture", new Class[] { MediaModel.class });
                  setPicture.invoke(item, new Object[] { mediaContainerService.getMediaForFormat(
                     medias.get(0), mainImageFormat) });
               }
               catch (final SecurityException e)
               {

                  LOG.error(" SecurityException  casued while  setting gallery images ", e);
                  return false;
               }
               catch (final NoSuchMethodException e)
               {
                  LOG.error(
                     "The item '"
                        + item.getPk()
                        + "' of type '"
                        + item.getItemtype()
                        + "' does not have either the 'galleryImages' or 'picture' attribute, yet the Feed Processor for its type has sent it for image processing.",
                     e);

                  return false;
               }
               catch (final IllegalArgumentException e)
               {

                  LOG.error(" IllegalArgumentException  casued while setting gallery images  ", e);
                  return false;
               }
               catch (final IllegalAccessException e)
               {

                  LOG.error(" IllegalAccessException  casued while setting gallery images ", e);
                  return false;
               }
               catch (final InvocationTargetException e)
               {

                  LOG.error(" InvocationTargetException  casued while setting gallery images ", e);
                  return false;
               }
            }
         }

         modelService.save(item);
      }
      return true;
   }

   protected Map<String, MediaContainerModel> getMediaContainerMap(
      final CatalogVersionModel catalogVersion)
   {
      if (mediaContainers == null || mediaContainers.isEmpty())
      {
         final FlexibleSearchService flexibleSearchService =
            Registry.getApplicationContext().getBean(FlexibleSearchService.class);
         final FlexibleSearchQuery query = new FlexibleSearchQuery(ALL_MEDIA_CONTAINERS_QUERY);
         query.addQueryParameter("catalogVersion", catalogVersion);
         SearchResult<MediaContainerModel> result;
         try
         {
            result = flexibleSearchService.<MediaContainerModel> search(query);
         }
         catch (final ModelNotFoundException e)
         {
            LOG.error("No Media Containers were found in Hybris.", e);
            return Collections.emptyMap();
         }
         mediaContainers = new HashMap<String, MediaContainerModel>(result.getTotalCount());
         for (final MediaContainerModel mediaContainer : result.getResult())
         {
            if (StringUtils.isNotBlank(mediaContainer.getMediaMogulId()))
            {
               if (!mediaContainers.containsKey(mediaContainer.getMediaMogulId()))
               {
                  mediaContainers.put(mediaContainer.getMediaMogulId(), mediaContainer);
               }
               else
               {
                  imageLogger.warn("Multiple Media has been imported for Media Mogul ID: '"
                     + mediaContainer.getMediaMogulId()
                     + "'. Using the first available.\nAsset IDs found: "
                     + mediaContainer.getQualifier() + " and "
                     + mediaContainers.get(mediaContainer.getMediaMogulId()).getQualifier() + ".");
                  LOG.warn("Multiple Media has been imported for Media Mogul ID: '"
                     + mediaContainer.getMediaMogulId()
                     + "'. Using the first available.\nAsset IDs found: "
                     + mediaContainer.getQualifier() + " and "
                     + mediaContainers.get(mediaContainer.getMediaMogulId()).getQualifier() + ".");
               }
            }
         }
      }
      return mediaContainers;
   }

   /**
    * @param location
    * @param line
    */
   protected void handleWeatherData(final LocationModel location, final Map<String, String> line)
   {
      final ModelService modelService =
         (ModelService) Registry.getApplicationContext().getBean("modelService");
      final WeatherModel weatherModel = modelService.create(WeatherModel.class);
      location.setWeather(weatherModel);
      final List<WeatherTypeModel> weatherTypes = new ArrayList<WeatherTypeModel>();
      weatherModel.setWeatherTypes(weatherTypes);

      final String averageTempMonthlyRawValue = line.get("averageTempMonthly");
      if (!StringUtils.isBlank(averageTempMonthlyRawValue))
      {

         final WeatherTypeModel weatherTypeModel = modelService.create(WeatherTypeModel.class);
         weatherTypeModel.setWeatherTypeName(WeatherTypeName.TEMPERATURE);
         weatherTypes.add(weatherTypeModel);
         final List<WeatherTypeValueModel> weatherTypeValues =
            new ArrayList<WeatherTypeValueModel>();
         weatherTypeModel.setWeatherTypeValues(weatherTypeValues);

         createAverageMonthlyTempWeatherData(averageTempMonthlyRawValue, modelService,
            weatherTypeValues);
      }

      final String averageSunshineMonthlyRawValue = line.get("sunshineHoursMonthly");
      if (!StringUtils.isBlank(averageSunshineMonthlyRawValue))
      {

         final WeatherTypeModel weatherTypeModel = modelService.create(WeatherTypeModel.class);
         weatherTypeModel.setWeatherTypeName(WeatherTypeName.SUNSHINE);
         weatherTypes.add(weatherTypeModel);
         final List<WeatherTypeValueModel> weatherTypeValues =
            new ArrayList<WeatherTypeValueModel>();
         weatherTypeModel.setWeatherTypeValues(weatherTypeValues);

         createAverageMonthlySunshineWeatherData(averageSunshineMonthlyRawValue, modelService,
            weatherTypeValues);
      }

      final String averageRainfallMonthlyRawValue = line.get("rainfallMonthly");
      if (!StringUtils.isBlank(averageRainfallMonthlyRawValue))
      {

         final WeatherTypeModel weatherTypeModel = modelService.create(WeatherTypeModel.class);
         weatherTypeModel.setWeatherTypeName(WeatherTypeName.RAINFALL);
         weatherTypes.add(weatherTypeModel);
         final List<WeatherTypeValueModel> weatherTypeValues =
            new ArrayList<WeatherTypeValueModel>();
         weatherTypeModel.setWeatherTypeValues(weatherTypeValues);

         createAverageMonthlyRainfallWeatherData(averageRainfallMonthlyRawValue, modelService,
            weatherTypeValues);
      }
   }

   private void createAverageMonthlyTempWeatherData(final String averageTempMonthlyRawValue,
      final ModelService modelService, final List<WeatherTypeValueModel> weatherTypeValues)
   {
      final StringTokenizer strTok = new StringTokenizer(averageTempMonthlyRawValue, "|");
      while (strTok.hasMoreTokens())
      {
         final WeatherTypeValueModel weatherTypeValue =
            getWeatherValue(modelService, strTok.nextToken());
         weatherTypeValue.setWeatherUnitType(WeatherUnit.DEGC);
         weatherTypeValues.add(weatherTypeValue);
      }
   }

   private void createAverageMonthlySunshineWeatherData(
      final String averageSunshineMonthlyRawValue, final ModelService modelService,
      final List<WeatherTypeValueModel> weatherTypeValues)
   {
      final StringTokenizer strTok = new StringTokenizer(averageSunshineMonthlyRawValue, "|");
      while (strTok.hasMoreTokens())
      {
         final WeatherTypeValueModel weatherTypeValue =
            getWeatherValue(modelService, strTok.nextToken());
         weatherTypeValue.setWeatherUnitType(WeatherUnit.HR);
         weatherTypeValues.add(weatherTypeValue);
      }
   }

   private void createAverageMonthlyRainfallWeatherData(
      final String averageRainfallMonthlyRawValue, final ModelService modelService,
      final List<WeatherTypeValueModel> weatherTypeValues)
   {
      final StringTokenizer strTok = new StringTokenizer(averageRainfallMonthlyRawValue, "|");
      while (strTok.hasMoreTokens())
      {
         final WeatherTypeValueModel weatherTypeValue =
            getWeatherValue(modelService, strTok.nextToken());
         weatherTypeValue.setWeatherUnitType(WeatherUnit.MM);
         weatherTypeValues.add(weatherTypeValue);
      }
   }

   private WeatherTypeValueModel getWeatherValue(final ModelService modelService,
      final String averageWeatherMeasurmentAndMonth)
   {
      final WeatherTypeValueModel weatherTypeValueModel =
         modelService.create(WeatherTypeValueModel.class);
      if (!StringUtils.isBlank(averageWeatherMeasurmentAndMonth)
         && averageWeatherMeasurmentAndMonth.indexOf(':') != -1)
      {
         final String month =
            averageWeatherMeasurmentAndMonth.substring(0,
               averageWeatherMeasurmentAndMonth.indexOf(':'));
         final String weatherMeasurment =
            averageWeatherMeasurmentAndMonth.substring(
               averageWeatherMeasurmentAndMonth.indexOf(':') + 1,
               averageWeatherMeasurmentAndMonth.length());

         weatherTypeValueModel.setMonthType(Month.valueOf(month));
         if (!StringUtils.isBlank(weatherMeasurment))
         {
            weatherTypeValueModel.setAverage(Double.valueOf(weatherMeasurment));
         }
      }
      return weatherTypeValueModel;
   }

   protected Map<String, String> prepareDataMap(final String[] values)
   {
      final Map<String, Integer> posMap = getPositionMap();
      final Map<String, String> line = new HashMap<String, String>();
      final Iterator<Entry<String, Integer>> itr = posMap.entrySet().iterator();

      while (itr.hasNext())
      {
         final Entry<String, Integer> column = itr.next();
         if (column.getValue().intValue() < values.length)
         {
            line.put(column.getKey(), values[column.getValue().intValue()]);
         }
      }
      return line;
   }

   protected void moveToOutputFolder(final String inputDir, final String outputDir,
      final String fileName)
   {

      try
      {

         final File input =
            new File(Config.getString(DATADIR, "") + "/" + inputDir + "/" + fileName);
         input.renameTo(new File(Config.getString(DATADIR, "") + "/" + outputDir + "/" + fileName));
      }
      catch (final Exception ex)
      {
         LOG.error("Error moving file to output directory" + outputDir, ex);
      }
   }

   protected String[][] parseCSV(final String rootDir, final String fileName)
      throws FileNotFoundException
   {

      String[][] values = null;
      InputStreamReader reader = null;
      try
      {
         reader =
            new InputStreamReader(new FileInputStream(getDataFilePath(rootDir, fileName)),
               Charset.forName("ISO-8859-1"));
         final CSVReader csvReader = new CSVReader(reader, ',', '"');
         values = csvReader.readAll().toArray(new String[0][0]);
      }
      catch (final IOException e)
      {
         LOG.error("Error Parsing feed file  " + getDataFilePath(rootDir, fileName), e);
      }
      finally
      {
         try
         {
            if (reader != null)
            {
               reader.close();
            }
         }
         catch (final IOException e)
         {
            LOG.error("Error Closing the feed file reader " + getDataFilePath(rootDir, fileName), e);
         }
      }
      return values;
   }

   protected List<String> parseContentFromFile(final String rootDir, final String fileName)
      throws FileNotFoundException
   {
      List<String> feedData = null;
      try
      {
         feedData =
            Files.readLines(new File(getDataFilePath(rootDir, fileName)),
               Charset.forName("ISO-8859-1"));
      }
      catch (final IOException e)
      {
         LOG.error(e.getMessage(), e);
      }
      return feedData;
   }

   protected String getDataFilePath(final String rootDir, final String fileName)
   {
      final StringBuilder sb = new StringBuilder();
      sb.append(Config.getString(DATADIR, ""));
      sb.append("/").append(rootDir).append("/").append(fileName);
      return sb.toString();
   }

   protected Logger setUpImageLog(final String outputDir)
   {
      imageLogger = Logger.getLogger("dam_assets");
      try
      {
         final Date date = new Date();
         final SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy");
         final StringBuilder sb = new StringBuilder();
         sb.append(Config.getString(DATADIR, "")).append("/").append(outputDir).append("/");
         sb.append("dam_assets").append("_").append(format.format(date)).append(".log");
         final FileAppender appender = new FileAppender(new SimpleLayout(), sb.toString(), true);
         imageLogger.setLevel(Level.INFO);
         imageLogger.setAdditivity(false);
         imageLogger.removeAllAppenders();
         imageLogger.addAppender(appender);
      }
      catch (final Exception ex)
      {
         LOG.error("cannot construct the logger for creating output log file", ex);
      }
      return imageLogger;
   }

   protected Logger getCustomLog(final String outputDir, final String outputLog)
   {

      logToFile = Logger.getLogger(outputLog);
      try
      {
         final Date date = new Date();
         final SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy");
         final StringBuilder sb = new StringBuilder();
         sb.append(Config.getString(DATADIR, "")).append("/").append(outputDir).append("/");
         final File f = new File(sb.toString());
         if (!f.isDirectory())
         {
            final boolean isDirectoryExists = f.mkdir();
            LOG.info("Directory Created" + isDirectoryExists);
         }
         sb.append(outputLog).append("_").append(format.format(date)).append(".log");
         final FileAppender appender = new FileAppender(new SimpleLayout(), sb.toString(), false);
         logToFile.setLevel(Level.INFO);
         logToFile.setAdditivity(false);
         logToFile.removeAllAppenders();
         logToFile.addAppender(appender);
      }
      catch (final Exception ex)
      {
         LOG.error("cannot construct the logger for creating output log file", ex);
      }
      return logToFile;
   }
}
