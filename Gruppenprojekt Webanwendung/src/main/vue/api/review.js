import axios from 'axios';

export default {
    get(id) {
        return axios.get('/api/review/' + id);
    },
    update(review) {
        let reviewCmd = {
            reviewComplete: review.reviewComplete,
            reviewPosition: review.reviewPosition,
            listReviews: Array.from(review.listReviews),
            formattingReviews: Array.from(review.formattingReviews),
            linkReviews: Array.from(review.linkReviews),
            imageReviews: Array.from(review.imageReviews),
            videoReviews: Array.from(review.videoReviews),
            automaticReviews: Array.from(review.automaticReviews)
        };
        return axios.put('/api/review/' + review.id, reviewCmd);
    },
    save(review) {
        let reviewCmd = {
            reviewComplete: review.reviewComplete,
            reviewPosition: review.reviewPosition,
            listReviews: Array.from(review.listReviews),
            formattingReviews: Array.from(review.formattingReviews),
            linkReviews: Array.from(review.linkReviews),
            imageReviews: Array.from(review.imageReviews),
            videoReviews: Array.from(review.videoReviews),
            automaticReviews: Array.from(review.automaticReviews)
        };
        return axios.post('/api/review', reviewCmd);
    }
}
