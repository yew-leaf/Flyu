package us.xingkong.flyu;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

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
public class PicturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;
    private Context mContext;
    private LayoutInflater inflater;
    private List<Uri> mList;
    private View mFooterView;
    private onItemClickListener mListener;

    public PicturesAdapter(Context context, List<Uri> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(onItemClickListener listener) {
        mListener = listener;
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
            return new PicturesHolder(mFooterView);
        }
        View view = inflater.inflate(R.layout.activity_photo_item, parent, false);
        setItemWidth(view, parent);
        return new PicturesHolder(view);
    }

    private void setItemWidth(View view, ViewGroup parent) {
        view.getLayoutParams().width = parent.getWidth() / 3;
        view.getLayoutParams().height = parent.getWidth() / 3;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_NORMAL:
                if (mList != null) {
                    Uri uri = mList.get(position);
                    Glide.with(mContext)
                            .load(uri)
                            .thumbnail(0.1f)
                            .into(((PicturesHolder) holder).photoImage);
                }
                if (mListener != null) {
                    ((PicturesHolder) holder).photoItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onItemClick(position);
                        }
                    });
                }
                break;
            case TYPE_FOOTER:
                if (mListener != null) {
                    ((PicturesHolder) holder).photoAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onItemClick(position);
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
        if (mList == null) {
            return 1;
        } else {
            return mList.size() + 1;
        }


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
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    class PicturesHolder extends RecyclerView.ViewHolder {

         CardView photoAdd, photoItem;
         ImageView photoImage;

        PicturesHolder(View itemView) {
            super(itemView);
            photoAdd = itemView.findViewById(R.id.photo_add);
            photoItem = itemView.findViewById(R.id.photo_item);
            photoImage = itemView.findViewById(R.id.photo_image);
        }
    }
}
