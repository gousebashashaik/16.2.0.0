package uk.co.portaltech.tui.converters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccommodationOption
{

   public static final AccommodationOption PRIMARY_IMAGE = new AccommodationOption("PRIMARY_IMAGE");

   public static final AccommodationOption PRICE = new AccommodationOption("PRICE");

   public static final AccommodationOption BASIC = new AccommodationOption("BASIC");

   public static final AccommodationOption SUMMARY = new AccommodationOption("SUMMARY");

   public static final AccommodationOption DESCRIPTION = new AccommodationOption("DESCRIPTION");

   public static final AccommodationOption GALLERY = new AccommodationOption("GALLERY");

   public static final AccommodationOption CATEGORIES = new AccommodationOption("CATEGORIES");

   public static final AccommodationOption VARIANT_FULL = new AccommodationOption("VARIANT_FULL");

   public static final AccommodationOption LOCATION_KEYFACTS = new AccommodationOption(
      "LOCATION_KEYFACTS");

   public static final AccommodationOption LOCATION_INFO = new AccommodationOption("LOCATION_INFO");

   public static final AccommodationOption GALLERY_IMAGE = new AccommodationOption("GALLERY_IMAGE");

   public static final AccommodationOption EDITORIAL_INFO = new AccommodationOption(
      "EDITORIAL_INFO");

   public static final AccommodationOption EDITORIAL_INTRO = new AccommodationOption(
      "EDITORIAL_INTRO");

   public static final AccommodationOption DISCLAIMER = new AccommodationOption("DISCLAIMER");

   public static final AccommodationOption RESTRICTION = new AccommodationOption("RESTRICTION");

   public static final AccommodationOption HEALTHANDSAFETY = new AccommodationOption(
      "HEALTHANDSAFETY");

   public static final AccommodationOption FACILITIES = new AccommodationOption("FACILITIES");

   public static final AccommodationOption PASSPORTANDVISA = new AccommodationOption(
      "PASSPORTANDVISA");

   public static final AccommodationOption ALL_INCLUSIVE = new AccommodationOption("ALL_INCLUSIVE");

   public static final AccommodationOption AT_A_GLANCE = new AccommodationOption("AT_A_GLANCE");

   public static final AccommodationOption PRODUCTRANGE = new AccommodationOption("PRODUCTRANGE");

   public static final AccommodationOption GETTINGAROUND = new AccommodationOption("GETTINGAROUND");

   public static final AccommodationOption WHEN_TO_GO = new AccommodationOption("WHEN_TO_GO");

   public static final AccommodationOption T_RATING = new AccommodationOption("T_Rating");

   public static final AccommodationOption ENTERTAINMENT = new AccommodationOption("ENTERTAINMENT");

   public static final AccommodationOption THUMBNAILMAP = new AccommodationOption("THUMBNAILMAP");

   public static final AccommodationOption NAME = new AccommodationOption("NAME");

   public static final AccommodationOption ENRICH = new AccommodationOption("ENRICH");

   public static final AccommodationOption HIGHLIGHTS = new AccommodationOption("HIGHLIGHTS");

   public static final AccommodationOption ROOMS = new AccommodationOption("ROOMS");

   public static final AccommodationOption AWARD_INFO = new AccommodationOption("AWARD_INFO");

   public static final AccommodationOption INTRODUCTION = new AccommodationOption("INTRODUCTION");

   public static final AccommodationOption ACCOM_LOCATION_DATA = new AccommodationOption(
      "ACCOM_LOCATION_DATA");

   public static final AccommodationOption SPECIALOFFER = new AccommodationOption("SPECIALOFFER");

   public static final AccommodationOption FULLDATA = new AccommodationOption("FULLDATA");

   public static final AccommodationOption ACCOM_GEO = new AccommodationOption("ACCOM_GEO");

   public static final AccommodationOption ACCOM_INFO = new AccommodationOption("ACCOM_INFO");

   public static final AccommodationOption BOARDBAIS_DATA = new AccommodationOption(
      "BOARDBAIS_DATA");

   private static List<AccommodationOption> internalValues = Arrays.asList(PRIMARY_IMAGE, PRICE,
      BASIC, SUMMARY, DESCRIPTION, GALLERY, CATEGORIES, VARIANT_FULL, AT_A_GLANCE, ENRICH,
      HIGHLIGHTS);

   private final String code;

   protected AccommodationOption(final String code)
   {
      this.code = code;
   }

   public static AccommodationOption valueOf(final String option)
   {
      final AccommodationOption possibleOption = new AccommodationOption(option);
      final int internalValuesIndex = internalValues.indexOf(possibleOption);
      if (internalValuesIndex != -1)
      {
         return internalValues.get(internalValuesIndex);
         // just to always return one instance not many the same (equal)
      }
      else
      {
         throw new IllegalArgumentException("Cannot parse into an element of "
            + AccommodationOption.class.getSimpleName() + ": '" + option + "'");
      }
   }

   @Override
   public String toString()
   {
      return code;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (obj instanceof AccommodationOption)
      {
         return code.equals(((AccommodationOption) obj).code);
      }
      return false;
   }

   @Override
   public int hashCode()
   {
      return code.hashCode();
   }

   public static Map<String, AccommodationOption> getAllPopulators()
   {
      final Map<String, AccommodationOption> populatorsMap =
         new HashMap<String, AccommodationOption>();
      populatorsMap.put("PRIMARY_IMAGE", PRIMARY_IMAGE);
      populatorsMap.put("PRICE", PRICE);
      populatorsMap.put("BASIC", BASIC);
      populatorsMap.put("SUMMARY", SUMMARY);
      populatorsMap.put("DESCRIPTION", DESCRIPTION);
      populatorsMap.put("GALLERY", GALLERY);
      populatorsMap.put("CATEGORIES", CATEGORIES);
      populatorsMap.put("VARIANT_FULL", VARIANT_FULL);
      populatorsMap.put("LOCATION_KEYFACTS", LOCATION_KEYFACTS);
      populatorsMap.put("LOCATION_INFO", LOCATION_INFO);
      populatorsMap.put("GALLERY_IMAGE", GALLERY_IMAGE);
      populatorsMap.put("EDITORIAL_INFO", EDITORIAL_INFO);
      populatorsMap.put("EDITORIAL_INTRO", EDITORIAL_INTRO);
      populatorsMap.put("DISCLAIMER", DISCLAIMER);
      populatorsMap.put("RESTRICTION", RESTRICTION);
      populatorsMap.put("HEALTHANDSAFETY", HEALTHANDSAFETY);
      populatorsMap.put("FACILITIES", FACILITIES);
      populatorsMap.put("PASSPORTANDVISA", PASSPORTANDVISA);
      populatorsMap.put("ALL_INCLUSIVE", ALL_INCLUSIVE);
      populatorsMap.put("AT_A_GLANCE", AT_A_GLANCE);
      populatorsMap.put("PRODUCTRANGE", PRODUCTRANGE);
      populatorsMap.put("GETTINGAROUND", GETTINGAROUND);
      populatorsMap.put("WHEN_TO_GO", WHEN_TO_GO);
      populatorsMap.put("T_RATING", T_RATING);
      populatorsMap.put("ENTERTAINMENT", ENTERTAINMENT);
      populatorsMap.put("THUMBNAILMAP", THUMBNAILMAP);
      populatorsMap.put("NAME", NAME);
      populatorsMap.put("ENRICH", ENRICH);
      populatorsMap.put("HIGHLIGHTS", HIGHLIGHTS);
      populatorsMap.put("ROOMS", ROOMS);
      populatorsMap.put("AWARD_INFO", AWARD_INFO);
      populatorsMap.put("INTRODUCTION", INTRODUCTION);
      populatorsMap.put("ACCOM_LOCATION_DATA", ACCOM_LOCATION_DATA);
      populatorsMap.put("SPECIALOFFER", SPECIALOFFER);
      return populatorsMap;
   }
}
