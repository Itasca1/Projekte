package gpse.example.domain.entities;

import javax.persistence.*;

/**
 * Model representation for a HTML-Object, containing the html string and the review.
 */
@Entity
public class HTML {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Lob
    @Column
    private String htmlString;
    @OneToOne
    private Review review;
    @Column
    private String name;

    protected HTML() {
    }

    /**
     * Constructor.
     *
     * @param htmlString the string containing the html code
     * @param review the review for the moodle/ilias course
     * @param name the filename
     */
    public HTML(final String htmlString, final Review review, final String name) {
        this.htmlString = htmlString;
        this.name = name;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getHtmlString() {
        return htmlString;
    }

    public void setHtmlString(final String html) {
        this.htmlString = html;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(final Review review) {
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the score for the html based on the review.
     *
     * @return the score
     */
    public int getScore() {
        if (review == null) {
            return 0;
        }
        return review.getScore();
    }
}
