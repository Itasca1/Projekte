package gpse.example.domain.cmds;

import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.HTML;

import java.util.List;

/**
 * Command object for courses.
 * Encapsulates all relevant information needed by api action.
 */
public class AccessCourseCmd {
    private String name;
    private List<HTML> htmlList;
    private AccessGroup group;
    private String groupName;
    private Boolean coursePublic;
    private Long groupID;
    private Boolean visible;
    private Boolean weighted;

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(final Boolean visible) {
        this.visible = visible;
    }


    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(final Long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<HTML> getHtmlList() {
        return htmlList;
    }

    public void setHtmlList(final List<HTML> htmlList) {
        this.htmlList = htmlList;
    }

    public AccessGroup getGroup() {
        return group;
    }

    public void setGroup(final AccessGroup group) {
        this.group = group;
    }

    public Boolean isCoursePublic() {
        return coursePublic;
    }

    public void setCoursePublic(final Boolean coursePublic) {
        this.coursePublic = coursePublic;
    }

    public Boolean isWeighted() {
        return weighted;
    }

    public void setWeighted(final Boolean weighted) {
        this.weighted = weighted;
    }
}

