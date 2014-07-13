package ListData;

/**
 * Created by Administrator on 7/6/2014.
 */
public class ListData {
    private String title;
    private String author;
    private String imageUrl;
    private Long postTime;
    private String rScore;
    private String comments;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getPostTime() {


        return postTime;
    }

    public void setPostTime(Long postTime) {
        this.postTime = postTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getrScore() {
        return rScore;
    }

    public void setrScore(String rScore) {
        this.rScore = rScore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
