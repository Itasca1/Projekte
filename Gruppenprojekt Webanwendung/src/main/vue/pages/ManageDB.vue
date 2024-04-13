<template>

    <div class="q-pa-md">
        <EntityList
            :title="faculty_table_title"
            :columns="faculty_columns"
            :rows="faculty_rows"
            row_key="id"
            selection="single"
            v-model:selected="selected_faculty"

            :filter_method="filterMethod"
            :selection_method="facultySelection"
            :filter_placeholder="$t('manage.search')"
            :no_data_message="$t('manage.no_faculty_available')"
        />
    </div>
    <div v-if="selected_faculty.length>0" v-for="input_obj in faculty_input_list" :key="input_obj">
        <q-input
            v-model="input_obj.new_value.value"
            :label="input_obj.label"
            :readonly="input_obj.readonly"
            dense
        />
    </div>
    <div>
        <q-btn v-if="selected_faculty.length>0" color="primary" :label="$t('manage.confirm')" rounded
               @click="save_faculty_changes"/>
    </div>
    <div class="q-pa-md">
        <EntityList
            :title="group_table_title"
            :columns="group_columns"
            :rows="group_row"
            row_key="id"
            selection="single"
            v-model:selected="selected_group"

            :filter_method="filterMethod"
            :selection_method="groupSelection"
            :filter_placeholder="$t('manage.search')"
            :no_data_message="$t('manage.no_group_available')"
        />
    </div>
    <div v-if="selected_group.length>0" v-for="input_obj in group_input_list" :key="input_obj">
        <div v-if="input_obj.label === group_input_list[3].label">
            <q-select
                v-model="input_obj.new_value.value"
                :options="faculties"
                option-label="name"
                :label="$tc('database.faculty', 2)"
                dense
            >
            </q-select>
        </div>
        <div v-else>
            <q-input
                v-model="input_obj.new_value.value"
                :label="input_obj.label"
                :readonly="input_obj.readonly"
                dense
            />
        </div>
    </div>
    <div>
        <q-btn v-if="selected_group.length>0" color="primary" :label="$t('manage.confirm')" rounded
               @click="save_group_changes"/>
        <q-btn v-if="selected_group.length>0" color="red" icon="delete" rounded fab-mini
               @click="confirm_group_delete = true"/>
    </div>
    <div class="q-pa-md">
        <EntityList
            :title="course_table_title"
            :columns="course_columns"
            :rows="course_row"
            row_key="id"
            selection="single"
            v-model:selected="selected_course"

            :filter_method="filterMethod"
            :selection_method="fillCourseInputs"
            :filter_placeholder="$t('manage.search')"
            :no_data_message="$t('manage.no_course_available')"
        />
    </div>
    <div v-if="selected_course.length>0" v-for="input_obj in course_input_list" :key="input_obj">
        <div v-if="input_obj.label === course_input_list[3].label">
            <q-select
                v-model="input_obj.new_value.value"
                :options="groups"
                option-label="name"
                :label="$t('database.group')"
                dense
            >
            </q-select>
        </div>
        <div v-else-if="input_obj.label === course_input_list[4].label">
                <q-checkbox v-model="input_obj.new_value.value" :label="$t('database.course_public')"/>
        </div>
        <div v-else>
            <q-input
                v-model="input_obj.new_value.value"
                :label="input_obj.label"
                :readonly="input_obj.readonly"
                dense
            />
        </div>
    </div>
    <div>
        <q-btn v-if="selected_course.length>0" color="primary" :label="$t('manage.confirm')" rounded
               @click="save_course_changes"/>
        <q-btn v-if="selected_course.length>0" color="red" icon="delete" rounded fab-mini
               @click="confirm_course_delete = true"/>
    </div>
    <div>
        <q-dialog v-model="confirm_faculty_change" persistent>
            <q-card>
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.update_faculty') }}</div>
                    <q-separator inset/>
                    <div v-for="elem in faculty_input_list" :key="elem">
                        <div>
                            {{ elem.label }}:
                            <b> {{ elem.new_value.value }} </b>
                        </div>
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                    <q-btn flat :label="$t('manage.save')" color="primary" v-close-popup
                           @click="update_faculty_changes"/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>
    <div>
        <q-dialog v-model="confirm_group_change" persistent>
            <q-card>
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.update_group') }}</div>
                    <q-separator inset/>
                    <div v-for="elem in group_input_list" :key="elem">
                        <div v-if="elem.label === $t('database.faculty')">
                            {{ elem.label }}:
                            <b> {{ elem.new_value.value.name }} </b>
                        </div>
                        <div v-else>
                            {{ elem.label }}:
                            <b> {{ elem.new_value.value }} </b>
                        </div>
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                    <q-btn flat :label="$t('manage.save')" color="primary" v-close-popup @click="update_group_changes"/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>
    <div>
        <q-dialog v-model="confirm_course_change" persistent>
            <q-card>
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.update_course') }}</div>
                    <q-separator inset/>
                    <div v-for="elem in course_input_list" :key="elem">
                        <div v-if="elem.label === $t('database.group')">
                            {{ elem.label }}:
                            <b> {{ elem.new_value.value.name }} </b>
                        </div>
                        <div v-else-if="elem.label === $t('database.course_public')">
                            {{ elem.label }}:
                            <b> {{ elem.new_value.value }} </b>
                        </div>
                        <div v-else>
                            {{ elem.label }}:
                            <b> {{ elem.new_value.value }} </b>
                        </div>
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                    <q-btn flat :label="$t('manage.save')" color="primary" v-close-popup
                           @click="update_course_changes"/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>
    <div>
        <q-dialog v-model="confirm_faculty_delete" persistent>
            <q-card>
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.delete_selected_faculty') }}</div>
                    <q-separator inset/>

                    <div>
                        {{ $t('database.faculty_id') }}: <b>{{ selected_faculty[0].id }}</b>
                    </div>
                    <div>
                        {{ $t('database.faculty_name') }}: <b>{{ selected_faculty[0].name }}</b>
                    </div>
                    <div>
                        {{ $t('database.faculty_score') }}: <b>{{ selected_faculty[0].score }}</b>
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                    <q-btn flat :label="$t('manage.delete')" color="primary" v-close-popup @click="deleteFaculty"/>
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
                        {{ $t('database.group_id') }}: <b>{{ selected_group[0].id }}</b>
                    </div>
                    <div>
                        {{ $t('database.group_name') }}: <b>{{ selected_group[0].name }}</b>
                    </div>
                    <div>
                        {{ $t('database.semester') }}: <b>{{ selected_group[0].semester }}</b>
                    </div>
                    <div>
                        {{ $t('database.group_score') }}: <b>{{ selected_group[0].score }}</b>
                    </div>
                    <div>
                        {{ $t('database.faculty_of_group') }}: <b>{{ selected_group[0].faculty.name }}</b>
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                    <q-btn flat :label="$t('manage.delete')" color="primary" v-close-popup @click="deleteGroup"/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>

    <div>
        <q-dialog v-model="confirm_course_delete" persistent>
            <q-card>
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.delete_selected_course') }}</div>
                    <q-separator inset/>

                    <div>
                        {{ $t('database.course_id') }}: <b>{{ selected_course[0].id }}</b>
                    </div>
                    <div>
                        {{ $t('database.course_name') }}: <b>{{ selected_course[0].name }}</b>
                    </div>
                    <div>
                        {{ $t('database.course_score') }}: <b>{{ selected_course[0].score }}</b>
                    </div>
                    <div>
                        {{ $t('database.group_of_course') }}: <b>{{ selected_course[0].group.name }}</b>
                    </div>
                </q-card-section>

                <q-card-actions align="right">
                    <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                    <q-btn flat :label="$t('manage.delete')" color="primary" v-close-popup @click="deleteCourse"/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>


