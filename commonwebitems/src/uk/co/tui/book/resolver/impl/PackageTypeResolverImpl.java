/**
 *
 */
package uk.co.tui.book.resolver.impl;

import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.resolver.PackageTypeResolver;

/**
 * The Class PackageTypeResolverImpl.
 *
 * @author samantha.gd
 */
public class PackageTypeResolverImpl implements PackageTypeResolver
{

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.book.resolver.PackageTypeResolver#resolve(java.lang.Object)
    */
   @Override
   public PackageType resolve(final Object holiday)
   {
      PackageType packageType = null;
      if (holiday instanceof Holiday)
      {
         packageType = PackageType.INCLUSIVE;
      }
      else if (holiday instanceof PackageItemValue)
      {
         packageType =
            PackageType.valueOfPackageType(((PackageItemValue) holiday).getCruiseSearchVariant());
      }
      return packageType;
   }
}
