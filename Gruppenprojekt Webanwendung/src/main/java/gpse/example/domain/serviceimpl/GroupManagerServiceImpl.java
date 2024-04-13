package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.User;
import gpse.example.domain.repositories.GroupManagerRepository;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.web.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link GroupManagerService} needed to load employees.
 */
@Service
public class GroupManagerServiceImpl implements GroupManagerService {

    private final GroupManagerRepository groupManagerRepository;

    @Autowired
    public GroupManagerServiceImpl(final GroupManagerRepository groupManagerRepository) {
        this.groupManagerRepository = groupManagerRepository;
    }

    @Override
    public List<GroupManager> getGroupManagers() {
        final List<GroupManager> groupManagers = new ArrayList<>();
        groupManagerRepository.findAll().forEach(groupManagers::add);
        return groupManagers;
    }

    @Override
    public GroupManager loadGroupManagerById(final Long id) {
        return groupManagerRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("GroupManager with id " + id + " not found."));
    }

    @Override
    public GroupManager saveGroupManager(final GroupManager groupManager) {
        return groupManagerRepository.save(groupManager);
    }


    @Override
    public GroupManager createGroupManager(final GroupManager tmpGroupManager) {
        final GroupManager groupManager = new GroupManager(tmpGroupManager.getUser(), tmpGroupManager.getAccessGroup());
        return groupManagerRepository.save(groupManager);
    }

    @Override
    public GroupManager updateGroupManager(final String id, final GroupManager tmpGroupManager) {
        final Long groupManagerID = Long.valueOf(id);
        final GroupManager groupManager = groupManagerRepository.findById(groupManagerID)
            .orElseThrow(BadRequestException::new);
        groupManager.setAccessGroup(tmpGroupManager.getAccessGroup());
        groupManager.setUser(tmpGroupManager.getUser());
        return groupManagerRepository.save(groupManager);
    }

    @Override
    public void deleteGroupManager(final GroupManager groupManager) {
        groupManagerRepository.delete(groupManager);
    }

    @Override
    public Optional<GroupManager> getGroupManager(final String id) {
        final Long groupManagerId = Long.valueOf(id);
        return groupManagerRepository.findById(groupManagerId);
    }

    @Override
    public List<GroupManager> getGroupManagerByGroup(final AccessGroup accessGroup) {
        final List<GroupManager> groupManagers = new ArrayList<>();
        final List<GroupManager> groupManagersFinal = new ArrayList<>();
        groupManagerRepository.findAll().forEach(groupManagers::add);
        for (final GroupManager groupManager:groupManagers
        ) {
            if (groupManager.getAccessGroup() == accessGroup) {
                groupManagersFinal.add(groupManager);
            }
        }
        return groupManagersFinal;
    }

    @Override
    public List<GroupManager> getGroupManagerByUser(final User user) {
        final List<GroupManager> groupManagers = new ArrayList<>();
        final List<GroupManager> groupManagersFinal = new ArrayList<>();
        groupManagerRepository.findAll().forEach(groupManagers::add);
        boolean userfound = false;
        for (final GroupManager groupManager:groupManagers) {
            if (groupManager.getUser().getId().equals(user.getId())) {
                groupManagersFinal.add(groupManager);
                userfound = true;
            }
        }
        if (userfound) {
            return groupManagersFinal;
        } else {
            return new ArrayList<>();
            //throw new UsernameNotFoundException("no Employee found hahaha ");
        }

    }

    @Override
    public GroupManager searchForGroupManager(final User user, final AccessGroup group) {
        try {
            final List<GroupManager> groupManagerList = getGroupManagerByUser(user);
            for (final GroupManager groupManager : groupManagerList
            ) {
                if (Objects.equals(groupManager.getAccessGroup().getName(), group.getName())) {
                    return groupManager;
                }
            }
            return null;
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }
}

