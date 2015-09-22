/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author amaresh.d
 *
 */
public final class MediaContentViewData {


    /** The media contents. */
    private List<MediaContents> mediaContents = new ArrayList<MediaContents>();

    /** The small media contents. */
    private MediaContents smallMediaContents;

    /** The medium media contents. */
    private MediaContents mediumMediaContents;

    /** The large media contents. */
    private MediaContents largeMediaContents;

    /** The extra large media contents. */
    private MediaContents extraLargeMediaContents;
    /**
     * Instantiates a new media content view data.
     *
     * @param mediaContents
     *            the media contents
     */
    public MediaContentViewData(List<MediaContents> mediaContents) {
        this.mediaContents = mediaContents;

        populateSmallMediaContent();
        populateMediumMediaContent();
        populateLargeMediaContent();
        populateXtraLargeMediaContent();
    }

    /**
     *  The method to populate XtraLargeMediaContent.
     */
    private void populateXtraLargeMediaContent() {
        List<MediaContents> xLargeMediaContent = getApplicableMedia(MediaContents.MediaTypeFormat.XTRALARGE);
        if (CollectionUtils.isNotEmpty(xLargeMediaContent)) {
            this.extraLargeMediaContents = xLargeMediaContent.get(0);
        }
    }

    /**
     * The method to populate largeMediaContent.
     */
    private void populateLargeMediaContent() {
        List<MediaContents> largeMediaContent = getApplicableMedia(MediaContents.MediaTypeFormat.LARGE);
        if (CollectionUtils.isNotEmpty(largeMediaContent)) {
            this.largeMediaContents = largeMediaContent.get(0);
        }
    }

    /**
     * The method to populate mediumMediaContent.
     */
    private void populateMediumMediaContent() {
        List<MediaContents> mediumMediaContent = getApplicableMedia(MediaContents.MediaTypeFormat.MEDIUM);
        if (CollectionUtils.isNotEmpty(mediumMediaContent)) {
            this.mediumMediaContents = mediumMediaContent.get(0);
        }
    }

    /**
     * The method to populate smallMediaContent.
     */
    private void populateSmallMediaContent() {
        List<MediaContents> smallMediaContent = getApplicableMedia(MediaContents.MediaTypeFormat.SMALL);
        if (CollectionUtils.isNotEmpty(smallMediaContent)) {
            this.smallMediaContents = smallMediaContent.get(0);
        }
    }

    /**
     * Gets all the media contents.
     *
     * @return the mediaContents
     */
    public List<MediaContents> getMediaContents() {
        return mediaContents;
    }

    /**
     * @return the smallMediaContents
     */
    public MediaContents getSmallMediaContents() {
        return smallMediaContents;
    }

    /**
     * @return the mediumMediaContents
     */
    public MediaContents getMediumMediaContents() {
        return mediumMediaContents;
    }

    /**
     * @return the largeMediaContents
     */
    public MediaContents getLargeMediaContents() {
        return largeMediaContents;
    }

    /**
     * @return the extraLargeMediaContents
     */
    public MediaContents getExtraLargeMediaContents() {
        return extraLargeMediaContents;
    }


    /**
     * Gets the applicable media.
     *
     * @param type the type
     * @return the applicable media
     */
    private List<MediaContents> getApplicableMedia(
            final MediaContents.MediaTypeFormat type) {

        return (List<MediaContents>) CollectionUtils.select(this.mediaContents,
                new Predicate() {
                    @Override
                    public boolean evaluate(final Object media) {
                        return MediaContents.class.cast(media)
                                .getMediaFormatType() == type;
                    }
                });
    }

}
