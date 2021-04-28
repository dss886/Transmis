package com.dss886.transmis.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dss886.transmis.R;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.ExtentionsKt;
import com.dss886.transmis.utils.TransmisManager;
import com.dss886.transmis.view.SwitchConfig;
import com.dss886.transmis.view.SwitchItemView;

import org.jetbrains.annotations.NotNull;

import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;


/**
 * Created by duansishu on 2018/1/26.
 */

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SWITCH = 1024;
    private static final int TYPE_VALUE = 1025;

    private static final int HEADER_COUNT = 1;

    private final BaseActivity mActivity;
    private final FilterType mType;

    FilterAdapter(BaseActivity activity, FilterType type) {
        mActivity = activity;
        mType = type;
    }

    void add(String value) {
        TransmisManager.INSTANCE.addFilter(mType, value);
        notifyItemInserted(HEADER_COUNT);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_SWITCH : TYPE_VALUE;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SWITCH) {
            SwitchConfig config = new SwitchConfig("", mType.getModeSpKey());
            config.setDefaultValue(true);
            config.setOnCheckedChangeListener((buttonView, isChecked) ->
                    config.setTitle("过滤模式：" + (isChecked ? "黑名单" : "白名单")));
            return new SwitchViewHolder(ExtentionsKt.buildView(config, mActivity));
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.filter_item, parent, false);
            return new ValueViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SwitchViewHolder) {
            ((SwitchItemView) holder.itemView).onResume();
        } else if (holder instanceof ValueViewHolder) {
            int dataPosition = getDataPosition(position);
            String value = TransmisManager.INSTANCE.getFilters(mType).get(dataPosition);
            ((ValueViewHolder) holder).mTitle.setText(value);
            holder.itemView.setOnLongClickListener(v -> {
                DialogBuilder.showAlertDialog(mActivity, "是否删除过滤项目？", () -> {
                    int nowPosition = holder.getAdapterPosition();
                    TransmisManager.INSTANCE.removeFilter(mType, getDataPosition(nowPosition));
                    notifyItemRemoved(nowPosition);
                    return Unit.INSTANCE;
                });
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return HEADER_COUNT + TransmisManager.INSTANCE.getFilterCount(mType);
    }

    private int getDataPosition(int position) {
        if (position <= 0) {
            throw new IllegalArgumentException("position must above zero!");
        }
        int dataPosition = position - HEADER_COUNT;
        // reverse list
        return TransmisManager.INSTANCE.getFilterCount(mType) - 1 - dataPosition;
    }

    private static class SwitchViewHolder extends RecyclerView.ViewHolder {
        SwitchViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class ValueViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ValueViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
        }
    }
}
