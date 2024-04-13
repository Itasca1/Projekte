package gpse.example.domain.serviceimpl;

import gpse.example.domain.repositories.GroupRepository;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.services.AccessGroupService;
import gpse.example.domain.services.NotFoundException;
import gpse.example.web.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Impl of Accessgroupservice.
 */
@Service
public class AccessGroupServiceImpl implements AccessGroupService {

    private final GroupRepository groupRepository;

    public AccessGroupServiceImpl(final GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<AccessGroup> getAllGroups() {
        final List<AccessGroup> groups = new ArrayList<>();
        groupRepository.findAll().forEach(groups::add);
        return groups;
    }

    @Override
    public List<AccessGroup> getGroupsByFaculty(final Faculty faculty) {
        final List<AccessGroup> groups = new ArrayList<>();
        for (final AccessGroup accessGroup : groupRepository.findAll()) {
            if (accessGroup.getFaculty().getName().equals(faculty.getName())) {
                groups.add(accessGroup);
            }
        }
        return groups;
    }

    @Override
    public Optional<AccessGroup> getGroupById(final Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public AccessGroup getGroupByName(final String name) throws NotFoundException {
        final AccessGroup group = groupRepository.findByName(name);
        if (group == null) {
            throw new NotFoundException("Group not found");
        } else {
            return group;
        }
    }

    @Override
    public AccessGroup saveAccessGroup(final AccessGroup accessGroup) {
        return groupRepository.save(accessGroup);
    }

    @Override
    public AccessGroup createAccessGroup(final String groupname, final String semester, final Faculty faculty) {
        final AccessGroup accessGroup = new AccessGroup(groupname, semester, faculty);
        return groupRepository.save(accessGroup);
    }

    @Override
    public AccessGroup updateAccessGroup(final String id, final AccessGroup tempGroup) {
        final AccessGroup group = groupRepository.findById(Long.valueOf(id)).orElseThrow(BadRequestException::new);
        group.setFaculty(tempGroup.getFaculty());
        group.setName(tempGroup.getName());
        group.setSemester(tempGroup.getSemester());
        return groupRepository.save(group);
    }

    @Override
    public AccessGroup deleteGroup(final AccessGroup group) {
        groupRepository.delete(group);
        return group;
    }
}

