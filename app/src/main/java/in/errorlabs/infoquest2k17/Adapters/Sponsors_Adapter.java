package in.errorlabs.infoquest2k17.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import in.errorlabs.infoquest2k17.Models.Sponsors_Model;
import in.errorlabs.infoquest2k17.R;

/**
 * Created by root on 1/9/17.
 */

public class Sponsors_Adapter extends RecyclerView.Adapter<Sponsors_Adapter.SponserViewHolder>{
    List<Sponsors_Model> list;
    Context context;

    public Sponsors_Adapter(List<Sponsors_Model> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public SponserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsors_model, parent, false);
        return new SponserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SponserViewHolder holder, int position) {
        Sponsors_Model model = list.get(position);
        holder.desc.setText(String.valueOf(model.getDesctiption()));
        Glide.with(context).load(model.getPro_pic()).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(android.R.drawable.ic_dialog_alert).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class SponserViewHolder extends RecyclerView.ViewHolder{

       // CircularImageView image;
        ImageView image;
        TextView desc;
        ProgressBar progressBar;

        public SponserViewHolder(View itemView) {
            super(itemView);
          //  image= (CircularImageView) itemView.findViewById(R.id.sponser_img);
            image= (ImageView) itemView.findViewById(R.id.sponser_img);
            desc= (TextView) itemView.findViewById(R.id.sponser_desc);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progress);


        }


    }

}
