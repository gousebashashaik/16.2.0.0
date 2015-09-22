/*
 * Copyright 2010 Portaltech.
 * 
 * Originating Unit: Portal Technology Systems Ltd. http://www.portaltech.co.uk
 * 
 * Please contact authors regarding licensing and redistribution.
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.core.model.ItemModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchRequestData
{

   /*
    * You can set either page or offset depending if you want pagination, or just want to choose a
    * specific subset. Count is always required.
    */
   private int page;

   private int offset;

   private int count;

   private String categoryCode;

   private String componentReference;

   private String lookUpType;

   @JsonIgnore
   private ItemModel relevantItem;

   private String productCode;

   private String quickSearchParameter;

   private String pageType;

   private String seoPageType;

   private String sortBy;

   private List<String> facetOptionNames;

   private List<String> filterParams;

   private boolean isBreadCrumbRequired;

   private boolean tabbedResult;

   private String accommodationTypeContext;

   private String nonCoreHolidayType = StringUtils.EMPTY;

   private Map<String, String> additionalParams = new HashMap<String, String>();

   private List<String> productCodes;

   public SearchRequestData()
   {
      this.page = 1;
      this.offset = 0;
      this.count = 1;
   }

   /**
    * @param categoryCode
    * @param pageType
    * @param seoPageType
    * @param componentReference
    * @param lookUpType
    * @param relevantItem
    */
   public SearchRequestData(final String categoryCode, final String pageType,
                            final String seoPageType, final ItemModel relevantItem)
   {
      super();
      this.categoryCode = categoryCode;
      this.pageType = pageType;
      this.seoPageType = seoPageType;
      this.relevantItem = relevantItem;
   }

   /**
    * @return the productCodes
    */
   public List<String> getProductCodes()
   {
      return productCodes;
   }

   /**
    * @param productCodes the productCodes to set
    */
   public void setProductCodes(final List<String> productCodes)
   {
      this.productCodes = productCodes;
   }

   /**
    * @return the nonCoreHolidayType
    */
   public String getNonCoreHolidayType()
   {
      return nonCoreHolidayType;
   }

   /**
    * @param nonCoreHolidayType the nonCoreHolidayType to set
    */
   public void setNonCoreHolidayType(final String nonCoreHolidayType)
   {
      this.nonCoreHolidayType = nonCoreHolidayType;
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

   public int getPage()
   {
      return page;
   }

   public void setPage(final int page)
   {
      this.page = page;
      this.offset = (page - 1) * count;
   }

   public int getPageSize()
   {
      return count;
   }

   public void setPageSize(final int pageSize)
   {
      this.count = pageSize;
      this.offset = (page - 1) * count;
   }

   public int getOffset()
   {
      return offset;
   }

   public void setOffset(final int offset)
   {
      this.offset = offset;
   }

   public ItemModel getRelevantItem()
   {
      return relevantItem;
   }

   public void setRelevantItem(final ItemModel relevantItem)
   {
      this.relevantItem = relevantItem;
   }

   /**
    * @return the categoryCode
    */
   public String getCategoryCode()
   {
      return categoryCode;
   }

   /**
    * @param categoryCode the locationCode to set
    */
   public void setCategoryCode(final String categoryCode)
   {
      this.categoryCode = categoryCode;
   }

   /**
    * @return the productCode
    */
   public String getProductCode()
   {
      return productCode;
   }

   /**
    * @param productCode the productCode to set
    */
   public void setProductCode(final String productCode)
   {
      this.productCode = productCode;
   }

   /**
    * @return the componentReference
    */
   public String getComponentReference()
   {
      return componentReference;
   }

   /**
    * @param componentReference the componentReference to set
    */
   public void setComponentReference(final String componentReference)
   {
      this.componentReference = componentReference;
   }

   /**
    * @return the quickSearchParameter
    */
   public String getQuickSearchParameter()
   {
      return quickSearchParameter;
   }

   /**
    * @param quickSearchParameter the quickSearchParameter to set
    */
   public void setQuickSearchParameter(final String quickSearchParameter)
   {
      this.quickSearchParameter = quickSearchParameter;
   }

   /**
    * @return the lookUpType
    */
   public String getLookUpType()
   {
      return lookUpType;
   }

   /**
    * @param lookUpType the lookUpType to set
    */
   public void setLookUpType(final String lookUpType)
   {
      this.lookUpType = lookUpType;
   }

   /**
    * @return the pageType
    */
   public String getPageType()
   {
      return pageType;
   }

   /**
    * @param pageType the pageType to set
    */
   public void setPageType(final String pageType)
   {
      this.pageType = pageType;
   }

   /**
    * @return the seoPageType
    */
   public String getSeoPageType()
   {
      return seoPageType;
   }

   /**
    * @param seoPageType the seoPageType to set
    */
   public void setSeoPageType(final String seoPageType)
   {
      this.seoPageType = seoPageType;
   }

   /**
    * @return the facetOptionNames
    */
   public List<String> getFacetOptionNames()
   {
      return facetOptionNames;
   }

   /**
    * @param facetOptionNames the facetOptionNames to set
    */
   public void setFacetOptionNames(final List<String> facetOptionNames)
   {
      this.facetOptionNames = facetOptionNames;
   }

   /**
    * @return the filterParams
    */
   public List<String> getFilterParams()
   {
      return filterParams;
   }

   /**
    * @param filterParams the filterParams to set
    */
   public void setFilterParams(final List<String> filterParams)
   {
      this.filterParams = filterParams;
   }

   /**
    * @return the isBreadCrumbRequired
    */
   public boolean isBreadCrumbRequired()
   {
      return isBreadCrumbRequired;
   }

   /**
    * @param isBreadCrumbRequired the isBreadCrumbRequired to set
    */
   public void setBreadCrumbRequired(final boolean isBreadCrumbRequired)
   {
      this.isBreadCrumbRequired = isBreadCrumbRequired;
   }

   /**
    * @return the tabbedResult
    */
   public boolean isTabbedResult()
   {
      return tabbedResult;
   }

   /**
    * @param tabbedResult the tabbedResult to set
    */
   public void setTabbedResult(final boolean tabbedResult)
   {
      this.tabbedResult = tabbedResult;
   }

   /**
    * @return the accommodationTypeContext
    */
   public String getAccommodationTypeContext()
   {
      return accommodationTypeContext;
   }

   /**
    * @param accommodationTypeContext the accommodationTypeContext to set
    */
   public void setAccommodationTypeContext(final String accommodationTypeContext)
   {
      this.accommodationTypeContext = accommodationTypeContext;
   }

   /**
    * @return the additionalParams
    */
   public Map<String, String> getAdditionalParams()
   {
      return additionalParams;
   }

   /**
    * @param additionalParams the additionalParams to set
    */
   public void setAdditionalParams(final Map<String, String> additionalParams)
   {
      this.additionalParams = additionalParams;
   }

   /**
    * This method has been overridden to generate unique hash code for EHCache implementation for
    * endeca browse component.
    */
   @Override
   public int hashCode()
   {

      return generateHashCode();
   }

   /**
    * @return the count
    */
   public int getCount()
   {
      return count;
   }

   @Override
   public boolean equals(final Object obj)
   {
      return super.equals(obj);
   }

   /**
    * @param count the count to set
    */
   public void setCount(final int count)
   {
      this.count = count;
   }

   /**
    * Generate Hash code
    * 
    * @return int generated has code
    */
   private int generateHashCode()
   {
      final HashCodeBuilder hashBuilder = new HashCodeBuilder();
      hashBuilder.append(this.page);
      hashBuilder.append(this.offset);
      hashBuilder.append(this.count);
      if (!StringUtils.isEmpty(categoryCode))
      {
         hashBuilder.append(this.categoryCode);
      }
      if (!StringUtils.isEmpty(componentReference))
      {
         hashBuilder.append(this.componentReference);
      }
      if (!StringUtils.isEmpty(lookUpType))
      {
         hashBuilder.append(this.lookUpType);
      }
      if (relevantItem != null)
      {
         hashBuilder.append(this.relevantItem.getPk().getLongValue());
      }
      if (!StringUtils.isEmpty(productCode))
      {
         hashBuilder.append(this.productCode);
      }
      if (!StringUtils.isEmpty(quickSearchParameter))
      {
         hashBuilder.append(this.quickSearchParameter);
      }
      if (!StringUtils.isEmpty(pageType))
      {
         hashBuilder.append(this.pageType);
      }
      if (!StringUtils.isEmpty(seoPageType))
      {
         hashBuilder.append(this.seoPageType);
      }
      if (!StringUtils.isEmpty(sortBy))
      {
         hashBuilder.append(this.sortBy);
      }

      updateFacetHashCode(hashBuilder);

      hashBuilder.append(this.isBreadCrumbRequired);
      hashBuilder.append(this.tabbedResult);
      hashBuilder.append(this.accommodationTypeContext);
      hashBuilder.append(this.additionalParams);
      return hashBuilder.toHashCode();
   }

   /**
    * update HashCode - for Facets
    * 
    * @param hashBuilder
    */
   private void updateFacetHashCode(final HashCodeBuilder hashBuilder)
   {
      if (facetOptionNames != null && !facetOptionNames.isEmpty())
      {
         for (final String facetname : facetOptionNames)
         {
            if (!StringUtils.isEmpty(facetname))
            {
               hashBuilder.append(facetname);
            }
         }
      }
      if (facetOptionNames != null && !facetOptionNames.isEmpty())
      {
         for (final String filterParam : filterParams)
         {
            if (!StringUtils.isEmpty(filterParam))
            {
               hashBuilder.append(filterParam);
            }
         }
      }
   }

}
