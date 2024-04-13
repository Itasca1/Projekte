<template>
    <div class="headnav" style="text-align: left">
        <p class="navelem navactive">Dashboard</p>
    </div>
    <h1>{{ $t("dashboard.Your_courses_and_facultys") }}</h1>

    <div class="overview">
        <ProgressBar></ProgressBar> <!--progress bar showing how many % are already AA-->
    </div>

    <div class="buttonclass">
        <q-btn class="buttonitem" @click="goToAddCourse()" unelevated rounded color="primary">{{ $t("dashboard.add_course") }}</q-btn>
        <q-btn class="buttonitem" @click="goToManagePage" outline rounded color="black">{{ $t("dashboard.manage_course") }}</q-btn>
    </div>


    <div class="q-pa-md q-gutter-sm">
        <q-tree
            v-if="tree"
            :nodes="tree"
            node-key="label"

        >
            <template v-slot:header-generic="prop">
                <div style="display: flex; flex-direction: row; width: 100%" class="row items-center">
                    <q-icon :name="prop.node.icon" color="$primary" size="28px" class="q-mr-sm" />
                    <router-link :to="'/Faculty/'+ prop.node.id" class="navelem">{{ prop.node.label }}</router-link>
                    <div style="flex-grow: 2"></div>
                    <p v-if="prop.node.score >= 50" class="scoreRectSmall">{{ prop.node.score }}%</p>
                    <p v-else class="scoreRectSmall scoreNegative">{{ prop.node.score }}%</p>
                </div>
            </template>

            <template v-slot:header-group="prop">
                <div style="display: flex; flex-direction: row; width: 100%" class="row items-center">
                    <q-icon :name="prop.node.icon" color="$primary" size="28px" class="q-mr-sm" />
                    <router-link :to="'/Group/'+ prop.node.id" class="navelem">{{ prop.node.label }}</router-link>
                    <div style="flex-grow: 2"></div>
                    <p v-if="prop.node.score >= 50" class="scoreRectSmall">{{ prop.node.score }}%</p>
                    <p v-else class="scoreRectSmall scoreNegative">{{ prop.node.score }}%</p>
                </div>
            </template>

            <template v-slot:header-course="prop">
                <div style="display: flex; flex-direction: row; width: 100%" class="row items-center">
                    <router-link :to="'/Course/'+ prop.node.id" class="navelem">{{ prop.node.label }}</router-link>
                    <div style="flex-grow: 2"></div>
                    <p v-if="prop.node.score >= 50" class="scoreRectSmall">{{ prop.node.score }}%</p>
                    <p v-else class="scoreRectSmall scoreNegative">{{ prop.node.score }}%</p>
                </div>
            </template>

        </q-tree>
    </div>

</template>

<script>
import Tree from "../components/Dashboard_Tree.vue"
import {ref} from 'vue';
import ProgressBar from "../components/ProgressBar.vue";


export default {
    name: "dashboard",
    // generate tree structure for our data
    setup() {
        const faculties = ref([])
        const groups = ref([])
        const node = ref([])

        return {
            node: node, faculties, groups
        }
    },
    components: {ProgressBar, Tree},

    methods: {
        //path to the addAccesCourse page
        goToAddCourse() {
            this.$router.push('/AddAccessCourse');
        },
        //path to the Manage-DB page
        goToManagePage() {
            this.$router.push('/Manage-DB');
        }
    },
}
</script>

<script setup>

import {useCourseStore} from "../stores/courses";
import {useGroupStore} from "../stores/groups";
import {useFacultyStore} from "../stores/faculties";
import {useUserStore} from "../stores/users";
import {onMounted} from "vue";
import api from "../api";

const courseStore = useCourseStore()
const groupStore = useGroupStore()
const facultyStore = useFacultyStore()
const userStore = useUserStore()

const tree = ref(null);

let rawTree = []

let checksCourses = false;
let elems = 0;

onMounted(buildTree);

async function buildTree() {
    console.log(await getCourses());
    console.log(await getGroups());
    console.log(await getFaculties());
    console.log(rawTree);
    genTree();
}

window.addEventListener('unhandledrejection', function(event) {
    // the event object has two special properties:

});

function genTree() {

    let elemsDone = 0;
    for (let faculty in rawTree) {
        const groups = rawTree[faculty].children;
        for (let group in groups) {
            const courses = rawTree[faculty].children[group].children;
            for (let course in courses) {
                courseStore.getScore(courses[course].id).then(res => {
                    courses[course].score = res.data;
                    elemsDone++;
                    if (elems === elemsDone) {
                        tree.value = rawTree;
                        return; //necessary
                    }
                });
            }

            groupStore.getScore(groups[group].id).then(res => {
                groups[group].score = res.data;
                elemsDone++;
                if (elems === elemsDone) {
                    tree.value = rawTree;
                    return; //necessary
                }
            });
        }
        facultyStore.getScore(rawTree[faculty].id).then(res => {
            rawTree[faculty].score = res.data;
            elemsDone++;
            if (elems === elemsDone) {
                tree.value = rawTree;
                return; //necessary
            }
        });
    }
}


