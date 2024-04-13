<template>
    <div v-if="html">
        <div v-if="faculty" class="headerLeft">

            <div class="headnav" style="text-align: left">
                <router-link :to="'/Dashboard'" class="navelem">{{$t('menu.dashboard')}}</router-link> <q-icon name="chevron_right" size="16px"/>
                <router-link :to="'/Faculty/'+ faculty.id" class="navelem">{{ faculty.name }}</router-link> <q-icon name="chevron_right" size="16px"/>
                <router-link :to="'/Course/' + course.id" class="navelem">{{ course.name }}</router-link> <q-icon name="chevron_right" size="16px"/>
                <p class="navelem navactive">{{ html.name }}</p>
            </div>
            <h1>{{$t('detailedView.review')}}: <p style="display: inline !important; font-weight: 300 !important;">{{html.name}}</p></h1>
        </div>

        <div class="buttonclass">
            <input type="file" id="html-input" accept=".html, .htm" hidden @change="uploadHTML($event)" ref="file" />
            <q-btn class="buttonitem" unelevated rounded color="primary">
                <label for="html-input">{{ $t('detailedView.retest') }}</label>
            </q-btn>
            <q-btn  @click="openInNewTab" class="buttonitem" outline rounded color="black">{{ $t('detailedView.show') }}</q-btn>
            <div class="buttonitem">
                <q-btn flat round color="black" icon="more_vert" />
                <q-menu touch-position>
                    <q-list style="min-width: 100px">
                        <q-item clickable v-close-popup>
                            <q-item-section @click="confirm_deletion = true">{{ $t('detailedView.delete') }}</q-item-section>
                        </q-item>
                    </q-list>
                </q-menu>
            </div>
        </div>

        <div class="row q-pb-lg" style="display: none">
            <q-dialog v-model="confirm_deletion">
                <q-card>
                    <q-card-section>
                        <div class="text-h6">{{ $t('detailedView.delete_title') }}</div>
                    </q-card-section>

                    <q-card-section class="q-pt-none">
                        {{ $t('detailedView.delete_desc') }}
                    </q-card-section>

                    <q-card-actions align="right">
                        <q-btn v-close-popup> {{ $t('detailedView.delete_cancel') }} </q-btn>
                        <q-btn v-close-popup @click="deletePage"> {{ $t('detailedView.delete_confirm') }} </q-btn>
                    </q-card-actions>
                </q-card>
            </q-dialog>
        </div>

        <h2 style="margin-top: 20px !important;">{{ $t('detailedView.checklist') }}</h2>

        <div class="row q-pb-md">
            <div v-if="review">
                <q-item>
                    <q-item-section top avatar>
                        <q-icon v-if="!review.automaticReviews.length > 0" name="check" size="32px"  color="positive"/>
                        <q-icon v-else name="close" size="32px" color="negative"/>
                    </q-item-section>

                    <q-item-section>
                        <q-item-label style="text-align: left"><div class="text-h6"> {{ $t('detailedView.automatic') }} </div></q-item-label>
                        <q-item-label caption v-if="review.automaticReviews.length > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ $t('detailedView.automatic_desc') }}
                        </q-item-label>
                    </q-item-section>

                </q-item>

                <q-item id="Images" v-if="review.imageReviews.length > 0">
                    <q-item-section top avatar>
                        <q-icon v-if="getPassedImages(review.imageReviews)" name="check" size="32px"  color="positive"/>
                        <q-icon v-else name="close" size="32px" color="negative"/>
                    </q-item-section>

                    <q-item-section>
                        <q-item-label style="text-align: left"><div class="text-h6"> {{ $t('detailedView.images') }} </div></q-item-label>
                        <q-item-label caption v-if="errVals[0] > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ errVals[0] }} {{ $t('detailedView.image_desc_alt') }}
                        </q-item-label>
                        <q-item-label caption v-if="errVals[0] > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ errVals[1] }} {{ $t('detailedView.image_desc_txt') }}
                        </q-item-label>
                    </q-item-section>
                </q-item>

                <q-item id="Videos" v-if="review.videoReviews.length > 0">
                    <q-item-section top avatar>
                        <q-icon v-if="getPassedVideos(review.videoReviews)" name="check" size="32px"  color="positive"/>
                        <q-icon v-else name="close" size="32px" color="negative"/>
                    </q-item-section>

                    <q-item-section>
                        <q-item-label style="text-align: left"><div class="text-h6"> {{ $t('detailedView.videos') }} </div></q-item-label>
                        <q-item-label caption v-if="errVals[2] > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ errVals[2] }}  {{ $t('detailedView.video_desc_ad') }}
                        </q-item-label>
                        <q-item-label caption v-if="errVals[3] > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ errVals[3] }} {{ $t('detailedView.video_desc_vid') }}
                        </q-item-label>
                    </q-item-section>
                </q-item>

                <q-item id="Links" v-if="review.linkReviews.length > 0">
                    <q-item-section top avatar>
                        <q-icon v-if="getPassedLinks(review.linkReviews)" name="check" size="32px"  color="positive"/>
                        <q-icon v-else name="close" size="32px" color="negative"/>
                    </q-item-section>

                    <q-item-section>
                        <q-item-label style="text-align: left"><div class="text-h6"> {{ $t('detailedView.links') }} </div></q-item-label>
                        <q-item-label caption v-if="errVals[4] > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ errVals[4] }}  {{ $t('detailedView.link_desc') }}
                        </q-item-label>
                    </q-item-section>
                </q-item>

                <q-item id="Lists" v-if="review.listReviews.length > 0">
                    <q-item-section top avatar>
                        <q-icon v-if="getPassedLists(review.listReviews)" name="check" size="32px"  color="positive"/>
                        <q-icon v-else name="close" size="32px" color="negative"/>
                    </q-item-section>

                    <q-item-section>
                        <q-item-label style="text-align: left"><div class="text-h6"> {{ $t('detailedView.lists') }} </div></q-item-label>
                        <q-item-label caption v-if="errVals[5] > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ errVals[5] }}  {{ $t('detailedView.list_desc') }}
                        </q-item-label>
                    </q-item-section>
                </q-item>

                <q-item id="Text" v-if="review.formattingReviews.length > 0">
                    <q-item-section top avatar>
                        <q-icon v-if="getPassedFormatting(review.formattingReviews)" name="check" size="32px"  color="positive"/>
                        <q-icon v-else name="close" size="32px" color="negative"/>
                    </q-item-section>

                    <q-item-section>
                        <q-item-label style="text-align: left"><div class="text-h6"> {{ $t('detailedView.formatting') }} </div></q-item-label>
                        <q-item-label caption v-if="errVals[6] > 0" style="text-align: left">
                            <q-icon name="close" size="16px" color="negative"/>
                            {{ errVals[6] }}  {{ $t('detailedView.formatting_desc') }}
                        </q-item-label>
                    </q-item-section>
                </q-item>
            </div>
        </div>
    </div>
