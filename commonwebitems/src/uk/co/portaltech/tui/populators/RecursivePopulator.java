
package uk.co.portaltech.tui.populators;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Interface for a Recursive populator.
 * This populator is need to be used when any recursive population is required. for ex category population
 *
 *
 * @param <S> the type of the source object
 * @param <T> the type of the destination object
 */
public interface RecursivePopulator<S, T>
{
    /**
     * populated the source to target with given depth.
     * @param source
     * @param target
     * @param currDepth
     * @param depthToFetch
     * @throws ConversionException
     */
    void populate(S source, T target, int currDepth, int depthToFetch) throws ConversionException;
}
