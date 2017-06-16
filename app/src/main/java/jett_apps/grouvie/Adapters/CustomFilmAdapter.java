package jett_apps.grouvie.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

import jett_apps.grouvie.HelperObjects.Film;
import jett_apps.grouvie.R;


public class CustomFilmAdapter extends ArrayAdapter<Film>{

    private Context context;
    private Bitmap b;

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
        String imageUrl = f.getImageUrl();
        RequestOptions options = new RequestOptions();
        int posterWidth = 300;
        int posterHeight = 600;
        options.override(posterWidth, posterHeight).fitCenter();
        Glide.with(context).load(imageUrl).apply(options).into(filmPoster);
//        b = downloadBitmap(imageUrl);
//        filmPoster.setImageBitmap(b);

        return customView;
    }

    private Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    b = BitmapFactory.decodeStream(inputStream);


                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return b;
    }
}
