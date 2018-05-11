package us.xingkong.flyu;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: TouchHelper
 * @创建时间: 2018/5/11 15:06
 * @最后修改于:
 * @版本: 1.0
 * @描述: RecyclerView的拖拽
 * @更新日志:
 */
public class TouchHelper extends ItemTouchHelper.Callback {

    private PicturesAdapter mAdapter;
    private Paint mPaint;    //虚线画笔
    private int mPadding;   //虚线框框跟按钮间的距离

    public TouchHelper(PicturesAdapter adapter, int padding) {
        mAdapter = adapter;
        mPadding = padding;
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        PathEffect pathEffect = new DashPathEffect(new float[]{5f, 5f}, 5f);    //虚线
        mPaint.setPathEffect(pathEffect);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getLayoutPosition() == mAdapter.getItemCount()) {
            return 0;
        }
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (toPosition == mAdapter.getItemCount()) {
            return false;
        }
        mAdapter.moveItem(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ACTION_STATE_DRAG) {
            PicturesAdapter.PicturesHolder holder = (PicturesAdapter.PicturesHolder) viewHolder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.photoItem.setElevation(8);
            }
            holder.photoItem.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        PicturesAdapter.PicturesHolder holder = (PicturesAdapter.PicturesHolder) viewHolder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.photoItem.setElevation(2);
        }
        holder.photoItem.setBackgroundColor(Color.parseColor("#f0f0f0"));
    }
}
