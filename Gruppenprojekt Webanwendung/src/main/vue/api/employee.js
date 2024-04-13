import axios from 'axios';

export default {
    getEmployeesOfCourse(courseID) {
        return axios.get('/api/course-employees/' + courseID);
    },
    getAllEmployees() {
        return axios.get('/api/employees')
    },
    getMyEmployees() {
        return axios.get('/api/getMyEmployees')
    },
    getMyDeletableEmployees() {
        return axios.get('/api/getMyDeletableEmployees')
    }
}
