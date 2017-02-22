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

import in.errorlabs.infoquest2k17.Models.Others_Model;
import in.errorlabs.infoquest2k17.R;

/**
 * Created by root on 1/8/17.
 */

public class Others_Adapter extends RecyclerView.Adapter<Others_Adapter.OthersViewHolder> {
    List<Others_Model> list;
    Context context;

    public Others_Adapter(List<Others_Model> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public OthersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listmodel, parent, false);
        return new OthersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final OthersViewHolder holder, int position) {
        Others_Model model = list.get(position);
       // Glide.with(context).load(model.getOthers_pic()).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_error).into(holder.others_pic);
        Glide.with(context).load(model.getOthers_pic()).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(android.R.drawable.ic_dialog_alert).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.others_pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class OthersViewHolder extends RecyclerView.ViewHolder {

        ImageView others_pic;
        ProgressBar progressBar;
        public OthersViewHolder(View itemView) {
            super(itemView);
            others_pic = (ImageView) itemView.findViewById(R.id.home_imgview);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }
}
