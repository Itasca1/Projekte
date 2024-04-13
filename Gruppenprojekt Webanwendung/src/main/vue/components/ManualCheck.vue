<template>
    <div style="padding-bottom: 20px">
        <h1>{{ $t("manual_check.manual_check") }}</h1>
        <!--<q-btn flat no-caps padding="2px" icon="arrow_back" @click="getBack()">
            {{ $t("manual_check.prev_question") }}
        </q-btn>-->
        <!--suppress JSCheckFunctionSignatures -->
        <h2>{{ $t("manual_check.question", [number, 5, page.question]) }}</h2>
        <ul>
            <li v-for="item in page.description">
                {{ item }}
            </li>
        </ul>
    </div>
</template>

<script>
import {ref} from "vue";
import router from "../router";
import {useHTMLStore} from "../stores/html";
import {useReviewStore} from "../stores/reviews";

export default {
    name: "ManualCheck",
    props: {
        number: String,
        page: {number:-1, question: "", description: ""}
    },
    data() {
        const htmlStore = useHTMLStore()
        const reviewStore = useReviewStore()
        const getBack = () => {
            const review = ref(null);
            const htmlID = JSON.parse(localStorage.getItem("mancheck")).htmlID;
            // noinspection JSUnresolvedFunction
            htmlStore.requestHTML(htmlID).then(() => {
                const html = htmlStore.getHTML(htmlID)
                review.value = html.review
                review.value.reviewPosition--;
                // noinspection JSUnresolvedFunction
                reviewStore.requestSaveReview(review.value).then(review => {
                    // noinspection JSUnresolvedFunction
                    htmlStore.requestHTML(htmlID).then(() => {
                        // noinspection JSUnresolvedFunction
                        const html = htmlStore.getHTML(htmlID);
                        html.review = review;
                        // noinspection JSUnresolvedFunction
                        htmlStore.requestUpdateHTML(html).then(() => {
                            router.back();
                        });
                    })
                })
            })
        }
        return {getBack}
    }
}
</script>

<style scoped>
</style>
