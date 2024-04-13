import axios from "axios";

export default {
    getManagersOfGroup(groupID) {
        return axios.get('/api/group-manager/' + groupID)
    },
    getAllGroupManagers() {
        return axios.get('/api/groupManager')
    },
    getMyGroupManagers() {
        return axios.get('/api/getMyGroupManagers')
    },
    getMyDeletableGroupManagers() {
        return axios.get('/api/getMyDeletableGroupManagers')
    }
}
