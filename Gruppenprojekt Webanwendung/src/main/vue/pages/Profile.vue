<template>

    <div class="headnav" style="text-align: left">
        <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
        <p class="navelem navactive">{{$t('profile.profile')}}</p>
    </div>
    <h1>{{$t('profile.profile') }}</h1>

    <div class="q-pa-md">
        <div class="q-gutter-y-md">
            <q-card>
                <q-tabs
                    v-model="tab"
                    dense
                    class="text-grey"
                    active-color="primary"
                    indicator-color="primary"
                    align="justify"
                    narrow-indicator
                >
                    <q-tab name="data" :label="$t('profile.data')" />
                    <q-tab name="responsibilities" :label="$t('profile.responsibilities.main')" />
                    <q-tab name="deleteResponsibilities" :label="$t('profile.deleteResponsibilities.main')" />
                    <q-tab name="settings" :label="$t('profile.settings')" />

                </q-tabs>

                <q-separator/>

                <q-tab-panels v-model="tab" animated>
                    <q-tab-panel name="data">
                        <q-icon name="person" size="128px" color="primary"/>
                        <q-input v-model="email" :label="$t('profile.email')" />
                        <q-input v-model="firstname" :label="$t('profile.vorname')" />
                        <q-input v-model="lastname" :label="$t('profile.nachname')" />
                        <q-input v-model="score" label="Score" :readonly="true"/>
                        <q-checkbox v-model="visible"> {{$t("profile.setVisible")}} </q-checkbox>

                        <div class="buttonclass">
                            <q-btn @click="save_user_changes" class="buttonitem" unelevated rounded color="primary">{{$t("manage.confirm")}}</q-btn>
                        </div>


                    </q-tab-panel>

                    <!-- tab to assign responsibilities -->
                    <q-tab-panel name="responsibilities">

                        <!-- assign faculty manager -->
                        <div class="q-mt-lg q-mr-md q-ml-md q-mb-sm">
                            {{ $t("profile.responsibilities.faculties.main") }}
                        </div>
                        <div class="row justify-around">
                            <div class="col-5">
                                <q-select filled v-model="faculty"
                                          :options="managedFaculties"
                                          :label="$t('profile.responsibilities.faculties.inputFac')"/>
                            </div>
                            <div class="col-5">
                                <q-select filled v-model="user"
                                          :options="requestedUsersEmails"
                                          :label="$t('profile.responsibilities.faculties.inputUser')"/>
                            </div>
                        </div>
                        <div class="buttonclass">
                            <q-btn @click="addFacultyManager" class="buttonitem" unelevated rounded color="primary">
                                {{ $t("profile.responsibilities.faculties.assign") }}
                            </q-btn>
                        </div>

                        <q-separator />

                        <!-- assign group manager -->
                        <div class="q-mt-lg q-mr-md q-ml-md q-mb-sm">
                            {{ $t("profile.responsibilities.groups.main") }}
                        </div>
                        <div class="row justify-around">
                            <div class="col-5">
                                <q-select filled v-model="group"
                                          :options="managedGroupsNames"
                                          :label="$t('profile.responsibilities.groups.inputGroup')"/>
                            </div>
                            <div class="col-5">
                                <q-select filled v-model="user"
                                          :options="requestedUsersEmails"
                                          :label="$t('profile.responsibilities.groups.inputUser')"/>
                            </div>
                        </div>

                        <div class="buttonclass">
                            <q-btn @click="addGroupManager" class="buttonitem" unelevated rounded color="primary">
                                {{ $t("profile.responsibilities.groups.assign") }}
                            </q-btn>
                        </div>

                        <q-separator />

                        <!-- assign course manager -->
                        <div class="q-mt-lg q-mr-md q-ml-md q-mb-sm">
                            {{ $t("profile.responsibilities.courses.main") }}
                        </div>
                        <div class="row justify-around">
                            <div class="col-5">
                                <q-select filled v-model="course"
                                          :options="managedCoursesNames"
                                          :label="$t('profile.responsibilities.courses.inputGroup')"/>
                            </div>
                            <div class="col-5">
                                <q-select filled v-model="user"
                                          :options="requestedUsersEmails"
                                          :label="$t('profile.responsibilities.courses.inputUser')"/>
                            </div>
                        </div>

                        <div class="buttonclass">
                            <q-btn @click="addEmployee" class="buttonitem" unelevated rounded color="primary">
                                {{ $t("profile.responsibilities.courses.assign") }}
                            </q-btn>
                        </div>

                    </q-tab-panel>

                    <!-- tab to delete responsibilities -->
                    <q-tab-panel name="deleteResponsibilities">

                        <!-- delete faculty manager -->
                        <div class="q-pa-md">
                            {{ $t("profile.deleteResponsibilities.faculties.main") }}
                            <q-table
                                title="Faculty Managers"
                                :rows="rowsFacTable"
                                :columns="columnsFacTable"
                                row-key="name"
                                dense>

                                <template #body="props" >
                                    <q-tr :props="props">
                                        <q-td
                                            key="faculty"
                                            :props="props">
                                            {{props.row.facManagerFacultyName}}
                                        </q-td>
                                        <q-td
                                            key="manager"
                                            :props="props">

                                            <q-chip icon="person" >
                                                {{props.row.facManagerUserName}}
                                            </q-chip>

                                        </q-td>

                                        <q-td
                                            key="action"
                                            :props="props">
                                            <q-btn dense round flat
                                                   color="grey"
                                                   @click="deleteFacultyManager(props.row)"
                                                   icon="delete">
                                            </q-btn>
                                        </q-td>
                                    </q-tr>
                                </template>

                            </q-table>
                        </div>

                        <q-separator />

                        <!-- delete group manager -->
                        <div class="q-pa-md">
                            {{ $t("profile.deleteResponsibilities.groups.main") }}
                            <q-table
                                title="Group Managers"
                                :rows="rowsGroupTable"
                                :columns="columnsGroupTable"
                                row-key="name"
                                dense>

                                <template #body="props" >
                                    <q-tr :props="props">
                                        <q-td
                                            key="group"
                                            :props="props">
                                            {{props.row.groupManagerGroupName}}
                                        </q-td>
                                        <q-td
                                            key="manager"
                                            :props="props">

                                            <q-chip icon="person" >
                                                {{props.row.groupManagerUserName}}
                                            </q-chip>

                                        </q-td>

                                        <q-td
                                            key="action"
                                            :props="props">
                                            <q-btn dense round flat
                                                   color="grey"
                                                   @click="deleteGroupManager(props.row)"
                                                   icon="delete">
                                            </q-btn>
                                        </q-td>
                                    </q-tr>
                                </template>

                            </q-table>
                        </div>

                        <q-separator />

                        <!-- delete course manager -->
                        <div class="q-pa-md">
                            {{ $t("profile.deleteResponsibilities.courses.main") }}
                            <q-table
                                title="Course Managers"
                                :rows="rowsCourseTable"
                                :columns="columnsCourseTable"
                                row-key="name"
                                dense>

                                <template #body="props" >
                                    <q-tr :props="props">
                                        <q-td
                                            key="course"
                                            :props="props">
                                            {{props.row.courseManagerCourseName}}
                                        </q-td>
                                        <q-td
                                            key="manager"
                                            :props="props">

                                            <q-chip icon="person" >
                                                {{props.row.courseManagerUserName}}
                                            </q-chip>

                                        </q-td>

                                        <q-td
                                            key="action"
                                            :props="props">
                                            <q-btn dense round flat
                                                   color="grey"
                                                   @click="deleteEmployee(props.row)"
                                                   icon="delete">
                                            </q-btn>
                                        </q-td>
                                    </q-tr>
                                </template>

                            </q-table>
                        </div>

                    </q-tab-panel>

                    <q-tab-panel name="settings">
                        <div class="text-h6">{{ $t('profile.settings') }}</div>
                        <q-select v-model="language" :options="languages" :label="$t('profile.sprache')"/>

                        <div class="q-mt-lg q-mr-md q-ml-md q-mb-sm">
                            <q-btn v-if="true" color="primary" @click="newlanguage">
                                {{ $t("manage.confirm") }}
                            </q-btn>
                        </div>


                    </q-tab-panel>

                </q-tab-panels>
            </q-card>
        </div>
    </div>
