<template>
    <q-page style="display: flex; flex-direction: column; padding: 2em">
        <div style="text-align: start;">
            <div class="buttonclass">
                <q-btn class="buttonitem" outline rounded color="black" icon="arrow_back" @click="answerClick(-1)">{{ $t("manual_check.prev_question") }}</q-btn>
                <div style="flex-grow: 2"></div>
                <q-btn class="buttonitem" outline rounded color="black" @click="answerClick(0)">{{$t("manual_check.quit")}}</q-btn>
            </div>
            <ManualCheck number="3" :page="$i18n.messages[$i18n.locale]['manual_check']['links']"/>
            <p>{{$t("manual_check.links.task")}}</p><br>
        </div>
        <div>
            <div v-for="(link, index) in list" class="mainContainer">
                <div class="linkOuter">
                    <div class="linkLeft">
                        <div :id="'link-'+index" style="display: none">{{link.text}}</div> <!-- storage only, not visible object -->

                        <div class="plainLink" v-if="isPlainText(link.text)">"<b>{{getInner(link.text)}}</b>"</div>
                        <!--unteres benötigt später @load="iframeclick(index)"-->
                        <iframe v-else :id="'iframe'+index" :srcdoc="elementToHtml(link.text, htmlpage)" sandbox style="border: solid 1px black"></iframe>

                        <div v-if="link.href === ''">
                            {{ $t("manual_check.links.types.null") }}
                        </div>
                        <div v-else-if="link.href === '#'">
                            {{ $t("manual_check.links.types.start") }}
                        </div>
                        <!--suppress HttpUrlsUsage -->
                        <div v-else-if="link.href.startsWith('http://') || link.href.startsWith('https://')">
                            {{ $t("manual_check.links.types.page") }} <a target="_blank" :href="link.href">{{ link.href }}</a>
                        </div>
                        <div v-else-if="link.href.startsWith('mailto:')">
                            {{ $t("manual_check.links.types.mail") }} {{link.href}}
                        </div>
                        <div v-else-if="link.href.startsWith('#')">
                            {{ $t("manual_check.links.types.section") }} <em>{{link.href}}</em>
                        </div>
                        <div v-else>
                            {{ $t("manual_check.links.types.page") }} {{link.href}}
                        </div>
                    </div>
                    <div class="radioBtns">
                        <q-list>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar>
                                    <q-radio v-model="radioBtn[index].value" val="correct" color="positive" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t("manual_check.links.correct") }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item tag="label" v-ripple>
                                <q-item-section avatar top>
                                    <q-radio v-model="radioBtn[index].value" val="incorrect" color="negative" />
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ $t("manual_check.links.incorrect") }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </div>

                </div>
            </div>
            <div v-if="visibleLength === 0">
                {{ $t('manual_check.links.no_links') }}
            </div>

            <div class="buttonclass">
                <q-btn @click="answerClick(1)" class="buttonitem" unelevated rounded color="primary">{{$t("continue")}}</q-btn>
            </div>
        </div>
    </q-page>
</template>

<script>
import ManualCheck from "../../components/ManualCheck.vue";
import PopupReader from "../../components/PopupReader.vue";
export default {
    name: "Links",
    components: {ManualCheck, PopupReader},
    data() {
        document.title = this.$t("manual_check.manual_check")

        const getTitle = (url) => {
            return fetch(`https://crossorigin.me/${url}`)
                .then((response) => response.text())
                .then((html) => {
                    const doc = new DOMParser().parseFromString(html, "text/html");
                    const title = doc.querySelectorAll('title')[0];
                    return title.innerText;
                });
        };

        const iframeclick = function(id) {
            document.getElementById(`iframe${id}`).contentWindow.document.body.onclick = function(event) {
                event.preventDefault()
            }
        }

        return {getTitle, iframeclick}
    }
}
</script>

<script setup>

import {ref} from "vue";
import {useHTMLStore} from "../../stores/html";
import router from "../../router";
import {useReviewStore} from "../../stores/reviews";

const reviewStore = useReviewStore();
const list = ref(null);
const mancheckObject = JSON.parse(localStorage.getItem("mancheck"));
const htmlID = mancheckObject.htmlID;
const htmlStore = useHTMLStore();
let len = 0;
const visibleLength = ref(0);
let radioBtn = [];
const htmlpage = ref(null);
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
    checkForWrongReviewStep(review.value,3);

    // noinspection JSUnresolvedFunction
    htmlStore.getLinksFromHTML(htmlID).then(response => {
        len = response.data.length;
        visibleLength.value = len;
        const temp = response.data;
        const toAdd = [];
        for (let elem in temp) {
            toAdd.push( { "href" : temp[elem][0], "text" : temp[elem][1]} );
            const prev = getAlreadyChecked(temp[elem][1]);
            radioBtn[elem] = ref(prev);
        }
        list.value = toAdd;
    })
})

function getAlreadyChecked(item) {
    const linkRevs = review.value.linkReviews;
    for (let k in linkRevs) {
        if (item === linkRevs[k].href) {
            return linkRevs[k].value;
        }
    }
    return "incorrect";
}

// noinspection JSUnresolvedFunction
htmlStore.getHTMLpage(htmlID).then(response => {
    htmlpage.value = response.data;
})

function elementToHtml(elem, document) {
    const html = new DOMParser().parseFromString(document.htmlString, "text/html");
    html.body.outerHTML = elem;
    return html.documentElement.outerHTML;
}

function isPlainText(elem) {
    const inner = new DOMParser().parseFromString(elem, "text/html");
    return (inner.activeElement.firstElementChild.firstElementChild == null);
}

function getInner(elem) {
    const inner = new DOMParser().parseFromString(elem, "text/html");
    return inner.activeElement.innerText;
}

function answerClick(position) {
    let results = []
    for (let k=0; k < len ; k++) {
        const linkContent = document.getElementById('link-'+k).innerText;
        const selection = radioBtn[k].value;
        results.push({"value" : selection, "href": linkContent});
    }
    review.value.linkReviews = results;
    localStorage.setItem("mancheck", JSON.stringify(mancheckObject));

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
                    router.push({path: "/manualcheck/2"});
                } else if (toPosition === 0) {
                    router.push({path: "/course/" + mancheckObject.courseID});
                } else {
                    router.push({path: "/manualcheck/4"});
                }
            });
        })
    })
}

</script>

<style scoped>

.plainLink {
    margin-right: 5px;
}

iframe {
    width: 400px;
    height: 50px;
    margin-right: 5px;
}

.mainContainer {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    margin: 50px 20px;
}


.radioBtns {
    margin: 30px 0;
    width: 400px;
}

.linkOuter {

}

.linkLeft {
    display: flex;
    flex-direction: row;
    align-items: center;
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
