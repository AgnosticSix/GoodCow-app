package adapters;

import android.content.Context;
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

import model.DataHelper;
import model.Palpamientos;

public class PalpamientoAdapter extends RecyclerView.Adapter<PalpamientoAdapter.PalViewHolder> implements Filterable {

    Context context;
    List<Palpamientos> palList;
    List<Palpamientos> palListFiltered;
    CustomItemClickListener itemClickListener;
    DataHelper dataHelper = new DataHelper();

    public PalpamientoAdapter(Context context, List<Palpamientos> palList, CustomItemClickListener itemClickListener){
        this.context = context;
        this.palList = palList;
        this.palListFiltered = palList;
        this.itemClickListener = itemClickListener;
    }

    public PalViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_palpa, parent, false);

        final PalViewHolder mViewHolder = new PalViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(PalViewHolder holder, int position){
        holder.resultado.setText(dataHelper.getResPalpamiento(palList.get(position).getResultado()));
        holder.fecha.setText(palList.get(position).getFecha());
    }

    @Override
    public int getItemCount(){
        return palList.size();
    }

    public class PalViewHolder extends RecyclerView.ViewHolder{
        public TextView resultado, fecha;

        public PalViewHolder(View view){
            super(view);
            resultado = (TextView) view.findViewById(R.id.resultadoPalRecy);
            fecha = (TextView) view.findViewById(R.id.fechaPalRecy);
        }
    }

    public void update(List<Palpamientos> data) {
        palList.clear();
        palList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    palListFiltered = palList;
                }else{
                    List<Palpamientos> listFiltered = new ArrayList<>();
                    for(Palpamientos row : palList){
                        if(row.getFecha().toLowerCase().contains(charString.toLowerCase()) || row.getFecha().contains(constraint))
                            listFiltered.add(row);
                    }
                    palListFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = palListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                palListFiltered = (List<Palpamientos>) results.values;
                palList.clear();
                palList.addAll(palListFiltered);
                notifyDataSetChanged();
            }
        };
    }
}
