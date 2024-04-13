package gpse.example.domain.services;

import gpse.example.domain.entities.HTML;
import gpse.example.domain.entities.Review;

import java.util.Optional;

/**
 * Service for loading html objects.
 */
public interface HTMLService {
    Optional<HTML> getHTML(String id);

    HTML createHTML(String htmlString, Review review, String name);

    HTML updateHTML(String id, HTML tempHtml);

    HTML getHTMLbyName(String name) throws NotFoundException;
}
