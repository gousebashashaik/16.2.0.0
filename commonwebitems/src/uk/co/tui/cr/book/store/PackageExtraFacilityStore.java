/**
 *
 */
package uk.co.tui.cr.book.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.portaltech.commons.SyntacticSugar;
import uk.co.tui.book.domain.PackageExtraFacilityResponse;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;

/**
 * @author pushparaja.g
 *
 */
public class PackageExtraFacilityStore
{
   private boolean cruiseExtraSet;

   private Map<String, PackageExtraFacilityResponse> packageExtraFacilityResponse =
      new HashMap<String, PackageExtraFacilityResponse>();

   /**
    * Flushes the data stored in the store.
    */
   public void flush()
   {
      this.packageExtraFacilityResponse = null;
   }

   /**
    * Gets the package extra facility response.
    *
    * @return the package extra facility response
    */
   private Map<String, PackageExtraFacilityResponse> getPackageExtraFacilityResponse()
   {
      return packageExtraFacilityResponse;
   }

   /**
    * Adds the extra facility response specific to a package on to the store.
    *
    * @param packageExtraFacilityResponse the package extra facility response
    */
   public void add(final PackageExtraFacilityResponse packageExtraFacilityResponse)
   {
      this.getPackageExtraFacilityResponse().put(packageExtraFacilityResponse.getPackageId(),
         packageExtraFacilityResponse);
   }

   /**
    * Gets the extra facility category list for the supplied package code from the store.
    *
    * @param packageCode the package code
    * @return the extra facility
    */
   public List<ExtraFacilityCategory> getExtraFacilityLite(final String packageCode)
   {
      if (SyntacticSugar.isNotNull(this.getPackageExtraFacilityResponse().get(packageCode)))
      {
         return this.getPackageExtraFacilityResponse().get(packageCode)
            .getExtraFacilityCategoryList();
      }
      return Collections.emptyList();
   }

   /**
    * @return the cruiseExtraSet
    */
   public boolean isCruiseExtraSet()
   {
      return cruiseExtraSet;
   }

   /**
    * @param cruiseExtraSet the cruiseExtraSet to set
    */
   public void setCruiseExtraSet(final boolean cruiseExtraSet)
   {
      this.cruiseExtraSet = cruiseExtraSet;
   }
}
