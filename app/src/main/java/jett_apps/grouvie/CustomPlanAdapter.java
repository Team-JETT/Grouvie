package jett_apps.grouvie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomPlanAdapter extends ArrayAdapter<Plan>{

    public CustomPlanAdapter(@NonNull Context context, ArrayList<Plan> currentPlans) {
        super(context, R.layout.custom_current_plan, currentPlans);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_current_plan, parent, false);

        Plan p = getItem(position);
        TextView planFilmText = (TextView) customView.findViewById(R.id.plan_film);
        TextView planDateText = (TextView) customView.findViewById(R.id.plan_date);

        planFilmText.setText(p.getSuggestedFilm());
        planDateText.setText(p.getSuggestedDate());

        return customView;
    }
}
