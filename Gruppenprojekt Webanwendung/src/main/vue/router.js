import Login from "./pages/Login.vue";
import PageNotFound from "./components/PageNotFound.vue";
import Dashboard from "./pages/Dashboard.vue";
import Registration from "./pages/UserRegistration.vue";
import Manage_DB from "./pages/ManageDB.vue";
import Faculty from "./pages/Faculty.vue";
import Adminpage from "./pages/Adminpage.vue";
import MancheckImages from "./pages/mancheck/Images.vue"
import MancheckVideos from "./pages/mancheck/Videos.vue"
import Statistic from "./pages/Statistic.vue";
import MancheckLists from "./pages/mancheck/Lists.vue"
import MancheckFormatting from "./pages/mancheck/Formatting.vue"
import MancheckLinks from "./pages/mancheck/Links.vue"
import Course from "./pages/Course.vue"
import {createRouter, createWebHistory} from "vue-router";
import AddAccessGroup from "./pages/AddAccessGroup.vue";
import AddAccessCourse from "./pages/AddAccessCourse.vue";
import {useUserStore} from "./stores/users";
import AutomaticResults from "./pages/AutomaticResults.vue";
import DetailedView from "./pages/DetailedView.vue";
import VerifyRegistration from "./pages/VerifyRegistration.vue"
import VerifyRegistrationFailed from "./pages/VerifyRegistrationFailed.vue"
import RecommendedCourses from "./pages/RecommendedCourses.vue";
import LandingPage from "./pages/LandingPage.vue"
import MyProfile from "./pages/Profile.vue";
import Profile from "./pages/ProfileView.vue";
import noPermission from "./components/noPermission.vue";
import Group from "./pages/Group.vue";

const routes = [
    {path: '/', component: LandingPage},
    {
        path: '/faculty/:id(\\d+)', component: Faculty,
        meta: {requiresAuth: true}
    },
    {path: '/:pathMatch(.*)*', name: 'NotFound', component: PageNotFound},
    {
        path: '/dashboard', component: Dashboard,
        meta: {requiresAuth: true}
    },
    {path: '/login', component: Login},
    {path: '/Registration', component: Registration},
    {path: '/Admin', component: Adminpage
    , meta: {requiresAuth: true}
    },
    {path: '/Course/:id(\\d+)', component: Course
        ,
        meta: {requiresAuth: true}},
    {path: '/AutomaticResults', component: AutomaticResults
        ,
        meta: {requiresAuth: true}},
    {path: '/manualcheck/1', component: MancheckImages,
    meta: {requiresAuth: true}
    },
    {path: '/manualcheck/2', component: MancheckVideos,
        meta: {requiresAuth: true}},
    {path: '/manualcheck/3', component: MancheckLinks,
        meta: {requiresAuth: true}},
    {path: '/manualcheck/4', component: MancheckLists,
        meta: {requiresAuth: true}},
    {path: '/manualcheck/5', component: MancheckFormatting,
        meta: {requiresAuth: true}},
    {
        path: '/AddAccessGroup', component: AddAccessGroup
        , meta: {requiresAuth: true}
    },
    {path: '/AddAccessCourse', component: AddAccessCourse,
        meta: {requiresAuth: true}},
    {path: '/Profile/', component: MyProfile,
        meta: {requiresAuth: true}},
    {path: '/Profile/:id(\\w+)', name: 'id', component: Profile,
        meta: {requiresAuth: true}},
    {path: '/Course/:co(\\d+)/DetailedView/:id(\\d+)', component: DetailedView,
        meta: {requiresAuth: true}},
    {path: '/Statistic', component: Statistic,
        meta: {requiresAuth: true}},
    {path: '/verifyAccount/:token', name: 'confirmationToken', component: VerifyRegistration},
    {path: '/verifyAccountFailed', component: VerifyRegistrationFailed },
    {path: '/RecommendedCourses', component: RecommendedCourses },
    {
        path: '/Course/:id(\\d+)', component: Course,
        meta: {requiresAuth: true}
    },
    {path: '/Manage-DB', component: Manage_DB,
        meta: {requiresAuth: true}},
    {path: "/403", component: noPermission},
    {path: '/Group/:id(\\d+)', component: Group}
]

// noinspection JSCheckFunctionSignatures
const router = createRouter({
    history: createWebHistory(),
    routes
})
router.beforeEach((to) => {
    to.path=to.path.toLowerCase()
    const userStore = useUserStore()
    userStore.authenticate(sessionStorage.getItem('token'))
    if (to.meta.requiresAuth && !userStore.authenticated) return '/login'

    if (to.path === '/addaccessgroup' ) {
        userStore.isFacultyManager().then(() => {
            if (sessionStorage.getItem('Role_Faculty_Manager').toString() === "false") {
                    router.push("/403")

            }
            else {
                router.push("/addaccessgroup")}
            }
        )
    }
    if (to.path === '/addaccesscourse' ) {
        userStore.isGroupManager().then(() => {
            if (sessionStorage.getItem('Role_Group_Manager').toString() === "false" ) {
                    router.push("/403")
                }
            else {
                router.push("/addaccesscourse")}
        }

        )
    }
    if (to.path === '/admin' || to.path==='/Manage-DB' ) {
        userStore.isAdmin().then(() => {
                if (sessionStorage.getItem('Role_Admin').toString() === "false" ) {
                    router.push("/403")}
                else {
                        router.push("/admin")}
                }
        )
    }
})
export default router
