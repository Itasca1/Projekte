import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useGroupStore = defineStore('groups', () => {
    const groups = ref([])
    const avgAllGroups = ref([])

    function getGroupsOverview() {
        return groups.value.map(({name, semester, group}) => {
            return {
                name,
                semester,
                group
            }
        })
    }

    function getGroup(id) {
        return groups.value.find(group => group.id == id)

    }

    function getAvgAllGroupsInStore() {
        return avgAllGroups.value
    }

    function getGroups() {
        return groups.value
    }

    function requestGroupsByFaculty(facultyId) {
        return new Promise((resolve, reject) => {
            api.group.listByFaculty(facultyId).then(res => {
                groups.value = res.data
                resolve()
            }).catch(() => {
                groups.value = []
                reject()
            })
        })
    }

    function updateGroup(id, newGroup) {
        const group = getGroup(id)
        if (group) {
            group.name = newGroup.name
            group.semester = newGroup.semester
            group.faculty = newGroup.faculty
        } else {
            groups.value.push(newGroup)
        }
    }

    function requestAllGroups() {
        return new Promise((resolve, reject) => {
            api.group.listall().then(res => {
                groups.value = res.data
                resolve()
            }).catch(() => {
                groups.value = []
                reject()
            })
        })
    }

    function requestAvgAllGroups() {
        return new Promise((resolve, reject) => {
            api.group.getAvgAllGroups().then(res => {
                avgAllGroups.value = res.data;
                resolve()
            }).catch(() => {
                avgAllGroups.value = [];
                reject()
            })
        })
    }

    function requestGroup(id) {
        return new Promise((resolve, reject) => {
            api.group.get(id).then(res => {
                updateGroup(id, res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestGroups() {
        return new Promise((resolve, reject) => {
            api.group.list().then(res => {
                groups.value = res.data
                resolve()
            }).catch(() => {
                groups.value = []
                reject()
            })
        })
    }


    function requestSaveGroup(group) {
        return new Promise((resolve, reject) => {
            api.group.save(group).then(res => {
                group = res.data
                updateGroup(group.id, group)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestUpdateGroup(group) {
        return new Promise((resolve, reject) => {
            api.group.update(group).then(res => {
                group = res.data
                updateGroup(group.id, group)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestDeleteGroup(group) {
        return new Promise((resolve, reject) => {
            api.group.delete(group).then(res => {
                group = res.data
                updateGroup(group.id, group)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function getScore(id) {
        return api.group.getScore(id);
    }

    function requestAllowEdit(id) {
        return api.group.allowedToEdit(id);
    }

    return {
        requestAllowEdit,
        requestGroupsByFaculty,
        requestDeleteGroup,
        requestAllGroups,
        requestAvgAllGroups,
        getAvgAllGroupsInStore,
        groups,
        getGroupsOverview,
        getGroup,
        getGroups,
        updateGroup,
        requestGroup,
        requestGroups,
        requestSaveGroup,
        requestUpdateGroup,
        getScore
    }
})
