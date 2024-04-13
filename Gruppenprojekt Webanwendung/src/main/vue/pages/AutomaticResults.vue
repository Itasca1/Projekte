<template>
    <h1>{{ $t('automatic_testing.title') }}</h1>
    <div class="buttonclass">
        <q-btn class="buttonitem" outline rounded color="black" @click="pushData">{{ $t('automatic_testing.continue_test') }}</q-btn>
    </div>
    <div class="q-pa-md">
        <div v-if="!results" class="q-pa-xl">
            <q-spinner
                color="primary"
                size="3em"
            />
            <div> {{ $t('automatic_testing.test_in_progress') }} </div>
        </div>

        <!-- This part is shown, when violations were found -->
        <div v-if="results && results[1].length > 0">
            <div class="row q-pb-sm"  v-if="results">
                <div class="col">
                    <q-stepper
                        class="no-border no-box-shadow"
                        v-model="step"
                        ref="stepper"
                        contracted
                        color="primary"
                        animated
                        header-nav
                    >
                        <q-step
                            v-for="(v_class,index) in results[1]"
                            :name="index+1"
                            :title="v_class.help"
                            icon="report_problem"
                            :done="step > index+1"
                        >
                            <div><b>{{ v_class.help }}</b></div>
                            {{ v_class.description }}
                        </q-step>

                        <template v-slot:navigation>
                            <q-stepper-navigation>
                                <div class="row q-pa-sm">
                                    <q-btn v-if="step > 1" flat color="primary" @click="$refs.stepper.previous()" class="q-ml-sm" ><q-icon name="navigate_before" /></q-btn>
                                    <q-space/>
                                    <q-btn v-if="step < results[1].length" flat @click="$refs.stepper.next()" color="primary" > <q-icon name="navigate_next" /></q-btn>
                                </div>
                                <div class="row q-pa-sm">
                                    <q-space class="q-pl-md q-pr-lg"/>
                                    <q-pagination
                                        v-model="current"
                                        :max="results[1][step-1].nodes.length"
                                        direction-links
                                        flat
                                        color="grey-4"
                                        active-color="primary"
                                        @click="highlightElement(results[1][step-1].nodes[current-1])"
                                    />
                                    <q-space/>
                                    <q-btn flat @click="getHelp">
                                        <q-icon name="help" color="primary"/>
                                        <q-tooltip anchor="top middle"> {{ $t('automatic_testing.help_tooltip') }} </q-tooltip>
                                    </q-btn>
                                </div>

                            </q-stepper-navigation>
                        </template>
                    </q-stepper>
                </div>
            </div>
        </div>
        <!-- When no violations were found, a message is displayed instead -->
        <div v-if="results && results[1].length === 0" class="q-pa-xl">
            <q-icon name="done" size="64px"/>
            <div> {{ $t('automatic_testing.no_err_found') }}  </div>
        </div>
    </div>
    <!-- Container for the iframe -->
    <div style="height: 50vh">
        <iframe @load="injectScript" ref="htmlFrame" id="htmlViewer" :srcdoc="html" :style="{ height: iframeHeight, width: '100%'}"></iframe>
    </div>

</template>

<script>
import {useHTMLStore} from "../stores/html"
import {useReviewStore} from "../stores/reviews";
import router from "../router";
import {ref} from "vue";
import axe from "axe-core"

import axeDa from "axe-core/locales/da.json"
import axeDe from "axe-core/locales/de.json"
import axeEs from "axe-core/locales/es.json"
import axeEu from "axe-core/locales/eu.json"
import axeFr from "axe-core/locales/fr.json"
import axeJa from "axe-core/locales/ja.json"
import axeKo from "axe-core/locales/ko.json"
import axeNl from "axe-core/locales/nl.json"
import axePl from "axe-core/locales/pl.json"
import axePtBR from "axe-core/locales/pt_BR.json"

