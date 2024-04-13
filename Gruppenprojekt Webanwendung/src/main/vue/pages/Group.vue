<template>

    <div v-if="group" class="router" >

        <div v-if="faculty" class="header">
            <div class="headerLeft">
                <div class="headnav" style="text-align: left">
                    <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
                    <router-link :to="'/Faculty/'+ faculty.id" class="navelem">{{ faculty.name }}</router-link> <q-icon name="chevron_right" size="16px"/>
                    <p class="navelem navactive">{{group.name}}</p>
                </div>
                <h1>{{group.name}}</h1>
            </div>

            <div v-if="score <50" style="display: flex; flex-direction: row; align-items: center; justify-content: center">
                <h4>{{$t('database.group_score')}}</h4> <p class="scoreRect scoreNegative">{{ score }}%</p>
            </div>
            <div v-else style="display: flex; flex-direction: row; align-items: center; justify-content: center">
                <h4>{{$t('database.group_score')}}</h4> <p class="scoreRect">{{ score }}%</p>
            </div>
            <q-icon name="info" style="color: #3F50B5; padding-top: 4vh;" size="sm">
                <q-tooltip>
                    {{ $t("group.infoGroup") }}
                </q-tooltip>
            </q-icon>
        </div>

        <div class="row items-center" >
            <div class="manager">
                {{$t('database.group_managers')}}
                :
                <q-chip v-if="group_managers.length >= 1" v-for="manager in group_managers" icon="person" >
                    {{manager.firstname}} {{manager.lastname}}
                </q-chip>
                <q-chip v-else icon="error" color="warning">
                    {{$t('database.no_manager_exists')}}
                </q-chip>
            </div>
        </div>

        <div class="buttonclass" v-if="allowedToEdit">
            <q-btn class="buttonitem" @click="addCourse" outline rounded icon="post_add" color="black">{{$t('manage.add_course')}}</q-btn>
            <div class="buttonitem">
                <q-btn flat round color="black" icon="more_vert" />
                <q-menu touch-position>
                    <q-list  style="min-width: 100px">
                        <q-item clickable v-close-popup color="primary" rounded icon="edit" >
                            <q-item-section @click="preEdit">{{$t('manage.edit')}}</q-item-section>
                        </q-item>
                        <q-item clickable v-close-popup color="red" rounded icon="delete" >
                            <q-item-section @click="confirm_group_delete = true">{{$t('manage.delete')}}</q-item-section>
                        </q-item>
                    </q-list>
                </q-menu>
            </div>
        </div>

        <div class="row">
            <div class="col">
                <q-table
                    :title="$tc('database.access_course',2)"
                    :columns="columns"
                    :rows="course_row"
                    row-key="id"
                    flat
                    @row-click="routeToCourse"
                >
                    <template v-slot:header="props">
                        <q-tr :props="props">
                            <q-th
                                v-for="col in props.cols"
                                :key="col.name"
                                :props="props"
                                style="text-decoration: underline; font-size: 1.3em;"
                            >
                                {{ col.label }}
                            </q-th>
                        </q-tr>
                    </template>
                    <template v-slot:body="props">
                        <q-tr :props="props">
                            <q-td key="id" :props="props">
                                {{ props.row.id }}
                            </q-td>
                            <q-td key="name" :props="props">
                                <q-btn outline color="black" rounded @click="routeToCourse(props.row)">
                                    <q-icon v-if="!props.row.visible" name="visibility_off" size="16px" class="q-pr-sm" /> {{ props.row.name }}
                                </q-btn>

                            </q-td>
                            <q-td key="employees" :props="props">
                                <q-chip v-for="empl in props.row.employees" icon="person">
                                    {{ empl }}
                                </q-chip>
                            </q-td>
                            <q-td key="weighting" :props="props">
                                <q-chip v-if="props.row.weighting.toString() === $t('course_page.weight_strong')" color="orange-5">{{ props.row.weighting }}</q-chip>
                                <q-chip v-else color="orange-2">{{ props.row.weighting }}</q-chip>
                            </q-td>
                            <q-td key="score" :props="props">
                                <p v-if="props.row.score < 50" class="scoreRectSmall scoreNegative">{{ props.row.score }}%</p>
                                <p v-else class="scoreRectSmall">{{ props.row.score }}%</p>
                            </q-td>
                        </q-tr>
                    </template>
                </q-table>
            </div>
        </div>
        <div>
            <q-dialog v-model="edit_group" persistent>
                <q-card style="width: 700px; max-width: 80vw;">
                    <q-card-section>
                        <div class="text-h5 q-mb-md">{{ $t('manage.update_group') }}</div>
                        <q-separator inset/>
                        <div class="q-mb-md">
                            <q-input
                                outlined
                                rounded
                                :label="$t('database.group_name')"
                                v-model="new_name"
                            >
                                <template v-slot:prepend>
                                    <q-icon name="edit" />
                                </template>
                            </q-input>
                        </div>
                        <div >
                            <q-select
                                outlined
                                rounded
                                v-model="new_faculty"
                                :options="faculties"
                                option-label="name"
                                :label="$t('database.faculty_of_group')"
                            >
                                <template v-slot:prepend>
                                    <q-icon name="edit" />
                                </template>
                            </q-select>
                        </div>
                    </q-card-section>

                    <q-card-actions align="right">
                        <q-btn
                            flat
                            :label="$t('manage.cancel')"
                            color="primary"
                            v-close-popup
                        />

                        <q-btn
                            flat
                            :label="$t('manage.save')"
                            color="primary"
                            v-close-popup
                            @click="updateGroup(new_name, new_faculty)"
                        />
                    </q-card-actions>
                </q-card>
            </q-dialog>
        </div>

        <div>
            <q-dialog v-model="confirm_group_delete" persistent>
                <q-card>
                    <q-card-section>
                        <div class="text-h5 q-mb-md">{{ $t('manage.delete_selected_group') }}</div>
                        <q-separator inset/>

                        <div>
                            {{$t('manage.delete_confirm')}}
                        </div>
                    </q-card-section>

                    <q-card-actions align="right">
                        <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                        <q-btn flat :label="$t('manage.delete')" color="red" v-close-popup @click="deleteGroup"/>
                    </q-card-actions>
                </q-card>
            </q-dialog>
        </div>

    </div>
