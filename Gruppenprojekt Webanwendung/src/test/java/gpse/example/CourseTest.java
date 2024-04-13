package gpse.example;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.HTML;
import gpse.example.domain.repositories.CourseRepository;
import gpse.example.domain.repositories.GroupRepository;
import gpse.example.domain.repositories.HTMLRepository;
import gpse.example.domain.serviceimpl.AccessCourseServiceImpl;
import gpse.example.domain.serviceimpl.AccessGroupServiceImpl;
import gpse.example.domain.serviceimpl.HTMLServiceImpl;
import gpse.example.domain.services.AccessCourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseTest {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private HTMLRepository htmlRepository;

    @Test
    /* default */ void courseCreation() {
        // Create course
        final AccessCourseService courseService = new AccessCourseServiceImpl(courseRepository);
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        final AccessGroup group = new AccessGroup("courseCreationTestGroup", "courseCreationTestSemester", null);
        AccessCourse course = courseService.createAccessCourse("courseCreationTestCourse", group, Boolean.valueOf("true"),true);
        groupService.saveAccessGroup(group);

        // Test
        final Optional<AccessCourse> courseOpt = courseService.getCourse(String.valueOf(course.getId()));
        assertThat(courseOpt.isPresent()).isTrue();
        course = courseOpt.get();
        assertThat(course.getId()).isNotNull();
        assertThat(course.getName()).isEqualTo("courseCreationTestCourse");
        assertThat(course.getGroup()).isEqualTo(groupService.getAllGroups().get(0));
        assertThat(course.isWeighted()).isEqualTo(true);
    }

    @Test
    /* default */ void courseUpdate() {
        // Create course
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        final AccessCourseServiceImpl courseService = new AccessCourseServiceImpl(courseRepository);
        final AccessGroup group = new AccessGroup("courseUpdateTestGroup", "courseUpdateTestSemester", null);
        final AccessCourse course = courseService.createAccessCourse("courseUpdateTestCourse", group, Boolean.valueOf("true"),true);
        groupService.saveAccessGroup(group);

        // Update course
        course.setName("courseUpdatedTestCourse");
        courseService.updateAccessCourse(String.valueOf(course.getId()), course);

        // Test
        final Optional<AccessCourse> updatedCourseOpt = courseService.getCourse(String.valueOf(course.getId()));
        assertThat(updatedCourseOpt.isPresent()).isTrue();
        final AccessCourse updatedCourse = updatedCourseOpt.get();
        assertThat(updatedCourse.getName()).isEqualTo("courseUpdatedTestCourse");
        assertThat(updatedCourse.getGroup()).isEqualTo(groupService.getAllGroups().get(0));
        assertThat(updatedCourse.isWeighted()).isEqualTo(true);
    }

    @Test
    /* default */ void courseDelete() {
        // Create course
        final AccessCourseService courseService = new AccessCourseServiceImpl(courseRepository);
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        final HTMLServiceImpl htmlService = new HTMLServiceImpl(htmlRepository);
        final AccessGroup group = new AccessGroup("courseDeleteTestGroup", "courseDeleteTestSemester", null);
        final AccessCourse course = new AccessCourse("courseDeleteTestCourse", group, false, true);
        final HTML html = htmlService.createHTML("courseDeleteTestHtml", null, "courseDeleteTestHtml");
        course.addHtml(html);
        courseService.saveAccessCourse(course);
        groupService.saveAccessGroup(group);

        // Delete course
        courseService.deleteAccessCourse(course);

        // Test
        assertThat(courseService.getCourse(String.valueOf(course.getId()))).isEmpty();
        assertThat(htmlService.getHTML(String.valueOf(html.getId()))).isEmpty();
    }
}
