package paginator.firebasepaginator;

/**
 * Created by Salva on 01/09/2017.
 */

public class Post {
    private String uid;
    private String text;
    private Object timestamp;

    public Post() {
    }

    public Post(String text, Object timestamp) {
        this.text = text;
        this.timestamp = timestamp;

    }



    public String getText() {
        return text;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