</template>

<script>
import {useRoute} from "vue-router";
import router from "../router";
import {useFacultyStore} from "../stores/faculties";
import {useGroupStore} from "../stores/groups";
import {useCourseStore} from "../stores/courses";
import {useEmployeeStore} from "../stores/employees";
import {useGroupManagerStore} from "../stores/groupManagers";
import {useI18n} from "vue-i18n";
import {ref} from "vue";
import {useQuasar} from "quasar";
import EntityList from "../components/EntityList.vue";

export default {
    name: "Group",
    emits: ['updateRecentGroups'],
    components: {
        EntityList
    },
    methods: {
        filterMethod(rows, terms) {
            let lowerCaseTerm = terms.toLowerCase()
            return rows.filter(row => row.name.toLowerCase().startsWith(lowerCaseTerm))
        },
    },
    setup() {
        const {t} = useI18n({})
        const $q = useQuasar()

        const route = useRoute()
        const groupID = route.params.id         // Group id
        let group = ref()             // Group object
        const score = ref("")
        const allowedToEdit = ref(false)

        // Boolean to check if user wants to edit group
        const edit_group = ref(false)
        const new_name = ref("")
        const new_faculty = ref()
        const faculties = ref([])
        const confirm_group_delete = ref(false)

        // Stores
        const facultyStore = useFacultyStore()
        const groupStore = useGroupStore()
        const courseStore = useCourseStore()
        const employeeStore = useEmployeeStore()
        const groupManagerStore = useGroupManagerStore()

        let faculty = ref([])           // Faculty of group
        const courses = ref([])             // courses of group
        const courses_I_manage = ref([])    // courses that the current user manages
        const group_managers = ref([])      // Managers of group

        // values for table
        const course_table_title = t('database.course')     // Title of table
        const course_row = ref([])          // (Rows of table)

        // columns for table
        const columns = [
            {
                name: 'id',
                align: 'center',
                label: t('database.id'),
                field: row => row.id
            },
            {
                name: 'name',
                align: 'left',
                label: t('database.name'),
                field: row => row.name
            },
            {
                name: 'employees',
                align: 'left',
                label: t('database.employee'),
                field: row => row.employees.toString()
            },
            {
                name: 'weighting',
                align: 'center',
                label: t('manage.weighting'),
                field: row => row.weighting
            },
            {
                name: 'score',
                align: 'center',
                label: t('database.score'),
                field: row => row.score
            }
        ]

        loadGroup()
        loadCourses()
        loadCoursesIManage()
        loadFaculties()
        loadGroupManagers()
        loadScore()
        checkAuthorization()

        function checkAuthorization() {
            groupStore.requestAllowEdit(groupID).then(res => {
                allowedToEdit.value = res.data
            })
        }

        // loads group score
        function loadScore() {
            groupStore.getScore(groupID).then(res => {
                score.value = res.data
            })
        }

        // deletes group
        function deleteGroup() {
            groupStore.requestDeleteGroup(group.value).then(() => {
                $q.notify({
                    type: 'positive',
                    message: t("manage.changes_saved"),
                    caption: t("manage.changes_saved_in_db")
                })
                router.push('../../Dashboard')
            }).catch(() => {
                $q.notify({
                    type: 'negative',
                    message: t("manage.changes_failed"),
                    caption: t("manage.changes_could_not_save")
                })
            })

        }

        // loads all group managers of current group
        function loadGroupManagers() {
            groupManagerStore.requestManagers(groupID).then(() => {
                group_managers.value = groupManagerStore.getManagers()
            })
        }

        // routes to AddAccessCourse
        function addCourse() {
            router.push('../AddAccessCourse')
        }

        // updates group with new name and faculty to backend
        function updateGroup(change_name, change_faculty) {
            if (group == null) {
                $q.notify({
                    type: 'negative',
                    message: t('manage.no_course_available'),
                    caption: t('manage.no_couse_selected')
                })
            }
            let new_group = {
                id: group.value.id,
                name: change_name,
                score: group.value.score,
                faculty: change_faculty
            }
            groupStore.requestUpdateGroup(new_group).then(() => {
                $q.notify({
                    type: 'positive',
                    message: t("manage.changes_saved"),
                    caption: t("manage.changes_saved_in_db")
                })
            }).catch(() => {
                $q.notify({
                    type: 'negative',
                    message: t("manage.changes_failed"),
                    caption: t("manage.changes_could_not_save")
                })
            })

            faculty.value = change_faculty
            if (faculty.value.id != change_faculty.id) {
                router.push('../')
            }
        }

        function loadFaculties() {
            facultyStore.requestFaculties().then(() => {
                faculties.value = facultyStore.getFaculties()
            })
        }

        // to set up edit dialog. gets called before editing
        function preEdit() {
            edit_group.value = true
            new_faculty.value = group.value.faculty
            new_name.value = group.value.name
        }

        // loads group with given groupID
        function loadGroup() {
            //noinspection JSUnresolvedFunction
            groupStore.requestGroup(groupID).then(() => {
                group.value = groupStore.getGroup(groupID)
                faculty.value = groupStore.getGroup(groupID).faculty
                document.title = group.value.name
            })

            groupStore.getScore(groupID).then(res => {
                score.value = res.data
            })
        }

        // routes to selected course
        function routeToCourse(course) {
            router.push('/Course/' + course.id.toString())
        }

        // loads all courses of group
        function loadCourses() {
            courseStore.requestCoursesByGroup(groupID).then(() => {
                courses.value = courseStore.getCoursesOverview()
                courses.value.forEach(course_entry => {
                    let employees = []
                    employeeStore.requestEmployees(course_entry.id).then(() => {
                        employees = employeeStore.getEmployees()
                        let employeeNames = []
                        employees.forEach(employee => {
                            employeeNames.push(employee.firstname.toString() + " " + employee.lastname.toString())
                        })
                        let course_score = -1
                        courseStore.getScore(course_entry.id).then(res => {
                            course_score = res.data
                            const row_entry = {
                                id: course_entry.id,
                                name: course_entry.name,
                                employees: employeeNames,
                                weighting: convertWeightedToString(course_entry.weighted),
                                score: course_score
                            }
                            course_row.value.push(row_entry)
                        })

                    })
                })
                loadCoursesIManage()
            })
        }

        function convertWeightedToString(weighted) {
            const strong = t('course_page.weight_strong')
            const weak = t('course_page.weight_weak')
            if (weighted) {
                return strong
            } else {
                return weak
            }
        }

        // loads all courses the user manages
        function loadCoursesIManage() {
            courseStore.requestCourses().then(() => {
                courses_I_manage.value = courseStore.getCoursesOverview()
            })
        }

        return {
            allowedToEdit,
            confirm_group_delete,
            deleteGroup,
            group_managers,
            addCourse,
            score,
            preEdit,
            updateGroup,
            faculties,
            new_faculty,
            new_name,
            edit_group,
            routeToCourse,
            course_table_title,
            columns,
            course_row,
            loadCourses,
            group,
            faculty,
            courses,
            courses_I_manage
        }
    }
}
</script>

<style scoped>

</style>
