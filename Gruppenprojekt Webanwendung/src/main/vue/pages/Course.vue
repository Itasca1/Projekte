<template>
    <div v-if="course">

        <div v-if="faculty" class="header">
            <div class="headerLeft">
                <div class="headnav" style="text-align: left">
                    <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
                    <router-link :to="'/Faculty/'+ faculty.id" class="navelem">{{ faculty.name }}</router-link> <q-icon name="chevron_right" size="16px"/>
                    <router-link :to="'/Group/'+ group.id" class="navelem">{{ group.name }}</router-link> <q-icon name="chevron_right" size="16px"/>
                    <p class="navelem navactive">{{course.name}}</p>
                </div>
                <h1>{{course.name}}</h1>
            </div>

            <div v-if="score <50" style="display: flex; flex-direction: row; align-items: center; justify-content: center">
                <h4>ACCESS-Score:</h4> <p class="scoreRect scoreNegative">{{ score }}%</p>
            </div>
            <div v-else style="display: flex; flex-direction: row; align-items: center; justify-content: center">
                <h4>ACCESS-Score:</h4> <p class="scoreRect">{{ score }}%</p>
            </div>
            <q-icon name="info" style="color: #3F50B5; padding-top: 4vh;" size="sm">
                <q-tooltip>
                    {{ $t("course_page.infoCourse") }}
                </q-tooltip>
            </q-icon>
        </div>

        <div class="buttonclass" v-if="allowedToEdit">
            <input type="file" id="html-input" accept=".html, .htm" hidden @change="uploadHTML($event)" ref="file" />
            <q-btn class="buttonitem" for="html-input" unelevated rounded icon="post_add" color="primary">
                <label v-if="allowedToEdit" for="html-input">{{ $t("course_page.new_review") }}</label>
            </q-btn>
            <div class="buttonitem">
                <q-btn flat round color="black" icon="more_vert" />
                <q-menu touch-position>
                    <q-list style="min-width: 100px">
                        <q-item clickable v-close-popup color="primary" rounded icon="edit" >
                            <q-item-section @click="edit_course = true">{{$t('course_page.manage_course')}}</q-item-section>
                        </q-item>
                        <q-item clickable v-close-popup color="red" rounded icon="delete" >
                            <q-item-section @click="delete_course = true">{{$t('manage.delete')}}</q-item-section>
                        </q-item>
                    </q-list>
                </q-menu>
            </div>
        </div>

    </div>

    <div style="text-align: left; margin: 30px 0 0 0">
        <h2>{{$t('Pages')}}</h2>
        <div v-if="course" class="row" style="padding-top: 5px">
            <div v-for="html in htmlList">
                <div v-if="isFinished(html)" class="button" style="display: flex; align-items: center; background: white; color: #1D1D1D; font-size: medium" @click="goToHTMLPage(html)">
                    <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                    <div class="a-link">{{html.name}}</div>
                </div>
            </div>
        </div>

        <h2 v-if="allowedToEdit">{{$t('Incomplete')}}</h2>
        <div v-if="course && allowedToEdit" class="row" style="padding-top: 5px">
            <div v-for="html in htmlList">
                <div v-if="!isFinished(html)" class="button" style="display: flex; align-items: center; background: white; color: #1D1D1D; font-size: medium" @click="continueReview(html)">
                    <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                    <div class="a-link">{{html.name}}</div>
                </div>
            </div>
        </div>
        <h2>{{$t('Checklist')}}</h2>
        <div v-if="course" style="padding-top: 5px; text-align: left">
            <div v-if="listsFalse.length > 0">
                <q-item style="font-size: large">
                    <q-item-section avatar top>
                        <q-avatar icon="close" color="white" text-color="negative" style="font-size: 500%"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label lines="1"><b>{{ $t("course_page.lists_result") }}</b></q-item-label>
                        <div v-for="list in listsFalse">
                            <q-item-label caption style="color: #1D1D1D; font-size: smaller"
                                          @click="gotoHTMLFromChecklist(list)">
                                <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                                <u style="cursor: pointer">{{ list }}</u>
                            </q-item-label>
                        </div>
                    </q-item-section>
                </q-item>
            </div>
            <div v-if="!listsFalse.length > 0">
                <div v-if="listsTrue.length > 0">
                    <q-item style="font-size: large">
                        <q-item-section avatar top>
                            <q-avatar icon="done" color="white" text-color="positive" style="font-size: 500%"/>
                        </q-item-section>
                        <q-item-section>
                            <q-item-label lines="1"><b>{{ $t("course_page.lists_result") }}</b></q-item-label>
                        </q-item-section>
                    </q-item>
                </div>
            </div>
            <div v-if="linksFalse.length > 0">
                <q-item style="font-size: large">
                    <q-item-section avatar top>
                        <q-avatar icon="close" color="white" text-color="negative" style="font-size: 500%"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label lines="1"><b>{{ $t("course_page.link_result") }}</b></q-item-label>
                        <div v-for="link in linksFalse">
                            <q-item-label caption style="color: #1D1D1D; font-size: smaller"
                                          @click="gotoHTMLFromChecklist(link)">
                                <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                                <u style="cursor: pointer">{{ link }}</u>
                            </q-item-label>
                        </div>
                    </q-item-section>
                </q-item>
            </div>
            <div v-if="!linksFalse.length > 0">
                <div v-if="linksTrue.length > 0">
                    <q-item style="font-size: large">
                        <q-item-section avatar top>
                            <q-avatar icon="done" color="white" text-color="positive" style="font-size: 500%"/>
                        </q-item-section>
                        <q-item-section>
                            <q-item-label lines="1"><b>{{ $t("course_page.link_result") }}</b></q-item-label>
                        </q-item-section>
                    </q-item>
                </div>
            </div>
            <div v-if="textFalse.length > 0">
                <q-item style="font-size: large">
                    <q-item-section avatar top>
                        <q-avatar icon="close" color="white" text-color="negative" style="font-size: 500%"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label lines="1"><b>{{ $t("course_page.textFormatting_result") }}</b></q-item-label>
                        <div v-for="text in textFalse">
                            <q-item-label caption style="color: #1D1D1D; font-size: smaller"
                                          @click="gotoHTMLFromChecklist(text)">
                                <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                                <u style="cursor: pointer">{{ text }}</u>
                            </q-item-label>
                        </div>
                    </q-item-section>
                </q-item>
            </div>
            <div v-if="!textFalse.length > 0">
                <div v-if="textTrue.length > 0">
                    <q-item style="font-size: large">
                        <q-item-section avatar top>
                            <q-avatar icon="done" color="white" text-color="positive" style="font-size: 500%"/>
                        </q-item-section>
                        <q-item-section>
                            <q-item-label lines="1"><b>{{ $t("course_page.textFormatting_result") }}</b></q-item-label>
                        </q-item-section>
                    </q-item>
                </div>
            </div>
            <div v-if="videosFalse.length > 0">
                <q-item style="font-size: large">
                    <q-item-section avatar top>
                        <q-avatar icon="close" color="white" text-color="negative" style="font-size: 500%"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label lines="1"><b>{{ $t("course_page.video_test") }}</b></q-item-label>
                        <div v-for="video in videosFalse">
                            <q-item-label caption style="color: #1D1D1D; font-size: smaller"
                                          @click="gotoHTMLFromChecklist(video)">
                                <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                                <u style="cursor: pointer">{{ video }}</u>
                            </q-item-label>
                        </div>
                    </q-item-section>
                </q-item>
            </div>
            <div v-if="!videosFalse.length > 0">
                <div v-if="videosTrue.length > 0">
                    <q-item style="font-size: large">
                        <q-item-section avatar top>
                            <q-avatar icon="done" color="white" text-color="positive" style="font-size: 500%"/>
                        </q-item-section>
                        <q-item-section>
                            <q-item-label lines="1"><b>{{ $t("course_page.video_test") }}</b></q-item-label>
                        </q-item-section>
                    </q-item>
                </div>
            </div>
            <div v-if="imageFalse.length > 0">
                <q-item style="font-size: large">
                    <q-item-section avatar top>
                        <q-avatar icon="close" color="white" text-color="negative" style="font-size: 500%"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label lines="1"><b>{{ $t("course_page.image_test") }}</b></q-item-label>
                        <div v-for="image in imageFalse">
                            <q-item-label caption style="color: #1D1D1D; font-size: smaller"
                                          @click="gotoHTMLFromChecklist(image)">
                                <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                                <u style="cursor: pointer">{{ image }}</u>
                            </q-item-label>
                        </div>
                    </q-item-section>
                </q-item>
            </div>
            <div v-if="!imageFalse.length > 0">
                <div v-if="imageTrue.length > 0">
                    <q-item style="font-size: large">
                        <q-item-section avatar top>
                            <q-avatar icon="done" color="white" text-color="positive" style="font-size: 500%"/>
                        </q-item-section>
                        <q-item-section>
                            <q-item-label lines="1"><b>{{ $t("course_page.image_test") }}</b></q-item-label>
                        </q-item-section>
                    </q-item>
                </div>
            </div>
            <div v-if="automaticFalse.length > 0">
                <q-item style="font-size: large">
                    <q-item-section avatar top>
                        <q-avatar icon="close" color="white" text-color="negative" style="font-size: 500%"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label lines="1"><b>{{ $t("course_page.automatic_test") }}</b></q-item-label>
                        <div v-for="automatic in automaticFalse">
                            <q-item-label caption style="color: #1D1D1D; font-size: smaller"
                                          @click="gotoHTMLFromChecklist(automatic)">
                                <q-avatar icon="description" style="cursor: pointer"></q-avatar>
                                <u style="cursor: pointer">{{ automatic }}</u>
                            </q-item-label>
                        </div>
                    </q-item-section>
                </q-item>
            </div>
            <div v-if="!automaticFalse.length > 0">
                <div v-if="automaticTrue.length > 0">
                    <q-item style="font-size: large">
                        <q-item-section avatar top>
                            <q-avatar icon="done" color="white" text-color="positive" style="font-size: 500%"/>
                        </q-item-section>
                        <q-item-section>
                            <q-item-label lines="1"><b>{{ $t("course_page.automatic_test") }}</b></q-item-label>
                        </q-item-section>
                    </q-item>
                </div>
            </div>
        </div>
    </div>

    <div>
        <q-dialog v-model="edit_course" persistent>
            <q-card style="width: 500px; max-width: 80vw;">
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.update_course') }}</div>
                    <q-separator inset/>
                    <div class="q-mb-md">
                        <q-input
                            outlined
                            rounded
                            :label="$t('database.course_name')"
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
                            v-model="new_group"
                            :options="groups"
                            option-label="name"
                            :label="$t('database.group')"
                        >
                        <template v-slot:prepend>
                            <q-icon name="edit" />
                        </template>
                        </q-select>
                    </div>
                    <div>
                        <q-checkbox v-model="new_coursePublic" :label="$t('database.course_public')"/>
                    </div>
                    <div>
                        {{ $t('database.weight') }} :
                        <q-select
                            outlined
                            v-model="new_weighted"
                            :options="weighing"
                            option-label="weightig"
                            :label="$t('database.weight')"
                        />
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
                        @click="updateCourse(new_name, new_group,new_weighted, new_coursePublic)"
                    />
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>

    <div>
        <q-dialog v-model="delete_course" persistent>
            <q-card style="width: 400px; max-width: 80vw;">
                <q-card-section>
                    <div class="text-h5 q-mb-md">{{ $t('manage.delete_selected_course') }}</div>
                    <q-separator inset/>
                    <div>
                        {{ $t('manage.delete_confirm') }}
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
                        :label="$t('manage.delete')"
                        color="primary"
                        @click="deleteCourse"
                        v-close-popup
                    />
                </q-card-actions>
            </q-card>
        </q-dialog>
    </div>

