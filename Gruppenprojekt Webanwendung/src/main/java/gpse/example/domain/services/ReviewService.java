package gpse.example.domain.services;

import gpse.example.domain.cmds.ReviewCmd;
import gpse.example.domain.entities.Review;

import java.util.List;
import java.util.Optional;

/**
 * Service for loading review objects.
 */
public interface ReviewService {
    Optional<Review> getReview(String id);

    Review saveReview(Review review);

    @SuppressWarnings("checkstyle:ParameterNumber")
    Review createReview(Boolean reviewComplete, Integer reviewPosition, List<Review.RevList> listReviews,
                        List<Review.RevFormatting> formattingReviews,
                        List<Review.RevLinks> linkReviews, List<Review.RevImage> imageReviews,
                        List<Review.RevVideo> videoReviews,
                        List<Review.AutomaticReview> automaticReviews);

    Review updateReview(String id, ReviewCmd reviewCmd);

}
