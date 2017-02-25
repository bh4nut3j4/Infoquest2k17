package in.errorlabs.infoquest2k17.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.errorlabs.infoquest2k17.Models.TrackMyPaper_Model;
import in.errorlabs.infoquest2k17.R;

/**
 * Created by root on 2/25/17.
 */

public class TrackMyPaper_Adapter extends RecyclerView.Adapter<TrackMyPaper_Adapter.TMP_ViewHolder> {

    Context context;
    List<TrackMyPaper_Model> list;

    public TrackMyPaper_Adapter(List<TrackMyPaper_Model> list,Context context){
        this.context=context;
        this.list=list;

    }

    @Override
    public TMP_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_model, parent, false);
        return new TMP_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TMP_ViewHolder holder, int position) {
        TrackMyPaper_Model model = list.get(position);
        holder.papername.setText(String.valueOf(model.getPPT_Title()));
        holder.regid.setText(String.valueOf(model.getTeamID()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class TMP_ViewHolder extends RecyclerView.ViewHolder {
        TextView papername,regid;
        public TMP_ViewHolder(View itemView) {
            super(itemView);
            papername = (TextView) itemView.findViewById(R.id.papername);
            regid= (TextView) itemView.findViewById(R.id.event_reg_id);
        }
    }
}