async function getCourses() {
    return new Promise((resolve) => {
        try {
            courseStore.requestCourses().then(() => {
                checksCourses = true;
                const courses = ref();
                courses.value = courseStore.getCoursesOverview();
                for (let course in courses.value) {

                    let alreadyAdded = false;
                    const id = courses.value[course].group.faculty.id;
                    let currentFaculty = rawTree.length;
                    for (let faculty in rawTree) {
                        if (rawTree[faculty].id === id) {
                            alreadyAdded = true;
                            currentFaculty = faculty;
                            break;
                        }
                    }
                    if (!alreadyAdded) {
                        elems++;
                        rawTree.push( { "id" : id, "label": courses.value[course].group.faculty.name ,children : [],
                            header : "generic", icon : "apartment", score : null
                        } );
                    }

                    console.log("currentFac",rawTree[currentFaculty]);

                    const groupid = courses.value[course].group.id;
                    console.log("groupid",groupid);
                    const childs = rawTree[currentFaculty].children;
                    let currentGroup = childs.length;
                    alreadyAdded = false;
                    for (let group in childs) {
                        if (childs[group].id === groupid) {
                            alreadyAdded = true;
                            currentGroup = group;
                            break;
                        }
                    }
                    if (!alreadyAdded) {
                        elems++;
                        childs.push( { "id" : groupid, "label" : courses.value[course].group.name, children : [],
                            header : "group", icon : "group"
                        } );
                    }

                    elems++;
                    childs[currentGroup].children.push(
                        {"id" : courses.value[course].id , "label" : courses.value[course].name, "header" : "course"}
                    );

                }
                resolve();
            })
        } catch (e) {
            throw new Error("nocourse");
        }
    })
}

async function getGroups() {
    return new Promise((resolve) => {
        userStore.isGroupManager().then(() => {
            if (sessionStorage.getItem('Role_Group_Manager').toString() === "true") {
                const groups = ref();
                groupStore.requestGroups().then(() => {
                    groups.value = groupStore.getGroups()

                    for (let group in groups.value) {
                        console.log(groups.value);
                        let alreadyAdded = false;
                        let currentFaculty = rawTree.length;
                        for (let faculty in rawTree) {
                            if (rawTree[faculty].id === groups.value[group].faculty.id) {
                                alreadyAdded = true;
                                currentFaculty = faculty;
                                break;
                            }
                        }
                        if (!alreadyAdded) {
                            elems++;
                            rawTree.push( { "id" : groups.value[group].faculty.id, "label": groups.value[group].faculty.name ,children : [],
                                header : "generic", icon : "apartment"
                            });
                        }

                        const childs = rawTree[currentFaculty].children;
                        alreadyAdded = false;
                        for (let oldgroup in childs) {
                            if (childs[oldgroup].id === groups.value[group].id) {
                                alreadyAdded = true;
                                break;
                            }
                        }
                        if (!alreadyAdded) {
                            elems++;
                            childs.push( { "id" : groups.value[group].id, "label": groups.value[group].name ,children : [],
                                header : "group", icon : "group"
                            } );
                        }
                    }
                    resolve();
                })
            } else {
                resolve();
            }
        })
    })
}

async function getFaculties() {
    return new Promise((resolve) => {
        userStore.isFacultyManager().then(() => {
            if (sessionStorage.getItem('Role_Faculty_Manager').toString() === "true") {
                const faculties = ref();
                facultyStore.requestFaculties().then(() => {
                    faculties.value = facultyStore.getFaculties()
                    for (let faculty in faculties.value) {
                        let alreadyChecked = false;
                        for (let old in rawTree) {
                            if (rawTree[old].id === faculties.value[faculty].id) {
                                alreadyChecked = true;
                                break;
                            }
                        }
                        if (!alreadyChecked) {
                            elems++;
                            rawTree.push( { "id" : faculties.value[faculty].id, "label": faculties.value[faculty].name ,children : [],
                                header : "generic", icon : "apartment"
                            });
                        }
                    }
                    resolve();
                })
            } else {
                resolve();
            }
        })
    })
}

</script>

<style lang="scss">
@import "src/quasar-variables.sass";

.overview {
    border-radius: 15px;
    border: 2px solid $secondary;
    padding: 5px;
}

</style>
<style>

</style>

