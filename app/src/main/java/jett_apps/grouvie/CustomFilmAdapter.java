package jett_apps.grouvie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class CustomFilmAdapter extends ArrayAdapter<Film>{

    private Context context;

    public CustomFilmAdapter(@NonNull Context context, ArrayList<Film> films) {
        super(context, R.layout.custom_film_layout, films);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_film_layout, parent, false);

        Film f = getItem(position);
        TextView filmTitle = (TextView) customView.findViewById(R.id.filmTitle);
        filmTitle.setText(f.getFilmName());

        ImageView filmPoster = (ImageView) customView.findViewById(R.id.filmPoster);
        Glide.with(context).load(f.getImageUrl()).load(filmPoster);

        return customView;
    }
}
