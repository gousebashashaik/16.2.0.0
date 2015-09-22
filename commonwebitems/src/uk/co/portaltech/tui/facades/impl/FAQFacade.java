/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.jackson.databind.HTMLCharacterEscapes;

import com.tui.uk.integration.faq.model.Category;
import com.tui.uk.integration.faq.model.Document;
import com.tui.uk.integration.faq.model.SubCategory;
import com.tui.uk.integration.faq.services.FaqService;

/**
 * This is the helper class which helps FAQ service for getting the FAQ details from Eptica.
 *
 * @author madhu.p
 *
 */
public class FAQFacade
{

   /** The faq service. */
   @Resource
   private FaqService faqService;

   /** The configuration service. */
   @Resource
   private ConfigurationService configurationService;

   /** The Constant serverUnavailable. */
   private static final String SERVERUNAVAILABLE = "Server Unavailable";

   /**
    * Method to submitting the customer feed back to Eptica.
    *
    * @param documentID
    * @param customerSatisfied
    */
   public void submitFeedBack(final String documentID, final boolean customerSatisfied,
      final String brand)
   {
      faqService.submitFeedback(documentID, customerSatisfied, brand);
   }

   /**
    * Method create the categories which are relevant to the brand.
    *
    * @return the category
    */
   public List<Category> getCategory(final String brand)
   {
      return faqService.getCategory(brand);
   }

   /**
    * Method create the categories which are relevant to the brand.
    *
    * @return the category
    */
   public List<Category> getCategoryLite(final String brand)
   {
      return faqService.getCategoryLite(brand);
   }

   /**
    * Method fetches the sub-categories to the given category.
    *
    * @param categoryID the categoryID
    * @return the sub category
    */
   public String getSubcategoriesForCategory(final String categoryID, final String brand)
   {
      final List<SubCategory> subCategories = faqService.getSubCategory(categoryID, brand);
      if (subCategories == null)
      {
         return SERVERUNAVAILABLE;
      }
      return HTMLCharacterEscapes.writeValue(subCategories);
   }

   /**
    * Method to fetches the FAQs for the given sub categories.
    *
    * @param subCategoryID the subCategoryID return faq
    */
   public String getFaqsForSubCategory(final String subCategoryID, final String brand)
   {
      List<Document> faqsForSubCategory = faqService.getDocuments(subCategoryID, brand);

      if (faqsForSubCategory == null)
      {
         return SERVERUNAVAILABLE;
      }
      else if (faqsForSubCategory.size() > Integer.parseInt(configurationService.getConfiguration()
         .getString("faqsCategories.maxlimit", "10")))
      {
         faqsForSubCategory =
            faqsForSubCategory.subList(
               0,
               Integer.parseInt(configurationService.getConfiguration().getString(
                  "faqsCategories.maxlimit", "10")));
      }

      return HTMLCharacterEscapes.writeValue(faqsForSubCategory);
   }

   /**
    * Method to fetches the content for the given FAQ id.
    *
    * @param documentID the documentID
    * @return the content
    */
   public String getContentForDocument(final String documentID, final String brand)
   {
      final String content = faqService.getContent(documentID, brand);
      if (StringUtils.isBlank(content))
      {
         return SERVERUNAVAILABLE;
      }
      return HTMLCharacterEscapes.writeValue(content);
   }

   /**
    * Method to find the the FAQs for the given query.
    *
    * @param query
    * @return list of Document
    * @throws UnsupportedEncodingException
    */
   public List<Document> searchDocuments(final String query, final String brand)
      throws UnsupportedEncodingException
   {

      List<Document> documents =
         faqService.searchDocuments(brand, URLEncoder.encode(query, "UTF-8"));
      if (documents != null
         && documents.size() > Integer.parseInt(configurationService.getConfiguration().getString(
            "faqsCategories.faqsearchresultslimit", "5")))
      {
         documents =
            documents.subList(
               0,
               Integer.parseInt(configurationService.getConfiguration().getString(
                  "faqsCategories.faqsearchresultslimit", "5")));
      }
      return documents;
   }
}
