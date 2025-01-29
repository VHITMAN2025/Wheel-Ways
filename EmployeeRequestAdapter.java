package com.example.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeRequestAdapter extends RecyclerView.Adapter<EmployeeRequestAdapter.EmployeeRequestViewHolder> {

    private final List<EmployeeRequest> employeeRequestList;

    public EmployeeRequestAdapter(List<EmployeeRequest> employeeRequestList) {
        this.employeeRequestList = employeeRequestList;
    }

    @NonNull
    @Override
    public EmployeeRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tanuja_userentry, parent, false);
        return new EmployeeRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeRequestViewHolder holder, int position) {
        EmployeeRequest employeeRequest = employeeRequestList.get(position);
        holder.bind(employeeRequest);
    }

    @Override
    public int getItemCount() {
        return employeeRequestList.size();
    }

    public static class EmployeeRequestViewHolder extends RecyclerView.ViewHolder {

         EditText emp_id;
        EditText cycle_colour;
         EditText location;

        public EmployeeRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            emp_id = itemView.findViewById(R.id.Tid);
            cycle_colour = itemView.findViewById(R.id.Tcolour);
            location = itemView.findViewById(R.id.Tlocation);

        }
        public void bind(EmployeeRequest employeeRequest) {
            emp_id.setText(employeeRequest.getEmp_id());
            cycle_colour.setText(employeeRequest.getCycle_color());
            location.setText(employeeRequest.getCycle_location());
        }
    }
}