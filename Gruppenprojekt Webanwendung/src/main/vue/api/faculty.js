import axios from 'axios';

export default {
    listall() {
        return axios.get('/api/allfaculties');
    },
    list() {
        return axios.get('/api/faculties');
    },
    get(id) {
        return axios.get('/api/faculties/' + id);
    },
    save(faculty) {
        let facultyCmd = {name: faculty.name};
        return axios.post('/api/faculties', facultyCmd);
    },
    update(faculty) {
        let facultyCmd = {name: faculty.name};
        return axios.put('/api/faculties/' + faculty.id, facultyCmd);
    },
    getScore(id) {
        return axios.get('/api/faculty/'+id+'/score')
    },
    delete(faculty) {
        let facultyCmd = {name: faculty.name}
        return axios.post('/api/deleteFaculty', facultyCmd);
    },
    allowedToEdit(id) {
        return axios.get('/api/faculty-edit/' + id);
    }
}
