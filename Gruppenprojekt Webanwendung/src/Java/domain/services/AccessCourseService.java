package gpse.example.domain.services;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;

import java.util.List;
import java.util.Optional;

/**
 * Service for loading courses.
 */
public interface AccessCourseService {
    List<AccessCourse> getCourses();

    List<AccessCourse> getCoursesByGroup(AccessGroup group);

    Optional<AccessCourse> getCourse(String id);

    AccessCourse getCourseByName(String name) throws NotFoundException;

    AccessCourse saveAccessCourse(AccessCourse accessCourse);

    AccessCourse createAccessCourse(String coursename, AccessGroup group, Boolean visible, Boolean weighted);

    AccessCourse updateAccessCourse(String id, AccessCourse tempCourse);

    AccessCourse deleteAccessCourse(AccessCourse accessCourse);

    List<AccessCourse> getVisibleCourses();
}

