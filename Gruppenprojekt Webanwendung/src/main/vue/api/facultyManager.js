import axios from "axios";

export default {
    getManagersOfFaculty(facultyId) {
        return axios.get('/api/faculty-managers/' + facultyId)
    },
    getMyFacManagers() {
        return axios.get('/api/getMyFacManagers')
    },
    getMyDeletableFacManagers() {
        return axios.get('/api/getMyDeletableFacManagers')
    },
    getAllManagers() {
        return axios.get('/api/getAllFacultyManagers/')
    }
}
