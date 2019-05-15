package com.dev.eventmanagement.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dev.eventmanagement.R;
import com.dev.eventmanagement.callback.OnItemClickListener;
import com.dev.eventmanagement.databinding.ItemEventListingBinding;
import com.dev.eventmanagement.model.EventListItem;
import com.dev.eventmanagement.utilities.AppUtils;

import java.util.List;

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.EventListItemsHolder> {
    private List<EventListItem> mEventListItems;
    private OnItemClickListener<EventListItem> onItemClickListener;

    public ListItemsAdapter(List<EventListItem> EventListItems) {
        mEventListItems = EventListItems;
    }

    public void setOnItemClickListener(OnItemClickListener<EventListItem> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public EventListItemsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ItemEventListingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_event_listing, viewGroup, false);
        return new EventListItemsHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListItemsHolder viewHolder, int position) {
        viewHolder.bindView(mEventListItems.get(position));

    }

    public List getItemList() {
        return mEventListItems;
    }

    @Override
    public int getItemCount() {
        return mEventListItems.size();
    }

    class EventListItemsHolder extends RecyclerView.ViewHolder {
        private ItemEventListingBinding binding;
        private EventListItem itemModel;

        EventListItemsHolder(ItemEventListingBinding viewDataBinding, OnItemClickListener<EventListItem> holderClick) {
            super(viewDataBinding.getRoot());
            binding = viewDataBinding;
            /*itemView.setOnClickListener(v -> {
                if (null == holderClick) {
                    AppUtils.showLog("Holder", "Trying to work on a null object ,returning.");
                    return;
                }
                holderClick.onItemViewClick(v, itemModel);
            });*/
            binding.setItemClick(holderClick);
        }

        void bindView(EventListItem itemModel) {
            this.itemModel = itemModel;
            if (itemModel == null) {
                AppUtils.showLog("Holder", "Trying to work on a null object ,returning.");
                return;
            }
            binding.setViewModel(itemModel);
        }

    }
}
    
    