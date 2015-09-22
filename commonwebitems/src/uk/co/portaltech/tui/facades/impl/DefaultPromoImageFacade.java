/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.components.model.PromoImageComponentModel;
import uk.co.portaltech.tui.facades.LocationFacade;
import uk.co.portaltech.tui.facades.PromoImageFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.PromoImageComponentData;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author veena.pn
 *
 */
public class DefaultPromoImageFacade implements PromoImageFacade
{

    private static final TUILogUtils LOG = new TUILogUtils("DefaultPromoImageFacade");


    @Resource
    private LocationFacade locationFacade;


    @SuppressWarnings("boxing")
    @Override
    public PromoImageComponentData getLocationData(final PromoImageComponentModel promoImageComponentModel)
    {
        LOG.debug("Entering DefaultPromoImageFacade");
        if (promoImageComponentModel != null && promoImageComponentModel.getVisibleItems() != null)
        {
            final PromoImageComponentData promoImageComponentData = new PromoImageComponentData(
                    promoImageComponentModel.getVisibleItems());
            final int visibleItems = promoImageComponentModel.getVisibleItems() != null ? promoImageComponentModel
                    .getVisibleItems().intValue() : 0;
            final List<LocationData> locationViewDataList = new ArrayList<LocationData>();
            //getting all the location models
            final LocationModel location1 = promoImageComponentModel.getLocation1();
            final LocationModel location2 = promoImageComponentModel.getLocation2();
            final LocationModel location3 = promoImageComponentModel.getLocation3();
            final LocationModel location4 = promoImageComponentModel.getLocation4();
            final LocationModel location5 = promoImageComponentModel.getLocation5();

            final List<LocationModel> locationmodelList = new ArrayList<LocationModel>();

            locationmodelList.add(location1);
            locationmodelList.add(location2);
            locationmodelList.add(location3);
            locationmodelList.add(location4);
            locationmodelList.add(location5);

            if (!locationmodelList.isEmpty())
            {

                for (final LocationModel location : locationmodelList)
                {
                    if (location != null)
                    {
                        //Getting location code for each location entry in list
                        final String locationCode = location.getCode();
                        if (locationCode != null)
                        {
                            final LocationData locationData = locationFacade.getLocationData(locationCode);
                            locationViewDataList.add(locationData);
                        }
                    }
                }
                promoImageComponentData.setLocationViewDataList(locationViewDataList);
                promoImageComponentData.setVisibleItems(visibleItems);
                promoImageComponentData.setPromoImageTitle(promoImageComponentModel.getTitle());
                promoImageComponentData.setPromoImageDescription1(promoImageComponentModel.getPromoImageDescription1());
                promoImageComponentData.setPromoImageDescription2(promoImageComponentModel.getPromoImageDescription2());
                promoImageComponentData.setPromoImageUrl(promoImageComponentModel.getPromoImageUrl());
                promoImageComponentData.setPosition(promoImageComponentModel.getPosition().getCode());
                promoImageComponentData.setWidth(promoImageComponentModel.getWidth().getCode());

                LOG.debug("End of DefaultPromoImageFacade");
                return promoImageComponentData;
            }
            LOG.debug("End of DefaultPromoImageFacade");

            return promoImageComponentData;
        }
        else
        {
            LOG.debug("promoImageComponentModel or visible items may be null");

            return new PromoImageComponentData();
        }

    }
}
