package gpse.example.domain.cmds;

import gpse.example.domain.entities.Review;

import java.util.List;

/**
 * Command object for review objects.
 * Encapsulates all relevant information needed by api action.
 */
public class ReviewCmd {
    private List<Review.RevList> listReviews;
    private List<Review.RevFormatting> formattingReviews;
    private List<Review.RevImage> imageReviews;
    private List<Review.RevVideo> videoReviews;
    private List<Review.RevLinks> linkReviews;
    private List<Review.AutomaticReview> automaticReviews;
    private Integer reviewPosition;
    private Boolean reviewComplete;

    public Integer getReviewPosition() {
        return reviewPosition; }

    public void setReviewPosition(final Integer reviewPosition) {
        this.reviewPosition = reviewPosition; }

    public Boolean getReviewComplete() {
        return reviewComplete; }

    public void setReviewComplete(final Boolean reviewComplete) {
        this.reviewComplete = reviewComplete; }

    public List<Review.RevList> getListReviews() {
        return listReviews;
    }

    public void setListReviews(final List<Review.RevList> listReviews) {
        this.listReviews = listReviews;
    }

    public List<Review.RevFormatting> getFormattingReviews() {
        return formattingReviews;
    }

    public void setFormattingReviews(final List<Review.RevFormatting> formattingReviews) {
        this.formattingReviews = formattingReviews;
    }


    public List<Review.RevImage> getImageReviews() {
        return imageReviews;
    }

    public void setImageReviews(final List<Review.RevImage> imageReviews) {
        this.imageReviews = imageReviews;
    }

    public List<Review.RevVideo> getVideoReviews() {
        return videoReviews;
    }

    public void setVideoReviews(final List<Review.RevVideo> videoReviews) {
        this.videoReviews = videoReviews;
    }

    public List<Review.RevLinks> getLinkReviews() {
        return linkReviews; }

    public void setLinkReviews(final List<Review.RevLinks> linkReviews) {
        this.linkReviews = linkReviews; }

    public List<Review.AutomaticReview> getAutomaticReviews() {
        return automaticReviews;
    }

    public void setAutomaticReviews(final List<Review.AutomaticReview> automaticReviews) {
        this.automaticReviews = automaticReviews;
    }
}
