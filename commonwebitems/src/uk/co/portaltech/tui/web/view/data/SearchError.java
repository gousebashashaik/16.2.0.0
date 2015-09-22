package uk.co.portaltech.tui.web.view.data;

import java.util.List;

public class SearchError
{
   private String code;

   private String message;

   private List<?> entry;

   private List<?> matches;

   public SearchError()
   {
   }

   public SearchError(final String code, final String message)
   {
      this.code = code;
      this.message = message;
   }

   public SearchError(final String code, final String message, final List<?> entry,
                      final List<?> matches)
   {
      this(code, message);
      this.entry = entry;
      this.matches = matches;

   }

   /**
    * @return the matchs
    */
   public List<?> getMatches()
   {
      return matches;
   }

   /**
    * @param matchs the matchs to set
    */
   public void setMatches(final List<String> matches)
   {
      this.matches = matches;
   }

   public String getCode()
   {
      return code;
   }

   public void setCode(final String code)
   {
      this.code = code;
   }

   public String getMessage()
   {
      return message;
   }

   /**
    * @return the entry
    */
   public List<?> getEntry()
   {
      return entry;
   }

   /**
    * @param entry the entry to set
    */
   public void setEntry(final List<?> entry)
   {
      this.entry = entry;
   }

   public void setMessage(final String message)
   {
      this.message = message;
   }

}
