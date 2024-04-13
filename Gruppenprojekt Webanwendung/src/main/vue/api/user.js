import axios from 'axios';

export default {
    list() {
        return axios.get('/api/user');
    },
    myId() {
        return axios.get('/api/myid');
    },
    isAdmin() {
        return axios.get('/api/isadmin');
    },
    isGroupMangerFor() {
        return axios.get('/api/isgroupmanagerfor');
    },
    get(id) {
        return axios.get('/api/user/' + id);
    },
    getAllFacultyManagers() {
        return axios.get('/api/getAllFacultyManagers');
    },
    getByUsername(username) {
        return axios.get('/api/username/' + username)
    },
    save(user) {
        let userCmd = {email: user.email, firstname: user.firstname, lastname: user.lastname, password: user.password, visible: false};
        return axios.post('/api/users', userCmd);
    },
    update(user) {
        let userCmd = {
            email: user.email, firstname: user.firstname, lastname: user.lastname,
            recentlyVisitedCourses: Array.from(user.recentlyVisitedCourses), menuOpen: user.menuOpen, password: user.password, visible: user.visible
        };
        return axios.put('/api/users/' + user.id, userCmd);
    },
    updateInfo(user) {
        let userCmd = {
            email: user.email, firstname: user.firstname, lastname: user.lastname,
             recentlyVisitedCourses: Array.from(user.recentlyVisitedCourses), menuOpen: user.menuOpen,
              visible: user.visible
        };
        return axios.put('/api/userInfo', userCmd);
    },
    updateInformation(user) {
        let userCmd = {
            email: user.email, firstname: user.firstname, lastname: user.lastname,
            recentlyVisitedCourses: null, password: null, visible: user.visible
        };
        return axios.put('/api/userInformation/' + user.id, userCmd)
    },
    saveFacultyManager(facultyManager) {
        let facultyManagerCmd = {userID: facultyManager.value.userID, facultyID: facultyManager.value.facultyID};
        return axios.post('/api/AddFacultyManager', facultyManagerCmd)
    },
    saveGroupManager(groupManager) {
        let groupManagerCmd = {userID: groupManager.value.userID, groupID: groupManager.value.groupID};
        return axios.post('/api/groupManager', groupManagerCmd)
    },
    saveEmployee(employee) {
        let employeeCmd = {userID: employee.value.userID, courseID: employee.value.courseID};
        return axios.post('api/employees', employeeCmd)
    },
    deleteFacultyManager(facultyManager) {
        let facultyManagerCmd = {userID: facultyManager.value.userID, facultyID: facultyManager.value.facultyID};
        return axios.post('/api/deleteFacultyManager', facultyManagerCmd)
    },
    deleteGroupManager(groupManager) {
        let groupManagerCmd = {userID: groupManager.value.userID, groupID: groupManager.value.groupID};
        return axios.post('/api/deleteGroupManager', groupManagerCmd)
    },
    deleteEmployee(employee) {
        let employeeCmd = {userID: employee.value.userID, courseID: employee.value.courseID};
        return axios.post('api/deleteEmployee', employeeCmd)
    },
    deleteUser(id) {
        let userCmd = {id: id};
        return axios.post('/api/deleteUser', userCmd)
    },
    getRecentlyVisitedCourses(id) {
        return axios.get("/api/user/" + id + "/recentlyVisited")
    },
    hasMenuOpen(id) {
        return axios.get("/api/user/" + id + "/hasMenuOpen")
    },
    addRoleAdmin(id) {
        let userCmd = {id: id}
        return axios.post('api/addAdmin', userCmd);
    },
    verify(confirmationToken) {
        let confirmationTokenCmd = {confirmationToken: confirmationToken}
        return axios.put("../api/confirm_account", confirmationTokenCmd);
    },
    isFacultyManager() {
        return axios.get('/api/isFacultyManager');
    },
    isGroupManager() {
        return axios.get('/api/isGroupManager');
    },
}
