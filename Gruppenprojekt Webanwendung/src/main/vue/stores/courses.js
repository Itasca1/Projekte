import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useCourseStore = defineStore('courses', () => {
    const courses = ref([])
    const avgMyCourses = ref([])

    function getCoursesOverview() {
        return courses.value
    }

    function getCourse(id) {
        return courses.value.find(course => course.id == id)
    }

    function getCourses()
    {
        return courses.value
    }

    function requestCoursesByGroup(groupId) {
        return new Promise((resolve, reject) => {
            api.course.listByGroup(groupId).then(res => {
                courses.value = res.data
                resolve()
            }).catch(() => {
                courses.value = []
                reject()
            })
        })
    }

    function requestCoursesByVisibility() {
        return new Promise((resolve, reject) => {
            api.course.listbyvisibility().then(res => {
                courses.value = res.data
                resolve()
            }).catch(() => {
                courses.value = []
                reject()
            })
        })
    }

    function updateCourse(id, newCourse) {
        const course = getCourse(id)
        if (course) {
            course.name = newCourse.name;
            course.htmlList = newCourse.htmlList;
            course.group = newCourse.group;
            course.weighted=newCourse.weighted;
            course.visible = newCourse.visible;
        } else {
            courses.value.push(newCourse)
        }
    }

    function requestCourse(id) {
        return new Promise((resolve, reject) => {
            api.course.get(id).then(res => {
                updateCourse(id, res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestAllCourses() {
        return new Promise((resolve, reject) => {
            api.course.listall().then(res => {
                courses.value = res.data
                resolve()
            }).catch(() => {
                courses.value = []
                reject()
            })
        })
    }


    function requestAvgMyCourses() {
        return new Promise((resolve, reject) => {
            api.course.getAvgMyCourses().then(res => {
                avgMyCourses.value = res.data;
                resolve()
            }).catch(() => {
                avgMyCourses.value = [];
                reject()
            })
        })
    }

    function getAvgMyCoursesInStore() {
        return avgMyCourses.value
    }

    function requestCourses() {
        return new Promise((resolve, reject) => {
            api.course.list().then(res => {
                courses.value = res.data
                resolve()
            }).catch(() => {
                courses.value = []
                reject()
            })
        })
    }

    function requestSaveCourse(course) {
        return new Promise((resolve, reject) => {
            api.course.save(course).then(res => {
                course = res.data
                updateCourse(course.id, course)
                resolve(course)
            }).catch(() => {
                reject()
            })
        })
    }

    function requestUpdateCourse(course) {
        return new Promise((resolve, reject) => {
            api.course.update(course).then(res => {
                course = res.data
                updateCourse(course.id, course)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestAddHtml(course, html) {
        course.htmlList.push(html)
        return new Promise((resolve, reject) => {
            api.course.update(course).then(res => {
                course = res.data
                updateCourse(course.id, course)
                resolve(course)
            }).catch(error => {
                reject(error)
            })
        })
    }

    function requestDeleteCourse(course) {
        return new Promise((resolve, reject) => {
            api.course.delete(course).then(res => {
                course = res.data
                updateCourse(course.id, course)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function getScore(id) {
        return api.course.getScore(id);
    }

    function requestAllowEdit(id) {
        return api.course.allowedToEdit(id);
    }

    return {
        requestAllowEdit,
        requestCoursesByGroup,
        requestDeleteCourse,
        requestAllCourses,
        requestAvgMyCourses,
        getAvgMyCoursesInStore,
        courses,
        getCoursesOverview,
        getCourse,
        getCourses,
        requestCourse,
        requestCourses,
        requestSaveCourse,
        requestUpdateCourse,
        requestAddHtml,
        getScore,
        requestCoursesByVisibility
    }
})
