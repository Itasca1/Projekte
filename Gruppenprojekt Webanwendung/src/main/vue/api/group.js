import axios from 'axios';

export default {
    listall() {
        return axios.get('/api/allgroups');
    },
    list() {
        return axios.get('/api/groups');
    },
    listByFaculty(facultyId) {
        return axios.get('/api/faculty-groups/' + facultyId)
    },
    get(id) {
        return axios.get('/api/groups/' + id);
    },
    save(group) {
        let groupCmd = {name: group.value.name, facultyID: group.value.facultyID};
        return axios.post('/api/groups', groupCmd);
    },
    update(group) {
        let groupCmd = {name: group.name, semester: group.semester, faculty: group.faculty};
        return axios.put('/api/groups/' + group.id, groupCmd);
    },
    getScore(id) {
        return axios.get('/api/group/'+id+'/score')
    },
    getAvgAllGroups() {
        return axios.get('/api/avgAllGroups')
    },
    delete(group) {
        return axios.put('/api/group/' + group.id);
    },
    allowedToEdit(id) {
        return axios.get('/api/group-edit/' + id);
    }
}
