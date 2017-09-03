package paginator.firebasepaginator;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salva on 01/09/2017.
 */

public class WallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> postList;

    public WallAdapter(List<Post> postList){
        this.postList = postList;
    }

    protected class FavouritesViewHolder extends RecyclerView.ViewHolder{

        private final View mView;

        @BindView(R.id.post_text)               TextView post_text;
        @BindView(R.id.post_timestamp)          TextView post_timestamp;

        public FavouritesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, mView);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new WallAdapter.FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Post post = postList.get(position);
        final WallAdapter.FavouritesViewHolder mHolder = (WallAdapter.FavouritesViewHolder) holder;

        mHolder.post_timestamp.setText(DateUtils.getRelativeTimeSpanString((long) post.getTimestampCreatedLong()).toString());
        mHolder.post_text.setText(post.getText());
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }
}
