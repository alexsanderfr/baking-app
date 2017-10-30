package com.example.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder>{
    private String[] mRecipeStepsArray;
    public RecipeStepsAdapterOnClickHandler mClickHandler;

    public interface RecipeStepsAdapterOnClickHandler {
        void onClick(String stepId);
    }

    public RecipeStepsAdapter(String[] data, RecipeStepsAdapterOnClickHandler onClickHandler) {
        mRecipeStepsArray = data;
        mClickHandler = onClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.recipeStepNameTextView.setText(mRecipeStepsArray[position]);
    }

    @Override
    public int getItemCount() {
        if (mRecipeStepsArray == null) return 0;
        return mRecipeStepsArray.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView recipeStepNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            recipeStepNameTextView = itemView.findViewById(R.id.recipe_step_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));

        }
    }
}
