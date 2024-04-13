<template>
    <q-page style="display: flex; flex-direction: column;">
        <div style="text-align: start;">
            <div class="buttonclass">
                <q-btn class="buttonitem" outline rounded color="black" icon="arrow_back" @click="collectSelections(-1)">{{ $t("manual_check.prev_question") }}</q-btn>
                <div style="flex-grow: 2"></div>
                <q-btn class="buttonitem" outline rounded color="black" @click="collectSelections(0)">{{$t("manual_check.quit")}}</q-btn>
            </div>
            <ManualCheck number="1" :page="$i18n.messages[$i18n.locale]['manual_check']['images']"/>
            <p>{{$t("manual_check.images.task")}}</p><br>
            <PopupReader :label="$t('manual_check.show_context')" :htmlID="htmlID"/>
        </div>
        <div>
            <div v-for="(image, index) in list" class="imgContainer">
                <div id="imgSizeFixer"><img :id="'img-'+index" class="previewImg" :src="image[0]" :alt="image[1]"></div>
                <div class="imgSubcontainer">
                    <b v-if="image[1] === '' || image[1] === null"><em>{{$t("manual_check.images.no_alt")}}</em></b>
                    <p v-else>{{$t("manual_check.images.alternativetext")}}: <b>{{image[1]}}</b></p>
                    <div class="radioBtnsAlt">
                        <q-list>
                            <q-item v-if="image[1] !== '' && image[1] !== null" tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioAlt[index].value" val="correct" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.images.alt_correct') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioAlt[index].value" val="decorative" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.images.alt_decorative') }}</q-item-label>
                                    <q-item-label caption>{{ $t('manual_check.images.alt_decorative_desc') }} </q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar top>
                                    <q-radio v-model="radioAlt[index].value" val="incorrect" color="negative" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.images.alt_incorrect') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </div>

                    <div class="radioBtnsText">
                        <q-list>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioTxt[index].value" val="correct" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.images.txt_correct') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioTxt[index].value" val="isLogo" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.images.txt_isLogo') }}</q-item-label>
                                    <q-item-label caption>{{ $t('manual_check.images.txt_isLogo_desc') }} </q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar top>
                                    <q-radio v-model="radioTxt[index].value" val="incorrect" color="negative" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t('manual_check.images.txt_incorrect') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </div>
                </div>
            </div>
            <div v-if="visibleLength === 0">
                {{ $t('manual_check.page1.no_images') }}
            </div>
            <div class="buttonclass">
                <q-btn @click="collectSelections(1)" class="buttonitem" unelevated rounded color="primary">{{$t("continue")}}</q-btn>
            </div>
        </div>
    </q-page>
</template>

<script>
import ManualCheck from "../../components/ManualCheck.vue";
import PopupReader from "../../components/PopupReader.vue";
export default {
    name: "Images",
    components: {ManualCheck, PopupReader},
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
const htmlStore = useHTMLStore()
const visibleLength = ref(0);
let len = 0;
let radioAlt = [];
let radioTxt = [];
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
    checkForWrongReviewStep(review.value, 1);

    // noinspection JSUnresolvedFunction
    htmlStore.getImagesFromHTML(htmlID).then(response => {
        len = response.data.length;
        visibleLength.value = len;
        for (let k=0; k<len; k++) {
            const data = response.data[k];
            const previousData = getAlreadyChecked(data[0]);
            if (previousData.alt === null) {
                if (response.data[k][1] === '') {
                    radioAlt[k] = ref("decorative");
                } else {
                    radioAlt[k] = ref("incorrect");
                }
                radioTxt[k] = ref("incorrect");
            } else {
                radioAlt[k] = ref(previousData.alt);
                radioTxt[k] = ref(previousData.txt);
            }
        }
        list.value = response.data;
    })
})

function getAlreadyChecked(item) {
    const imageRevs = review.value.imageReviews;
    for (let k in imageRevs) {
        if (item === imageRevs[k].imgValue) {
            return {"alt" : imageRevs[k].altValue, "txt" : imageRevs[k].txtValue};
        }
    }
    return {"alt" : null, "txt" : null};
}

function collectSelections(position) {
    let results = []
    for (let k=0; k < len ; k++) {
        const imgValue = document.getElementById('img-'+k).src;
        const altValue = radioAlt[k].value;
        const txtValue = radioTxt[k].value;
        results.push({altValue, txtValue, imgValue});
    }
    review.value.imageReviews = results;
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
                    router.push({path: "/AutomaticResults"});
                } else if (toPosition === 0) {
                    router.push({path: "/course/" + mancheckObject.courseID});
                } else {
                    router.push({path: "/manualcheck/2"});
                }
            });
        })
    })
}

</script>

<style scoped>

.previewImg {
    min-height: 50px;
    max-height: 300px;
    min-width: 50px;
    max-width: 300px;
    border: 1px black solid;
}

#imgSizeFixer {
    display: flex;
    align-items: flex-start;
    justify-content: flex-end;
    margin: 0 20px 0 0;
    width: 300px;
    height: 300px;
}

.imgContainer {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    margin: 50px 20px;
}

.imgSubcontainer {
    text-align: left;
}

.radioBtnsAlt, .radioBtnsText {
    margin: 30px 0;
}

p {
    display: inline-block;
    margin: 0;
    text-align: left;
}

b {
    text-align: left;
}

</style>
