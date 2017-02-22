package in.errorlabs.infoquest2k17.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import in.errorlabs.infoquest2k17.Models.Gaming_Model;
import in.errorlabs.infoquest2k17.R;

/**
 * Created by root on 1/8/17.
 */

public class Gaming_Adapter extends RecyclerView.Adapter<Gaming_Adapter.GamingViewHolder> {
    private List<Gaming_Model> arraylist;
    private Context context;

    public Gaming_Adapter(List<Gaming_Model>arraylist,Context context){
        this.arraylist=arraylist;
        this.context=context;
    }

    @Override
    public GamingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listmodel, parent, false);
        return new GamingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GamingViewHolder holder, int position) {
        Gaming_Model model = arraylist.get(position);
       // Glide.with(context).load(model.getGaming_pic()).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_error).into(holder.game_pic);
        Glide.with(context).load(model.getGaming_pic()).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(android.R.drawable.ic_dialog_alert).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.game_pic);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }


    public static class GamingViewHolder extends RecyclerView.ViewHolder{

        ImageView game_pic;
        ProgressBar progressBar;

        public GamingViewHolder(View itemView) {
            super(itemView);

            game_pic = (ImageView) itemView.findViewById(R.id.home_imgview);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }
}
