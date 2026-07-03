package com.asaphmwangi.studentattendace.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.models.Subject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<Subject> subjectList;
    private Context context;

    public SubjectAdapter(List<Subject> subjectList, Context context) {
        this.subjectList = subjectList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.name.setText(subject.getName());
        holder.room.setText("Room: " + subject.getRoom());

        holder.itemView.setOnClickListener(v -> {
            String[] options = {"Generate QR Code", "View Attendance"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose Action");
            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    showQRDialog(subject);
                } else {
                    viewAttendance(subject);
                }
            });
            builder.show();
        });
    }

    private void viewAttendance(Subject subject) {
        Intent intent = new Intent(context, LecturerAttendanceView.class);
        intent.putExtra("subjectId", subject.getId());
        context.startActivity(intent);
    }

    private void showQRDialog(Subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("QR Code for " + subject.getName());

        ImageView imageView = new ImageView(context);
        imageView.setPadding(32, 32, 32, 32);
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(subject.getId(), BarcodeFormat.QR_CODE, 600, 600);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        builder.setView(imageView);
        builder.setPositiveButton("Close", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, room;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.subject_name_text);
            room = itemView.findViewById(R.id.subject_room_text);
        }
    }
}
