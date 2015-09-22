/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.tui.book.page.response.PageResponse;

/**
 * The Class RoomAndBoardOptionsPageViewData.
 *
 * @author munisekhar.k
 */
public class RoomAndBoardOptionsPageViewData
{

   /**
    * Holds the data specific to summary panel.
    */
   private SummaryPanelViewData summaryViewData;

   /**
    * Holds the Package Relevant View Data.
    */
   private PackageViewData packageViewData;

   /**
    * including portaltech accom data as a temporary fix to reuse in room type and all inclusive
    * section of room options page.
    */
   private AccommodationViewData accomViewData;

   /**
    * Container for the all the extra facility.
    */
   private ExtraFacilityViewDataContainer extraFacilityViewDataContainer;

   /** The room options view data. */
   private List<RoomOptionsViewData> roomOptionsViewData;

   /** The alert messages. */
   private List<AlertViewData> alertMessages;

   /** The list of board basis. */
   private List<BoardBasisViewData> alternateBoardBasis;

   /** The RoomOptionsContentViewData. */
   private RoomOptionsContentViewData roomOptionsContentViewData;

   /** The room options static content view data. */
   private RoomOptionsStaticContentViewData roomOptionsStaticContentViewData;

   /** The RoomOptionsContentViewData. */
   private boolean villaAcommodation;

   /** The session time interval. */
   private int sessionTimePeriod;

   /** The package type. */
   private String packageType;

   private PageResponse pageResponse;

   private boolean enableBoardBasisComponent = true;

   /**
    * @return the packageType
    */
   public String getPackageType()
   {
      return packageType;
   }

   /**
    * @param packageType the packageType to set
    */
   public void setPackageType(final String packageType)
   {
      this.packageType = packageType;
   }

   /**
    * Gets the alert messages.
    *
    * @return the alert messages
    */
   public List<AlertViewData> getAlertMessages()
   {
      return alertMessages;
   }

   /**
    * Sets the alert messages.
    *
    * @param alertMessages the new alert messages
    */
   public void setAlertMessages(final List<AlertViewData> alertMessages)
   {
      this.alertMessages = alertMessages;
   }

   /**
    * Gets the package view data.
    *
    * @return the packageViewData
    */
   public PackageViewData getPackageViewData()
   {
      if (packageViewData == null)
      {
         packageViewData = new PackageViewData();
      }
      return packageViewData;
   }

   /**
    * Sets the package view data.
    *
    * @param packageViewData the packageViewData to set
    */
   public void setPackageViewData(final PackageViewData packageViewData)
   {
      this.packageViewData = packageViewData;
   }

   /**
    * Return the string representation of the object.
    *
    * @return the string representation of the object.
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return ToStringBuilder.reflectionToString(this);
   }

   /**
    * Gets the summary view data.
    *
    * @return the summaryViewData
    */
   public SummaryPanelViewData getSummaryViewData()
   {
      if (summaryViewData == null)
      {
         summaryViewData = new SummaryPanelViewData();
      }
      return summaryViewData;
   }

   /**
    * Sets the summary view data.
    *
    * @param summaryViewData the summaryViewData to set
    */
   public void setSummaryViewData(final SummaryPanelViewData summaryViewData)
   {
      this.summaryViewData = summaryViewData;
   }

   /**
    * Gets the accom view data.
    *
    * @return the accomViewData
    */
   public AccommodationViewData getAccomViewData()
   {
      if (accomViewData == null)
      {
         accomViewData = new AccommodationViewData();
      }
      return accomViewData;
   }

   /**
    * Sets the accom view data.
    *
    * @param accomViewData the accomViewData to set
    */
   public void setAccomViewData(final AccommodationViewData accomViewData)
   {
      this.accomViewData = accomViewData;
   }

   /**
    * Gets the extraFacilityViewDataContainer.
    *
    * @return the extraFacilityViewDataContainer
    */
   public ExtraFacilityViewDataContainer getExtraFacilityViewDataContainer()
   {
      return extraFacilityViewDataContainer;
   }

