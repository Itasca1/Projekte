package gpse.example.web;

import gpse.example.domain.entities.HTML;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.HTMLService;
import gpse.example.domain.services.NoAuthorizationException;
import gpse.example.domain.services.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for fetching and modifying specific html parts.
 */
@SuppressWarnings({"ClassCanBeRecord", "checkstyle:MultipleStringLiterals", "PMD.ExcessiveImports"})
@RestController
@CrossOrigin
@RequestMapping("/api/htmlcrawler")
public class HTMLCrawler {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String SOURCE = "src";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String BORDER_CSS = "<style id='%s'>%s {"
            + "outline: 3px dashed #117733 !important;"
            + "border: 3px dashed white !important;"
            + "}</style>";
    private final HTMLService htmlService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private final AccessCourseService accessCourseService;
    private final GroupManagerService groupManagerService;
    private final FacultyManagerService facultyManagerService;

    /**
     * Constructor.
     *
     * @param htmlService           the htmlService
     * @param employeeService       the employeeService
     * @param userService           the userService
     * @param accessCourseService   the courseService
     * @param groupManagerService   the groupManagerService
     * @param facultyManagerService the facultyManagerService
     */
    @Autowired
    public HTMLCrawler(final HTMLService htmlService, final EmployeeService employeeService,
                       final UserService userService, final AccessCourseService accessCourseService,
                       final GroupManagerService groupManagerService,
                       final FacultyManagerService facultyManagerService) {
        this.htmlService = htmlService;
        this.employeeService = employeeService;
        this.userService = userService;
        this.accessCourseService = accessCourseService;
        this.groupManagerService = groupManagerService;
        this.facultyManagerService = facultyManagerService;
    }

    /**
     * Retrieves the html-string from the HTML ID and marks all lists with dashed outlines.
     *
     * @param id the id for the {@link HTML} object
     * @return the modified string
     */
    @GetMapping("/marklists/{id:\\d+}")
    @Secured(ROLE_USER)
    public String markLists(@PathVariable("id") final String id) throws NoAuthorizationException {
        if (isManager(id)) {
            if (htmlService.getHTML(id).isEmpty()) {
                throw new BadRequestException();
            }
            final HTML html = htmlService.getHTML(id).get();
            final Document doc = Jsoup.parse(html.getHtmlString());
            doc.head().append(String.format(BORDER_CSS, "listStyle", "ul, ol, dl"));
            return doc.toString();
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }

    }

    /**
     * Retrieves the html-string from the HTML ID and returns any image source and alternative texts.
     *
     * @param id the id for the {@link HTML} object
     * @return list of html image components
     */
    @GetMapping("/getimagesrcandalt/{id:\\d+}")
    @Secured(ROLE_USER)
    public List<String[]> getImageSrcAndAlt(@PathVariable final String id) throws NoAuthorizationException {
        if (isManager(id)) {
            if (htmlService.getHTML(id).isEmpty()) {
                throw new BadRequestException();
            }
            final HTML html = htmlService.getHTML(id).get();
            final Document doc = Jsoup.parse(html.getHtmlString());
            final Elements img = doc.getElementsByTag("img");
            final List<String[]> images = new ArrayList<>();
            for (final Element elem : img) {
                images.add(new String[]{elem.attr(SOURCE), elem.attr("alt")});
            }
            return images;
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }

    }

