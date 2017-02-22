package in.errorlabs.infoquest2k17.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.errorlabs.infoquest2k17.Models.RegisteredEvent_F_Model;
import in.errorlabs.infoquest2k17.R;

/**
 * Created by root on 1/11/17.
 */

public class RegisteredEvent_F_Adapter extends RecyclerView.Adapter<RegisteredEvent_F_Adapter.RegEvent_F_ViewHolder> {
    List<RegisteredEvent_F_Model> list;
    Context context;


    public RegisteredEvent_F_Adapter(List<RegisteredEvent_F_Model> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public RegEvent_F_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_event_f_model, parent, false);
        return new RegEvent_F_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RegEvent_F_ViewHolder holder, int position) {
        RegisteredEvent_F_Model model = list.get(position);
        holder.eventname.setText(String.valueOf(model.getEventname()));
        holder.event_reg_id.setText(String.valueOf(model.getEvent_reg_id()));
        if (model.getTeam_id().isEmpty() || model.getTeam_id()==null || model.getTeam_id()==""){
            holder.team_text.setVisibility(View.GONE);
            holder.teamid.setVisibility(View.GONE);
        }else {
            holder.teamid.setText(String.valueOf(model.getTeam_id()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RegEvent_F_ViewHolder extends RecyclerView.ViewHolder {

        TextView eventname;
        TextView event_reg_id,team_text,teamid;
        public RegEvent_F_ViewHolder(View itemView) {
            super(itemView);
            eventname= (TextView) itemView.findViewById(R.id.eventname);
            event_reg_id= (TextView) itemView.findViewById(R.id.event_reg_id);
            team_text= (TextView) itemView.findViewById(R.id.t_id_text);
            teamid= (TextView) itemView.findViewById(R.id.teamid);
          /*  itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TextView e_name = (TextView) view.findViewById(R.id.eventname);
                    final TextView e_id = (TextView) view.findViewById(R.id.event_reg_id);
                    final String eventname=e_name.getText().toString();
                    final String eventid=e_id.getText().toString();
                    Log.d("LOG","LONGGGG"+eventname);

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    alertDialog.setTitle("Confirm Delete...");

                    alertDialog.setMessage("Are you sure you want delete this? \n" +
                            "NOTE: If you have already made the payment for this event and you still delete it without attending,You cannot attend the event and Money wont be Refunded.");

                    alertDialog.setIcon(R.drawable.applogo);

                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            Intent i = new Intent(context, DeleteEvent.class);
                            i.putExtra("eventname",eventname);
                            i.putExtra("eventid",eventid);
                            context.startActivity(i);

                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

                    return false;
                }
            });*/

        }
    }

}