</template>

<script>

import {useHTMLStore} from "../stores/html.js";
import {useCourseStore} from "../stores/courses.js";
import {ref} from 'vue';
import router from "../router";
import {useRoute} from "vue-router";
import {useReviewStore} from "../stores/reviews";

export default {
    name: "DetailedView",
    setup(){
        const html = ref(null)
        const course = ref(null)
        const group = ref(null)
        const faculty = ref(null)
        const review = ref(null)
        const err_vals = ref([])

        const htmlStore = useHTMLStore()
        const courseStore = useCourseStore()
        const reviewStore = useReviewStore()


        const confirm_deletion = ref(false)
        const confirm_retest = ref(false)
        const html_id = useRoute().params.id
        const course_id = useRoute().params.co
        htmlStore.requestHTML(html_id).then(() => {
            html.value = htmlStore.getHTML(html_id)
            review.value = html.value.review

        })

        courseStore.requestCourse(course_id).then(() => {
            course.value = courseStore.getCourse(course_id)
            group.value = course.value.group
            faculty.value = group.value.faculty
        })

        for(let i = 0; i < 7; i++){
            err_vals.value.push(0);
        }

        function getPassedAutomatic(e){
            for(let i in e){
                if(e[i].passed === false) return false;
            }
            return true;
        }

        function getPassedImages(e){
            err_vals.value[0] = 0;
            err_vals.value[1] = 0;
            let errors = true;
            for(let i in e){
                if(e[i].altValue === "incorrect"){
                    err_vals.value[0]++;
                    errors = false;
                }
                if(e[i].txtValue === "incorrect"){
                    err_vals.value[1]++;
                    errors = false;
                }
            }
            return errors;
        }

        function getPassedVideos(e){
            err_vals.value[2] = 0;
            err_vals.value[3] = 0;
            let errors = true;
            for(let i in e){
                if(e[i].adValue === "incorrect"){
                    err_vals.value[2]++;
                    errors = false;
                }
                if(e[i].vidValue === "incorrect"){
                    err_vals.value[3]++;
                    errors = false;
                }
            }
            return errors;
        }

        function getPassedLinks(e){
            err_vals.value[4] = 0;
            let errors = true;
            for(let i in e){
                if(e[i].value === "incorrect"){
                    err_vals.value[4]++;
                    errors = false;
                }
            }
            return errors;
        }

        function getPassedLists(e){
            err_vals.value[5] = 0;
            let errors = true;
            for(let i in e){
                if(e[i].value === "incorrect"){
                    err_vals.value[5]++;
                    errors = false;
                }
            }
            return errors;
        }

        function getPassedFormatting(e){
            err_vals.value[6] = 0;
            let errors = true;
            for(let i in e){
                if(e[i].value === "incorrect"){
                    err_vals.value[6]++;
                    errors = false;
                }
            }
            return errors;
        }

        function openInNewTab(){
            const wnd = window.open("about:blank", "");
            wnd.document.write(html.value.htmlString);
            wnd.document.close();

        }
        function deletePage(){

            htmlStore.deleteHTML(html_id);
            htmlStore.requestHTML(html_id).then(() => {
                html.value = htmlStore.getHTML(html_id)
            })
            router.push({path: "/Course/" + course_id})
        }


        function uploadHTML(event) {
            const file = event.target.files[0];
            const reader = new FileReader();

            reader.onload = (res) => {

                const html = res.target.result
                // Saves name
                const name = file.name

                const empty = {
                    htmlID: html.id, courseID: course.value, review: {
                        id: review.value.id,
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
                reviewStore.requestUpdateReview(empty.review).then(review => {


                    // noinspection JSUnresolvedFunction
                    htmlStore.requestUpdateHTML({id: html_id, htmlString: html, review: review, name: name}).then(html => {
                        // noinspection JSUnresolvedFunction
                        localStorage.setItem("mancheck", JSON.stringify({
                            htmlID: html_id,
                            courseID: course_id,
                        }))
                        router.push({path: "/AutomaticResults"})
                    })
                })


            };
            reader.readAsText(file);
        }


        return {
            html,
            review,
            errVals: err_vals,
            html_id,
            course_id,
            course,
            group,
            faculty,
            confirm_deletion,
            confirm_retest,
            getPassedAutomatic,
            getPassedImages,
            getPassedVideos,
            getPassedLists,
            getPassedLinks,
            getPassedFormatting,
            openInNewTab,
            deletePage,
            uploadHTML
        }
    }
}
</script>

<style scoped>

</style>