   /**
    * Sets the extraFacilityViewDataContainer.
    *
    * @param extraFacilityViewDataContainer the extraFacilityViewDataContainer to set
    */
   public void setExtraFacilityViewDataContainer(
      final ExtraFacilityViewDataContainer extraFacilityViewDataContainer)
   {
      this.extraFacilityViewDataContainer = extraFacilityViewDataContainer;
   }

   /**
    * Gets the room options view data.
    *
    * @return the room options view data
    */
   public List<RoomOptionsViewData> getRoomOptionsViewData()
   {
      return roomOptionsViewData;
   }

   /**
    * Sets the room options view data.
    *
    * @param roomOptionsViewData the new room options view data
    */
   public void setRoomOptionsViewData(final List<RoomOptionsViewData> roomOptionsViewData)
   {
      this.roomOptionsViewData = roomOptionsViewData;
   }

   /**
    * @return the sessionTimePeriod
    */
   public int getSessionTimePeriod()
   {
      return sessionTimePeriod;
   }

   /**
    * @param sessionTimePeriod the sessionTimePeriod to set
    */
   public void setSessionTimePeriod(final int sessionTimePeriod)
   {
      this.sessionTimePeriod = sessionTimePeriod;
   }

   /**
    * Gets the alternate board basis.
    *
    * @return the alternate board basis
    */
   public List<BoardBasisViewData> getAlternateBoardBasis()
   {
      return alternateBoardBasis;
   }

   /**
    * Sets the alternate board basis.
    *
    * @param alternateBoardBasis the new alternate board basis
    */
   public void setAlternateBoardBasis(final List<BoardBasisViewData> alternateBoardBasis)
   {
      this.alternateBoardBasis = alternateBoardBasis;
   }

   /**
    * Gets the roomOptionsContentViewData.
    *
    * @return the roomOptionsContentViewData.
    */
   public RoomOptionsContentViewData getRoomOptionsContentViewData()
   {
      return roomOptionsContentViewData;
   }

   /**
    * Sets the roomOptionsContentViewData.
    *
    * @param roomOptionsContentViewData the roomOptionsContentViewData
    */
   public void setRoomOptionsContentViewData(
      final RoomOptionsContentViewData roomOptionsContentViewData)
   {
      this.roomOptionsContentViewData = roomOptionsContentViewData;
   }

   /**
    * @param villaAcommodation the villaAcommodation to set
    */
   public void setVillaAcommodation(final boolean villaAcommodation)
   {
      this.villaAcommodation = villaAcommodation;
   }

   /**
    * @return the villaAcommodation
    */
   public boolean isVillaAcommodation()
   {
      return villaAcommodation;
   }

   /**
    * @return the roomOptionsStaticContentViewData
    */
   public RoomOptionsStaticContentViewData getRoomOptionsStaticContentViewData()
   {
      return roomOptionsStaticContentViewData;
   }

   /**
    * @param roomOptionsStaticContentViewData the roomOptionsStaticContentViewData to set
    */
   public void setRoomOptionsStaticContentViewData(
      final RoomOptionsStaticContentViewData roomOptionsStaticContentViewData)
   {
      this.roomOptionsStaticContentViewData = roomOptionsStaticContentViewData;
   }

   /**
    * @return the pageResponse
    */
   public PageResponse getPageResponse()
   {
      return pageResponse;
   }

   /**
    * @param pageResponse the pageResponse to set
    */
   public void setPageResponse(final PageResponse pageResponse)
   {
      this.pageResponse = pageResponse;
   }

   /**
    * @return the enableBoardBasisComponent
    */
   public boolean isEnableBoardBasisComponent()
   {
      return enableBoardBasisComponent;
   }

   /**
    * @param enableBoardBasisComponent the enableBoardBasisComponent to set
    */
   public void setEnableBoardBasisComponent(final boolean enableBoardBasisComponent)
   {
      this.enableBoardBasisComponent = enableBoardBasisComponent;
   }

}
