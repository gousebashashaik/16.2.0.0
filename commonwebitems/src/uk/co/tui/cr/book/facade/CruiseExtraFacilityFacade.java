/**
 *
 */
package uk.co.tui.cr.book.facade;

import java.util.List;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.cr.book.data.ClassActWorkshopsCriteria;
import uk.co.tui.cr.book.data.KidsActivityCriteria;
import uk.co.tui.cr.book.view.data.CruiseOptionsPageViewData;
import uk.co.tui.cr.book.view.data.RoomAndBoardOptionsPageViewData;
import uk.co.tui.cr.book.view.data.RoomOptionsStaticContentViewData;

/**
 * @author samantha.gd
 *
 */
public interface CruiseExtraFacilityFacade
{

   /**
    * @param cruiseOptionsViewData
    *
    */
   void renderCruiseExtras(CruiseOptionsPageViewData cruiseOptionsViewData);

   /**
    * @param criteria
    * @return
    */
   RoomAndBoardOptionsPageViewData removeProductForKidsActivity(List<KidsActivityCriteria> criteria);

   /**
    * @param cruiseOptionsPageViewData
    */
   void populateCabinOptionsStaticContentViewData(
      RoomOptionsStaticContentViewData cabinOptionsStaticContentViewData);

   /**
    * @param cruiseOptionsPageViewData
    */
   void populateCabinOptionsContentViewData(
      RoomAndBoardOptionsPageViewData cruiseOptionsPageViewData);

   /**
    * @param packageModel
    * @param cruiseOptionsViewData
    */
   void populateCruiseOptionsAllView(BasePackage packageModel,
      RoomAndBoardOptionsPageViewData cruiseOptionsViewData);

   /**
    * @param criteria
    * @return
    */
   RoomAndBoardOptionsPageViewData updateProductForKidsActivity(List<KidsActivityCriteria> criteria);

   /**
    * @param workshopsCriteria
    * @return
    */
   RoomAndBoardOptionsPageViewData updateProductForWorkshopOptions(
      List<ClassActWorkshopsCriteria> workshopsCriteria);

   /**
    * @param viewData
    */
   void populateCabinsForWebAnalytics(RoomAndBoardOptionsPageViewData viewData);

}
