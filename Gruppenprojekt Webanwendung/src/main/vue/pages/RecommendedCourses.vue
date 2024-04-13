<template>
    <div class="headnav" style="text-align: left">
        <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
        <p class="navelem navactive">{{$t('menu.public_courses')}}</p>
    </div>
    <h1>{{$t('menu.public_courses') }}</h1>
    <div>
        <div v-for="fac in node">
            <div> <!-- central tree browser for Public Courses-->
                <Tree
                    :node="fac"/>
            </div>
        </div>
    </div>
</template>

<script>
import {ref} from "vue";
import {useCourseStore} from "../stores/courses";
import ProgressBar from "../components/ProgressBar.vue";
import Tree from "../components/Dashboard_Tree.vue";

export default {
    setup() {
        const faculties = ref([])
        const groups = ref([])
        const courses = ref([])
        const myCourses = ref([])
        const myFacultys = ref([])
        const courseStore = useCourseStore()
        const node = ref([])
        let root = ref([])

        //calls all courses with visibility on true
        courseStore.requestCoursesByVisibility().then(() => {
            courses.value = courseStore.getCoursesOverview();
            console.log(courses)
            const contain = ref(false)
            //find all faculties of visible Courses
            for (let i = 0; i < courses.value.length; i++) {
                for (let j = 0; j < myFacultys.value.length; j++) {
                    if (myFacultys.value[j] == courses.value[i].group.faculty.id) {
                        contain.value = true
                    }
                }
                if (!contain.value) {
                    myFacultys.value.push(
                        courses.value[i].group.faculty.id
                    )
                }
                contain.value = false
            }
            //find all courses in each faculty
            for (let j = 0; j < myFacultys.value.length; j++) {
                for (let i = 0; i < courses.value.length; i++) {
                    if (courses.value[i].group != null && courses.value[i].group.faculty != null) {
                        if (courses.value[i].group.faculty.id == myFacultys.value[j]) {
                            myCourses.value.push(
                                courses.value[i]
                            )
                        }
                    }
                }
                if (myCourses.value.length != 0) {
                    makeTree(myCourses)
                }
                myCourses.value = []
            }
        })

        //makes the tree for each faculty
        function makeTree(courseListInput) {
            const facultyGroupIDs = ref([])
            const children = ref([])
            const courseList = courseListInput

            //sorts the courses in groups
            for (let i = 0; i < courseList.value.length; i++) {
                const contain = ref(true)
                for (let j = 0; j < facultyGroupIDs.value.length; j++) {
                    if (facultyGroupIDs.value[j] == courseList.value[i].group.id) {
                        contain.value = false
                    }
                }
                if (contain.value) {
                    facultyGroupIDs.value.push(
                        courseList.value[i].group.id
                    )
                }
            }
            for (let j = 0; j < facultyGroupIDs.value.length; j++) {
                const tempGroup = ref([])
                for (let i = 0; i < courseList.value.length; i++) {
                    if (facultyGroupIDs.value[j] == courseList.value[i].group.id) {
                        tempGroup.value.push(
                            courseList.value[i]
                        )
                    }
                }
                children.value.push(
                    {
                        label: tempGroup.value[0].group.name,
                        courseId: null,
                        score: parseInt(tempGroup.value[0].group.score),
                        children: makeCourseNode(tempGroup)
                    }
                )
            }
            node.value.push(
                {
                    label: courseList.value[0].group.faculty.name,
                    courseId: null,
                    children: children,
                    score: parseInt(courseList.value[0].group.faculty.score),
                }
            )
            root = node
        }

        //makes the nodes for courses
        function makeCourseNode(inputCourses) {
            const CourseInGroupe = inputCourses
            const children = ref([])
            for (let i = 0; i < CourseInGroupe.value.length; i++) {
                children.value.push(
                    {
                        label: CourseInGroupe.value[i].name,
                        courseId: CourseInGroupe.value[i].id,
                        score: parseInt(CourseInGroupe.value[i].score)
                    }
                )
            }
            return children
        }

        return {
            node: node, faculties, groups,
        }
    },
    components: {ProgressBar, Tree},
}
</script>

<style scoped>

</style>
