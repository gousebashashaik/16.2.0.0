/**
 *
 */
package uk.co.portaltech.tui.web.tag;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import uk.co.portaltech.tui.web.view.data.TUICategoryData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author gagan
 *
 */
public class TreeStructIterator implements Tag, Serializable
{

   /**
     *
     */
   private static final int NUM_OF_ROWS_ALLOWED_IN_A_COLUMN = 6;

   private static final TUILogUtils LOG = new TUILogUtils("TreeStructIterator");

   private PageContext pc;

   private Tag parent;

   private TUICategoryData category;

   private int depth;

   private String accomType;

   private String componentID;

   /**
    * @return the componentID
    */
   public String getComponentID()
   {
      return componentID;
   }

   /**
    * @param componentID the componentID to set
    */
   public void setComponentID(final String componentID)
   {
      this.componentID = componentID;
   }

   @Override
   public int doEndTag() throws JspException
   {
      return EVAL_PAGE;
   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.servlet.jsp.tagext.Tag#doStartTag()
    */
   @Override
   public int doStartTag() throws JspException
   {
      final JspWriter writer = pc.getOut();
      try
      {
         renderGeographicalNavigationData(category, writer, depth, depth - 1);
      }
      catch (final IOException e)
      {
         LOG.error(
            "An I/O failure occured while rendering HTML for navigation tree for the location : "
               + category.getCode(), e);
      }

      return SKIP_BODY;
   }

   private void renderGeographicalNavigationData(final TUICategoryData tuiCategoryData,
      final JspWriter writer, final int navigationLevelDepth, final int ctrs) throws IOException
   {
      int ctr = ctrs;
      if (ctr < navigationLevelDepth)
      {
         final List<TUICategoryData> subcategories = tuiCategoryData.getCategories();
         if (subcategories != null && !subcategories.isEmpty())
         {
            final int subCategorySize = subcategories.size();
            int numOfColumnsForSubCategory = 1;
            if (subCategorySize >= NUM_OF_ROWS_ALLOWED_IN_A_COLUMN)
            {
               if (subCategorySize % NUM_OF_ROWS_ALLOWED_IN_A_COLUMN == 0)
               {
                  numOfColumnsForSubCategory = subCategorySize / NUM_OF_ROWS_ALLOWED_IN_A_COLUMN;
               }
               else
               {
                  numOfColumnsForSubCategory =
                     (subCategorySize / NUM_OF_ROWS_ALLOWED_IN_A_COLUMN) + 1;
               }
            }

            int loopInitCounter = 0;
            int loopControlCounter = 0;
            ++ctr;
            for (int i = 0; i < numOfColumnsForSubCategory; i++)
            {
               if (i > 0)
               {
                  writer.write("<ul class=\"no-border\">");
               }
               else
               {
                  writer.write("<ul>");
               }
               loopInitCounter = loopControlCounter;
               for (int j = loopInitCounter; j < subcategories.size(); j++)
               {
                  ++loopControlCounter;
                  if (loopControlCounter / NUM_OF_ROWS_ALLOWED_IN_A_COLUMN == (i + 1)
                     && (loopControlCounter % NUM_OF_ROWS_ALLOWED_IN_A_COLUMN != 0))
                  {
                     loopControlCounter--;
                     break;
                  }

                  final TUICategoryData subCategoryData = subcategories.get(j);
                  writer.write("<li><a class='ensLinkTrack' data-componentId='" + componentID
                     + "' href='" + subCategoryData.getUrl() + "'>" + subCategoryData.getName()
                     + "</a></li>");
                  renderGeographicalNavigationData(subCategoryData, writer, navigationLevelDepth,
                     ctr);
               }
               writer.write("</ul>");
            }
         }

      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.servlet.jsp.tagext.Tag#getParent()
    */
   @Override
   public Tag getParent()
   {
      return parent;
   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.servlet.jsp.tagext.Tag#release()
    */
   @Override
   public void release()
   {
      pc = null;
      parent = null;

   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext)
    */
   @Override
   public void setPageContext(final PageContext pc)
   {
      this.pc = pc;

   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.servlet.jsp.tagext.Tag#setParent(javax.servlet.jsp.tagext.Tag)
    */
   @Override
   public void setParent(final Tag tag)
   {
      this.parent = tag;

   }

   /**
    * @return the category
    */
   public TUICategoryData getCategory()
   {
      return category;
   }

   /**
    * @param category the category to set
    */
   public void setCategory(final TUICategoryData category)
   {
      this.category = category;
   }

   /**
    * @return the depth
    */
   public int getDepth()
   {
      return depth;
   }

   /**
    * @param depth the depth to set
    */
   public void setDepth(final int depth)
   {
      this.depth = depth;
   }

   /**
    * @return the accomType
    */
   public String getAccomType()
   {
      return accomType;
   }

   /**
    * @param accomType the accomType to set
    */
   public void setAccomType(final String accomType)
   {
      this.accomType = accomType;
   }

}
