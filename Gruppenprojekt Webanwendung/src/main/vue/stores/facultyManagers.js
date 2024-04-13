import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useFacultyManagerStore = defineStore('facultyManager', () => {
    const managers = ref([])

    function getManagers() {
        return managers.value
    }

    function requestManagers(facultyId) {
        return new Promise((resolve, reject) => {
            api.facultyManager.getManagersOfFaculty(facultyId).then(res => {
                managers.value = res.data
                resolve()
            }).catch(() => {
                managers.value = []
                reject()
            })
        })
    }

    function requestAllFacManagers() {
        return new Promise((resolve, reject) => {
            api.facultyManager.getAllManagers().then(res => {
                managers.value = res.data
                resolve()
            }).catch(() => {
                managers.value = []
                reject()
            })
        })
    }

    function requestMyFacManagers() {
        return new Promise((resolve, reject) => {
            api.facultyManager.getMyFacManagers().then(res => {
                managers.value = res.data
                resolve()
            }).catch(() => {
                managers.value =[]
                reject()
            })
        })
    }

    function requestMyDeletableFacManagers() {
        return new Promise((resolve, reject) => {
            api.facultyManager.getMyDeletableFacManagers().then(res => {
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
        requestMyFacManagers,
        requestMyDeletableFacManagers,
        requestAllFacManagers
    }
})
