<template>

    <q-layout v-if="!landingPage" view="hHh LpR fFf">
        <q-drawer v-if="showMenu" v-model="drawer" show-if-above :mini="menuClosed" :width="200"
                  :breakpoint="500" elevated class="bg-white" content-class="column justify-between no-wrap">
            <q-list padding style="height: 100%; display: flex; flex-direction: column">
                <q-item clickable v-ripple @click="menuButtonClick" style="margin-bottom: 30px">
                    <q-item-section avatar>
                        <q-icon name="menu"></q-icon>
                    </q-item-section>
                </q-item>

                <q-item id="dashboardBtn" to="/Dashboard" clickable v-ripple>
                    <q-item-section avatar>
                        <q-icon name="dashboard"></q-icon>
                    </q-item-section>

                    <q-item-section class="text-left">
                        {{ $t("menu.dashboard") }}
                    </q-item-section>

                    <q-tooltip v-if="menuClosed" class="bg-primary" anchor="center right" self="center left"
                               transition-show="scale" transition-hide="scale">
                        {{ $t("menu.dashboard") }}
                    </q-tooltip>
                </q-item>

                <q-item to="/statistic" clickable v-ripple>
                    <q-item-section avatar>
                        <q-icon name="leaderboard"></q-icon>
                    </q-item-section>

                    <q-item-section class="text-left">
                        {{ $t("menu.statistics") }}
                    </q-item-section>

                    <q-tooltip v-if="menuClosed" class="bg-primary" anchor="center right" self="center left"
                               transition-show="scale" transition-hide="scale">
                        {{ $t("menu.statistics") }}
                    </q-tooltip>
                </q-item>

                <q-item to="/RecommendedCourses" clickable v-ripple>
                    <q-item-section avatar>
                        <q-icon name="star"></q-icon>
                    </q-item-section>

                    <q-item-section class="text-left">
                        {{ $t("menu.public_courses") }}
                    </q-item-section>

                    <q-tooltip v-if="menuClosed" class="bg-primary" anchor="center right" self="center left"
                               transition-show="scale" transition-hide="scale">
                        {{ $t("menu.public_courses") }}
                    </q-tooltip>
                </q-item>

                <q-item v-if="!menuClosed">
                    <q-item-section>
                        <q-item-label class="text-uppercase">
                            {{ $t("menu.recently_visited") }}
                        </q-item-label>
                    </q-item-section>
                </q-item>

                <q-item v-for="course in recentlyVisitedCourses" :to="'/course/'+course.id" clickable v-ripple>

                    <q-item-section avatar>
                        <q-icon name="school"></q-icon>
                    </q-item-section>

                    <q-item-section class="text-left">
                        {{ course.name }}
                    </q-item-section>

                    <q-tooltip v-if="menuClosed" class="bg-primary" anchor="center right" self="center left"
                               transition-show="scale" transition-hide="scale">
                        {{ course.name }}
                    </q-tooltip>
                </q-item>

                <q-item style="flex-grow: 1"></q-item>

                <q-item to="/admin" clickable v-ripple>
                    <q-item-section avatar>
                        <q-icon name="font_download"></q-icon>
                    </q-item-section>

                    <q-item-section class="text-left">
                        Admin
                    </q-item-section>
                </q-item>

                <q-item to="/Profile" clickable v-ripple>
                    <q-item-section avatar>
                        <q-icon name="person"></q-icon>
                    </q-item-section>

                    <q-item-section class="text-left">
                        {{ $t("menu.profile") }}
                    </q-item-section>

                    <q-tooltip v-if="menuClosed" class="bg-primary" anchor="center right" self="center left"
                               transition-show="scale" transition-hide="scale">
                        {{ $t("menu.profile") }}
                    </q-tooltip>
                </q-item>

                <q-item @click="logout" clickable v-ripple>
                    <q-item-section avatar>
                        <q-icon name="logout"></q-icon>
                    </q-item-section>

                    <q-item-section class="text-left">
                        {{ $t("menu.logout") }}
                    </q-item-section>

                    <q-tooltip v-if="menuClosed" class="bg-primary" anchor="center right" self="center left"
                               transition-show="scale" transition-hide="scale">
                        {{ $t("menu.logout") }}
                    </q-tooltip>
                </q-item>
            </q-list>
        </q-drawer>

        <div style="padding-top: 5vh; min-height: 100vh; display: flex; flex-direction: column">
            <div id="contentcard">
                <q-page-container style="padding: 0;">
                    <router-view v-if="isCoursePage" @updateRecentCourses="updateRecentCourses"/>
                    <router-view v-else/>
                </q-page-container>
            </div>
        </div>
    </q-layout>
    <q-layout v-if="landingPage">
        <router-view/>
    </q-layout>


