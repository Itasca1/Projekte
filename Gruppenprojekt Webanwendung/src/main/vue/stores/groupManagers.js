import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useGroupManagerStore = defineStore('groupManager', () => {
    const managers = ref([])

    function getManagers() {
        return managers.value
    }

    function requestManagers(groupID) {
        return new Promise((resolve, reject) => {
            api.groupManager.getManagersOfGroup(groupID).then(res => {
                managers.value = res.data
                resolve()
            }).catch(() => {
                managers.value = []
                reject()
            })
        })
    }

    function requestAllGroupManagers() {
        return new Promise((resolve, reject) => {
            api.groupManager.getAllGroupManagers().then(res => {
                managers.value = res.data
                resolve()
            }).catch(() => {
                managers.value = []
                reject()
            })
        })
    }

    function requestMyGroupManagers() {
        return new Promise((resolve, reject) => {
            api.groupManager.getMyGroupManagers().then(res => {
                managers.value = res.data
                resolve()
            }).catch(() => {
                managers.value =[]
                reject()
            })
        })
    }

    function requestMyDeletableGroupManagers() {
        return new Promise((resolve, reject) => {
            api.groupManager.getMyDeletableGroupManagers().then(res => {
                managers.value = res.data
                resolve()
            }).catch(() => {
                managers.value =[]
                reject()
            })
        })
    }

    return {
        getManagers,
        requestManagers,
        requestAllGroupManagers,
        requestMyGroupManagers,
        requestMyDeletableGroupManagers
    }
})