import {useI18n} from "vue-i18n"
export default {
    setup(){
        // i18n File, used to get the current language of the user
        const t = useI18n({ useScope: 'global' })
        // parsed content given by the course page. Contains the html_id
        const jContent = ref(null)
        // Database ID of the file that requires testing
        const html_id = ref(null)
        // The html file as a string, that is passed to the iframe
        const html = ref(null)
        // Testing results, that are displayed to the user
        const results = ref(null)
        // currently selected violation group
        const step = ref(1)
        // currently selected violation of the current group
        const current = ref(1)
        // the iframe containing the html file
        const htmlFrame = ref(null)
        // the height style element of the iframe
        const iframeHeight = ref("0px")
        // used to reset the style of an html element
        let lastStyle;
        // Frame is loaded twice, used to avoid initiating testing before content is loaded
        let frameLoaded = false;
        const reviewStore = useReviewStore()
        // set Axe language
        switch(t.locale.value){
            case "da":
                axe.configure({locale: axeDa})
                break
            case "de":
                axe.configure({locale: axeDe})
                break
            case "es":
                axe.configure({locale: axeEs})
                break
            case "eu":
                axe.configure({locale: axeEu})
                break
            case "fr":
                axe.configure({locale: axeFr})
                break
            case "ja":
                axe.configure({locale: axeJa})
                break
            case "ko":
                axe.configure({locale: axeKo})
                break
            case "nl":
                axe.configure({locale: axeNl})
                break
            case "pl":
                axe.configure({locale: axePl})
                break
            case "pt":
                axe.configure({locale: axePtBR})
                break
            default:
                break
        }

        // Parse HTML ID from localStorage and use it to acces the HTML String from the DB
        jContent.value = JSON.parse(localStorage.getItem('mancheck'));
        html_id.value = jContent.value.htmlID;
        const htmlStore = useHTMLStore();
        htmlStore.requestHTML(html_id.value).then(() => {
            let html_entity = htmlStore.getHTML(html_id.value);
            html.value =  html_entity.htmlString;
        })


        function startTest(){
            // Wait for Script to be applied to frame
            setTimeout(function (){

                // If the frame is loaded the first time, do not test
                if(frameLoaded){
                    axe.run(htmlFrame.value, {elementRef: true}).then(r => {
                        results.value = []
                        let axeRes = [r.passes, r.violations]
                        // For whatever reason, color contrast is not detected when Iframe is scaled before testing.
                        iframeHeight.value = "50vh"

                        // Get the main content of an ilias or moodle page (when it exists)
                        let ilias_main = htmlFrame.value.contentWindow.document.getElementById("il_center_col")
                        let moodleMain = htmlFrame.value.contentWindow.document.querySelector('[role="main"]')

                        // Check which of the two pages is to be tested
                        let mainElem = ilias_main == null ? moodleMain : ilias_main

                        // Filter all Passes and Violations to only include areas that the user can modify
                        axeRes.forEach( resClass => {
                            let filteredResults = []
                            resClass.forEach(testGroup => {
                                let filteredNodes = []
                                testGroup.nodes.forEach( item => {
                                    // Check if the node element is contained in the main area of the document
                                    let elem = htmlFrame.value.contentWindow.document.querySelector(item.target[1]);
                                    if (mainElem.querySelector(item.target[1]) != null && elem != null)  {
                                        // If it's an icon, skip it.
                                        if( !elem.classList.contains("icon"))
                                            filteredNodes.push(item);
                                    }
                                });
                                testGroup.nodes = filteredNodes
                                // When the filtered Group is not empty, add it to the displayed results
                                if(testGroup.nodes.length > 0){
                                    filteredResults.push(testGroup)
                                }
                            })
                            results.value.push(filteredResults)
                        })
                    })
                }
                else{
                    frameLoaded = true;
                }
            },200)
        }

        // Injects the axeMin Script into the iframe, so it can be tested
        function injectScript() {
            let frHead = document.getElementById('htmlViewer').contentWindow.document.getElementsByTagName("head")[0];
            let axeScript = document.createElement('script')
            axeScript.type = 'text/javascript'
            axeScript.src = "/node_modules/axe-core/axe.js"
            frHead.appendChild(axeScript)
            startTest()

        }

        function highlightElement(snippet) {
            // Get the selected element from the frame
            let elem = htmlFrame.value.contentWindow.document.querySelector(snippet.target[1]);
            // remove styling from the previously selected element
            if(lastStyle != null){
                lastStyle.style.borderStyle = "none"
                lastStyle.style.boxShadow = null
            }
            lastStyle = elem

            elem.style.borderStyle = "dashed"
            elem.style.boxShadow = "0 0 5px #f0f"

            elem.scrollIntoView()
            // Scroll a little further, so content is nopt obstructed by top panel
            htmlFrame.value.contentWindow.scrollBy(0,-200);


        }

        // Passes the test results to the manual testing page
        function pushData(){
            let passes = results.value[0];
            let violations = results.value[1];
            let rev = [];
            passes.forEach(
                e=>{
                    rev.push({
                        description: e.description,
                        help: e.help,
                        helpUrl: e.helpUrl,
                        passed: true
                    })
                }
            )

            violations.forEach(
                e=>{
                    rev.push({
                        description: e.description,
                        help: e.help,
                        helpUrl: e.helpUrl,
                        passed: false
                    })
                }
            )
            //jContent.value.review.auto= rev;

            const htmlID = JSON.parse(localStorage.getItem("mancheck")).htmlID;
            const review = ref(null);
            // noinspection JSUnresolvedFunction
            htmlStore.requestHTML(htmlID).then(() => {
                const html = htmlStore.getHTML(htmlID)
                review.value = html.review
                review.value.reviewPosition = 1;
                review.value.automaticReviews = rev;
                // noinspection JSUnresolvedFunction
                reviewStore.requestSaveReview(review.value).then(review => {
                    // noinspection JSUnresolvedFunction
                    htmlStore.requestHTML(htmlID).then(() => {
                        // noinspection JSUnresolvedFunction
                        const html = htmlStore.getHTML(htmlID);
                        html.review = review;
                        // noinspection JSUnresolvedFunction
                        htmlStore.requestUpdateHTML(html).then(() => {
                            router.push({path: "/manualcheck/1"});
                        });
                    })
                })
            })

        }

        // Opens the corrsponding help page of each group
        function getHelp(){
            window.open(results.value[1][step.value-1].helpUrl, '_blank').focus();
        }

        return {
            step,
            html_id,
            results,
            html,
            jContent,
            current,
            highlightElement,
            getHelp,
            pushData,
            htmlFrame,
            injectScript,
            iframeHeight
        }

    },
    name: "AutomaticResults",
}
</script>

<style scoped>

</style>