</template>

<script>
export default {
    name: "Course",
    emits: ["updateRecentCourses"],
}
</script>

<script setup>
import {useGroupStore} from "../stores/groups";
import {useCourseStore} from "../stores/courses";
import {useHTMLStore} from "../stores/html";
import {useUserStore} from "../stores/users";
import {useRoute} from "vue-router";
import {ref, toRaw} from "vue";
import router from "../router";
import {useQuasar} from "quasar";
import {useI18n} from "vue-i18n";
import {useReviewStore} from "../stores/reviews";

const $q = useQuasar()
const {t} = useI18n()
const groupStore = useGroupStore()
const reviewStore = useReviewStore();
const courseStore = useCourseStore()
const htmlStore = useHTMLStore()
const userStore = useUserStore();
const route = useRoute()
const courseID = route.params.id
const course = ref(null)
const score = ref(null)

// says wether a user can edit a course
const allowedToEdit = ref(false)

const emit = defineEmits(['updateRecentCourses'])
const coursePublic = ref('')

// Boolean to check if user wants to edit course
const edit_course = ref(false)

// Boolean to check if user wants to delete course
const delete_course = ref(false)

// New course name from user
const new_name = ref("")

// New group from user
const new_group = ref()

// New visibility option from user
const new_coursePublic = ref('')

