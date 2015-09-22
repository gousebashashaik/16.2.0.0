/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author niranjani.r
 *
 */
public class CollectionGroupTabData {

    /**
     * All Collection Group Data Along With product range data.
     */
    private List<CollectionGroupViewData> collectionGroupData;

    /**
     * All Collection  Data .
     */
    private List<CollectionViewData> allCollectionData;

    /**
     * @return the collectionGroupData
     */
    public List<CollectionGroupViewData> getCollectionGroupData() {
        return collectionGroupData;
    }

    /**
     * @param collectionGroupData the collectionGroupData to set
     */
    public void setCollectionGroupData(
            List<CollectionGroupViewData> collectionGroupData) {
        this.collectionGroupData = collectionGroupData;
    }

    /**
     * @return the allCollectionData
     */
    public List<CollectionViewData> getAllCollectionData() {
        return allCollectionData;
    }

    /**
     * @param allCollectionData the allCollectionData to set
     */
    public void setAllCollectionData(List<CollectionViewData> allCollectionData) {
        this.allCollectionData = allCollectionData;
    }

}
