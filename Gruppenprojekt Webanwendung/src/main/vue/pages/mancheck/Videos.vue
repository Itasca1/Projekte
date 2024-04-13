<template>
    <q-page style="display: flex; flex-direction: column; padding: 2em">
        <div style="text-align: start;">
            <div class="buttonclass">
                <q-btn class="buttonitem" outline rounded color="black" icon="arrow_back" @click="collectSelections(-1)">{{ $t("manual_check.prev_question") }}</q-btn>
                <div style="flex-grow: 2"></div>
                <q-btn class="buttonitem" outline rounded color="black" @click="collectSelections(0)">{{$t("manual_check.quit")}}</q-btn>
            </div>
            <ManualCheck number="2" :page="$i18n.messages[$i18n.locale]['manual_check']['videos']"/>
            <p>{{$t("manual_check.videos.task")}}</p><br>
        </div>
        <div>
            <div v-for="(video, index) in list" class="vidContainer">
                <div :id="'vidsrc-'+index" class="videoItem" v-html="video"></div>
                <div class="vidSubcontainer">
                    <div class="radioBtnsSubtitle">
                        <q-list>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioVid[index].value" val="correct" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.videos.vid_correct') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar top>
                                    <q-radio v-model="radioVid[index].value" val="incorrect" color="negative" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.videos.vid_incorrect') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </div>
                    <div class="radioBtnsAudio">
                        <q-list>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioAD[index].value" val="notRequired" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.videos.ad_notRequired') }}</q-item-label>
                                    <q-item-label caption>{{ $t('manual_check.videos.ad_notRequired_desc') }} </q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioAD[index].value" val="correct" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.videos.ad_correct') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar top>
                                    <q-radio v-model="radioAD[index].value" val="incorrect" color="negative" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.videos.ad_incorrect') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </div>
                </div>
            </div>
            <div v-if="visibleLength === 0">
                {{ $t('manual_check.videos.no_videos') }}
            </div>
            <div class="buttonclass">
                <q-btn @click="collectSelections(1)" class="buttonitem" unelevated rounded color="primary">{{$t("continue")}}</q-btn>
            </div>
        </div>
    </q-page>
</template>

<script>
import ManualCheck from "../../components/ManualCheck.vue";
export default {
    name: "Videos",
    components: {ManualCheck},
    data() {
        document.title = this.$t("manual_check.manual_check")
        return {}
    }
}
</script>

<script setup>
import {ref} from "vue";
import router from "../../router";
import {useHTMLStore} from "../../stores/html";
import {useReviewStore} from "../../stores/reviews";

const reviewStore = useReviewStore();
const list = ref(null);
const mancheckObject = JSON.parse(localStorage.getItem("mancheck"));
const htmlID = mancheckObject.htmlID;
const htmlStore = useHTMLStore();
const visibleLength = ref(0);
let len = 0;
let radioVid = [];
let radioAD = [];
const review = ref(null);

function checkForWrongReviewStep(review, position) {
    if (review.reviewPosition !== position) {
        review.reviewPosition = position;
    }
}

// noinspection JSUnresolvedFunction
htmlStore.requestHTML(htmlID).then(() => {
    const html = htmlStore.getHTML(htmlID)
    review.value = html.review
    checkForWrongReviewStep(review.value,2);

    // noinspection JSUnresolvedFunction
    htmlStore.getVideosFromHTML(htmlID).then(response => {
        len = response.data.length;
        visibleLength.value = len;
        for (let k=0; k<len; k++) {
            const data = response.data[k];

            const previousData = getAlreadyChecked(data);

            if (previousData.vid === null) {
                radioVid[k] = ref("incorrect");
                radioAD[k] = ref("incorrect");
            } else {
                radioVid[k] = ref(previousData.vid);
                radioAD[k] = ref(previousData.AD);
            }
        }
        list.value = response.data;
    })
})

function getAlreadyChecked(item) {
    const videoRevs = review.value.videoReviews;
    for (let k in videoRevs) {
        if (item === videoRevs[k].vidObject) {
            return {"vid" : videoRevs[k].vidValue, "AD" : videoRevs[k].adValue};
        }
    }
    return {"vid" : null, "AD" : null};
}

function collectSelections(position) {
    let results = []
    for (let k=0; k < len ; k++) {
        const vidValue = radioVid[k].value;
        const adValue = radioAD[k].value;
        const vidObject = document.getElementById('vidsrc-'+k).innerHTML;
        results.push({vidValue, adValue, vidObject});
    }
    review.value.videoReviews = results;
    saveReview(position);
}

function saveReview(toPosition) {
    review.value.reviewPosition += toPosition;
    // noinspection JSUnresolvedFunction
    reviewStore.requestSaveReview(review.value).then(review => {
        // noinspection JSUnresolvedFunction
        htmlStore.requestHTML(htmlID).then(() => {
            // noinspection JSUnresolvedFunction
            const html = htmlStore.getHTML(htmlID);
            html.review = review;
            // noinspection JSUnresolvedFunction
            htmlStore.requestUpdateHTML(html).then(() => {
                if (toPosition === -1) {
                    router.push({path: "/manualcheck/1"});
                } else if (toPosition === 0) {
                    router.push({path: "/course/" + mancheckObject.courseID});
                } else {
                    router.push({path: "/manualcheck/3"});
                }
            });
        })
    })
}

</script>

<style scoped>

.vidContainer {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    margin: 50px 20px;
}

.videoItem {
    margin: 0 20px 0 0;
}

video {
    object-fit: inherit !important;
}

.vidSubcontainer {
    text-align: left;
}

.radioBtnsSubtitle, .radioBtnsAudio {
    margin: 30px 0;
}

</style>
