package paginator.firebasepaginator;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Salva on 01/09/2017.
 */

public class Post {
    private String uid;
    private String text;
    private HashMap<String, Object> timestampCreated;
    public Post() {
    }

    public Post(String text, HashMap<String, Object>  timestampCreatedObj) {
        this.text = text;
        timestampCreatedObj = new HashMap<String, Object>();
        timestampCreatedObj.put("date", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampCreatedObj;

    }



    public String getText() {
        return text;
    }

//    public Object getTimestamp() {
//        return timestamp;
//    }

    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("date");
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

