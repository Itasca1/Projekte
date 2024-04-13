import {defineStore} from 'pinia'
import {ref} from 'vue'
import api from "../api";

export const useReviewStore = defineStore('reviews', () => {
    const reviews = ref([])

    function updateReview(id, newReview) {
        const review = getReview(id)
        if (review) {
            review.reviewComplete = newReview.reviewComplete;
            review.reviewPosition = newReview.reviewPosition;
            review.listReviews = newReview.listReviews;
            review.formattingReviews = newReview.formattingReviews;
            review.linksCorrect = newReview.linkReviews;
            review.imageReviews = newReview.imageReviews;
            review.videoReviews = newReview.videoReviews;
            review.automaticReviews = newReview.automaticReviews;
        } else {
            reviews.value.push(newReview)
        }
    }

    function requestSaveReview(review) {
        return new Promise((resolve, reject) => {
            api.review.save(review).then(res => {
                review = res.data
                updateReview(review.id, review)
                resolve(review)
            }).catch(error => {
                reject()
            })
        })
    }

    function requestUpdateReview(review) {
        return new Promise((resolve, reject) => {
            api.review.update(review).then(res => {
                review = res.data
                updateReview(review.id, review)
                resolve(review)
            }).catch(() => {
                reject()
            })
        })
    }

    function getReview(id) {
        // noinspection EqualityComparisonWithCoercionJS
        return api.review.get(id);
    }

    function requestReview(id) {
        return new Promise((resolve, reject) => {
            api.review.get(id).then(res => {
                updateReview(id, res.data)
                resolve()
            }).catch(() => {
                reject()
            })
        })
    }

    return {
        reviews,
        requestSaveReview,
        getReview,
        requestUpdateReview,
        requestReview
    }
})
