package com.example.mynewday;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

class DiffCallback extends DiffUtil.Callback {
    private List<BookType> mOldList;
    private List<BookType> mNewList;

    public DiffCallback(List<BookType> mOldList, List<BookType> mNewList) {
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
        return  (mNewList.get(newItemPosition).getName().equals(mOldList.get(oldItemPosition).getName()));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(mNewList.get(newItemPosition).getImageid() != mOldList.get(oldItemPosition).getImageid())
            return true;
        return false;
    }
}
