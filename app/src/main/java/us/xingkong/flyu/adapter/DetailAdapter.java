package us.xingkong.flyu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.flyu.R;
import us.xingkong.flyu.model.DownloadModel;
import us.xingkong.flyu.util.GlideUtil;
import us.xingkong.flyu.util.L;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/19 18:06
 * @描述:
 * @更新日志:
 */
public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;
    private Reference<String> mReference;

    public DetailAdapter(@NonNull Context context, @Nullable List<String> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_detail, parent, false);
        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DetailHolder detailHolder = (DetailHolder) holder;

        mReference = new SoftReference<>(mList.get(position));
        if (mReference.get() != null) {
            Glide.with(mContext)
                    .load(DownloadModel.convertUrl(mReference.get()))
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .listener(GlideUtil.listener(detailHolder.image))
                    .into(detailHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        L.i("DetailAdapter", "ViewRecycled");
        if (mReference != null && mReference.get() != null) {
            mReference.clear();
            System.gc();
        }
    }

    class DetailHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        AppCompatImageView image;

        DetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
