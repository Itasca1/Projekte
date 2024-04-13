<template>
    <q-page style="display: flex; flex-direction: column; min-height: calc(95vh - 2em)">
        <div style="text-align: start; padding-bottom: 5px">
            <div class="buttonclass">
                <q-btn class="buttonitem" outline rounded color="black" icon="arrow_back" @click="continueClick(-1)">{{ $t("manual_check.prev_question") }}</q-btn>
                <div style="flex-grow: 2"></div>
                <q-btn class="buttonitem" outline rounded color="black" @click="continueClick(0)">{{$t("manual_check.quit")}}</q-btn>
            </div>
            <ManualCheck number="4" :page="$i18n.messages[$i18n.locale]['manual_check']['lists']"/>
            <div style="display: flex; align-items: center; justify-content: space-between">
                <span>{{$t("manual_check.lists.faulty_lists") + " " + faultyElementsCount}}</span>
                <div class="buttonclass">
                    <q-btn @click="continueClick(1)" class="buttonitem" unelevated rounded color="primary">{{$t("continue")}}</q-btn>
                </div>
            </div>
        </div>
        <iframe v-if="iFrameHtml" :srcdoc="iFrameHtml" id="iframe" @load="onIFrameLoad" style="flex-grow: 1; margin-bottom: 10px" />

        <q-dialog v-model="addElementDialog">
            <q-card style="width: 900px; max-width: 80vw; height: 500px; padding: 1em; display: flex; flex-direction: column;">
                <q-card-section class="text-h6">{{$t("manual_check.lists.dialog_question")}}</q-card-section>
                <iframe :srcdoc="selectedElementHtml" id="dialogIFrame" @load="dialogOnIFrameLoad" style="flex-grow: 1"/>
                <q-card-actions align="right">
                    <q-btn flat :label='$t("manual_check.dont_mark")' color="primary" v-close-popup @click="markElementAsFaulty(false)"/>
                    <q-btn flat :label='$t("manual_check.mark")' color="primary" v-close-popup @click="markElementAsFaulty(true)"/>
                </q-card-actions>
            </q-card>
        </q-dialog>
    </q-page>
</template>

<script>
import {ref} from "vue";
import router from "../../router";
import ManualCheck from "../../components/ManualCheck.vue";
import {useHTMLStore} from "../../stores/html";
import {useReviewStore} from "../../stores/reviews";

