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
public class AtcomInventoryPackagePopulator implements Populator<Holiday, BasePackage>
{

    /**
     * This method populates the atcom specific details to the bookflow packagemodel
     */
    @Override
    public void populate(final Holiday source, final BasePackage target) throws ConversionException
    {
        populateInventoryDetails(source.getInventoryInfo(), target);
    }

    /**
     * To set the atcom details
     *
     * @param tracsDetails
     * @param target
     */
    private void populateInventoryDetails(final InventoryInfo tracsDetails, final BasePackage target)
    {
        final SupplierProductDetails supplierDetails = new SupplierProductDetails();
        supplierDetails.setPromoCode(tracsDetails.getProm());
        supplierDetails.setProductCode(tracsDetails.getAtcomId());
        supplierDetails.setSellingCode(tracsDetails.getSellingCode());
        supplierDetails.setSupplierNumber(null);
        target.getInventory().setSupplierProductDetails(supplierDetails);
    }

}
