package com.example.mynewday;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

class DiffCallbackDay extends DiffUtil.Callback {
    private List<Day> mOldList;
    private List<Day> mNewList;

    public DiffCallbackDay(List<Day> mOldList, List<Day> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0 ;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0 ;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return  (mNewList.get(newItemPosition).getTitle().equals(mOldList.get(oldItemPosition).getTitle()));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(!mNewList.get(newItemPosition).getTitle().equals(mOldList.get(oldItemPosition).getTitle()))
            return true;
        return false;
    }
}
