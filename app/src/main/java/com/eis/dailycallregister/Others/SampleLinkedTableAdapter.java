package com.eis.dailycallregister.Others;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.eis.dailycallregister.R;

public class SampleLinkedTableAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl> {
    private static final int[] COLORS = new int[]{
            0xffe62a10, 0xffe91e63, 0xff9c27b0, 0xff673ab7, 0xff3f51b5,
            0xff5677fc, 0xff03a9f4, 0xff00bcd4, 0xff009688, 0xff259b24,
            0xff8bc34a, 0xffcddc39, 0xffffeb3b, 0xffffc107, 0xffff9800, 0xffff5722};
    private final LayoutInflater mLayoutInflater;
    private String[][] datalist;
    public int mColumnWidth;
    public int mRowHeight;
    public int mHeaderHeight;
    public int mHeaderWidth;

    public SampleLinkedTableAdapter(Context context, String arr2[][], String mode) {
        mLayoutInflater = LayoutInflater.from(context);
        datalist = new String[arr2.length][];
        for (int i = 0; i < datalist.length; ++i) {
            datalist[i] = new String[arr2[i].length];
            for (int j = 0; j < datalist[i].length; ++j) {
                datalist[i][j] = arr2[i][j];
            }
        }
        Resources res = context.getResources();
        if (mode.equalsIgnoreCase("1")) {
            mColumnWidth = res.getDimensionPixelSize(R.dimen.column_width1);
            mRowHeight = res.getDimensionPixelSize(R.dimen.row_height1);
            mHeaderHeight = res.getDimensionPixelSize(R.dimen.column_header_height1);
            mHeaderWidth = res.getDimensionPixelSize(R.dimen.row_header_width1);
        } else if (mode.equalsIgnoreCase("2")) {
            mColumnWidth = res.getDimensionPixelSize(R.dimen.column_width2);
            mRowHeight = res.getDimensionPixelSize(R.dimen.row_height2);
            mHeaderHeight = res.getDimensionPixelSize(R.dimen.column_header_height2);
            mHeaderWidth = res.getDimensionPixelSize(R.dimen.row_header_width2);
        } else if (mode.equalsIgnoreCase("3")) {
            mColumnWidth = res.getDimensionPixelSize(R.dimen.column_width3);
            mRowHeight = res.getDimensionPixelSize(R.dimen.row_height3);
            mHeaderHeight = res.getDimensionPixelSize(R.dimen.column_header_height3);
            mHeaderWidth = res.getDimensionPixelSize(R.dimen.row_header_width3);
        }
    }

    @Override
    public int getRowCount() {
        return datalist.length;
    }

    @Override
    public int getColumnCount() {
        return datalist[0].length;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new TestViewHolder(mLayoutInflater.inflate(R.layout.item_card, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderColumnViewHolder(mLayoutInflater.inflate(R.layout.item_header_column, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderRowViewHolder(mLayoutInflater.inflate(R.layout.item_header_row, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderLeftTopViewHolder(mLayoutInflater.inflate(R.layout.item_header_left_top, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {
        final TestViewHolder vh = (TestViewHolder) viewHolder;
        String itemData = datalist[row][column]; // skip headers

        if (TextUtils.isEmpty(itemData)) {
            itemData = "";
        }

        itemData = itemData.trim();
        vh.tvText.setVisibility(View.VISIBLE);
        vh.tvText.setText(itemData);

    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {
        int color = COLORS[column % COLORS.length];
        TestHeaderColumnViewHolder vh = (TestHeaderColumnViewHolder) viewHolder;
        vh.tvText.setText(datalist[0][column]);  // skip left top header
        GradientDrawable gd = new GradientDrawable(
                mIsRtl ? GradientDrawable.Orientation.RIGHT_LEFT : GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ColorUtils.setAlphaComponent(color, 50), 0x00000000});
        gd.setCornerRadius(0f);
        vh.vGradient.setBackground(gd);
        vh.vLine.setBackgroundColor(color);
    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {
        TestHeaderRowViewHolder vh = (TestHeaderRowViewHolder) viewHolder;
        vh.tvText.setText(datalist[row][0]);
    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {
        TestHeaderLeftTopViewHolder vh = (TestHeaderLeftTopViewHolder) viewHolder;
        vh.tvText.setText(datalist[0][0]);
    }

    @Override
    public int getColumnWidth(int column) {
        return mColumnWidth;
    }

    @Override
    public int getHeaderColumnHeight() {
        return mHeaderHeight;
    }

    @Override
    public int getRowHeight(int row) {
        return mRowHeight;
    }

    @Override
    public int getHeaderRowWidth() {
        return mHeaderWidth;
    }

    //------------------------------------- view holders ------------------------------------------

    private static class TestViewHolder extends ViewHolderImpl {
        TextView tvText;

        private TestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }

    private static class TestHeaderColumnViewHolder extends ViewHolderImpl {
        TextView tvText;
        View vGradient;
        View vLine;

        private TestHeaderColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            vGradient = itemView.findViewById(R.id.vGradient);
            vLine = itemView.findViewById(R.id.vLine);
        }
    }

    private static class TestHeaderRowViewHolder extends ViewHolderImpl {
        TextView tvText;

        TestHeaderRowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }

    private static class TestHeaderLeftTopViewHolder extends ViewHolderImpl {
        TextView tvText;

        private TestHeaderLeftTopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }
}
