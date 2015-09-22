/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MerchandiserRequest
{

   private String pageLabel;

   private String inventoryLabel;

   private String pageName = StringUtils.EMPTY;

   private String merchandiseType;

   private boolean categorizedPage;

   private int first;

   private int offset;

   private static final int NUM_PRIME_31 = 31;

   private static final int NUM_PRIME_17 = 17;

   /**
    * holds the searchRequestType "sort" or "filterPanel"
    */
   private String searchRequestType;

   private String sortBy;

   private IGFilterRequest filters;

   /**
    * @return the pageLabel
    */
   public String getPageLabel()
   {
      return pageLabel;
   }

   /**
    * @param pageLabel the pageLabel to set
    */
   public void setPageLabel(final String pageLabel)
   {
      this.pageLabel = pageLabel;
   }

   /**
    * @return the inventoryLabel
    */
   public String getInventoryLabel()
   {
      return inventoryLabel;
   }

   /**
    * @param inventoryLabel the inventoryLabel to set
    */
   public void setInventoryLabel(final String inventoryLabel)
   {
      this.inventoryLabel = inventoryLabel;
   }

   /**
    * @return the merchandiseType
    */
   public String getMerchandiseType()
   {
      return merchandiseType;
   }

   /**
    * @param merchandiseType the merchandiseType to set
    */
   public void setMerchandiseType(final String merchandiseType)
   {
      this.merchandiseType = merchandiseType;
   }

   /**
    * @return the searchRequestType
    */
   public String getSearchRequestType()
   {
      return searchRequestType;
   }

   /**
    * @param searchRequestType the searchRequestType to set
    */
   public void setSearchRequestType(final String searchRequestType)
   {
      this.searchRequestType = searchRequestType;
   }

   /**
    * @return the sortBy
    */
   public String getSortBy()
   {
      return sortBy;
   }

   /**
    * @param sortBy the sortBy to set
    */
   public void setSortBy(final String sortBy)
   {
      this.sortBy = sortBy;
   }

   /**
    * @return the first
    */
   public int getFirst()
   {
      return first;
   }

   /**
    * @param first the first to set
    */
   public void setFirst(final int first)
   {
      this.first = first;
   }

   /**
    * @return the offset
    */
   public int getOffset()
   {
      return offset;
   }

   /**
    * @param offset the offset to set
    */
   public void setOffset(final int offset)
   {
      this.offset = offset;
   }

   /**
    * @return the categorizedPage
    */
   public boolean isCategorizedPage()
   {
      return categorizedPage;
   }

   /**
    * @param categorizedPage the categorizedPage to set
    */
   public void setCategorizedPage(final boolean categorizedPage)
   {
      this.categorizedPage = categorizedPage;
   }

   @Override
   public int hashCode()
   {

      return generateHashCode();
   }

   /**
    *
    * * @return
    */
   private int generateHashCode()
   {
      final HashCodeBuilder hashBuilder = new HashCodeBuilder();

      hashBuilder.append(this.first);
      hashBuilder.append(this.pageLabel);
      // Generating unique based on prime number combination
      return NUM_PRIME_31 * NUM_PRIME_17 + hashBuilder.toHashCode();
   }

   @Override
   public boolean equals(final Object obj)
   {
      return super.equals(obj);
   }

   /**
    * @return the filters
    */
   public IGFilterRequest getFilters()
   {
      return filters;
   }

   /**
    * @param filters the filters to set
    */
   public void setFilters(final IGFilterRequest filters)
   {
      this.filters = filters;
   }

   /**
    * @return the pageName
    */
   public String getPageName()
   {
      return pageName;
   }

   /**
    * @param pageName the pageName to set
    */
   public void setPageName(final String pageName)
   {
      this.pageName = pageName;
   }

}