export default {
    name: "Lists",
    components: {ManualCheck},
    setup() {
        const mancheckObject = ref(null);
        mancheckObject.value = JSON.parse(localStorage.getItem("mancheck"));
        const htmlID = mancheckObject.value.htmlID;

        const faultyElementsCount = ref(0);
        const addElementDialog = ref(false);
        const selectedElement = ref(null);
        const selectedElementHtml = ref(null);

        const iFrameHtml = ref(null);
        const htmlStore = useHTMLStore();

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
            checkForWrongReviewStep(review.value,4);

            // noinspection JSUnresolvedFunction
            htmlStore.getHTMLWithMarkedLists(htmlID).then(res => {
                iFrameHtml.value = loadFromReview(res.data, review.value.listReviews)
            })
        })

        function loadFromReview(html, listReviews) {
            listReviews = listReviews? listReviews : []
            const faultyElements = listReviews.filter(elem => elem.value === "incorrect")
            faultyElementsCount.value = faultyElements.length;
            faultyElements.forEach(listElem => {
                const faultyElement = new DOMParser().parseFromString(listElem.listHtml, "text/html").body.firstElementChild;
                faultyElement.setAttribute("faultyElement", "")
                const element = new DOMParser().parseFromString(listElem.listHtml, "text/html").body.firstElementChild;
                html = html.replace(element.outerHTML, faultyElement.outerHTML)
            })
            return html
        }

        return {
            iFrameHtml,
            mancheckObject,
            review,
            htmlID,
            htmlStore,
            faultyElementsCount,
            addElementDialog,
            selectedElement,
            selectedElementHtml,
        }
    },
    data() {
        document.title = this.$t("manual_check.manual_check")

        const reviewStore = useReviewStore();
        const htmlStore = useHTMLStore();

        const iframeOnClick = event => {
            const iframe = document.getElementById("iframe");
            const idoc = iframe.contentDocument || iframe.contentWindow.document;
            const selectedElement = idoc.querySelector('[highlightedElement]')
            if (["ul", "ol", "dl"].includes(selectedElement.tagName.toLowerCase()))
                return
            this.selectedElement = selectedElement;
            this.selectedElementHtml = elementToHtml(selectedElement, idoc);

            this.addElementDialog = true;
            event.preventDefault()
        }

        const onIFrameLoad = () => {
            const iframe = document.getElementById("iframe");
            const idoc = iframe.contentDocument || iframe.contentWindow.document;
            iframe.contentWindow.document.body.onclick = event => iframeOnClick(event);

            const style = document.createElement('style');
            style.id = "highlightedElementStyle";
            style.innerHTML =
                '[highlightedElement] { outline: 3px dashed #882255; }' +
                '[faultyElement] { outline: 3px dashed #882255; }';
            idoc.head.appendChild(style)

            idoc.childNodes.forEach(childNode => {
                childNode.addEventListener("mouseover", function (event) {
                    event.target.setAttribute("highlightedElement", "")
                })
                childNode.addEventListener("mouseout", function (event) {
                    event.target.removeAttribute("highlightedElement")
                })
            })
        }

        const dialogOnIFrameLoad = () => {
            const dialogIFrame = document.getElementById("dialogIFrame");
            dialogIFrame.contentWindow.document.body.onclick = event => event.preventDefault();
        }

        const markElementAsFaulty = (mark) => {
            const alreadyMarked = this.selectedElement.hasAttribute("faultyElement")
            if (mark && !alreadyMarked) {
                this.selectedElement.setAttribute("faultyElement", "")
                this.faultyElementsCount++;
            } else if (!mark && alreadyMarked) {
                this.selectedElement.removeAttribute("faultyElement")
                this.faultyElementsCount--;
            }
        }

        function getReviews() {
            const iframe = document.getElementById("iframe");
            const idoc = iframe.contentDocument || iframe.contentWindow.document;
            let faultyElements = Array.from(idoc.querySelectorAll('[faultyElement]'))
            faultyElements = faultyElements.map(elem => {
                return {listHtml: elementToHtml(elem, idoc), value: "incorrect"}
            })
            let ulList = Array.from(idoc.getElementsByTagName("ul"));
            let olList = Array.from(idoc.getElementsByTagName("ol"));
            let dlList = Array.from(idoc.getElementsByTagName("dl"));
            ulList = ulList.map(elem => {
                return {listHtml: elementToHtml(elem, idoc), value: "correct"}
            })
            olList = olList.map(elem => {
                return {listHtml: elementToHtml(elem, idoc), value: "correct"}
            })
            dlList = dlList.map(elem => {
                return {listHtml: elementToHtml(elem, idoc), value: "correct"}
            })
            let listReviews = ulList.concat(olList.concat(dlList))
            return listReviews.concat(faultyElements)
        }

        const continueClick = (toPosition) => {
            this.review.listReviews = getReviews();
            saveReview(toPosition);
        }

        const saveReview = (toPosition) => {
            this.review.reviewPosition += toPosition;
            // noinspection JSUnresolvedFunction
            reviewStore.requestSaveReview(this.review).then(review => {
                // noinspection JSUnresolvedFunction
                htmlStore.requestHTML(this.htmlID).then(() => {
                    // noinspection JSUnresolvedFunction
                    const html = htmlStore.getHTML(this.htmlID);
                    html.review = review;
                    // noinspection JSUnresolvedFunction
                    htmlStore.requestUpdateHTML(html).then(() => {
                        if (toPosition === -1) {
                            router.push({path: "/manualcheck/3"});
                        } else if (toPosition === 0) {
                            router.push({path: "/course/" + this.mancheckObject.courseID});
                        } else {
                            router.push({path: "/manualcheck/5"});
                        }
                    });
                })
            })
        }

        function elementToHtml(elem, document) {
            const html = document.cloneNode(true);
            html.body.outerHTML = elem.outerHTML;
            html.querySelectorAll('[highlightedElement]').forEach(elem => elem.removeAttribute('highlightedElement'))
            html.querySelectorAll('[faultyElement]').forEach(elem => elem.removeAttribute('faultyElement'))
            html.getElementById("highlightedElementStyle").remove()
            html.getElementById("listStyle").remove()
            return html.documentElement.outerHTML;
        }

        return {onIFrameLoad, dialogOnIFrameLoad, markElementAsFaulty, continueClick}
    }
}
</script>

<style scoped>
</style>
