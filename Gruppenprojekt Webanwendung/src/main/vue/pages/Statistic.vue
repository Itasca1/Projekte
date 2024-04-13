<template>

    <div class="headnav" style="text-align: left">
        <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
        <p class="navelem navactive">{{$t('statistic.title')}}</p>
    </div>
    <h1>{{$t('statistic.title')}}</h1>

    <div class="q-pa-md">


        <q-tabs
            v-model="tab"
            dense
            class="text-grey"
            active-color="primary"
            indicator-color="primary"
            align="justify"
            narrow-indicator
        >
            <q-tab name="faculties" :label="$t('statistic.faculties')" />
            <q-tab name="groups" :label="$t('statistic.groups')" />
            <q-tab name="courses" :label="$t('statistic.courses')" />
            <q-tab name="users" :label="$t('statistic.users')" />
        </q-tabs>

        <q-separator />

        <q-tab-panels v-model="tab" animated>
            <q-tab-panel name="faculties">
                <q-table flat
                    title="Fakultäten"
                    :rows="listFaculties"
                    :columns="columns"
                    row-key="name"
                    :visible-columns="visibleColumns"
                >
                    <template v-slot:top>
                        <q-icon name="apartment" size="64px" class="q-pr-lg"/>


                        <h2> {{$t("statistic.faculties")}} </h2>
                        <q-space/>
                        <q-toggle v-model="toggle" @click="updateResults" :label="$t('statistic.setFaculties')"/>
                    </template>

                </q-table>
            </q-tab-panel>

            <q-tab-panel name="groups">
                <q-table flat
                    title="Gruppen"
                    :rows="listGroups"
                    :columns="columns"
                    row-key="name"
                    :visible-columns="visibleColumns"
                >
                    <template v-slot:top>
                        <q-icon name="groups" size="64px" class="q-pr-lg"/>


                        <h2> {{$t("statistic.groups")}} </h2>
                        <q-space/>
                        <q-toggle v-model="toggle" @click="updateResults" :label="$t('statistic.setGroups')"/>
                    </template>

                </q-table>
            </q-tab-panel>

            <q-tab-panel name="courses">
                <q-table flat
                    title="Kurse"
                    :rows="listCourses"
                    :columns="columns"
                    row-key="name"
                    :visible-columns="visibleColumns"
                >
                    <template v-slot:top>
                        <q-icon name="description" size="64px" class="q-pr-lg"/>


                        <h2> {{$t("statistic.courses")}} </h2>
                        <q-space/>
                        <q-toggle v-model="toggle" @click="updateResults" :label="$t('statistic.setCourses')"/>
                    </template>

                </q-table>
            </q-tab-panel>

            <q-tab-panel name="users">
                <q-table flat
                         title="User"
                         :rows="listUsers"
                         :columns="columns"
                         row-key="name"
                         :visible-columns="visibleColumns"
                >
                    <template v-slot:top>
                        <q-icon name="person" size="64px" class="q-pr-lg"/>


                        <h2> {{$t("statistic.users")}} </h2>
                        <q-space/>
                        <q-toggle v-model="toggle" @click="updateResults" :label="$t('statistic.setUsers')"/>
                    </template>

                </q-table>
            </q-tab-panel>
        </q-tab-panels>
    </div>
</template>
<script>
import {ref} from "vue";
import router from "../router";
import {useI18n} from "vue-i18n";
import axios from "axios";




const rows =  ref([])
export default {
    name: "Statistic",
    setup() {
        const {t} = useI18n({})
        const columns = [
        {
            name: 'rank',
            required: true,
            label: t('statistic.rank'),
            align: 'center',
            field: 'rank'
        },
          {
            name: 'name',
            required: true,
            label: t('statistic.name'),
            align: 'center',
            field: row => row.name,
            format: val => `${val}`,
          },
          { name: 'score', align: 'center', label: t('statistic.score'), field: 'points'},
        ]
        rows.value = []

        const facultyRanking =  ref([])
        const groupRanking =  ref([])
        const courseRanking =  ref([])
        const userRanking =  ref([])

        const faculties = ref([])
        const myFaculites = ref([])

        const listCourses = ref([])
        const listGroups = ref([])
        const listFaculties = ref([])
        const listUsers = ref([])

        const toggle = ref(false)

        let userId = sessionStorage.getItem('id')


        loadFaculties()
        function loadFaculties() {
            axios.get("/api/ranking/faculties/" + userId).then(r => {
                facultyRanking.value = r.data
                updateResults()
            })
        }

        loadGroups()
        function loadGroups() {
            axios.get("/api/ranking/groups/" + userId).then(r => {
                groupRanking.value = r.data
                updateResults()
            })
        }

        loadCourses()
        function loadCourses() {
            axios.get("/api/ranking/courses/" + userId).then(r => {
                courseRanking.value = r.data
                updateResults()
            })
        }

        loadUsers()
        function loadUsers() {
            axios.get("/api/ranking/users/" + userId).then(r => {
                userRanking.value = r.data
                updateResults()
            })
        }




        function updateResults(){
            if(toggle.value){
                listFaculties.value = facultyRanking.value.filter(e => e.usersCourse)
                listGroups.value = groupRanking.value.filter(e => e.usersCourse)
                listCourses.value = courseRanking.value.filter(e => e.usersCourse)
                listUsers.value = userRanking.value.filter(e => e.usersCourse)
            }
            else {
                listFaculties.value = facultyRanking.value;
                listGroups.value = groupRanking.value;
                listCourses.value = courseRanking.value
                listUsers.value = userRanking.value;
            }

        }

        function morestat() {
            router.push('/login')

        }

        function getFacultyRanking() {
            if(toggle.value) return facultyRanking.value = myFaculites.value;
            else return facultyRanking.value = faculties.value;
        }

        function getGroupRanking(){
            if(toggle.value) return myGroupsvalue;
            else return groupRanking.value;
        }

        function getCourseRanking(){
            if(toggle.value) return myCoursesvalue;
            else return courseRankingvalue;
        }


        return{columns, rows, faculties, morestat, tab: ref('faculties'), toggle,
            listFaculties, listGroups, listCourses, listUsers, updateResults}
    }
}

</script>

<style scoped>

</style>
