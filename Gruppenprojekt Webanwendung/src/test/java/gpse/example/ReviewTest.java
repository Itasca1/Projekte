package gpse.example;

import gpse.example.domain.cmds.ReviewCmd;
import gpse.example.domain.entities.Review;
import gpse.example.domain.repositories.HTMLRepository;
import gpse.example.domain.repositories.ReviewRepository;
import gpse.example.domain.serviceimpl.HTMLServiceImpl;
import gpse.example.domain.serviceimpl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewTest {
    @Autowired
    private HTMLRepository htmlRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    /* default */ void reviewCreation() {
        // Create review
        final ReviewServiceImpl reviewService = new ReviewServiceImpl(reviewRepository);
        final HTMLServiceImpl htmlService = new HTMLServiceImpl(htmlRepository);
        Review review = reviewService.createReview(false, null, null, null, null, null, null, null);
        htmlService.createHTML("<html></html>", review, "reviewCreationTestHtml");

        // Test
        final Optional<Review> reviewOpt = reviewService.getReview(String.valueOf(review.getId()));
        assertThat(reviewOpt).isPresent();
        review = reviewOpt.get();
        assertThat(review.getId()).isNotNull();
        assertThat(review.getReviewComplete()).isFalse();
    }

    @Test
    /* default */ void reviewUpdate() {
        // Create review
        final ReviewServiceImpl reviewService = new ReviewServiceImpl(reviewRepository);
        final HTMLServiceImpl htmlService = new HTMLServiceImpl(htmlRepository);
        final Review review = reviewService.createReview(false, null, null, null, null, null, null, null);
        htmlService.createHTML("<html></html>", review, "reviewCreationTestHtml");

        // Update html
        final ReviewCmd reviewCmd = new ReviewCmd();
        reviewCmd.setReviewComplete(true);
        reviewCmd.setReviewPosition(3);
        reviewService.updateReview(String.valueOf(review.getId()), reviewCmd);

        // Test
        final Optional<Review> updatedReviewOpt = reviewService.getReview(String.valueOf(review.getId()));
        assertThat(updatedReviewOpt).isPresent();
        final Review updatedReview = updatedReviewOpt.get();
        assertThat(updatedReview.getReviewComplete()).isTrue();
        assertThat(updatedReview.getReviewPosition()).isEqualTo(3);
    }
}
