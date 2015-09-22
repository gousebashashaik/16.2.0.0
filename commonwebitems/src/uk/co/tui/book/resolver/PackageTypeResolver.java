/**
 *
 */
package uk.co.tui.book.resolver;

import uk.co.tui.book.domain.lite.PackageType;

/**
 * The Interface PackageTypeResolver.
 *
 * @author samantha.gd
 */
public interface PackageTypeResolver {

    /**
     * Resolve.
     *
     * @param holiday
     *            the holiday
     * @return the package type enum
     */
    PackageType resolve(Object holiday);

}
