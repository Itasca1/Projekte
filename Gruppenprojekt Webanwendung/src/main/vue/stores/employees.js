import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useEmployeeStore = defineStore('employee', () => {
    const employees = ref([])

    function getEmployees() {
        return employees.value
    }

    function requestEmployees(courseID) {
        return new Promise((resolve, reject) => {
            api.employee.getEmployeesOfCourse(courseID).then(res => {
                employees.value = res.data
                resolve()
            }).catch(() => {
                employees.value = []
                reject()
            })
        })
    }
    function requestAllEmployees() {
        return new Promise((resolve, reject) => {
            api.employee.getAllEmployees().then(res => {
                employees.value = res.data
                resolve()
            }).catch(() => {
                employees.value = []
                reject()
            })
        })
    }

    function requestMyEmployees() {
        return new Promise((resolve, reject) => {
            api.employee.getMyEmployees().then(res => {
                employees.value = res.data
                resolve()
            }).catch(() => {
                employees.value =[]
                reject()
            })
        })
    }

    function requestMyDeletableEmployees() {
        return new Promise((resolve, reject) => {
            api.employee.getMyDeletableEmployees().then(res => {
                employees.value = res.data
                resolve()
            }).catch(() => {
                employees.value =[]
                reject()
            })
        })
    }

    return {
        getEmployees,
        requestEmployees,
        requestMyDeletableEmployees,
        requestAllEmployees,
        requestMyEmployees
    }

})
