package morpheus.softwares.projectmanagement.adapters;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import morpheus.softwares.projectmanagement.R;
import morpheus.softwares.projectmanagement.models.Database;
import morpheus.softwares.projectmanagement.models.Links;
import morpheus.softwares.projectmanagement.models.Projects;
import morpheus.softwares.projectmanagement.models.Student;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.Holder> {
    Context context;
    ArrayList<Student> students;
    Database database;

    public ProjectsAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
        this.database = new Database(context);
    }

    @NonNull
    @Override
    public ProjectsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.projects_recycler, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsAdapter.Holder holder, int position) {
        Student student = students.get(position);

        String firstTopic = student.getFirstProject(), secondTopic = student.getSecondProject(),
                thirdTopic = student.getThirdProject(), email = student.getEmail(), idNumber = student.getIdNumber(),
                firstStatus = student.getFirstStatus(), secondStatus = student.getSecondStatus(),
                thirdStatus = student.getThirdStatus(), supervisors = format("%s %s %s",
                new Links(context).matchSupervisors(student.getFirstArea()),
                new Links(context).matchSupervisors(student.getSecondArea()),
                new Links(context).matchSupervisors(student.getThirdArea()));

        holder.firstTopic.setText(firstTopic);
        holder.secondTopic.setText(secondTopic);
        holder.thirdTopic.setText(thirdTopic);
        holder.idNumber.setText(idNumber);
        holder.supervisors.setText(supervisors);

        if (firstStatus.equals(context.getString(R.string.approved))) {
            holder.secondApprove.setEnabled(false);
            holder.thirdApprove.setEnabled(false);
        } else if (secondStatus.equals(context.getString(R.string.approved))) {
            holder.firstApprove.setEnabled(false);
            holder.thirdApprove.setEnabled(false);
        } else if (thirdStatus.equals(context.getString(R.string.approved))) {
            holder.firstApprove.setEnabled(false);
            holder.secondApprove.setEnabled(false);
        }

        holder.firstApprove.setOnClickListener(v -> {
            Projects project = new Projects(0, email, firstTopic);
            database.insertProject(project);
            database.updateFistTopicApprovalStatus(email, context.getString(R.string.approved));
            database.updateSecondTopicApprovalStatus(email, context.getString(R.string.disapproved));
            database.updateThirdTopicApprovalStatus(email, context.getString(R.string.disapproved));

            holder.secondApprove.setEnabled(false);
            holder.thirdApprove.setEnabled(false);
            Toast.makeText(context, "First topic approved!", Toast.LENGTH_SHORT).show();
        });

        holder.secondApprove.setOnClickListener(v -> {
            Projects project = new Projects(0, email, secondTopic);
            database.insertProject(project);
            database.updateFistTopicApprovalStatus(email, context.getString(R.string.disapproved));
            database.updateSecondTopicApprovalStatus(email, context.getString(R.string.approved));
            database.updateThirdTopicApprovalStatus(email, context.getString(R.string.disapproved));

            holder.firstApprove.setEnabled(false);
            holder.thirdApprove.setEnabled(false);
            Toast.makeText(context, "Second topic approved!", Toast.LENGTH_SHORT).show();
        });

        holder.thirdApprove.setOnClickListener(v -> {
            Projects project = new Projects(0, email, thirdTopic);
            database.insertProject(project);
            database.updateFistTopicApprovalStatus(email, context.getString(R.string.disapproved));
            database.updateSecondTopicApprovalStatus(email, context.getString(R.string.disapproved));
            database.updateThirdTopicApprovalStatus(email, context.getString(R.string.approved));

            holder.firstApprove.setEnabled(false);
            holder.thirdApprove.setEnabled(false);
            Toast.makeText(context, "Third topic approved!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(ArrayList<Student> filteredStudents) {
        this.students = filteredStudents;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView firstTopic, secondTopic, thirdTopic, idNumber, supervisors;
        Button firstApprove, secondApprove, thirdApprove;

        public Holder(@NonNull View itemView) {
            super(itemView);

            firstTopic = itemView.findViewById(R.id.firstTopic);
            secondTopic = itemView.findViewById(R.id.secondTopic);
            thirdTopic = itemView.findViewById(R.id.thirdTopic);
            idNumber = itemView.findViewById(R.id.idNumber);
            supervisors = itemView.findViewById(R.id.supervisors);
            firstApprove = itemView.findViewById(R.id.firstApprove);
            secondApprove = itemView.findViewById(R.id.secondApprove);
            thirdApprove = itemView.findViewById(R.id.thirdApprove);
        }
    }
}