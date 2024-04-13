import axios from 'axios';

export default {
    listall() {
        return axios.get('/api/allcourses');
    },
    list() {
        return axios.get('/api/courses');
    },
    listByGroup(groupId) {
        return axios.get('/api/group-courses/' + groupId)
    },
    listbyvisibility() {
        return axios.get('/api/visible-courses')
    },
    get(id) {
        return axios.get('/api/courses/' + id);
    },
    save(course) {
        let courseCmd = {name: course.value.name, groupID: course.value.groupID, visible: course.visible, weighted: course.value.weighted};
        return axios.post('/api/courses', courseCmd);
    },
    update(course) {
        let courseCmd = {name: course.name, htmlList: Array.from(course.htmlList), group: course.group, visible: course.visible, weighted: course.weighted, coursePublic: course.coursePublic};
        return axios.put('/api/courses/' + course.id, courseCmd);
    },
    getScore(id) {
        return axios.get('/api/course/'+id+'/score')
    },
    getAvgMyCourses() {
        return axios.get('/api/avgMyCourses')
    },
    delete(course) {
        return axios.put('/api/course/' + course.id);
    },
    allowedToEdit(id) {
        return axios.get("/api/course-edit/" + id)
    }
}
