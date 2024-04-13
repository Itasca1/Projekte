package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.HTML;
import gpse.example.domain.entities.Review;
import gpse.example.domain.repositories.HTMLRepository;
import gpse.example.domain.services.HTMLService;
import gpse.example.domain.services.NotFoundException;
import gpse.example.web.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link HTMLService} needed to load html objects.
 */
@Service
public class HTMLServiceImpl implements HTMLService {
    private final HTMLRepository htmlRepository;

    @Autowired
    public HTMLServiceImpl(final HTMLRepository htmlRepository) {
        this.htmlRepository = htmlRepository;
    }

    @Override
    public Optional<HTML> getHTML(final String id) {
        return htmlRepository.findById(Long.parseLong(id));
    }

    @Override
    public HTML createHTML(final String htmlString, final Review review, final String name) {
        return htmlRepository.save(new HTML(htmlString, review, name));
    }

    @Override
    public HTML updateHTML(final String id, final HTML tempHtml) {
        final HTML html = htmlRepository.findById(Long.parseLong(id)).orElseThrow(BadRequestException::new);
        html.setHtmlString(tempHtml.getHtmlString());
        html.setReview(tempHtml.getReview());
        html.setName(tempHtml.getName());
        return htmlRepository.save(html);
    }

    @Override
    public HTML getHTMLbyName(final String name) throws NotFoundException {
        final HTML html = htmlRepository.findByName(name);
        if (html == null) {
            throw new NotFoundException("HTML not found");
        } else {
            return html;
        }
    }

}
