package uk.co.portaltech.tui.web.view.data;

import java.util.List;

public class AirportSearchResult
{

   private List<AirportData> airports;

   private SearchError error;

   // Added By RukminiKrishna
   private boolean nomatch;

   public AirportSearchResult()
   {
   }

   public List<AirportData> getAirports()
   {
      return airports;
   }

   public void setAirports(final List<AirportData> airports)
   {
      this.airports = airports;
   }

   public SearchError getError()
   {
      return error;
   }

   public void setError(final SearchError error)
   {
      this.error = error;
   }

   /**
    * @return the nomatch
    */
   public boolean isNomatch()
   {
      return nomatch;
   }

   /**
    * @param nomatch the nomatch to set
    */
   public void setNomatch(final boolean nomatch)
   {
      this.nomatch = nomatch;
   }

}
