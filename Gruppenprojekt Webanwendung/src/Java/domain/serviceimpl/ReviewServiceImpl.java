package gpse.example.domain.serviceimpl;

import gpse.example.domain.cmds.ReviewCmd;
import gpse.example.domain.entities.Review;
import gpse.example.domain.repositories.ReviewRepository;
import gpse.example.domain.services.ReviewService;
import gpse.example.web.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ReviewService} needed to load review objects.
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(final ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Optional<Review> getReview(final String id) {
        return reviewRepository.findById(Long.parseLong(id));
    }

    @Override
    public Review saveReview(final Review review) {
        return reviewRepository.save(review);
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    @Override
    public Review createReview(final Boolean reviewComplete, final Integer reviewPosition,
                               final List<Review.RevList> listReviews,
                               final List<Review.RevFormatting> formattingReviews,
                               final List<Review.RevLinks> linkReviews, final List<Review.RevImage> imageReviews,
                               final List<Review.RevVideo> videoReviews,
                               final List<Review.AutomaticReview> automaticReviews) {
        final Review review = new Review();
        review.setReviewComplete(reviewComplete);
        review.setReviewPosition(reviewPosition);
        review.setListReviews(listReviews);
        review.setFormattingReviews(formattingReviews);
        review.setLinkReviews(linkReviews);
        review.setImageReviews(imageReviews);
        review.setVideoReviews(videoReviews);
        review.setAutomaticReviews(automaticReviews);
        return saveReview(review);
    }

    @Override
    public Review updateReview(final String id, final ReviewCmd reviewCmd) {
        final Review review = reviewRepository.findById(Long.parseLong(id)).orElseThrow(BadRequestException::new);
        review.setReviewComplete(reviewCmd.getReviewComplete());
        review.setReviewPosition(reviewCmd.getReviewPosition());
        review.setListReviews(reviewCmd.getListReviews());
        review.setFormattingReviews(reviewCmd.getFormattingReviews());
        review.setLinkReviews(reviewCmd.getLinkReviews());
        review.setImageReviews(reviewCmd.getImageReviews());
        review.setVideoReviews(reviewCmd.getVideoReviews());
        review.setAutomaticReviews(reviewCmd.getAutomaticReviews());
        return reviewRepository.save(review);
    }
}
