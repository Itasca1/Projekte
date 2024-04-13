package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.repositories.CourseRepository;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.NotFoundException;
import gpse.example.web.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link AccessCourseService} needed to load courses.
 */
@Service
public class AccessCourseServiceImpl implements AccessCourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public AccessCourseServiceImpl(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<AccessCourse> getCourses() {
        final List<AccessCourse> courses = new ArrayList<>();
        courseRepository.findAll().forEach(courses::add);
        return courses;
    }

    @Override
    public List<AccessCourse> getCoursesByGroup(final AccessGroup group) {
        final List<AccessCourse> courses = new ArrayList<>();
        for (final AccessCourse accessCourse : courseRepository.findAll()) {
            if (accessCourse.getGroup().getId().equals(group.getId())) {
                courses.add(accessCourse);
            }
        }
        return courses;
    }

    @Override
    public Optional<AccessCourse> getCourse(final String id) {
        final Long courseId = Long.valueOf(id);
        return courseRepository.findById(courseId);
    }

    /**
     * Loads course from repository.
     *
     * @param name the name of the course
     * @return the course
     */
    @Override
    public AccessCourse getCourseByName(final String name) throws NotFoundException {
        final AccessCourse course = courseRepository.findByName(name);
        if (course == null) {
            throw new NotFoundException("Course not found");
        } else {
            return course;
        }
    }

    @Override
    public AccessCourse saveAccessCourse(final AccessCourse accessCourse) {
        return courseRepository.save(accessCourse);
    }

    @Override
    public AccessCourse createAccessCourse(final String coursename, final AccessGroup group,
                                           final Boolean visible, final Boolean weighted) {
        final AccessCourse course = new AccessCourse(coursename, group, visible, weighted);
        return courseRepository.save(course);
    }

    @Override
    public AccessCourse updateAccessCourse(final String id, final AccessCourse tempCourse) {
        final Long courseId = Long.valueOf(id);
        final AccessCourse course = courseRepository.findById(courseId).orElseThrow(BadRequestException::new);
        course.setName(tempCourse.getName());
        course.setHtmlList(tempCourse.getHtmlList());
        course.setGroup(tempCourse.getGroup());
        course.setCoursePublic(tempCourse.isCoursePublic());
        course.setVisible(tempCourse.getVisible());
        course.setWeighted(tempCourse.isWeighted());
        return courseRepository.save(course);
    }

    @Override
    public AccessCourse deleteAccessCourse(final AccessCourse accessCourse) {
        courseRepository.delete(accessCourse);
        return accessCourse;
    }

    @Override
    public List<AccessCourse> getVisibleCourses() {
        final List<AccessCourse> visibleCourses = new ArrayList<>();
        final List<AccessCourse> courses = new ArrayList<>();
        courseRepository.findAll().forEach(courses::add);
        for (final AccessCourse course : courses) {
            if (course.isCoursePublic()) {
                visibleCourses.add(course);
            }
        }
        return visibleCourses;
    }

}

