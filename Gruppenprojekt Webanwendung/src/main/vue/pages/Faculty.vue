<template>
    <div class="router" >
        <div v-if="faculty" class="header">
            <div class="headerLeft">
                <div class="headnav" style="text-align: left">
                    <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
                    <p class="navelem navactive">{{faculty.name}}</p>
                </div>
                <h1>{{faculty.name}}</h1>
            </div>

            <div v-if="score <50" style="display: flex; flex-direction: row; align-items: center; justify-content: center">
                <h4>{{$t('database.faculty_score')}}</h4> <p class="scoreRect scoreNegative">{{ score }}%</p>
            </div>
            <div v-else style="display: flex; flex-direction: row; align-items: center; justify-content: center">
                <h4>{{$t('database.faculty_score')}}</h4> <p class="scoreRect">{{ score }}%</p>
            </div>

            <q-icon name="info" style="color: #3F50B5; padding-top: 4vh;" size="sm">
                <q-tooltip>
                    {{ $t("faculty.infoFaculty") }}
                </q-tooltip>
            </q-icon>
        </div>

        <div class="row items-center" >
            <div class="manager">
                {{$t('database.faculty_managers')}}:
                <q-chip v-if="faculty_managers.length >= 1" v-for="manager in faculty_managers" icon="person" >
                    {{manager.firstname}} {{manager.lastname}}
                </q-chip>
                <q-chip v-else icon="error" color="warning">
                    {{$t('database.no_manager_exists')}}
                </q-chip>
            </div>
        </div>

        <div class="buttonclass" v-if="allowedToEdit">
            <q-btn class="buttonitem" @click="addGroup" outline rounded icon="post_add" color="black">{{$t('manage.add_group')}}</q-btn>
            <div class="buttonitem">
                <q-btn flat round color="black" icon="more_vert" />
                <q-menu touch-position>
                    <q-list style="min-width: 100px">
                        <q-item clickable v-close-popup color="primary" rounded icon="edit" >
                            <q-item-section @click="preEdit">{{$t('manage.edit')}}</q-item-section>
                        </q-item>
                        <q-item clickable v-close-popup color="red" rounded icon="delete" >
                            <q-item-section @click="confirm_faculty_delete = true">{{$t('manage.delete')}}</q-item-section>
                        </q-item>
                    </q-list>
                </q-menu>
            </div>
        </div>

        <div class="row">
            <div class="col">
                <q-table
                    :title="$tc('database.access_group',2)"
                    :columns="columns"
                    :rows="group_rows"
                    row-key="id"
                    flat

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
                                <q-btn outline rounded color="black" @click="routeToGroup(props.row)">
                                    {{ props.row.name }}
                                </q-btn>

                                </q-td>
                                <q-td key="managers" :props="props">
                                    <q-chip v-for="man in props.row.managers" icon="person">
                                        {{ man }}
                                    </q-chip>
                                </q-td>
                                <q-td key="weighting" :props="props">
                                    {{ props.row.weighting }}
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
                <q-dialog v-model="edit_faculty" persistent>
                    <q-card style="width: 700px; max-width: 80vw;">
                        <q-card-section>
                            <div class="text-h5 q-mb-md">{{ $t('manage.update_faculty') }}</div>
                            <q-separator inset/>
                            <div class="q-mb-md">
                                <q-input
                                    outlined
                                    rounded
                                    :label="$t('database.faculty_name')"
                                    v-model="new_name"
                                >
                                    <template v-slot:prepend>
                                        <q-icon name="edit" />
                                    </template>
                                </q-input>
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
                                @click="updateFaculty(new_name)"
                            />
                        </q-card-actions>
                    </q-card>
                </q-dialog>
            </div>
            <div>
                <q-dialog v-model="confirm_faculty_delete" persistent>
                    <q-card>
                        <q-card-section>
                            <div class="text-h5 q-mb-md">
                                {{ $t('manage.delete_selected_faculty') }}
                            </div>
                            <q-separator inset/>

                            <div>
                                {{$t('manage.delete_confirm')}}
                            </div>
                        </q-card-section>

                        <q-card-actions align="right">
                            <q-btn flat :label="$t('manage.cancel')" color="primary" v-close-popup/>
                            <q-btn flat :label="$t('manage.delete')" color="red" v-close-popup @click="deleteFaculty"/>
                        </q-card-actions>
                    </q-card>
                </q-dialog>
            </div>
    </div>
</template>

<script>
import {useFacultyStore} from "../stores/faculties"
import {useRoute} from 'vue-router'
import router from "../router"
import {ref} from "vue";
import {useI18n} from "vue-i18n";
import {useQuasar} from "quasar";
import {useGroupStore} from "../stores/groups";
import {useGroupManagerStore} from "../stores/groupManagers";
import {useFacultyManagerStore} from "../stores/facultyManagers";

