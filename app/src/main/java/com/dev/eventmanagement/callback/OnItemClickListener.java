package com.dev.eventmanagement.callback;

import android.view.View;

public interface OnItemClickListener<T> {
    /**
     * On item view click.
     *
     * @param view      the view
     * @param viewModel the view model
     */
    void onItemViewClick(View view, T viewModel, int requestCode);

}