</template>

<script>
import {ref} from 'vue'
import {useUserStore} from "../stores/users";
import {useFacultyStore} from "../stores/faculties";
import {useGroupStore} from "../stores/groups";
import {useQuasar} from "quasar";
import {useRouter} from 'vue-router';
import {useI18n} from "vue-i18n";
import {useFacultyManagerStore} from "../stores/facultyManagers";
import {useGroupManagerStore} from "../stores/groupManagers";
import {useEmployeeStore} from "../stores/employees";
import {useCourseStore} from "../stores/courses";
import axios from "axios";


export default {
    setup () {
        const $q = useQuasar()
        const { t, locale } = useI18n({ useScope: 'global' })
        sessionStorage.getItem('token')
        const router = useRouter()
        const userStore = useUserStore()
        const userFacultyStore = useFacultyStore()
        const facultyManagerStore = useFacultyManagerStore()
        const groupStore = useGroupStore()
        const groupManagerStore = useGroupManagerStore()
        const courseStore = useCourseStore()
        const userScoreDisabled = ref('')
        const courseManagerStore = useEmployeeStore()
        const user = ref([])
        let language = ref()
        let myemail = ref()
        let email = ref()
        let firstname = ref()
        let lastname = ref()
        let score = ref()
        let visible = ref()
        let faculty = ref(null)
        let facTable = ref(null)
        let group = ref(null)
        let course = ref(null)
        let requestedUsers = ref([])
        let requestedUsersEmails = ref([])
        let requestedFacultyManagersEmails = ref([])
        let managedFaculties = ref([])
        let requestedFaculties = ref([])
        let managedGroups = ref([])
        let managedGroupsNames = ref([])
        let managedCourses = ref([])
        let managedCoursesNames = ref([])
        const id = sessionStorage.getItem('id')

        // prepare table of faculty managers (for deleting them)
        const columnsFacTable = [
            { name: 'faculty', required: true, label: 'Fakultät', align: 'left', field: row => row.faculty, format: val => `${val}`, sortable: true},
            { name: 'manager', align: 'center', label: 'Manager', field: 'manager', sortable: true },
            { name: 'action', label: 'Aktion', field: 'action', sortable: true },
        ]
        const rowsFacTable = ref([])

        // prepare table of group managers (for deleting them)
        const columnsGroupTable = [
            { name: 'group', required: true, label: 'Gruppe', align: 'left', field: row => row.group, format: val => `${val}`, sortable: true},
            { name: 'manager', align: 'center', label: 'Manager', field: 'manager', sortable: true },
            { name: 'action', label: 'Aktion', field: 'action', sortable: true },
        ]
        const rowsGroupTable = ref([])

        // prepare table of course managers (for deleting them)
        const columnsCourseTable = [
            { name: 'course', required: true, label: 'Kurs', align: 'left', field: row => row.course, format: val => `${val}`, sortable: true},
            { name: 'manager', align: 'center', label: 'Manager', field: 'manager', sortable: true },
            { name: 'action', label: 'Aktion', field: 'action', sortable: true },
        ]
        const rowsCourseTable = ref([])

        // faculty manager to be saved
        const newFacultyManager = ref({
                userID: "",
                facultyID: ""
            }
        )
        // faculty manager to be deleted
        const oldFacultyManager = ref({
                userID: "",
                facultyID: ""
            }
        )
        // group manager to be deleted
        const oldGroupManager = ref({
                userID: "",
                groupID: ""
            }
        )
        // course manager ("employee") to be deleted
        const oldEmployee = ref({
                userID: "",
                courseID: ""
            }
        )
        // group manager to be saved
        const newGroupManager = ref({
                userID: "",
                groupID: ""
            }
        )
        const newEmployee = ref({
            userID: "",
            courseID: ""
        })
        const temp = ref([])
        const temp2 = ref([])
        const temp3 = ref([])

        requestall()

        function requestall() {

            // get the faculties the user is manager for
            userFacultyStore.requestFaculties().then(() => {
                temp.value = userFacultyStore.getFaculties()
                for (const argumentsKey in temp.value) {
                    managedFaculties.value.push(temp.value[argumentsKey].name)
                    requestedFaculties.value.push(temp.value[argumentsKey])
                }
                temp2.value = temp.value;
                for (const k in temp2.value) {
                    groupStore.requestGroupsByFaculty(temp2.value[k].id).then(() => {
                        temp.value = groupStore.getGroups()
                        for (const argumentsKey in temp.value) {
                            managedGroupsNames.value.push(temp.value[argumentsKey].name)
                            managedGroups.value.push(temp.value[argumentsKey])
                        }
                    })
                    temp3.value = temp.value;
                    for (const l in temp3.value) {
                        courseStore.requestCoursesByGroup(temp3.value[l].id).then(() => {
                            temp.value = courseStore.getCoursesOverview()
                            for (const argumentsKey in temp.value) {
                                managedCoursesNames.value.push(temp.value[argumentsKey].name)
                                managedCourses.value.push(temp.value[argumentsKey])
                            }
                        })
                    }
                }
            })

            // get the groups the current user is manager for
            groupStore.requestGroups().then(() => {
                temp.value = groupStore.getGroups()
                for (const argumentsKey in temp.value) {
                    managedGroupsNames.value.push(temp.value[argumentsKey].name)
                    managedGroups.value.push(temp.value[argumentsKey])
                }
                temp3.value = temp.value;
                for (const l in temp3.value) {
                    courseStore.requestCoursesByGroup(temp3.value[l].id).then(() => {
                        temp.value = courseStore.getCoursesOverview()
                        for (const argumentsKey in temp.value) {
                            managedCoursesNames.value.push(temp.value[argumentsKey].name)
                            managedCourses.value.push(temp.value[argumentsKey])
                        }
                    })
                }
            })

            // get the courses the current user is manager for
            courseStore.requestCourses().then(() => {
                temp.value = courseStore.getCoursesOverview()
                for (const argumentsKey in temp.value) {
                    managedCoursesNames.value.push(temp.value[argumentsKey].name)
                    managedCourses.value.push(temp.value[argumentsKey])
                }
            })

            // get possible users to be assigned a new rule
            userStore.requestUsers().then(() => {
                temp.value = userStore.getUsers()
                for (const argumentsKey in temp.value) {
                    requestedUsersEmails.value.push(temp.value[argumentsKey].email)
                    requestedUsers.value.push(temp.value[argumentsKey])
                    requestedUsers.value.push(temp.value[argumentsKey])
                }
                email.value = userStore.getUser(id).email
                myemail.value = email.value
                firstname.value = userStore.getUser(id).firstname
                lastname.value = userStore.getUser(id).lastname
                axios.get("/api/ranking/users/" + id).then(r => {
                    score.value = r.data[0].points
                })
                userScoreDisabled.value = userStore.getUser(id).userScoreDisabled
                visible.value = userStore.getUser(id).visible
            })

            // get deletable faculty managers
            facultyManagerStore.requestMyDeletableFacManagers().then(() => {
                temp.value = facultyManagerStore.getManagers();
                for (const argumentsKey in temp.value) {
                    const rowEntry = {
                        facManagerID: temp.value[argumentsKey].id,
                        facManagerUserName: temp.value[argumentsKey].user.email,
                        facManagerUserID: temp.value[argumentsKey].user.id,
                        facManagerFacultyName: temp.value[argumentsKey].faculty.name,
                        facManagerFacultyID: temp.value[argumentsKey].faculty.id
                    }
                    rowsFacTable.value.push(rowEntry)
                }
            })

            // get deletable group managers
            groupManagerStore.requestMyDeletableGroupManagers().then(() => {
                temp.value = groupManagerStore.getManagers();
                for (const argumentsKey in temp.value) {
                    const rowEntry = {
                        groupManagerID: temp.value[argumentsKey].id,
                        groupManagerUserName: temp.value[argumentsKey].user.email,
                        groupManagerUserID: temp.value[argumentsKey].user.id,
                        groupManagerGroupName: temp.value[argumentsKey].accessGroup.name,
                        groupManagerGroupID: temp.value[argumentsKey].accessGroup.id
                    }
                    rowsGroupTable.value.push(rowEntry)
                }
            })

            // get deletable course managers
            courseManagerStore.requestMyDeletableEmployees().then(() => {
                temp.value = courseManagerStore.getEmployees();
                for (const argumentsKey in temp.value) {
                    const rowEntry = {
                        courseManagerID: temp.value[argumentsKey].id,
                        courseManagerUserName: temp.value[argumentsKey].user.email,
                        courseManagerUserID: temp.value[argumentsKey].user.id,
                        courseManagerCourseName: temp.value[argumentsKey].accessCourse.name,
                        courseManagerCourseID: temp.value[argumentsKey].accessCourse.id
                    }
                    rowsCourseTable.value.push(rowEntry)
                }
            })

        }

        function addFacultyManager() {
            // get name and ID of selected faculty
            let facName = faculty.value
            let facID = ""
            for (let i = 0; i < requestedFaculties.value.length; i++) {
                if (requestedFaculties.value[i].name === facName) {
                    facID = requestedFaculties.value[i].id
                }
            }
            // store faculty ID
            newFacultyManager.value.facultyID = facID
            // get name and ID of selected user
            let userName = user.value
            let userID = ""
            for (let i = 0; i < requestedUsers.value.length; i++) {
                if (requestedUsers.value[i].email === userName) {
                    userID = requestedUsers.value[i].id
                }
            }
            // store user ID
            newFacultyManager.value.userID = userID
            // add new faculty manager to DB
            if (facID === "" || userID === "") {
                $q.notify({
                    type: 'negative',
                    message: 'Request failed',
                    caption: 'Requested user or faculty not valid',
                })
                return null;
            } else {
                userStore.requestAddFacultyManager(newFacultyManager).then(() => {
                    $q.notify({
                        type: 'positive',
                        message: 'Successfully added new faculty manager',
                    })
                }).catch(() => {
                    $q.notify({
                        type: 'negative',
                        message: 'Request failed',
                        caption: 'Request denied'
                    })
                })
            }
        }

        function deleteFacultyManager(rowProps) {
            // get name and ID of selected faculty
            oldFacultyManager.value.facultyID = rowProps.facManagerFacultyID;
            oldFacultyManager.value.userID = rowProps.facManagerUserID;

            // delete faculty manager from DB
            if (oldFacultyManager.value.facultyID === "" || oldFacultyManager.value.userID === "") {
                $q.notify({
                    type: 'negative',
                    message: 'Request failed',
                    caption: 'Requested user or faculty not valid',
                })
                return null;
            } else {
                userStore.requestDeleteFacultyManager(oldFacultyManager).then(() => {
                    $q.notify({
                        type: 'positive',
                        message: 'Successfully deleted faculty manager',
                    })
                }).catch(() => {
                    $q.notify({
                        type: 'negative',
                        message: 'Request failed',
                        caption: 'Request denied'
                    })
                })
            }
        }

        function addGroupManager() {
            // get name and ID of selected group
            let groupName = group.value
            let groupID = ""
            for (let i = 0; i < managedGroups.value.length; i++) {
                if(managedGroups.value[i].name === groupName) {
                    groupID = managedGroups.value[i].id
                }
            }
            // store group ID
            newGroupManager.value.groupID = groupID
            // get name and ID of selected user
            let userName = user.value
            let userID = ""
            for (let i = 0; i < requestedUsers.value.length; i++) {
                if(requestedUsers.value[i].email === userName) {
                    userID = requestedUsers.value[i].id
                }
            }
            // store user ID
            newGroupManager.value.userID = userID
            // add new group manager to DB
            if (groupID === "" || userID === "") {
                $q.notify({
                    type: 'negative',
                    message: 'Request failed',
                    caption: 'Requested user or Access Group not valid',
                })
                return null;
            } else {
                userStore.requestAddGroupManager(newGroupManager).then(() => {
                    $q.notify({
                        type: 'positive',
                        message: 'Successfully added new Access Group Manager',
                    })
                }).catch(() => {
                    $q.notify({
                        type: 'negative',
                        message: 'Request failed',
                        caption: 'Request denied'
                    })
                })
            }
        }

        function deleteGroupManager(rowProps) {
            // get name and ID of selected group
            oldGroupManager.value.groupID = rowProps.groupManagerGroupID;
            oldGroupManager.value.userID = rowProps.groupManagerUserID;
            // delete group manager from DB
            if (oldGroupManager.value.groupID === "" || oldGroupManager.value.userID === "") {
                $q.notify({
                    type: 'negative',
                    message: 'Request failed',
                    caption: 'Requested user or group not valid',
                })
                return null;
            } else {
                userStore.requestDeleteGroupManager(oldGroupManager).then(() => {
                    $q.notify({
                        type: 'positive',
                        message: 'Successfully deleted group manager',
                    })
                }).catch(() => {
                    $q.notify({
                        type: 'negative',
                        message: 'Request failed',
                        caption: 'Request denied'
                    })
                })
            }
        }

        function addEmployee() {
            // get name and ID of selected faculty
            let courseName = course.value
            let courseID = ""
            for (let i = 0; i < managedCourses.value.length; i++) {
                if(managedCourses.value[i].name === courseName) {
                    courseID = managedCourses.value[i].id;
                }
            }
            // store group ID
            newEmployee.value.courseID = courseID;
            // get name and ID of selected user
            let userName = user.value
            let userID = ""
            for (let i = 0; i < requestedUsers.value.length; i++) {
                if(requestedUsers.value[i].email === userName) {
                    userID = requestedUsers.value[i].id
                }
            }
            // store user ID
            newEmployee.value.userID = userID
            // add new employee (course manager) to DB
            if (courseID === "" || userID === "") {
                $q.notify({
                    type: 'negative',
                    message: 'Request failed',
                    caption: 'Requested user or access group not valid',
                })
                return null;
            } else {
                userStore.requestAddEmployee(newEmployee).then(() => {
                    $q.notify({
                        type: 'positive',
                        message: 'New employee successfully added',
                    })
                }).catch(() => {
                    $q.notify({
                        type: 'negative',
                        message: 'Request failed',
                        caption: 'Request denied'
                    })
                })
            }
        }

        function deleteEmployee(rowProps) {
            // get name and ID of selected group
            oldEmployee.value.courseID = rowProps.courseManagerCourseID;
            oldEmployee.value.userID = rowProps.courseManagerUserID;

            // delete group manager from DB
            if (oldEmployee.value.courseID === "" || oldEmployee.value.userID === "") {
                $q.notify({
                    type: 'negative',
                    message: 'Request failed',
                    caption: 'Requested user or course not valid',
                })
                return null;
            } else {
                userStore.requestDeleteEmployee(oldEmployee).then(() => {
                    $q.notify({
                        type: 'positive',
                        message: 'Successfully deleted course manager',
                    })
                    // neu laden???
                }).catch(() => {
                    $q.notify({
                        type: 'negative',
                        message: 'Request failed',
                        caption: 'Request denied'
                    })
                })
            }
        }


        function save_user_changes() {
            const updatedUser = ref({
                id: id,
                email: "",
                firstname: "",
                lastname: "",
                //userScoreDisabled: userScoreDisabled,
                visible: false
            })
            if ((email.value === "") || (firstname.value === "") || (lastname.value === "")) {
                $q.notify({
                    type: 'negative',
                    message: 'Benutzer Informationen ändern fehlgeschlagen.',
                    caption: 'Angabe leer'
                })

            } else {
                updatedUser.value.email = email.value
                updatedUser.value.firstname = firstname.value
                updatedUser.value.lastname = lastname.value
                updatedUser.value.visible = visible.value
                userStore.requestUpdateUserInformation(updatedUser.value).then(() => {
                    $q.notify({
                        type: 'positive',
                        message: 'Benutzer Informationen geändert.',
                    })
                })
                    .catch((error) => {
                        console.error(error)

                        $q.notify({
                            type: 'negative',
                            message: 'Benutzer Informationen ändern fehlgeschlagen.',
                            caption: 'Beim ändern der Benutzer Informationen ist ein Fehler aufgetreten.'
                        })
                    })
                if (!(email.value === myemail.value)) {
                    userStore.logout();
                    router.push("/login");
                }
            }
        }

        function newlanguage() {
            locale.value = language.value
        }

        return {
            t,
            id,
            user,
            email,
            firstname,
            lastname,
            language,
            tab: ref('data'),
            languages: ["de", "en"],
            columnsFacTable,
            rowsFacTable,
            columnsGroupTable,
            rowsGroupTable,
            columnsCourseTable,
            rowsCourseTable,
            faculty,
            facTable,
            group,
            course,
            managedGroups,
            managedGroupsNames,
            managedCourses,
            managedCoursesNames,
            requestedUsers,
            requestedUsersEmails,
            managedFaculties,
            requestedFacultyManagersEmails,
            save_user_changes,
            newlanguage,
            addFacultyManager,
            deleteFacultyManager,
            addGroupManager,
            score,
            userScoreDisabled,
            visible,
            deleteGroupManager,
            addEmployee,
            deleteEmployee

        }
    }
}
</script>

<style scoped>

</style>
