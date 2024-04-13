package gpse.example;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.HTML;
import gpse.example.domain.repositories.CourseRepository;
import gpse.example.domain.repositories.HTMLRepository;
import gpse.example.domain.serviceimpl.AccessCourseServiceImpl;
import gpse.example.domain.serviceimpl.HTMLServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HTMLTest {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private HTMLRepository htmlRepository;

    @Test
    /* default */ void htmlCreation() {
        // Create html
        final HTMLServiceImpl htmlService = new HTMLServiceImpl(htmlRepository);
        final AccessCourseServiceImpl courseService = new AccessCourseServiceImpl(courseRepository);
        final AccessCourse course = new AccessCourse("htmlCreationTestCourse", null, true,true);
        HTML html = htmlService.createHTML("<html>i am an html file</html>", null,"htmlCreationTest.html");
        course.addHtml(html);
        courseService.saveAccessCourse(course);

        // Test
        assertThat(courseService.getCourses().get(0).getHtmlList().isEmpty()).isFalse();
        html = courseService.getCourses().get(0).getHtmlList().get(0);
        assertThat(html.getId()).isNotNull();
        assertThat(html.getHtmlString()).isEqualTo("<html>i am an html file</html>");
        assertThat(html.getName()).isEqualTo("htmlCreationTest.html");
    }

    @Test
    /* default */ void htmlUpdate() {
        // Create html
        final HTMLServiceImpl htmlService = new HTMLServiceImpl(htmlRepository);
        final AccessCourseServiceImpl courseService = new AccessCourseServiceImpl(courseRepository);
        AccessCourse course = new AccessCourse("htmlUpdateTestCourse", null, true,true);
        HTML html = htmlService.createHTML("<html>i am an html file</html>", null, "htmlUpdateTest.html");
        course.addHtml(html);
        course = courseService.saveAccessCourse(course);

        // Update html
        html = course.getHtmlList().get(0);
        html.setHtmlString("<html>i am an updated html file</html>");
        html.setName("htmlUpdatedTest.html");
        htmlService.updateHTML(String.valueOf(html.getId()), html);

        // Test
        final HTML updatedHtml = courseService.getCourses().get(0).getHtmlList().get(0);
        assertThat(updatedHtml.getHtmlString()).isEqualTo("<html>i am an updated html file</html>");
        assertThat(updatedHtml.getName()).isEqualTo("htmlUpdatedTest.html");
    }
}
