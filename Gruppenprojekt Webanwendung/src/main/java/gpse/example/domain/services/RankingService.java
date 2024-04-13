package gpse.example.domain.services;

import gpse.example.domain.entities.Rank;

import java.util.List;

/**
 * Service to generate the relevant data for a scoreboard.
 */
public interface RankingService {

    List<Rank> getFacultyRanking(String userID);

    List<Rank> getGroupRanking(String userID);

    List<Rank> getCourseRanking(String userID);

    List<Rank> getUserRanking(String userID);

}