</template>

<script>
// noinspection JSUnusedGlobalSymbols
import {useRouter} from "vue-router";

export default {
    data() {
        return {
            showMenu: true,
            landingPage: false,
            isCoursePage: false,
            recentlyVisitedCourses: []
        }
    },
    watch: {
        $route(newRoute) {
            this.showMenu = !["/login", "/registration", "/"].includes(newRoute.fullPath.toLowerCase());
            this.landingPage = newRoute.fullPath.toLowerCase() === "/";
            this.isCoursePage = newRoute.path.toLowerCase().startsWith("/course/");
        }
    },
    methods: {
        updateRecentCourses(courses) {
            this.recentlyVisitedCourses = courses
        }
    }
}
</script>

<script setup>
import router from "./router";
import {useUserStore} from "./stores/users";
import {ref} from "vue";
import {useRouter} from 'vue-router';

const menuClosed = ref(true);
const drawer = ref(null);
const userStore = useUserStore();
const recentlyVisitedCourses = ref([]);


(async () => {
    userStore.authenticate(sessionStorage.getItem("token"))
    if (userStore.authenticated) {
        const currentUserId = sessionStorage.getItem('id');
        recentlyVisitedCourses.value = (await userStore.getRecentlyVisitedCourses(currentUserId)).data;
        menuClosed.value = !(await userStore.hasMenuOpen(currentUserId)).data;
    }
})()

function menuButtonClick() {
    menuClosed.value = !menuClosed.value
    // noinspection JSUnresolvedFunction
    userStore.setMenuOpen(sessionStorage.getItem('id'), !menuClosed.value)
}

function logout() {
    // noinspection JSUnresolvedFunction
    userStore.logout();
    router.push("/login");
}

</script>

<style lang="scss">
@import "src/quasar-variables.sass";
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700&display=swap');

body {
    background-color: $secondary;
}

/*noinspection CssUnusedSymbol*/
#app, .buttonitem {
    font-family: Inter, sans-serif !important;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
}

#contentcard {
    width: 60%;
    flex-grow: 1;
    margin-left: auto;
    margin-right: auto;
    padding-top: 2em;
    padding-left: 2em;
    padding-right: 2em;
    box-shadow: 0 0 9px rgba(0, 0, 0, 0.25);
    border-top-left-radius: 15px;
    border-top-right-radius: 15px;
    background-color: white;
}

//contains full header (left-header: title & navigation bar, right-header: main score)
.header {
    display: flex;
    flex-direction: row;
    align-items: center;
    flex-wrap: wrap;
}

//left-header
.headerLeft {
    flex-grow: 2;
}

.scoreRect, .scoreRectSmall {
    font-weight: bold !important;
    color: white;
    background-color: $positive;
}

.scoreRect {
    border-radius: 15px;
    margin: 0 0 0 15px !important;
    font-size: 20px !important;
    padding: 7px 20px;
}

.scoreRectSmall {
    border-radius: 9px;
    margin: 0 !important;
    font-size: 15px !important;
    padding: 4px 12px;
}

.scoreNegative {
    background-color: $negative;
}

.headnav {
    display: flex;
    flex-direction: row;
    align-items: center;
    flex-wrap: wrap;
}

.navelem {
    text-decoration: none;
    margin: 0;
    color: black;
    font-weight: 500 !important;
}

.navactive {
    color: $secondary;
    margin: 0;
}

h1, h2, h3, h4, h5, h6 {
    display: block !important;
    line-height: normal !important;
    text-align: left !important;
}

.a-link {
    font-weight: bold !important;
    text-decoration: none;
}

a:hover, .a-link:hover {
    color: $primary !important;
    cursor: pointer;
}

button {
    text-decoration: none !important;
    text-transform: uppercase !important;
}

//usage: main title
h1 {
    font-size: 2em !important;
    margin: 0.2em 0 0.67em 0 !important;
    font-weight: bold !important;
    text-decoration: none;
}

//usage: only section titles
h2 {
    font-size: 1.5em !important;
    margin: 0.83em 0 !important;
    text-decoration: underline;
}

//usage: table title
h3 {
    font-size: 1.2em !important;
    margin: 1em 0 !important;
    text-decoration: underline;
}

h4 {
    font-size: 1.2em !important;
    margin: 1.33em 0 !important;
}

.buttonclass {
    display: flex;
    margin: 20px 0;
}

.buttonitem {
    margin: 5px 5px 5px 0 !important;
    font-weight: bold !important;
}

</style>
