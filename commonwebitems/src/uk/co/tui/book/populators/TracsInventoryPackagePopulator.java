/**
 *
 */
package uk.co.tui.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.model.results.InventoryInfo;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.SupplierProductDetails;


/**
 * @author anithamani.s
 *
 */
public class TracsInventoryPackagePopulator implements Populator<Holiday, BasePackage>
{



    /**
     * This method populates the tracs specific details.
     *
     */
    @Override
    public void populate(final Holiday source, final BasePackage target) throws ConversionException
    {
        populateInventoryDetails(source.getInventoryInfo(), target);
    }

    /**
     * Populate tracs details.
     *
     * @param tracsDetails
     *           the tracs details
     * @param target
     *           the target
     */
    private void populateInventoryDetails(final InventoryInfo tracsDetails, final BasePackage target)
    {
        final SupplierProductDetails supplierDetails = new SupplierProductDetails();
        supplierDetails.setProductCode(tracsDetails.getProductCode());
        supplierDetails.setSubProductcode(tracsDetails.getSubProductCode());
        supplierDetails.setPrimeSubProductCode(tracsDetails.getPrimeSubProductCode());
        supplierDetails.setSellingCode(tracsDetails.getSellingCode());
        supplierDetails.setSupplierNumber(tracsDetails.getTracsPackageId().substring(1));
        supplierDetails.setPromoCode(supplierDetails.getProductCode());
        target.getInventory().setSupplierProductDetails(supplierDetails);
    }

}
