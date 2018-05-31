package us.xingkong.flyu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import us.xingkong.flyu.PhotoBean;
import us.xingkong.flyu.R;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: PicturesAdapter
 * @创建时间: 2018/5/10 13:02
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
    private Context mContext;
    private LayoutInflater inflater;
    private List<PhotoBean> mList;
    private View mFooterView;
    private onItemClickListener itemListener;
    private onAddClickListener addListener;

    public PhotosAdapter(Context context, List<PhotoBean> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void print() {
        for (PhotoBean bean : mList) {
            Log.i("uri", bean.getUri());
            Log.i("position", bean.getPosition()+"");
        }
    }

    public void clear(){
        if (mList!=null)
        mList.clear();
    }

    public interface onAddClickListener {
        void onAddClick();
    }

    public void setOnAddClickListener(onAddClickListener listener) {
        this.addListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(onItemClickListener listener) {
        itemListener = listener;
    }

    public void delete(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
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
                            .into(((ItemHolder) holder).photoImage);
                }
                if (itemListener != null) {
                    ((ItemHolder) holder).photoItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemListener.onItemClick(holder.getLayoutPosition());
                        }
                    });
                }
                ((ItemHolder) holder).photoItem.setOnLongClickListener(new View.OnLongClickListener() {
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
                if (mList == null || mList.size() < 3) {
                    ((FooterHolder) holder).photoAdd.setEnabled(true);
                } else
                    ((FooterHolder) holder).photoAdd.setEnabled(false);

                if (addListener != null) {
                    ((FooterHolder) holder).photoAdd.setOnClickListener(new View.OnClickListener() {
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

        CardView photoAdd;

        FooterHolder(View itemView) {
            super(itemView);
            photoAdd = itemView.findViewById(R.id.photo_add);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        CardView photoItem;
        ImageView photoImage, delete;

        ItemHolder(View itemView) {
            super(itemView);
            photoItem = itemView.findViewById(R.id.photo_item);
            photoImage = itemView.findViewById(R.id.photo_image);
            delete = itemView.findViewById(R.id.delete_item);
        }
    }
}
