/**
 *
 */
package uk.co.portaltech.tui.impex.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.feeds.context.AbstractImportContext;

/**
 *
 * This utility class prepares data fetched from csv file; which will then read by
 * 'tui_touristBoard.impex' script in order to import TouristBoard data into the System
 *
 * @author exts6p
 *
 */

public class TouristBoardImportContext extends AbstractImportContext
{

   public static final String MC = "MC";

   public static final String TB = "TB";

   public static final String UNDER_SCORE = "_";

   public static final String LOGO_SMALL = "Logo_small";

   public static final String LOGO = "Logo";

   public static final String LOGO_MEDIUM = "Logo_medium";

   public static final String MIME = "image/gif";

   public static final String ALTTEXT = "Tourist Board Logo";

   public static final String DESCRIPTION = "Tourist Board Logo";

   public static final String MEDIA_FORMAT_SMALL = "232x130";

   public static final String MEDIA_FORMAT_MEDIUM = "488x274";

   public static final String MEDIA_CONTAINER = "Media Container for Country";

   /**
    * The qualifier name for TouristBoardLocationRelevancyRel relation
    */
   public static final String QUALIFIER = "TouristBoardLocationRelevancyRel";

   private static final TUILogUtils LOG = new TUILogUtils("TouristBoardImportContext");

   // for avoiding Magic Number
   private static final int NUMBER_FOURZEROZERO = 400;

   private static final int NUMBER_FOURZEROONE = 401;

   private static final int NUMBER_FOURZEROTWO = 402;

   private static final int NUMBER_FOURZEROTHREE = 403;

   private static final int NUMBER_FOURZEROFOUR = 404;

   private static final int NUMBER_FOURZEROSIX = 406;

   private static final int NUMBER_FOURZEROSEVEN = 407;

   private static final int NUMBER_FOURTWOSIX = 426;

   private static final int TWO = 2;

   public TouristBoardImportContext()
   {

   }

   /**
    * This method creates MediaContainer for a TouristBoard
    *
    * @param line
    */
   public void prepareMediaContainers(final Map<Integer, String> line)
   {

      if (!StringUtils.isBlank(line.get(Integer.valueOf(1))))
      {
         final StringBuilder sb = new StringBuilder();
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(MC);
         line.put(Integer.valueOf(NUMBER_FOURZEROZERO), sb.toString());
         line.put(Integer.valueOf(NUMBER_FOURZEROONE), MEDIA_CONTAINER);
         LOG.debug("Adding Media Container with code " + sb.toString());

         sb.delete(0, sb.length());
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(TB).append(LOGO_SMALL);
         sb.append(",");
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(TB).append(LOGO_MEDIUM);
         line.put(Integer.valueOf(NUMBER_FOURTWOSIX), sb.toString());

      }

   }

   /**
    * This method creates Medias to associate it with MediaContainer of a TouristBoard
    *
    * @param line
    */
   public void prepareMediasSmall(final Map<Integer, String> line)
   {

      if (!StringUtils.isBlank(line.get(Integer.valueOf(1))))
      {
         final StringBuilder sb = new StringBuilder();
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(TB).append(LOGO_SMALL);
         line.put(Integer.valueOf(NUMBER_FOURZEROZERO), sb.toString());
         line.put(Integer.valueOf(NUMBER_FOURZEROONE), MIME);
         line.put(Integer.valueOf(NUMBER_FOURZEROTWO), ALTTEXT);
         line.put(Integer.valueOf(NUMBER_FOURZEROTHREE), DESCRIPTION);
         line.put(Integer.valueOf(NUMBER_FOURZEROFOUR), MEDIA_FORMAT_SMALL);
         line.put(Integer.valueOf(NUMBER_FOURZEROSIX), line.get(Integer.valueOf(TWO)));

         LOG.debug("Adding Media with code " + sb.toString());

         sb.delete(0, sb.length());
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(MC);
         line.put(Integer.valueOf(NUMBER_FOURZEROSEVEN), sb.toString());
      }
   }

   /**
    * This method creates Medias to associate it with MediaContainer of a TouristBoard
    *
    * @param line
    */
   public void prepareMediasMedium(final Map<Integer, String> line)
   {

      if (!StringUtils.isBlank(line.get(Integer.valueOf(1))))
      {
         final StringBuilder sb = new StringBuilder();
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(TB).append(LOGO_MEDIUM);
         line.put(Integer.valueOf(NUMBER_FOURZEROZERO), sb.toString());
         line.put(Integer.valueOf(NUMBER_FOURZEROONE), MIME);
         line.put(Integer.valueOf(NUMBER_FOURZEROTWO), ALTTEXT);
         line.put(Integer.valueOf(NUMBER_FOURZEROTHREE), DESCRIPTION);
         line.put(Integer.valueOf(NUMBER_FOURZEROFOUR), MEDIA_FORMAT_MEDIUM);
         line.put(Integer.valueOf(NUMBER_FOURZEROSIX), line.get(Integer.valueOf(TWO)));

         LOG.debug("Adding Media with code " + sb.toString());

         sb.delete(0, sb.length());
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(MC);
         line.put(Integer.valueOf(NUMBER_FOURZEROSEVEN), sb.toString());
      }
   }

   /**
    * This method creates TouristBoard instances
    *
    * @param line
    */
   public void prepareTouristBoards(final Map<Integer, String> line)
   {

      if (!StringUtils.isBlank(line.get(Integer.valueOf(1))))
      {
         final StringBuilder sb = new StringBuilder();
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(TB);
         line.put(Integer.valueOf(NUMBER_FOURZEROZERO), sb.toString());

         LOG.debug("Adding TouristBoard with code " + sb.toString());

         sb.delete(0, sb.length());
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(MC);
         line.put(Integer.valueOf(NUMBER_FOURZEROONE), sb.toString());
         // Adding below piece of code to insert brands for Tourist Board table.
         final List<Integer> brandIndexList =
            new LinkedList(Arrays.asList(new Integer[] { Integer.valueOf(NUMBER_FOUR),
               Integer.valueOf(NUMBER_FIVE), Integer.valueOf(NUMBER_SIX),
               Integer.valueOf(NUMBER_SEVEN), Integer.valueOf(NUMBER_EIGHT) }));

         final String brand = brand(line, brandIndexList);
         // Inserting the brands into Tourist Board.
         line.put(Integer.valueOf(NUMBER_FOURZEROSIX), brand);

      }
   }

   /**
    * This method sets TouristBoard attribute to a Location
    *
    * @param line
    */
   public void prepareLocations(final Map<Integer, String> line)
   {

      if (!StringUtils.isBlank(line.get(Integer.valueOf(1))))
      {
         final StringBuilder sb = new StringBuilder();
         sb.append(line.get(Integer.valueOf(1))).append(UNDER_SCORE).append(TB);
         line.put(Integer.valueOf(NUMBER_FOURZEROTWO), sb.toString());

         LOG.debug("Adding Country Tourist Board with tourist board code " + sb.toString());

         line.put(Integer.valueOf(NUMBER_FOURZEROTHREE), "countries");
      }
   }
}
