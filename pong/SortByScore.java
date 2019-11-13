import java.util.Comparator;

public class SortByScore implements Comparator<Score> {

    // This class is used for sorting scores to be outputted in the high score table
    public int compare(Score a, Score b) {
        return a.score - b.score;
    }

}
