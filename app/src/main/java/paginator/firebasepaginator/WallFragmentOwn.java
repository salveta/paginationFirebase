package paginator.firebasepaginator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Salva on 01/09/2017.
 */

public class WallFragmentOwn extends Fragment {
    public static final String TAG = "WallFragment";
    private static final String KEY_LAYOUT_POSITION = "layoutPosition";

    private int mRecyclerViewPosition = 0;

    private ArrayList<Post> postList = new ArrayList<>();
    private ArrayList<Post> postListLoadItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private WallAdapter mAdapter;

    private Query allPostsQuery;
    private final static int PAGESIZE = 8;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;

    public WallFragmentOwn() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wall_fragment, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);


        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new WallAdapter(postList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            loadMoreItems();
                        }
                    }
                }
            }
        });

        return rootView;
    }


    public void loadData(){
        allPostsQuery = getBaseRef().child("posts").orderByChild("timestampCreated/date").limitToLast(PAGESIZE);
        allPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getChildrenCount();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    post.setUid(postSnapshot.getKey());
                    postList.add(post);
                    Log.e("counter", post.getText() + " , " +post.getTimestampCreatedLong());

                }
                Collections.reverse(postList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void loadMoreItems(){
        Log.e("counter search", postList.get(postList.size()-1).getText() + " , " +postList.get(postList.size()-1).getTimestampCreatedLong());

        allPostsQuery = getBaseRef().child("posts").orderByChild("timestampCreated/date")
                .endAt(postList.get(postList.size()-1).getTimestampCreatedLong())
                .limitToLast(PAGESIZE);
        allPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postListLoadItems.clear();

                if(dataSnapshot.getValue() == null){
                    return;
                }

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);

                    if(postList.get(postList.size()-1).getTimestampCreatedLong() == post.getTimestampCreatedLong()){
                        setAdapter();
                        return;
                    }

                    Log.e("counter", post.getText() + " , " +post.getTimestampCreatedLong());
                    post.setUid(postSnapshot.getKey());
                    postListLoadItems.add(post);
                }

                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void setAdapter(){
        Collections.reverse(postListLoadItems);
        postList.addAll(postListLoadItems);
        mAdapter.notifyDataSetChanged();
        loading = true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mRecyclerViewPosition = (int) savedInstanceState
                    .getSerializable(KEY_LAYOUT_POSITION);
            mRecyclerView.scrollToPosition(mRecyclerViewPosition);
            // TODO: RecyclerView only restores position properly for some tabs.
        }

        Log.d(TAG, "Restoring recycler view position (all): " + mRecyclerViewPosition);

        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        int recyclerViewScrollPosition = getRecyclerViewScrollPosition();
        Log.d(TAG, "Recycler view scroll position: " + recyclerViewScrollPosition);
        savedInstanceState.putSerializable(KEY_LAYOUT_POSITION, recyclerViewScrollPosition);
        super.onSaveInstanceState(savedInstanceState);
    }

    private int getRecyclerViewScrollPosition() {
        int scrollPosition = 0;
        // TODO: Is null check necessary?
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        return scrollPosition;
    }



    ///////////////////////////////
    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }
}

