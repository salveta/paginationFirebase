package paginator.firebasepaginator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import static paginator.firebasepaginator.WallFragmentOwn.getBaseRef;

public class MainActivity extends AppCompatActivity {

    protected static FirebaseAuth auth;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

//        createPost();
    }

    public void sendMessage(View view) {
        createPost();
    }

    public void createPost(){
        DatabaseReference postsRef = getBaseRef().child("posts");


        Post newPost = new Post(String.valueOf(count), ServerValue.TIMESTAMP);
        postsRef.push().setValue(newPost);
        count ++;

    }

    public static FirebaseAuth getFirebaseAuthInstance(){
        return auth;
    }

}
