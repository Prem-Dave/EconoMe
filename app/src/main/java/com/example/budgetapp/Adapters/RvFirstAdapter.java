package com.example.budgetapp.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.R;
import com.example.budgetapp.dataclass.Expense;

import java.util.ArrayList;

public class RvFirstAdapter extends RecyclerView.Adapter<RvFirstAdapter.ViewHolder> {

    ArrayList<Expense> dataList;

    public RvFirstAdapter(ArrayList<Expense> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RvFirstAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_first, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvFirstAdapter.ViewHolder holder, int position) {

        Expense exception = dataList.get(position);

        holder.amount.setText(exception.getAmount());
        holder.costType.setText(exception.getExpenseType());
        holder.note.setText(exception.getNote());

        if(exception.getType().equals("Expanse")){
            holder.amount.setTextColor(Color.RED);
        }
        else{
            holder.amount.setTextColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView costType, note, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            costType = itemView.findViewById(R.id.costType);
            note = itemView.findViewById(R.id.note);
            amount = itemView.findViewById(R.id.amount);

        }
    }
}
