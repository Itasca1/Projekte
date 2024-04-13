package gpse.example.domain.cmds;

import gpse.example.domain.entities.Review;

/**
 * Command object for HTML objects.
 * Encapsulates all relevant information needed by api action.
 */
public class HTMLCmd {
    private String htmlString;
    private Review review;
    private String name;

    public String getHtmlString() {
        return htmlString;
    }

    public void setHtmlString(final String htmlString) {
        this.htmlString = htmlString;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(final Review review) {
        this.review = review;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
