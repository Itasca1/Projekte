import {defineStore} from 'pinia'
import {ref} from 'vue'
import axios from "axios";
import api from "../api";

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)
    const users = ref([])
    let userWithId = []

    function getUserWithId() {
        return userWithId
    }

    function setUserWithId(user) {
        userWithId = user
    }

    function authenticate(token) {
        if (token !== null) {
            authenticated.value = true
            sessionStorage.setItem('token', token);
            axios.defaults.headers['Authorization'] = token
        } else {
            authenticated.value = false
        }
    }

    function getUser(id) {
        return users.value.find(user => user.id == id)
    }

    function getUsers() {
        return users.value;
    }

    function requestToken(credentials) {
        return new Promise((resolve, reject) => {
            api.auth.login(credentials.username, credentials.password).then(res => {
                authenticate(res.headers.authorization)
                resolve()
            }).catch(() => {
                authenticate(null)
                reject()
            })
        })
    }

    function myId() {
        return new Promise((resolve, reject) => {
            api.user.myId().then(res => {
                sessionStorage.setItem('id', res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    async function isAdmin() {
        return await new Promise((resolve, reject) => {
            api.user.isAdmin().then(res => {
                sessionStorage.setItem('Role_Admin', res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestAllFacultyManagers() {
        return new Promise((resolve, reject) => {
            api.user.getAllFacultyManagers().then(res => {
                users.value = res.data;
                resolve()
            }).catch(() => {
                users.value = []
                reject()
            })
        })
    }

    function updateUser(id, newUser) {
        const user = api.user.get(id)
        if (user) {
            user.email = newUser.email
            user.firstname = newUser.firstname
            user.lastname = newUser.lastname
            user.recentlyVisitedCourses = newUser.recentlyVisitedCourses
            user.menuOpen = newUser.menuOpen
            user.password = newUser.password
            user.userScoreDisabled = newUser.userScoreDisabled
        } else {
            users.value.push(newUser)
        }
    }

    function updateUserInfo(id, newUser) {

        const user = api.user.get(id)
        if (user) {
            user.email = newUser.email
            user.firstname = newUser.firstname
            user.lastname = newUser.lastname
        }
    }

    function logout() {
        sessionStorage.removeItem('token')
        sessionStorage.removeItem('id')
        authenticated.value = false;
    }

    function requestUser(id) {
        return new Promise((resolve, reject) => {
            api.user.get(id).then(res => {
                updateUser(id, res.data)
                setUserWithId(res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestUsers() {
        return new Promise((resolve, reject) => {
            api.user.list().then(res => {
                users.value = res.data
                resolve()
            }).catch(() => {
                users.value = []
                reject()
            })
        })
    }

    function requestSaveUser(user) {
        return new Promise((resolve, reject) => {
            api.user.save(user).then(res => {
                user = res.data
                updateUser(user.id, user)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    async function requestValidateUser(token) {
        return await new Promise((resolve, reject) => {
            api.user.verify(token).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    async function requestUpdateUser(user) {
        return await new Promise((resolve, reject) => {
            api.user.update(user).then(res => {
                user = res.data
                updateUser(user.id, user)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestUpdateUserInfo(updatedUser) {
        return new Promise((resolve, reject) => {
            api.user.get(updatedUser.id).then(res => {
                let user = res.data;
                user.email = updatedUser.email;
                user.firstname = updatedUser.firstname;
                user.lastname = updatedUser.lastname;
                user.userScoreDisabled = updatedUser.userScoreDisabled
                api.user.updateInfo(user).then(res => {
                    user = res.data
                    updateUser(user.id, user)
                    resolve()
                }).catch(error => reject(error))
            }).catch(error => reject(error))
        })
    }

    function requestUpdateUserInformation(user) {
        return new Promise((resolve, reject) => {
            api.user.updateInformation(user).then(res => {
                user = res.data
                updateUserInfo(user.id, user)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }



    function requestAddFacultyManager(facultyManager) {
        return new Promise((resolve, reject) => {
            api.user.saveFacultyManager(facultyManager).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestAddGroupManager(groupManager) {
        return new Promise((resolve, reject) => {
            api.user.saveGroupManager(groupManager).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestAddEmployee(employee) {
        return new Promise((resolve, reject) => {
            api.user.saveEmployee(employee).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestDeleteFacultyManager(facultyManager) {
        return new Promise((resolve, reject) => {
            api.user.deleteFacultyManager(facultyManager).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestDeleteGroupManager(groupManager) {
        return new Promise((resolve, reject) => {
            api.user.deleteGroupManager(groupManager).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestDeleteEmployee(employee) {
        return new Promise ((resolve, reject) => {
            api.user.deleteEmployee(employee).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestDeleteUser(id) {
        return new Promise((resolve, reject) => {
            api.user.deleteUser(id).then(() => {
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    function requestAddAdminRole(id) {
        return new Promise((resolve, reject) => {
            api.user.addRoleAdmin(id).then(res => {
                resolve(res)
            }).catch(() => {
                reject()
            })
        })
    }

    function getRecentlyVisitedCourses(id) {
        return new Promise((resolve, reject) => {
            api.user.getRecentlyVisitedCourses(id)
                .then(res => resolve(res))
                .catch(error => reject(error))
        })
    }

    function addRecentlyVisitedCourse(id, course) {
        return new Promise((resolve, reject) => {
            api.user.get(id).then(res => {
                let user = res.data;

                // update recentlyVisitedCourses
                const newRVCourses = []
                // move current course to front
                newRVCourses.push(course)
                // then add old courses
                for (let i = 0; i < user.recentlyVisitedCourses.length; i++) {
                    if (user.recentlyVisitedCourses[i] && user.recentlyVisitedCourses[i].id !== course.id)
                        newRVCourses.push(user.recentlyVisitedCourses[i])
                }
                // finally only take the first 5 courses
                user.recentlyVisitedCourses = newRVCourses.slice(0, 5);

                api.user.updateInfo(user).then(res => {
                    user = res.data
                    updateUser(user.id, user)
                    resolve(user.recentlyVisitedCourses)
                }).catch(error => reject(error))
            }).catch(error => reject(error))
        })
    }

    function hasMenuOpen(id) {
        return new Promise((resolve, reject) => {
            api.user.hasMenuOpen(id)
                .then(res => resolve(res))
                .catch(error => reject(error))
        })
    }

    function setMenuOpen(id, menuOpen) {
        return new Promise((resolve, reject) => {
            api.user.get(id).then(res => {
                let user = res.data;
                user.menuOpen = menuOpen;

                api.user.updateInfo(user).then(res => {
                    user = res.data
                    updateUser(user.id, user)
                    resolve(user.menuOpen)
                }).catch(error => reject(error))
            }).catch(error => reject(error))
        })
    }

    async function isFacultyManager() {
        return await new Promise((resolve, reject) => {
            api.user.isFacultyManager().then(res => {
                sessionStorage.setItem('Role_Faculty_Manager', res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    async function isGroupManager() {
        return await new Promise((resolve, reject) => {
            api.user.isGroupManager().then(res => {
                sessionStorage.setItem('Role_Group_Manager', res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    return {
        myId,
        getUser,
        getUsers,
        requestAllFacultyManagers,
        users,
        authenticated,
        authenticate,
        requestToken,
        logout,
        isAdmin,
        updateUser,
        requestUser,
        requestUsers,
        requestSaveUser,
        requestUpdateUser,
        requestAddFacultyManager,
        requestAddGroupManager,
        requestAddEmployee,
        requestDeleteFacultyManager,
        requestDeleteGroupManager,
        requestDeleteEmployee,
        requestDeleteUser,
        requestUpdateUserInfo,
        getRecentlyVisitedCourses,
        addRecentlyVisitedCourse,
        hasMenuOpen,
        setMenuOpen,
        requestAddAdminRole,
        requestValidateUser,
        isFacultyManager,
        isGroupManager,
        getUserWithId,
        setUserWithId,
        requestUpdateUserInformation
    }
})

axios.defaults.headers['Authorization'] = sessionStorage.getItem('token')
