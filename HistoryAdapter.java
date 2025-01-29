package com.example.navigation;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> historyList;
    FirebaseFirestore df;
    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);

        // Format date and time strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Set issuance date and time
        holder.issuanceDateTextView.setText(dateFormat.format(history.getIssuanceDate())+"  "+timeFormat.format(history.getIssuanceDate()));
        //holder.issuanceTimeTextView.setText(timeFormat.format(history.getIssuanceDate()));

        // Set return date and time
        holder.returnDateTextView.setText(dateFormat.format(history.getReturnDate())+"  "+timeFormat.format(history.getReturnDate()));
       // holder.returnTimeTextView.setText(timeFormat.format(history.getReturnDate()));

        // Set employee ID and cycle ID
        holder.employeeIdTextView.setText(history.getActual_employee_id());
        holder.cycleIdTextView.setText(history.getActual_cycle_id());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView issuanceDateTextView;
        TextView issuanceTimeTextView;
        TextView returnDateTextView;
        TextView returnTimeTextView;
        TextView employeeIdTextView;
        TextView cycleIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            issuanceDateTextView = itemView.findViewById(R.id.H_issued_date);
            //issuanceTimeTextView = itemView.findViewById(R.id.issuanceTimeTextView);
            returnDateTextView = itemView.findViewById(R.id.H_returned_date);
           // returnTimeTextView = itemView.findViewById(R.id.returnTimeTextView);
            employeeIdTextView = itemView.findViewById(R.id.H_emp_id);
            cycleIdTextView = itemView.findViewById(R.id.H_cycle_id);
        }
    }
}