const new_weighted = ref()
// all availabel groups
const groups = ref([])
const weighing = ref([t("course_page.weight_weak"), t("course_page.weight_strong")])

//All htmlFiles
const htmlList = ref([])
//All reviews
const reviewList = ref([])
//automaticResultsTrue
const automaticTrue = ref([])
//automaticResultsFalse
const automaticFalse = ref([])
//videosResultsTrue
const videosTrue = ref([])
//videosResultFalse
const videosFalse = ref([])
//imageResultTrue
const imageTrue = ref([])
//imageResultFalse
const imageFalse = ref([])
//listResultTrue
const listsTrue = ref([])
//listsResultFalse
const listsFalse = ref([])
//textResultTrue
const textTrue = ref([])
//textResultFale
const textFalse = ref([])
//linksResultTrue
const linksTrue = ref([])
//linksResultFalse
const linksFalse = ref([])

const group = ref(null)

const faculty = ref(null)
const visible = ref(null)
const confirm_visible = ref(false)
let scoreColor = ref(null)

// noinspection JSUnresolvedFunction
courseStore.getScore(courseID).then(res => {
    score.value = res.data
})



loadPage()
loadGroups()
checkAuthorization()

function checkAuthorization() {
    courseStore.requestAllowEdit(courseID).then(res => {
        allowedToEdit.value = res.data
    })
}

