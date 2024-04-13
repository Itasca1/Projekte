package gpse.example.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model representation for a review.
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private Boolean reviewComplete;
    @Column
    private Integer reviewPosition;
    @ElementCollection
    private List<RevList> listReviews = new ArrayList<>();
    @ElementCollection
    private List<RevFormatting> formattingReviews = new ArrayList<>();
    @ElementCollection
    private List<RevImage> imageReviews = new ArrayList<>();
    @ElementCollection
    private List<RevVideo> videoReviews = new ArrayList<>();
    @ElementCollection
    private List<RevLinks> linkReviews = new ArrayList<>();
    @ElementCollection
    private List<AutomaticReview> automaticReviews = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boolean getReviewComplete() {
        return reviewComplete;
    }

    public void setReviewComplete(final Boolean reviewComplete) {
        this.reviewComplete = reviewComplete;
    }

    public Integer getReviewPosition() {
        return reviewPosition;
    }

    public void setReviewPosition(final Integer reviewPosition) {
        this.reviewPosition = reviewPosition;
    }

    public List<RevList> getListReviews() {
        return listReviews;
    }

    public void setListReviews(final List<RevList> listReviews) {
        this.listReviews = listReviews;
    }

    public List<RevFormatting> getFormattingReviews() {
        return formattingReviews;
    }

    public void setFormattingReviews(final List<RevFormatting> formattingReviews) {
        this.formattingReviews = formattingReviews;
    }

    public List<RevImage> getImageReviews() {
        return imageReviews;
    }

    public void setImageReviews(final List<RevImage> imageReviews) {
        this.imageReviews = imageReviews;
    }

    public List<RevVideo> getVideoReviews() {
        return videoReviews;
    }

    public void setVideoReviews(final List<RevVideo> videoReviews) {
        this.videoReviews = videoReviews;
    }

    public List<RevLinks> getLinkReviews() {
        return linkReviews;
    }

    public void setLinkReviews(final List<RevLinks> linkReviews) {
        this.linkReviews = linkReviews;
    }

    public List<AutomaticReview> getAutomaticReviews() {
        return automaticReviews;
    }

    public void setAutomaticReviews(final List<AutomaticReview> automaticReviews) {
        this.automaticReviews = automaticReviews;
    }

    /**
     * Calculates the score for the review.
     *
     * @return the score
     */
    @SuppressWarnings({"checkstyle:magicnumber", "PMD"})
    public int getScore() {
        final String incorrect = "incorrect";
        final int overallAmount = listReviews.size() + formattingReviews.size() + linkReviews.size()
            + imageReviews.size() * 2 + videoReviews.size() * 2 + automaticReviews.size();
        int amountCorrect = 0;
        for (final RevList listReview : listReviews) {
            if (!listReview.getValue().equals(incorrect)) {
                amountCorrect++;
            }
        }
        for (final RevFormatting formattingReview : formattingReviews) {
            if (!formattingReview.getValue().equals(incorrect)) {
                amountCorrect++;
            }
        }
        for (final RevLinks linkReview : linkReviews) {
            if (!linkReview.getValue().equals(incorrect)) {
                amountCorrect++;
            }
        }
        for (final RevImage imageReview : imageReviews) {
            if (!imageReview.getAltValue().equals(incorrect)) {
                amountCorrect++;
            }
            if (!imageReview.getTxtValue().equals(incorrect)) {
                amountCorrect++;
            }
        }
        for (final RevVideo videoReview : videoReviews) {
            if (!videoReview.getAdValue().equals(incorrect)) {
                amountCorrect++;
            }
            if (!videoReview.getVidValue().equals(incorrect)) {
                amountCorrect++;
            }
        }
        for (final AutomaticReview automaticReview : automaticReviews) {
            if (automaticReview.isPassed()) {
                amountCorrect++;
            }
        }
        int score = 0;
        if (overallAmount != 0) {
            score = Math.round(((float) amountCorrect) / overallAmount * 100);
        }
        return score;
    }

    /**
     * Container object including the results of the review for a specific list.
     */
    @Embeddable
    public static class RevList {
        @Lob
        private String listHtml;
        @Column
        private String value;

        protected RevList() {
        }

        public String getListHtml() {
            return listHtml;
        }

        public void setListHtml(final String listHtml) {
            this.listHtml = listHtml;
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }
    }

    /**
     * Container object including the results of the review for a specific formatted text.
     */
    @Embeddable
    public static class RevFormatting {
        @Lob
        private String formattingHtml;
        @Column
        private String value;

        protected RevFormatting() {
        }

        public String getFormattingHtml() {
            return formattingHtml;
        }

        public void setFormattingHtml(final String formattingHtml) {
            this.formattingHtml = formattingHtml;
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }
    }

    /**
     * Container object including the results of the review for a specific image.
     */
    @Embeddable
    public static class RevImage {
        @Column
        private String altValue;
        @Column
        private String txtValue;
        @Lob
        private String imgValue;

        protected RevImage() {
        }

        public String getAltValue() {
            return altValue;
        }

        public void setAltValue(final String altValue) {
            this.altValue = altValue;
        }

        public String getTxtValue() {
            return txtValue;
        }

        public void setTxtValue(final String txtValue) {
            this.txtValue = txtValue;
        }

        public String getImgValue() {
            return imgValue;
        }

        public void setImgValue(final String imgValue) {
            this.imgValue = imgValue;
        }
    }

    /**
     * Container object including the results of the review for a specific video.
     */
    @Embeddable
    public static class RevVideo {
        @Column
        private String adValue;
        @Column
        private String vidValue;
        @Lob
        private String vidObject;

        protected RevVideo() {
        }

        public String getAdValue() {
            return adValue;
        }

        public void setAdValue(final String adValue) {
            this.adValue = adValue;
        }

        public String getVidValue() {
            return vidValue;
        }

        public void setVidValue(final String vidValue) {
            this.vidValue = vidValue;
        }

        public String getVidObject() {
            return vidObject;
        }

        public void setVidObject(final String vidObject) {
            this.vidObject = vidObject;
        }
    }

    /**
     * Container object including the results of the review for a specific link.
     */
    @Embeddable
    public static class RevLinks {
        @Column
        private String value;
        @Lob
        private String href;

        protected RevLinks() {
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public String getHref() {
            return href;
        }

        public void setHref(final String href) {
            this.href = href;
        }
    }

    /**
     * Container object including the results of the automatic review.
     */
    @Embeddable
    public static class AutomaticReview {
        @Column
        private String description;
        @Column
        private String help;
        @Column
        private String helpUrl;
        @Column
        private boolean passed;
        @Lob
        private String htmlSnippet;

        protected AutomaticReview() {
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

        public String getHelp() {
            return help;
        }

        public void setHelp(final String help) {
            this.help = help;
        }

        public String getHelpUrl() {
            return helpUrl;
        }

        public void setHelpUrl(final String helpUrl) {
            this.helpUrl = helpUrl;
        }

        public boolean isPassed() {
            return passed;
        }

        public void setPassed(final boolean passed) {
            this.passed = passed;
        }

        public String getHtmlSnippet() {
            return htmlSnippet;
        }

        public void setHtmlSnippet(final String htmlSnippet) {
            this.htmlSnippet = htmlSnippet;
        }

    }
}


