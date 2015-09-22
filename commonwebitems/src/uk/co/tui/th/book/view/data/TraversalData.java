/**
 *
 */
package uk.co.tui.th.book.view.data;

/**
 * The Class TraversalData. This class contains traversal related date like preceding page url ,
 * next page url
 *
 * @author samantha.gd
 */
public class TraversalData
{
   /** The next step */
   private int nextStep;

   /** The preceding page URL. */
   private String precedingPageUrl;

   /** The preceding page text. */
   private String precedingPageText;

   /** The next page URL. */
   private String nextPageUrl;

   public TraversalData()
   {
   }

   /**
    * Gets the next page URL.
    *
    * @return the nextPageUrl
    */
   public String getNextPageUrl()
   {
      return nextPageUrl;
   }

   /**
    * Sets the next page URL.
    *
    * @param nextPageUrl the nextPageUrl to set
    */
   public void setNextPageUrl(final String nextPageUrl)
   {
      this.nextPageUrl = nextPageUrl;
   }

   /**
    * Instantiates a new navigation view data.
    */

   /**
    * Gets the next step.
    *
    * @return the nextStep
    */
   public int getNextStep()
   {
      return nextStep;
   }

   /**
    * Sets the next step.
    *
    * @param nextStep the nextStep to set
    */
   public void setNextStep(final int nextStep)
   {
      this.nextStep = nextStep;
   }

   /**
    * Gets the preceding page url.
    *
    * @return the precedingPageUrl
    */
   public String getPrecedingPageUrl()
   {
      return precedingPageUrl;
   }

   /**
    * Sets the preceding page url.
    *
    * @param precedingPageUrl the precedingPageUrl to set
    */
   public void setPrecedingPageUrl(final String precedingPageUrl)
   {
      this.precedingPageUrl = precedingPageUrl;
   }

   /**
    * Gets the preceding page text.
    *
    * @return the precedingPageText
    */
   public String getPrecedingPageText()
   {
      return precedingPageText;
   }

   /**
    * Sets the preceding page text.
    *
    * @param precedingPageText the precedingPageText to set
    */
   public void setPrecedingPageText(final String precedingPageText)
   {
      this.precedingPageText = precedingPageText;
   }
}