// loads all available groups from user
function loadGroups() {
    groupStore.requestGroups().then(() => {
        groups.value = groupStore.getGroups()
    })
}

function loadPage() {
    // noinspection JSUnresolvedFunction
    courseStore.requestCourse(courseID).then(() => {

        // noinspection JSUnresolvedFunction

        course.value = courseStore.getCourse(courseID)
        group.value = course.value.group
        faculty.value = group.value.faculty
        new_name.value = course.value.name
        new_group.value = course.value.group
        new_coursePublic.value = course.value.coursePublic
        new_weighted.value = null
    })

    // noinspection JSUnresolvedFunction
    courseStore.requestCourse(courseID).then(() => {
        // noinspection JSUnresolvedFunction
        course.value = courseStore.getCourse(courseID);
        document.title = course.value.name;
        (async () => {
            // noinspection JSUnresolvedFunction
            userStore.addRecentlyVisitedCourse(sessionStorage.getItem("id"), toRaw(course.value))
                .then(courses => emit('updateRecentCourses', courses))
        })()
        htmlList.value = course.value.htmlList
        for (let i in htmlList.value) {
            reviewList.value.push(htmlList.value[i].review)
        }
        createPageReview()
    })
}

function preEdit() {
    edit_course.value = true
    new_group.value = course.value.group
}

