package com.dss886.transmis.filter;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.StringUtils;
import com.dss886.transmis.view.SwitchItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by duansishu on 2018/1/26.
 */

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SWITCH = 1024;
    private static final int TYPE_VALUE = 1025;

    private static final int HEADER_COUNT = 1;

    private BaseActivity mActivity;
    private FilterActivity.Type mType;
    private List<String> mValueList;

    FilterAdapter(BaseActivity activity, FilterActivity.Type type) {
        mActivity = activity;
        mType = type;
        String valueString = App.sp.getString(mType.valueSpKey, null);
        mValueList = StringUtils.parseToList(valueString);
    }

    void add(String value) {
        if (mValueList == null) {
            mValueList = new ArrayList<>();
        }
        mValueList.add(value);
        notifyItemInserted(HEADER_COUNT);
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = App.sp.edit();
        editor.putString(mType.valueSpKey, StringUtils.listToString(mValueList));
        editor.apply();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_SWITCH : TYPE_VALUE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SWITCH) {
            SwitchItem switchItem = new SwitchItem(parent.getContext());
            return new SwitchViewHolder(switchItem);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.filter_item, parent, false);
            return new ValueViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SwitchViewHolder) {
            SwitchItem switchItem = ((SwitchViewHolder) holder).mSwitchItem;
            switchItem.setSpInfo(mType.modeSpKey, true);
            switchItem.setOnCheckedChangeListener((buttonView, isChecked) ->
                    switchItem.setTitle("过滤模式：" + (isChecked ? "黑名单" : "白名单")));
            switchItem.onResume();
        } else if (holder instanceof ValueViewHolder) {
            int dataPosition = getDataPosition(position);
            if (mValueList == null || mValueList.size() <= dataPosition) {
                return;
            }
            String value = mValueList.get(dataPosition);
            ((ValueViewHolder) holder).mTitle.setText(value);
            holder.itemView.setOnLongClickListener(v -> {
                DialogBuilder.showAlertDialog(mActivity, "是否删除过滤项目？", () -> {
                    int nowPosition = holder.getAdapterPosition();
                    mValueList.remove(getDataPosition(nowPosition));
                    notifyItemRemoved(nowPosition);
                    save();
                });
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return HEADER_COUNT + (mValueList == null ? 0 : mValueList.size());
    }

    private int getDataPosition(int position) {
        if (position <= 0) {
            throw new IllegalArgumentException("position must above zero!");
        }
        if (mValueList == null) {
            throw new IllegalArgumentException("mValueList must not be null!");
        }
        int dataPosition = position - HEADER_COUNT;
        // reverse list
        return mValueList.size() - 1 - dataPosition;
    }

    private class SwitchViewHolder extends RecyclerView.ViewHolder {
        SwitchItem mSwitchItem;
        SwitchViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof SwitchItem) {
                mSwitchItem = (SwitchItem) itemView;
            }
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
