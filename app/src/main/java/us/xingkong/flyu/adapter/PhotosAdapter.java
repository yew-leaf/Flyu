package us.xingkong.flyu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.flyu.R;
import us.xingkong.flyu.model.PhotoModel;
import us.xingkong.flyu.util.L;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/10 13:02
 * @描述:
 * @更新日志:
 */
public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
    private Context mContext;
    private LayoutInflater inflater;
    private List<PhotoModel> mList;
    private View mFooterView;
    private onItemClickListener itemListener;
    private onAddClickListener addListener;
    private Reference<PhotoModel> mReference;

    public PhotosAdapter(@NonNull Context context, @Nullable List<PhotoModel> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void delete(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public interface onAddClickListener {
        void onAddClick();
    }

    public void setOnAddClickListener(onAddClickListener listener) {
        addListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(onItemClickListener listener) {
        itemListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mFooterView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            setItemWidth(mFooterView, parent);
            return new FooterHolder(mFooterView);
        }
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        setItemWidth(view, parent);
        return new ItemHolder(view);
    }

    private void setItemWidth(View view, ViewGroup parent) {
        view.getLayoutParams().width = parent.getWidth() / 3;
        view.getLayoutParams().height = parent.getWidth() / 3;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FooterHolder && getItemViewType(position) == TYPE_FOOTER) {
            FooterHolder footerHolder = (FooterHolder) holder;

            if (mList == null || mList.size() < 3) {
                footerHolder.add.setEnabled(true);
            } else {
                footerHolder.add.setEnabled(false);
            }

            if (addListener != null) {
                footerHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addListener.onAddClick();
                    }
                });
            }
            return;
        }

        if (holder instanceof ItemHolder && getItemViewType(position) == TYPE_NORMAL) {
            final ItemHolder itemHolder = (ItemHolder) holder;

            mReference = new SoftReference<>(mList.get(position));
            if (mList != null && mReference.get() != null) {
                Glide.with(mContext)
                        .load(mReference.get().getUri())
                        .thumbnail(0.5f)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(itemHolder.image);
            }

            if (itemListener != null) {
                itemHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemListener.onItemClick(itemHolder.getLayoutPosition());
                    }
                });
            }

            itemHolder.item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            itemHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(itemHolder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mFooterView == null) {
            return mList.size();
        } else if (mList != null) {
            return mList.size() + 1;
        } else return 1;
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        L.i("PhotosAdapter", "ViewRecycled");
        if (mReference != null && mReference.get() != null) {
            mReference.clear();
            System.gc();
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.add)
        CardView add;

        FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item)
        CardView item;
        @BindView(R.id.image)
        AppCompatImageView image;
        @BindView(R.id.delete)
        AppCompatImageView delete;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