// updates course
function updateCourse(change_name, change_group, change_weighted, change_coursePublic) {
    const weighted = ref(null)
    if (course == null) {
        $q.notify({
            type: 'negative',
            message: t('manage.no_course_available'),
            caption: t('manage.no_couse_selected')
        })
    }
    weighted.value = change_weighted.toString() === t("course_page.weight_strong");
    let new_course = {
        id: course.value.id,
        name: change_name,
        htmlList: course.value.htmlList,
        group: change_group,
        coursePublic: change_coursePublic,
        //weighted: weighted.value
    }
    console.log(change_coursePublic)

    courseStore.requestUpdateCourse(new_course).then(() => {
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
    group.value = change_group
    if (group.value.id != change_group.id) {
        router.push('../')
    }

    loadPage()
    reloadPage()
}

// reloade Page
function reloadPage() {
    window.location.reload()
}

// deletes course
function deleteCourse() {
    courseStore.requestDeleteCourse(course.value).then(() => {
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

function isFinished(html) {
    return html.review.reviewComplete;
}

function continueReview(html) {
    const step = html.review.reviewPosition;
    localStorage.setItem("mancheck", JSON.stringify({
        htmlID: html.id,
        courseID: courseID
    }))
    if (step === 0) {
        router.push({path: `/AutomaticResults`});
    } else {
        router.push({path: `/manualcheck/${step}`});
    }
}

function gotoHTMLFromChecklist(html) {
    for (let i = 0; i < course.value.htmlList.length; i++) {
        if (course.value.htmlList[i].name === html) {
            if (course.value.htmlList[i].id !== null) {
                const courseID = course.value.id
                const id = course.value.htmlList[i].id
                const path = "/Course/" + courseID + "/DetailedView/" + id
                router.push(path)
            }
        }
    }
}

function goToHTMLPage(html) {
    for (let i = 0; i < course.value.htmlList.length; i++) {
        if (course.value.htmlList[i].name === html.name) {
            if (course.value.htmlList[i].id !== null) {
                const courseID = course.value.id
                const id = course.value.htmlList[i].id
                const path = "/Course/" + courseID + "/DetailedView/" + id
                router.push(path)
            }
        }
    }
}

//Fills all true/false Arrays with the name of the file
function createPageReview() {
    for (let i = 0; i < course.value.htmlList.length; i++) {
        if (course.value.htmlList[i].review.automaticReviews.length > 0) {
            let automaticResult = getPassedAutomatic(course.value.htmlList[i].review.automaticReviews)
            if (automaticResult || !isFinished(course.value.htmlList[i])) {
                automaticTrue.value.push(course.value.htmlList[i].name)
            } else {
                automaticFalse.value.push(course.value.htmlList[i].name)
            }
        }
        if (course.value.htmlList[i].review.videoReviews.length > 0) {
            let videoResult = getPassedVideos(course.value.htmlList[i].review.videoReviews)
            if (videoResult || !isFinished(course.value.htmlList[i])) {
                videosTrue.value.push(course.value.htmlList[i].name)
            } else {
                videosFalse.value.push(course.value.htmlList[i].name)
            }
        }
        if (course.value.htmlList[i].review.imageReviews.length > 0) {
            let imageResult = getPassedImages(course.value.htmlList[i].review.imageReviews)
            if (imageResult || !isFinished(course.value.htmlList[i])) {
                imageTrue.value.push(course.value.htmlList[i].name)
            } else {
                imageFalse.value.push(course.value.htmlList[i].name)
            }
        }
        if (course.value.htmlList[i].review.listReviews.length > 0) {
            let listsResult = getPassedLists(course.value.htmlList[i].review.listReviews)
            if (listsResult || !isFinished(course.value.htmlList[i])) {
                listsTrue.value.push(course.value.htmlList[i].name)
            } else {
                listsFalse.value.push(course.value.htmlList[i].name)
            }
        }
        if (course.value.htmlList[i].review.formattingReviews.length > 0) {
            let textResult = getPassedFormattedTexts(course.value.htmlList[i].review.formattingReviews)
            if (textResult || !isFinished(course.value.htmlList[i])) {
                textTrue.value.push(course.value.htmlList[i].name)
            } else {
                textFalse.value.push(course.value.htmlList[i].name)
            }
        }
        if (course.value.htmlList[i].review.linkReviews.length > 0) {
            let linkResults = getPassedLinks(course.value.htmlList[i].review.linkReviews)
            if (linkResults || !isFinished(course.value.htmlList[i])) {
                linksTrue.value.push(course.value.htmlList[i].name)
            } else {
                linksFalse.value.push(course.value.htmlList[i].name)
            }
        }
    }
}

function getPassedAutomatic(e) {
    for (let i in e) {
        if (e[i].passed === false) return false;
    }
    return true;
}

function getPassedImages(e) {
    for (let i in e) {
        if (e[i].altValue === "incorrect" || e[i].txtValue === "incorrect") return false;
    }
    return true;
}

function getPassedVideos(e) {
    for (let i in e) {
        if (e[i].adValue === "incorrect" || e[i].vidValue === "incorrect") return false;
    }
    return true;
}

function getPassedLinks(e) {
    for (let i in e) {
        if (e[i].value === "incorrect") return false;
    }
    return true;
}

function getPassedLists(e) {
    for (let i in e) {
        if (e[i].value === "incorrect") return false;
    }
    return true;
}

function getPassedFormattedTexts(e) {
    for (let i in e) {
        if (e[i].value === "incorrect") return false;
    }
    return true;
}

function uploadHTML(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = (res) => {

        const html = res.target.result
        // Saves name
        const name = file.name

        const empty = {
            htmlID: html.id, courseID: courseID, review: {
                reviewComplete: false,
                reviewPosition: 0,
                automaticReviews: [],
                formattingReviews: [],
                imageReviews: [],
                linkReviews: [],
                videoReviews: [],
                listReviews: []
            }
        }

        // noinspection JSUnresolvedFunction
        reviewStore.requestSaveReview(empty.review).then(review => {


            // noinspection JSUnresolvedFunction
            htmlStore.requestSaveHTML({htmlString: html, review: review, name: name}).then(html => {
                // noinspection JSUnresolvedFunction
                courseStore.requestAddHtml(course.value, html).then(() => {
                    localStorage.setItem("mancheck", JSON.stringify({
                        htmlID: html.id,
                        courseID: courseID,
                    }))
                    router.push({path: "/AutomaticResults"})
                })
            })
        })


    };
    reader.readAsText(file);
}


function changeToManage_Group() {
    router.push({path: "/Manage_Group"})
}

visible.value = false;
getCourseVisibility()
function getCourseVisibility(){
    courseStore.requestCourse(courseID).then(() => {
        visible.value = courseStore.getCourse(courseID).visible;
    })
}


function changeVisibility(){
    visible.value = !visible.value;

    courseStore.requestCourse(courseID).then(() => {
        let course = courseStore.getCourse(courseID);
        let new_course = {
            id: course.id,
            name: course.name,
            htmlList: course.htmlList,
            group: course.group,
            visible: visible.value
        }
        courseStore.requestUpdateCourse(new_course)
    })



}


</script>

<style scoped>

.button {
    background-color: #3F50B5;
    color: white;
    padding: 10px;
    border-radius: 15px;
    font-family: Inter, sans-serif;
    font-size: 20px;
}

.button:hover {
    background-color: #475BCC;
}
</style>
