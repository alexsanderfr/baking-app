package com.example.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private String[] mIngredientsArray;

    public IngredientsAdapter(String[] data) {
        mIngredientsArray = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);

        return new IngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ingredientTextView.setText(mIngredientsArray[position]);
    }

    @Override
    public int getItemCount() {
        if (mIngredientsArray == null) return 0;
        return mIngredientsArray.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientTextView;
        ViewHolder(View itemView) {
            super(itemView);
            ingredientTextView = itemView.findViewById(R.id.ingredient_tv);
        }
    }
}
