package gpse.example.domain.entities;

/**
 * Structure to store the data of a single rank entry.
 */
@SuppressWarnings("ClassCanBeRecord")
public class Rank {
    private final int rankInt;
    private final String name;
    private final int points;
    private final boolean usersCourse;


    /**
     * Constructor of Rank.
     * @param rankInt the ranking position.
     * @param name the name of the user.
     * @param points the users points.
     * @param usersCourse true if user is manager of the entity.
     */
    public Rank(final int rankInt, final String name, final int points, final boolean usersCourse) {
        this.rankInt = rankInt;
        this.name = name;
        this.points = points;
        this.usersCourse = usersCourse;
    }

    public int getRankInt() {
        return rankInt;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public boolean isUsersCourse() {
        return usersCourse;
    }
}
