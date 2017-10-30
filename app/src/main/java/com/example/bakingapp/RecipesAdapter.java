package com.example.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private String[] mRecipesArray;
    private RecipesAdapterOnClickHandler mClickHandler;

    public interface RecipesAdapterOnClickHandler {
        void onClick(String idInJson);
    }

    public RecipesAdapter(String[] data, RecipesAdapterOnClickHandler onClickHandler) {
        mRecipesArray = data;
        mClickHandler = onClickHandler;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.recipeNameTextView.setText(mRecipesArray[position]);
    }

    @Override
    public int getItemCount() {
        if (mRecipesArray == null) return 0;
        return mRecipesArray.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView recipeNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipe_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String idInJson = String.valueOf(adapterPosition+1);
            mClickHandler.onClick(idInJson);
        }
    }
}
