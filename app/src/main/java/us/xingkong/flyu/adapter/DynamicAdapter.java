package us.xingkong.flyu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.flyu.DownloadModel;
import us.xingkong.flyu.R;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 1:24
 * @描述:
 * @更新日志:
 */
public class DynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private String username;
    private List<DownloadModel.Message> mList;

    public DynamicAdapter(Context context, List<DownloadModel.Message> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dynamic, parent, false);
        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mList != null) {
            DownloadModel.Message message = mList.get(position);
            ((DynamicHolder) holder).author.setText(username);
            ((DynamicHolder) holder).content.setText(message.getText());
            List<String> urls = message.getImg();
            for (String url : urls) {
                url = DownloadModel.formatUrl(url);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .error(R.mipmap.ic_delete);
                Glide.with(mContext)
                        .load(url)
                        .thumbnail(0.5f)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .apply(options)
                        .into(((DynamicHolder) holder).preview);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    class DynamicHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        AppCompatTextView content;
        @BindView(R.id.author)
        AppCompatTextView author;
        @BindView(R.id.preview)
        AppCompatImageView preview;

        DynamicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
