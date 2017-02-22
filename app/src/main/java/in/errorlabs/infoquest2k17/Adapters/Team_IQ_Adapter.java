package in.errorlabs.infoquest2k17.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import in.errorlabs.infoquest2k17.Models.Team_IQ_Model;
import in.errorlabs.infoquest2k17.R;

/**
 * Created by root on 1/9/17.
 */

public class Team_IQ_Adapter extends RecyclerView.Adapter<Team_IQ_Adapter.Team_IQViewHolder> {

    List<Team_IQ_Model> list;
    Context context;

    public Team_IQ_Adapter(List<Team_IQ_Model> list, Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public Team_IQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_infoquestmodel, parent, false);
        return new Team_IQViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Team_IQViewHolder holder, int position) {
        Team_IQ_Model model = list.get(position);
        holder.name.setText(String.valueOf(model.getTeam_IQ_Name()));
        Glide.with(context).load(model.getTeam_IQ_Image()).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(android.R.drawable.ic_dialog_alert).listener(new RequestListener<String, GlideDrawable>() {
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


    public class Team_IQViewHolder extends RecyclerView.ViewHolder {

        CircularImageView image;
        TextView name;
        ProgressBar progressBar;
        public Team_IQViewHolder(View itemView) {
            super(itemView);
            image= (CircularImageView) itemView.findViewById(R.id.team_iq_img);
            name= (TextView) itemView.findViewById(R.id.team_iq_name);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }


}
