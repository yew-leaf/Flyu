package us.xingkong.flyu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.flyu.PhotoModel;
import us.xingkong.flyu.R;

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

    public PhotosAdapter(Context context, List<PhotoModel> list) {
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

    public void clear() {
        /*if (mList != null && mList.size() != 0) {
            mList.clear();
        }*/
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_NORMAL:
                if (mList != null) {
                    Uri uri = Uri.parse(mList.get(position).getUri());
                    Glide.with(mContext)
                            .load(uri)
                            .thumbnail(0.5f)
                            .into(((ItemHolder) holder).image);
                }
                if (itemListener != null) {
                    ((ItemHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemListener.onItemClick(holder.getLayoutPosition());
                        }
                    });
                }
                ((ItemHolder) holder).item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                ((ItemHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(holder.getLayoutPosition());
                    }
                });
                break;
            case TYPE_FOOTER:
                if (mList == null || mList.size() < 3)
                    ((FooterHolder) holder).add.setEnabled(true);
                else
                    ((FooterHolder) holder).add.setEnabled(false);

                if (addListener != null) {
                    ((FooterHolder) holder).add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addListener.onAddClick();
                        }
                    });
                }
                break;
            default:
                break;
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
