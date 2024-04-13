package gpse.example.domain.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model representation of an AccessCourse.
 */
@Entity
public class AccessCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private Boolean visible;
    @JoinColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<HTML> htmlList = new ArrayList<>();
    private Boolean coursePublic;
    @JoinColumn
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AccessGroup group;
    @Column
    private Boolean weighted;
    protected AccessCourse() {
    }

    /**
     * Constructor for AccessCourse with all needed parameters.
     *
     * @param name  the name of the AccessCourse
     * @param group the group to which the AccessCourse belongs
     * @param visible wether the course is visible for others
     * @param weighted wether the course-score is weighted
     */
    public AccessCourse(final String name, final AccessGroup group, final Boolean visible, final Boolean weighted) {
        this.name = name;
        this.group = group;
        this.coursePublic = false;
        this.weighted = weighted;
        this.visible = visible;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long courseId) {
        this.id = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String courseName) {
        this.name = courseName;
    }

    public AccessGroup getGroup() {
        return group;
    }

    public void setGroup(final AccessGroup group) {
        this.group = group;
    }

    public void setHtmlList(final List<HTML> htmlList) {
        this.htmlList = htmlList;
    }

    public List<HTML> getHtmlList() {
        return htmlList;
    }

    public Boolean getVisible() {
        if (visible == null) {
            return false;
        }
        return visible;
    }

    public void setVisible(final Boolean visible) {
        this.visible = visible;
    }

    public void addHtml(final HTML html) {
        this.htmlList.add(html);
    }

    public void addHTMLwithReview(final HTML html, final Review review) {
        html.setReview(review);
        this.htmlList.add(html);
    }
    public void setWeighted(final Boolean weighted) {
        this.weighted = weighted;
    }

    /**
     * Returns weighted value.
     * @return weighted value, false if is null
     */
    public Boolean isWeighted() {
        if (weighted == null) {
            return false;
        }
        return weighted;
    }

    public Boolean isCoursePublic() {
        return coursePublic;
    }

    public void setCoursePublic(final Boolean coursePublic) {
        this.coursePublic = coursePublic;
    }

    /**
     * Calculates the score for the course.
     *
     * @return the score
     */
    public int getScore() {
        int totalScore = 0;
        for (final HTML html : htmlList) {
            totalScore += html.getScore();
        }
        if (htmlList.isEmpty()) {
            return 0;
        }
        return Math.round(((float) totalScore) / htmlList.size());
    }
}