export default {
    setup() {
        const {t} = useI18n({})
        const $q = useQuasar()

        const route = useRoute()
        const facultyId = route.params.id
        const faculty = ref()
        const score = ref("")
        const allowedToEdit = ref(false)

        // Boolean to check if user wants to edit faculty
        const edit_faculty = ref(false)
        const new_name = ref("")
        const confirm_faculty_delete = ref(false)

        // Stores
        const facultyStore = useFacultyStore()
        const groupStore = useGroupStore()
        const groupManagerStore = useGroupManagerStore()
        const facultyManagerStore = useFacultyManagerStore()

        const groups = ref([])      // groups of faculty
        const groups_I_manage = ref([]) // groups the user manages
        const faculty_managers = ref([])    // managers of faculty

        // values for table
        const group_rows = ref([])      // rows of table

        //columns for table
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
                name: 'managers',
                align: 'left',
                label: t('database.manager'),
                field: row => row.managers.toString()
            },
            {
                name: 'score',
                align: 'center',
                label: t('database.score'),
                field: row => row.score
            }
        ]

        loadFaculty()
        loadGroups()
        loadGroupsIManage()
        loadFacultyManagers()
        loadScore()
        checkAuthorization()

        function checkAuthorization() {
            facultyStore.requestAllowEdit(facultyId).then(res => {
                allowedToEdit.value = res.data
            })
        }

        function loadScore() {
            facultyStore.getScore(facultyId).then(res => {
                score.value = res.data
            })
        }

        // deleted faculty
        function deleteFaculty() {
            facultyStore.requestDeleteFaculty(faculty.value).then(() => {
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

        // routes to AddAccessGroup
        function addGroup() {
            router.push('../AddAccessGroup')
        }

        // updates faculty with new name to backend
        function updateFaculty(change_name) {
            if (faculty == null) {
                $q.notify({
                    type: 'negative',
                    message: t('manage.no_course_available'),
                    caption: t('manage.no_couse_selected')
                })
            }
            let new_faculty = {
                id: faculty.value.id,
                name: change_name,
            }
            facultyStore.requestUpdateFaculty(new_faculty).then(() => {
                $q.notify({
                    type: 'positive',
                    message: t("manage.changes_saved"),
                    caption: t("manage.changes_saved_in_db")
                }).catch(() => {
                    $q.notify({
                        type: 'negative',
                        message: t("manage.changes_failed"),
                        caption: t("manage.changes_could_not_save")
                    })
                })
            })
        }

        // to set up edit dialog. gets clled before editing
        function preEdit() {
            edit_faculty.value = true
            new_name.value = faculty.value.name
        }

        // loads faculty object from database
        function loadFaculty() {
            facultyStore.requestFaculty(facultyId).then(() => {
                faculty.value = facultyStore.getFaculty(facultyId)
                document.title = faculty.value.name
            })

            facultyStore.getScore(facultyId).then(res => {
                score.value = res.data
            })
        }

        // loads all faculty managers of current faculty
        function loadFacultyManagers() {
            facultyManagerStore.requestManagers(facultyId).then(() => {
                faculty_managers.value = facultyManagerStore.getManagers()
            })
        }

        // loads all groups of faculty
        function loadGroups() {
            groupStore.requestGroupsByFaculty(facultyId).then(() => {
                groups.value = groupStore.getGroups()
                groups.value.forEach(group_entry => {
                    let managers = []
                    groupManagerStore.requestManagers(group_entry.id).then(() => {
                        managers = groupManagerStore.getManagers()
                        let managerNames = []
                        managers.forEach(manager => {
                            managerNames.push(manager.firstname.toString() + " " + manager.lastname.toString())
                        })
                        let group_score = -1
                        groupStore.getScore(group_entry.id).then(res => {
                            group_score = res.data
                            const row_entry = {
                                id: group_entry.id,
                                name: group_entry.name,
                                managers: managerNames,
                                score: group_score
                            }
                            group_rows.value.push(row_entry)
                        })
                    })
                })
            })
        }

        // loads all groups the user manages
        function loadGroupsIManage() {
            groupStore.requestGroups().then(() => {
                groups_I_manage.value = groupStore.getGroups()
            })
        }

        // routes to selected group
        function routeToGroup(group) {
            router.push('/Group/' + group.id.toString())
        }

        return {
            allowedToEdit,
            confirm_faculty_delete,
            deleteFaculty,
            preEdit,
            addGroup,
            updateFaculty,
            edit_faculty,
            score,
            faculty_managers,
            routeToGroup,
            columns,
            group_rows,
            facultyStore,
            faculty,
            new_name
        }
    }
}
</script>
