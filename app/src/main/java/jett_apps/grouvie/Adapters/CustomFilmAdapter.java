package jett_apps.grouvie.Adapters;

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
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import jett_apps.grouvie.HelperObjects.Film;
import jett_apps.grouvie.R;


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

        TextView filmOverview = (TextView) customView.findViewById(R.id.filmOverview);
        filmOverview.setText(f.getOverview());

        ImageView filmPoster = (ImageView) customView.findViewById(R.id.filmPoster);
        String imageUrl = f.getImageUrl();
        RequestOptions options = new RequestOptions();
        int posterWidth = 200;
        int posterHeight = 400;
        options.override(posterWidth, posterHeight).fitCenter();
        Glide.with(context).load(imageUrl).apply(options).into(filmPoster);

        return customView;
    }

}
