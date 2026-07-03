package com.asaphmwangi.studentattendace.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.models.AttendanceRecord;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<AttendanceRecord> attendanceList;
    private Context context;
    private boolean isLecturerView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());

    public AttendanceAdapter(List<AttendanceRecord> attendanceList, Context context, boolean isLecturerView) {
        this.attendanceList = attendanceList;
        this.context = context;
        this.isLecturerView = isLecturerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = attendanceList.get(position);
        
        if (isLecturerView) {
            holder.primaryText.setText(record.getStudentName() != null ? record.getStudentName() : "Unknown Student");
            holder.secondaryText.setText("Reg No: " + (record.getStudentRegNo() != null ? record.getStudentRegNo() : "N/A"));
        } else {
            holder.primaryText.setText(record.getSubjectName() != null ? record.getSubjectName() : "Unknown Subject");
            holder.secondaryText.setText("ID: " + record.getSubjectId());
        }

        if (record.getTimestamp() != null) {
            holder.timestampText.setText(dateFormat.format(record.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView primaryText, secondaryText, timestampText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.primary_text);
            secondaryText = itemView.findViewById(R.id.secondary_text);
            timestampText = itemView.findViewById(R.id.timestamp_text);
        }
    }
}