    /**
     * Retrieves the html-string from the HTML ID and returns any video source.
     *
     * @param id the id for the {@link HTML} object
     * @return list of html video components
     */
    @GetMapping("/getvideos/{id:\\d+}")
    @Secured(ROLE_USER)
    public List<String> getVideos(@PathVariable final String id) throws NoAuthorizationException {
        //noinspection DuplicatedCode
        checkAuthorization(id);
        final Optional<HTML> htmlOptional = htmlService.getHTML(id);
        if (htmlOptional.isEmpty()) {
            throw new BadRequestException();
        }
        final HTML html = htmlOptional.get();
        final Document doc = Jsoup.parse(html.getHtmlString());
        final Elements vids = doc.getElementsByTag("video");
        final List<String> videos = new ArrayList<>();
        for (final Element elem : vids) {
            elem.attr("width", "400px");
            videos.add(elem.toString());
        }
        final Elements iframes = doc.getElementsByTag("iframe");
        for (final Element iframe : iframes) {
            final String src = iframe.attr(SOURCE);
            //noinspection HttpUrlsUsage
            if (src.startsWith("http://www.youtube.com/embed") || src.startsWith("https://www.youtube.com/embed")) {
                iframe.attr("width", "400px");
                iframe.attr("height", "225px");
                iframe.attr("allowfullscreen", "null");
                videos.add(iframe.toString());
            }
        }
        final Elements moodleVideoDivs = doc.getElementsByClass("video-js");
        for (final Element video : moodleVideoDivs) {
            video.attr("width", "400px");
            videos.add(video.toString());
        }
        return videos;
    }

    /**
     * Retrieves the html-string from the HTML ID and returns any a-link.
     *
     * @param id the id for the {@link HTML} object
     * @return list of html link components and their href
     */
    @GetMapping("/getlinks/{id:\\d+}")
    @Secured(ROLE_USER)
    public List<String[]> getLinks(@PathVariable final String id) throws NoAuthorizationException {
        //noinspection DuplicatedCode
        checkAuthorization(id);
        final Optional<HTML> htmlOptional = htmlService.getHTML(id);
        if (htmlOptional.isEmpty()) {
            throw new BadRequestException();
        }
        final HTML html = htmlOptional.get();
        final Document doc = Jsoup.parse(html.getHtmlString());
        final Elements links = doc.getElementsByTag("a");
        final List<String[]> output = new ArrayList<>();
        for (final Element elem : links) {
            final String href = elem.attr("href");
            output.add(new String[]{href, elem.toString()});
        }
        return output;
    }

    /**
     * Retrieves the html-string from the HTML ID and marks all "strong" and "em" elements with dashed outlines.
     *
     * @param id the id for the {@link HTML} object
     * @return the modified string
     */
    @GetMapping("/markformatting/{id:\\d+}")
    @Secured(ROLE_USER)
    public String markFormatting(@PathVariable final String id) throws NoAuthorizationException {
        if (isManager(id)) {
            if (htmlService.getHTML(id).isEmpty()) {
                throw new BadRequestException();
            }
            final HTML html = htmlService.getHTML(id).get();
            final Document doc = Jsoup.parse(html.getHtmlString());
            doc.head().append(String.format(BORDER_CSS, "formattingStyle", "strong, em"));
            return doc.toString();
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }

    }

    private boolean isManager(final String id) {
        return HTMLController.isManager(id, userService, htmlService, accessCourseService, employeeService,
                groupManagerService, facultyManagerService);
    }

    /**
     * Retrieves the html-string from the HTML ID and marks all links elements with dashed outlines.
     *
     * @param id the id for the {@link HTML} object
     * @return the modified string
     */
    @GetMapping("/marklinks/{id:\\d+}")
    @Secured(ROLE_USER)
    public String markLinks(@PathVariable final String id) throws NoAuthorizationException {
        if (isManager(id)) {
            if (htmlService.getHTML(id).isEmpty()) {
                throw new BadRequestException();
            }
            final HTML html = htmlService.getHTML(id).get();
            final Document doc = Jsoup.parse(html.getHtmlString());
            doc.head().append("<style>a {outline: 3px dashed #882255; border: 3px dashed white}</style>");
            return doc.toString();
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }

    private void checkAuthorization(final String id) throws NoAuthorizationException {
        if (htmlService.getHTML(id).isEmpty()) {
            throw new BadRequestException();
        }
        if (!isManager(id)) {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }
}
