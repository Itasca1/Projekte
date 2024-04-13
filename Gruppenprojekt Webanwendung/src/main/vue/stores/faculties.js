import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useFacultyStore = defineStore('faculties', () => {
    const faculties = ref([])

    function getFacultiesOverview() {
        return faculties.value.map(({name}) => {
            return {
                name
            }
        })
    }

    function getFaculty(id) {
        return faculties.value.find(faculty => faculty.id == id)

    }

    function getFaculties() {
        return faculties.value
    }

    function updateFaculty(id, newFaculty) {
        const faculty = getFaculty(id)
        if (faculty) {
            faculty.name = newFaculty.name
        } else {
            faculties.value.push(newFaculty)
        }
    }

    function requestAllFaculties() {
        return new Promise((resolve, reject) => {
            api.faculty.listall().then(res => {
                faculties.value = res.data
                resolve()
            }).catch(() => {
                faculties.value = []
                reject()
            })
        })
    }

    function requestFaculty(id) {
        return new Promise((resolve, reject) => {
            api.faculty.get(id).then(res => {
                updateFaculty(id, res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestFaculties() {
        return new Promise((resolve, reject) => {
            api.faculty.list().then(res => {
                faculties.value = res.data
                resolve()
            }).catch(() => {
                faculties.value = []
                reject()
            })
        })
    }

    function requestSaveFaculty(faculty) {
        return new Promise((resolve, reject) => {
            api.faculty.save(faculty).then(res => {
                faculty = res.data
                updateFaculty(faculty.id, faculty)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestUpdateFaculty(faculty) {
        return new Promise((resolve, reject) => {
            api.faculty.update(faculty).then(res => {
                faculty = res.data
                updateFaculty(faculty.id, faculty)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestDeleteFaculty(faculty) {
        return new Promise((resolve, reject) => {
            api.faculty.delete(faculty).then(res => {
                faculty = res.data
                updateFaculty(faculty.id, faculty)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function getScore(id) {
        return api.faculty.getScore(id);
    }

    function requestAllowEdit(id) {
        return api.faculty.allowedToEdit(id);
    }

    return {
        requestAllowEdit,
        faculties,
        getFacultiesOverview,
        requestAllFaculties,
        getFaculties,
        getFaculty,
        updateFaculty,
        requestFaculty,
        requestFaculties,
        requestSaveFaculty,
        requestUpdateFaculty,
        requestDeleteFaculty,
        getScore
    }
})
