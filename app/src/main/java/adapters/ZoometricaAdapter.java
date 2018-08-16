package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.upc.agnosticsix.goodcow.CustomItemClickListener;
import com.upc.agnosticsix.goodcow.R;

import java.util.ArrayList;
import java.util.List;

import model.Zoometricas;

public class ZoometricaAdapter extends RecyclerView.Adapter<ZoometricaAdapter.ZooViewHolder> implements Filterable {

    Context context;
    List<Zoometricas> zooList;
    List<Zoometricas> zooListFiltered;
    CustomItemClickListener itemClickListener;

    public ZoometricaAdapter(Context context, List<Zoometricas> list, CustomItemClickListener itemClickListener){
        this.context = context;
        this.zooList = list;
        this.zooListFiltered = list;
        this.itemClickListener = itemClickListener;
    }

    public ZooViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_zoo, parent, false);

        final ZooViewHolder mViewHolder = new ZooViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ZooViewHolder holder, int position) {
        holder.alturaZooText.setText(zooList.get(position).getAltura());
        holder.pesoZooText.setText(zooList.get(position).getPeso());
        holder.fechaZooText.setText(zooList.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return zooList.size();
    }

    public class ZooViewHolder extends RecyclerView.ViewHolder{

        public TextView alturaZooText, pesoZooText, fechaZooText;

        public ZooViewHolder(View itemView) {
            super(itemView);
            alturaZooText = (TextView) itemView.findViewById(R.id.alturaZooRecy);
            pesoZooText = (TextView) itemView.findViewById(R.id.pesoZooRecy);
            fechaZooText = (TextView) itemView.findViewById(R.id.fechaZooRecy);
        }
    }

    public void update(List<Zoometricas> data) {
        zooList.clear();
        zooList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    zooListFiltered = zooList;
                }else{
                    List<Zoometricas> listFiltered = new ArrayList<>();
                    for(Zoometricas row : zooList){
                        if(row.getFecha().toLowerCase().contains(charString.toLowerCase()))
                            listFiltered.add(row);
                    }
                    zooListFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = zooListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                zooListFiltered = (List<Zoometricas>) results.values;
                zooList.clear();
                zooList.addAll(zooListFiltered);
                notifyDataSetChanged();
            }
        };
    }
}
