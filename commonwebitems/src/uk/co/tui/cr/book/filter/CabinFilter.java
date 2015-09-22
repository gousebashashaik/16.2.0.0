package uk.co.tui.cr.book.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;

import uk.co.tui.book.filter.Filter;
import uk.co.tui.book.filter.FilterInput;
import uk.co.tui.cr.book.view.data.CabinTypeViewData;
import uk.co.tui.cr.book.view.data.CabinViewData;


/**
 * The Class CabinFilter.
 *
 * @author ramkishore.p
 *
 *         Rule applied: Group the cabins by cabin title, after applying where condition result is ordered by price to
 *         get cheapest price result.
 *
 */
public class CabinFilter implements Filter<CabinTypeViewData, FilterInput<CabinViewData>>
{

   /** For logging */
   private static final Logger LOGGER = Logger.getLogger(CabinFilter.class.getName());

   private static final String QUERYSTRING = "SELECT * from uk.co.tui.cr.book.view.data.CabinViewData GROUP BY roomTitle ORDER BY deckTitle, roomCode";

   @SuppressWarnings("unchecked")
   @Override
   public List<CabinTypeViewData> filter(final FilterInput<CabinViewData> filterInput)
   {
      QueryResults result = null;
      final Query filterQuery = new Query();
      try
      {
         filterQuery.addFunctionHandler(this);
         filterQuery.parse(QUERYSTRING);
         result = filterQuery.execute(filterInput.getInputCollection());

      }
      catch (final QueryExecutionException e)
      {
         LOGGER.error(e);

      }
      catch (final QueryParseException e)
      {
         LOGGER.error(e);
      }
      return processResults(result.getGroupByResults());
   }

   /**
    * @param resultMap
    * @return List<CabinTypeViewData>
    */
   private List<CabinTypeViewData> processResults(final Map<List<String>, List<CabinViewData>> resultMap)
   {
      final List<CabinTypeViewData> cabinsTypeViewData = new ArrayList<CabinTypeViewData>();
      for (final Entry<List<String>, List<CabinViewData>> entry : resultMap.entrySet())
      {
         final CabinTypeViewData cabinTypeView = new CabinTypeViewData();
         cabinTypeView.setCabinType(entry.getKey().get(0));
         cabinTypeView.setListOfCabinViewData(entry.getValue());
         cabinsTypeViewData.add(cabinTypeView);
      }

      return cabinsTypeViewData;
   }

   @Override
   public boolean supportsAdditionalData(final FilterInput<CabinViewData> input)
   {
      return false;
   }

}
