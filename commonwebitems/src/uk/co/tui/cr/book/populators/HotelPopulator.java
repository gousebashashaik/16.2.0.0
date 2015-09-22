/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.results.InventoryDetails;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.MultiCenterUnits;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.domain.lite.SupplierProductDetails;

/**
 * @author ramkishore.p
 *
 */
public class HotelPopulator implements Populator<PackageItemValue, Stay> {

    /** The Constant INDEX_ZERO. */
    private static final int INDEX_ZERO = 0;

    private static final String HOTEL_STAY = "ACC";

    /** The Product Service . */
    @Resource
    private ProductService productService;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final PackageItemValue source, final Stay stay)
            throws ConversionException {
        final List<MultiCenterUnits> multiCentreData = source
                .getPckSailingInfo().get(INDEX_ZERO).getMulitCenterUnits();
        final MultiCenterUnits sourceAccom = getAccom(multiCentreData,
                HOTEL_STAY);
        // To be set to Package
        stay.setCode(sourceAccom.getId());
        stay.setDuration(sourceAccom.getStay());
        stay.setStartDate(sourceAccom.getStartDate().toDate());
        stay.setEndDate(populateAccomEndDate(sourceAccom));
        populateStayType(sourceAccom, stay);
        populateInventoryDetails(
                getAccom(
                        source.getPckSailingInfo().get(0).getMulitCenterUnits(),
                        HOTEL_STAY).getInventoryDetails(), stay);
    }

    /**
     * @param sourceAccom
     * @param stay
     */
    private void populateStayType(final MultiCenterUnits sourceAccom,
            final Stay stay) {
        final AccommodationModel accommodationModel = (AccommodationModel) productService
                .getProductForCode(sourceAccom.getId());
        stay.setType(StayType.valueOf(accommodationModel.getType().toString()));
    }

    /**
     * This method adds duration with destinations arrival date.
     *
     * @param sourceAccom
     *            the package sailing info
     * @return the date
     */
    private Date populateAccomEndDate(final MultiCenterUnits sourceAccom) {
        return sourceAccom.getStartDate().plusDays(sourceAccom.getStay())
                .toDate();
    }

    /**
     * @param multiCentreData
     * @return Accom
     */
    private MultiCenterUnits getAccom(
            final List<MultiCenterUnits> multiCentreData, final String stayType) {
        MultiCenterUnits sourceAccom = null;
        for (final MultiCenterUnits multiCentreDatum : multiCentreData) {
            if (StringUtils.equalsIgnoreCase(stayType,
                    multiCentreDatum.getStayType())) {
                sourceAccom = multiCentreDatum;
                break;
            }
        }
        return sourceAccom;
    }

    /**
     * To set the atcom details.
     *
     * @param inventoryDetails
     *            the package item value
     * @param target
     *            the target
     */
    private void populateInventoryDetails(
            final InventoryDetails inventoryDetails, final Stay target) {
        final SupplierProductDetails supplierDetails = new SupplierProductDetails();
        supplierDetails.setPromoCode(inventoryDetails.getProm());
        supplierDetails.setProductCode(inventoryDetails.getAtcomId());
        supplierDetails.setSellingCode(inventoryDetails.getSellingCode());
        supplierDetails.setSupplierNumber(null);
        target.setSupplierProductDetails(supplierDetails);
    }

}
