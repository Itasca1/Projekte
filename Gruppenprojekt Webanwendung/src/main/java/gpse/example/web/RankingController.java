package gpse.example.web;


import gpse.example.domain.entities.Rank;
import gpse.example.domain.services.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for the ranking of entities.
 */
@SuppressWarnings("ClassCanBeRecord")
@RestController
@CrossOrigin
@RequestMapping("/api")
public class RankingController {

    private final RankingService rankingService;

    @Autowired
    public RankingController(final RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/ranking/faculties/{id:\\d+}")
    public List<Rank> getFacultyRanking(@PathVariable final String id) {
        return rankingService.getFacultyRanking(id);
    }

    @GetMapping("/ranking/groups/{id:\\d+}")
    public List<Rank> getGroupRanking(@PathVariable final String id) {
        return rankingService.getGroupRanking(id);
    }

    @GetMapping("/ranking/courses/{id:\\d+}")
    public List<Rank> getCourseRanking(@PathVariable final String id) {
        return rankingService.getCourseRanking(id);
    }

    @GetMapping("/ranking/users/{id:\\d+}")
    public List<Rank> getUserRanking(@PathVariable final String id) {
        return rankingService.getUserRanking(id);
    }


}
