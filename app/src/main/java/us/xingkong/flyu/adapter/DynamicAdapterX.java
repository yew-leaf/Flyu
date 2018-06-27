package us.xingkong.flyu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
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
import us.xingkong.flyu.R;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.util.DateUtil;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 1:24
 * @描述:
 * @更新日志:
 */
public class DynamicAdapterX extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ERROR = 1000;
    private static final int TYPE_EMPTY = 2000;
    private static final int TYPE_NORMAL = 3000;
    private String name;
    private Context mContext;
    private List<DownloadModel.Message> mList;
    private LayoutInflater inflater;
    private onItemClickListener mListener;

    public DynamicAdapterX(@NonNull Context context, @Nullable List<DownloadModel.Message> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface onItemClickListener {
        void onItemClick(DownloadModel.Message message);

        void onReloadClick();
    }

    public void setItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null) {
            return TYPE_ERROR;
        }
        if (mList.isEmpty()) {
            return TYPE_EMPTY;
        }
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ERROR || viewType == TYPE_EMPTY) {
            View empty = inflater.inflate(R.layout.item_empty, parent, false);
            return new EmptyHolder(empty);
        }
        View view = inflater.inflate(R.layout.item_dynamic, parent, false);
        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyHolder) {
            EmptyHolder emptyHolder = (EmptyHolder) holder;
            if (getItemViewType(position) == TYPE_ERROR) {
                emptyHolder.hint.setText("加载不出来......");
                emptyHolder.reload.setVisibility(View.VISIBLE);
                if (mListener != null) {
                    emptyHolder.reload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onReloadClick();
                        }
                    });
                }
                return;
            }

            if (getItemViewType(position) == TYPE_EMPTY) {
                emptyHolder.hint.setText("空空如也......");
                emptyHolder.reload.setVisibility(View.GONE);
                return;
            }
        }

        if (holder instanceof DynamicHolder) {
            DynamicHolder dynamicHolder = (DynamicHolder) holder;
            final DownloadModel.Message message = mList.get(position);

            if (mListener != null) {
                dynamicHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        message.setName(name);
                        mListener.onItemClick(message);
                    }
                });
            }

            dynamicHolder.author.setText(name);
            dynamicHolder.content.setText(message.getText());
            dynamicHolder.time.setText(DateUtil.getTimeBefore(message.getTime()));

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_placeholder)
                    .error(R.mipmap.ic_error);

            String url = message.getImg().get(0);
            if (url.length() > 0) {
                Glide.with(mContext)
                        .load(DownloadModel.convertUrl(url))
                        .thumbnail(0.5f)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .apply(options)
                        .into(dynamicHolder.preview);
            } else {
                //防止glide抽风
                Glide.with(mContext)
                        .load(R.mipmap.ic_error)
                        .into(dynamicHolder.preview);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mList == null || mList.isEmpty()) ? 1 : mList.size();
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hint)
        AppCompatTextView hint;
        @BindView(R.id.reload)
        AppCompatButton reload;

        EmptyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DynamicHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.content)
        AppCompatTextView content;
        @BindView(R.id.author)
        AppCompatTextView author;
        @BindView(R.id.time)
        AppCompatTextView time;
        @BindView(R.id.preview)
        AppCompatImageView preview;

        DynamicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