</template>

<script>

import {ref} from 'vue'
import {useGroupStore} from "../stores/groups";
import {useCourseStore} from "../stores/courses";
import {useFacultyStore} from "../stores/faculties";
import CreateNewEntityForm from "../components/CreateNewEntityForm.vue";
import EntityList from "../components/EntityList.vue";
import {useQuasar} from "quasar";
import {useI18n} from "vue-i18n";

export default {
    name: "Manage_DB",
    components: {
        CreateNewEntityForm,
        EntityList
    },
    methods: {
        filterMethod(rows, terms) {
            let lowerCaseTerm = terms.toLowerCase()
            return rows.filter(row => row.name.toLowerCase().startsWith(lowerCaseTerm))
        },
    },
    setup() {
        const $q = useQuasar()
        const {t} = useI18n({})


        const faculty_table_title = t('database.faculty')
        const group_table_title = t('database.access_group')
        const course_table_title = t('database.access_course')

        let faculty_filter = ref('')
        let group_filter = ref('')
        let course_filter = ref('')

        const faculty_rows = ref([])         // Rows in Faculty-Table
        const faculties = ref([])           // all faculties of database
        const faculty_names = ref([])
        const facultyStore = useFacultyStore()      // FacultyStore for requests (api stuff)
        const selected_faculty = ref([])        // From user selected faculty

        const group_row = ref([])           // Rows in Group-Table
        const groups = ref([])          // stores all groups from user (before sorting for faculty)
        const groupStore = useGroupStore()          // GroupStore for requests (api stuff)
        let selected_group = ref([])          // From User selected group (row in table)

        const course_row = ref([])          // Rows in Course-Table
        const courses = ref([])                      // stores all courses from user
        const courseStore = useCourseStore()        // CourseStore for requests
        const selected_course = ref([])

        // Boolean Variables for Dialog saving changes
        let confirm_faculty_change = ref(false)
        let confirm_group_change = ref(false)
        let confirm_course_change = ref(false)

        // Boolen Variables for Dialog deleting entity
        let confirm_faculty_delete = ref(false)
        let confirm_group_delete = ref(false)
        let confirm_course_delete = ref(false)

        // Columns of faculty table
        const faculty_columns = [
            {
                name: 'id',
                align: 'center',
                label: t('database.id'),
                field: row => row.id,
                sortable: true,
                sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
            },
            {
                name: 'name',
                align: 'left',
                label: t('database.name'),
                field: row => row.name,
                format: val => `${val}`,
                sortable: true
            },
            {
                name: 'score',
                align: 'center',
                label: t('database.score'),
                field: row => row.score,
                sortable: true,
                sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
            }
        ]

        // Coulumns of group table
        const group_columns = [
            {
                name: 'id',
                align: 'center',
                label: t('database.id'),
                field: row => row.id,
                sortable: true,
                sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
            },
            {
                name: 'name',
                align: 'left',
                label: t('database.name'),
                field: row => row.name,
                format: val => `${val}`,
                sortable: true
            },
            {
                name: 'score',
                align: 'center',
                label: t('database.score'),
                field: row => row.score,
                sortable: true,
                sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
            },
            {
                name: 'faculty',
                align: 'left',
                label: t('database.faculty'),
                field: row => row.faculty.name,
                format: val => `${val}`,
                sortable: true
            }
        ]

        // Columns of course table
        const course_columns = [
            {
                name: 'id',
                align: 'center',
                label: t('database.id'),
                field: row => row.id,
                sortable: true,
                sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
            },
            {
                name: 'name',
                align: 'center',
                label: t('database.name'),
                field: row => row.name,
                format: val => `${val}`,
                sortable: true,
            },
            {
                name: 'score',
                label: t('database.score'),
                align: 'center',
                field: row => row.score,
                sortable: true,
                sort: (a, b) => parseInt(a, 10) - parseInt(b, 10)
            },
            {
                name: 'group',
                label: t('database.group'),
                field: row => row.group.name,
                format: val => `${val}`,
                sortable: true
            },
        ]

        // List for Changes in Faculty-Data
        let faculty_input_list = [
            {
                label: t('database.faculty_id'),
                new_value: ref(""),
                readonly: true
            },
            {
                label: t('database.faculty_name'),
                new_value: ref(""),
                readonly: false
            },
            {
                label: t('database.faculty_score'),
                new_value: ref(""),
                readonly: true
            }
        ]

        // List for Changes in Group-Data
        let group_input_list = [
            {
                label: t('database.group_id'),
                new_value: ref(""),
                readonly: true
            },
            {
                label: t('database.group_name'),
                new_value: ref(""),
                readonly: false
            },
            {
                label: t('database.score'),
                new_value: ref(""),
                readonly: true
            },
            {
                label: t('database.faculty'),
                new_value: ref(""),
                readonly: false
            }
        ]

        // List for Changes in Course-Data
        let course_input_list = [
            {
                label: t('database.course_id'),
                new_value: ref(""),
                readonly: true
            },
            {
                label: t('database.course_name'),
                new_value: ref(""),
                readonly: false
            },
            {
                label: t('database.score'),
                new_value: ref(""),
                readonly: true
            },
            {
                label: t('database.group'),
                new_value: ref(""),
                readonly: false
            },
            {
                label: t('database.course_public'),
                new_value: ref(""),
                readonly: false
            }
        ]

        // List to store changes of faculty attributes
        let new_faculty_attributes = [
            {
                name: t('database.name'),
                new_value: ref(""),
                hint: t('database.faculty_name'),
            },
            {
                name: t('database.score'),
                new_value: ref(""),
                hint: t('database.faculty_score'),
            }
        ]

        // List to store changes of group attributes
        let new_group_attributes = [
            {
                name: t('database.name'),
                new_value: ref(""),
                hint: t('database.group_name'),
            },
            {
                name: t('database.semester'),
                new_value: ref(""),
                hint: t('database.semester'),
            },
            {
                name: t('database.score'),
                new_value: ref(""),
                hint: t('database.group_score'),
            },
            {
                name: "Fakultät",
                new_value: ref(""),
                hint: t('database.faculty_of_group')
            },
        ]

        // List to store changes of course attributes
        let new_course_attributes = [
            {
                name: t('database.name'),
                new_value: ref(""),
                hint: t('database.course_name'),
            },
            {
                name: t('database.score'),
                new_value: ref(""),
                hint: t('database.course_score'),
            },
            {
                name: "Gruppe",
                new_value: ref(""),
                hint: t('database.group_of_course'),
            },
            {
                name: t('database.course_public'),
                new_value: ref(""),
                hint: t('database.course_public'),
            },
        ]

        loadPage()
        //loadFaculties_m()

        // Deletes Faculty in database
        function deleteFaculty() {
            if (selected_faculty.value.length <= 0) {
                return
            }
            let faculty = {
                id: selected_faculty.value[0].id,
                name: selected_faculty.value[0].name,
                score: selected_faculty.value[0].score
            }
            facultyStore.requestDeleteFaculty(faculty).then(() => {
                selected_faculty.value = []
                reloadPage()
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
            reloadPage()
        }

        // Deletes Group in database
        function deleteGroup() {
            if (selected_group.value.length <= 0) {
                return
            }
            let group = {
                id: selected_group.value[0].id,
                name: selected_group.value[0].name,
                semester: selected_group.value[0].semester,
                score: selected_group.value[0].score,
                faculty: selected_group.value[0].faculty,
            }
            groupStore.requestDeleteGroup(group).then(() => {
                selected_group.value = []
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
            reloadPage()
        }

        // Deletes Course in database
        function deleteCourse() {
            if (selected_course.value.length <= 0) {
                return
            }
            let course = {
                id: selected_course.value[0].id,
                name: selected_course.value[0].name,
                score: selected_course.value[0].score,
                group: selected_course.value[0].group,
                coursePublic: selected_course.value[0].coursePublic
            }
            courseStore.requestDeleteCourse(course).then(() => {
                selected_course.value = []
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
            reloadPage()
        }

        // gets called when user selects faculty in table
        function facultySelection(details) {
            if (details.added) {
                group_row.value = []        // empty group table
                selected_group.value = []   // empty group table -> no group selected
                course_row.value = []       // empty course table
                selected_course.value = []  // empty course table -> no course selected
                let selected = details.rows
                loadGroups(selected)
            } else {
                group_row.value = []        // empty group table
                selected_group.value = []   // empty group table -> no group selected
                course_row.value = []       // empty course table
                selected_course.value = []  // empty course table -> no course selected
            }
        }

        function groupSelection(details) {
            if (details.added) {
                course_row.value = []       // empty course table
                selected_course.value = []  // empty course table -> no course selected
                let selected = details.rows
                loadCourses(selected)
            } else {
                course_row.value = []       // empty course table
                selected_course.value = []  // empty course table -> no course selected
            }
        }

        // load every faculty, group and course the current user is "connected" to
        function loadPage() {
            loadFaculties_m()

            loadGroups_m()

            loadCourses_m()
        }

        // load every faculty from database
        function loadFaculties_m() {
            facultyStore.requestFaculties().then(() => {
                faculties.value = facultyStore.getFaculties()
                faculties.value.forEach(fac => {
                    let new_fac = {
                        id: fac.id,
                        name: fac.name,
                        score: 0
                    }
                    faculty_rows.value.push(new_fac)
                    faculty_names.value.push(fac.name)
                })
                faculties.value.forEach(fac => {
                    groupStore.requestGroupsByFaculty(fac.id).then(res => {
                        groups.value = groupStore.getGroups()
                        groups.value.forEach(group => {
                            let new_group = {
                                id: group.id,
                                name: group.name,
                                score: 0,
                                faculty: group.faculty
                            }
                            group_row.value.push(new_group)
                        })
                        group_row.value.forEach(group => {
                            groupStore.getScore(group.id).then(res => {
                                group.score = res.data
                            })
                        })
                    })
                })
                faculty_rows.value.forEach(fac => {
                    facultyStore.getScore(fac.id).then(res => {
                        fac.score = res.data
                    })
                })
            })
        }

        // load every groups from database
        function loadGroups_m() {
            groupStore.requestGroups().then(() => {
                groups.value = groupStore.getGroups()
                groups.value.forEach(group => {
                    let new_group = {
                        id: group.id,
                        name: group.name,
                        score: 0,
                        faculty: group.faculty
                    }
                    group_row.value.push(new_group)
                })
                groups.value.forEach(group => {
                    courseStore.requestCoursesByGroup(group.id).then(res => {
                        courseStore.requestCourses().then(() => {
                            courses.value = courseStore.getCoursesOverview()
                            courses.value.forEach(course => {
                                let new_course = {
                                    id: course.id,
                                    name: course.name,
                                    score: 0,
                                    group: course.group,
                                    coursePublic: course.coursePublic
                                }
                                course_row.value.push(new_course)
                            })
                            course_row.value.forEach(course => {
                                courseStore.getScore(course.id).then(res => {
                                    course.score = res.data
                                })
                            })
                        })
                    })
                })
                group_row.value.forEach(group => {
                    groupStore.getScore(group.id).then(res => {
                        group.score = res.data
                    })
                })
            })
        }


        // load every courses from database
        function loadCourses_m() {
            courseStore.requestCourses().then(() => {
                courses.value = courseStore.getCoursesOverview()
                courses.value.forEach(course => {
                    let new_course = {
                        id: course.id,
                        name: course.name,
                        score: 0,
                        group: course.group,
                        coursePublic: course.coursePublic
                    }
                    course_row.value.push(new_course)
                })
                course_row.value.forEach(course => {
                    courseStore.getScore(course.id).then(res => {
                        course.score = res.data
                    })
                })
            })
        }

        // load faculty-object by faculty name
        function getFaculty(faculty_name) {
            for (let i = 0; i < faculties.value.length; i++) {
                if (faculties.value[i].name === faculty_name) {
                    return faculties.value[i]
                }
            }
        }

        // updates the faculty in group_input_list
        function updateFacultyInInputList() {
            if (typeof group_input_list[3].new_value.value == 'string') {
                group_input_list[3].new_value.value = getFaculty(group_input_list[3].new_value.value)

            }
        }

        // load groups filtered by selected faculty
        function loadGroups(selected) {
            if (selected == null) {
                return
            }
            //selected = selected.rows
            fillFacultyInputs(selected)
            // Load groups by selected faculty
            groupStore.requestAllGroups().then(() => {
                groups.value = groupStore.getGroups()

                //deletes all previously selected groups from table
                while (group_row.value.length > 0) {
                    group_row.value.pop()
                }
                // stores only the groups from the selected group in table
                for (let j = 0; j < groups.value.length; j++) {
                    // continue to next group if current group has no faculty
                    if (groups.value[j].faculty == null) {
                        continue
                    }
                    // check if faculty of current group is equal to the selected faculty
                    if (groups.value[j].faculty.id == selected[0].id) {
                        let new_group = {
                            id: groups.value[j].id,
                            name: groups.value[j].name,
                            score: 0,
                            faculty: groups.value[j].faculty
                        }
                        groupStore.getScore(groups.value[j].id).then(res => {
                            new_group.score = res.data
                        })
                        group_row.value.push(new_group)
                    }
                }
                // checks if faculty-parameter is filled
                for (let i = 0; i < group_row.value.length; i++) {
                    if (group_row.value[i].faculty == null) {
                        group_row.value[i].faculty.name = t('manage.no_faculty_specified')
                        continue
                    }
                    if (group_row.value[i].faculty.name == null) {
                        group_row.value[i].faculty.name = t('manage.no_faculty_name')
                    }
                }
            })
        }

        // loads every course thats in the selected group
        function loadCourses(selected) {
            fillGroupInputs(selected)
            courseStore.requestAllCourses().then(() => {
                courses.value = courseStore.getCoursesOverview()
                // deletes all previous selected courses from table
                while (course_row.value.length > 0) {
                    course_row.value.pop()
                }
                // stores only the courses from the selected group in table
                for (let j = 0; j < courses.value.length; j++) {
                    if (courses.value[j].group == null) {
                        continue
                    }

                    if (courses.value[j].group.id == selected[0].id) {
                        course_row.value.push(courses.value[j])
                    }
                }
                // checks if group-parameter is filled
                for (let i = 0; i < course_row.value.length; i++) {
                    if (course_row.value[i].group == null) {
                        course_row.value[i].group.name = t('manage.no_group_specified')
                        continue
                    }
                    if (course_row.value[i].group.name == null) {
                        course_row.value[i].group.name = t('manage.no_group_name')
                    }
                }

            })
        }

        function fillFacultyInputs(selected) {
            faculty_input_list[0].new_value.value = selected[0].id
            faculty_input_list[1].new_value.value = selected[0].name
            faculty_input_list[2].new_value.value = selected[0].score
        }

        function fillGroupInputs(selected) {
            // geht das auch besser?
            group_input_list[0].new_value.value = selected[0].id
            group_input_list[1].new_value.value = selected[0].name
            group_input_list[2].new_value.value = selected[0].score
            group_input_list[3].new_value.value = selected[0].faculty
        }

        function fillCourseInputs(selected) {
            course_input_list[0].new_value.value = selected.rows[0].id
            course_input_list[1].new_value.value = selected.rows[0].name
            course_input_list[2].new_value.value = selected.rows[0].score
            course_input_list[3].new_value.value = selected.rows[0].group
            course_input_list[4].new_value.value = selected.rows[0].coursePublic
        }

        // sets variable to true so that dialog opens
        function save_faculty_changes() {
            if (selected_faculty.value.length > 0) {
                confirm_faculty_change.value = true
            }
            //CustomTable.clearSelection()
        }

        // actually updates faculty changes into database
        function update_faculty_changes() {
            if (selected_faculty.value.length > 0) {
                let faculty = {}
                faculty.id = faculty_input_list[0].new_value.value
                faculty.name = faculty_input_list[1].new_value.value
                faculty.score = faculty_input_list[2].new_value.value
                facultyStore.requestUpdateFaculty(
                    faculty
                ).then(() => {
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
            }
            loadFaculties_m()
            reloadPage()
        }

        // sets variable to true so that dialog opens
        function save_group_changes() {
            updateFacultyInInputList()
            if (selected_group.value.length > 0) {
                confirm_group_change.value = true
            }

        }

        // actually updates group changes into database
        function update_group_changes() {
            if (selected_group.value.length > 0) {
                let group = {
                    id: group_input_list[0].new_value.value,
                    name: group_input_list[1].new_value.value,
                    score: group_input_list[2].new_value.value,
                    faculty: group_input_list[3].new_value.value
                }
                groupStore.requestUpdateGroup(group).then(() => {
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
            }
            loadGroups(selected_faculty.value)
            reloadPage()
        }

        // sets variable to true so that dialog opens
        function save_course_changes() {
            if (selected_course.value.length > 0) {
                confirm_course_change.value = true
            }
        }

        // actually updates course changes into database
        function update_course_changes() {
            if (selected_course.value.length > 0) {
                let selectedCourse = courses.value.find(elem => elem.id == course_input_list[0].new_value.value)
                if (selectedCourse == null) {
                    $q.notify({
                        type: 'negative',
                        message: t('manage.no_course_available'),
                        caption: t('manage.no_couse_selected')
                    })
                }
                let course = {
                    id: course_input_list[0].new_value.value,
                    name: course_input_list[1].new_value.value,
                    htmlList: selectedCourse.htmlList,
                    group: course_input_list[3].new_value.value,
                    coursePublic: course_input_list[4].new_value.value
                }
                courseStore.requestUpdateCourse(course).then(() => {
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
            }
            reloadPage()
        }

        function reloadPage() {
            window.location.reload()
            //router.push('../Manage-DB')
        }

        function reloadComponents() {
            this.$refs.faculty_ref.reloadPage()
            this.$refs.group_ref.update()
            this.$refs.course_ref.update()
        }

        return {
            confirm_faculty_delete,
            confirm_group_delete,
            confirm_course_delete,
            deleteFaculty,
            deleteGroup,
            deleteCourse,
            reloadComponents,
            new_group_attributes,
            new_course_attributes,
            new_faculty_attributes,
            faculty_filter,
            group_filter,
            course_filter,
            facultySelection,
            groupSelection,
            faculty_names,
            faculties,
            selected_faculty,
            selected_group,
            selected_course,
            faculty_table_title,
            group_table_title,
            course_table_title,
            loadCourses,
            facultyStore,
            faculty_rows,
            faculty_columns,
            faculty_input_list,
            courseStore,
            courses,
            course_row,
            course_input_list,
            group_columns,
            course_columns,
            groupStore,
            groups,
            group_row,
            group_input_list,
            save_group_changes,
            save_faculty_changes,
            save_course_changes,
            confirm_faculty_change,
            confirm_group_change,
            confirm_course_change,
            fillCourseInputs,
            update_faculty_changes,
            update_group_changes,
            update_course_changes,
        }

    }

}
</script>

<style scoped>

</style>
