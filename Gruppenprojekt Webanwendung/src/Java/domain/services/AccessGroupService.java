package gpse.example.domain.services;

import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Faculty;

import java.util.List;
import java.util.Optional;

/**
 * Service for Accessgroups.
 */
public interface AccessGroupService {
    List<AccessGroup> getAllGroups();

    List<AccessGroup> getGroupsByFaculty(Faculty faculty);

    Optional<AccessGroup> getGroupById(Long id);

    AccessGroup getGroupByName(String name) throws NotFoundException;

    AccessGroup saveAccessGroup(AccessGroup accessGroup);

    AccessGroup createAccessGroup(String name, String semester, Faculty faculty);

    AccessGroup updateAccessGroup(String id, AccessGroup group);

    AccessGroup deleteGroup(AccessGroup group);
}

