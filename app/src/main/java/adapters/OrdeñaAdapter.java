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
import model.Ordenhas;

public class OrdeñaAdapter extends RecyclerView.Adapter<OrdeñaAdapter.OrdeViewHolder> implements Filterable {

    Context context;
    List<Ordenhas> ordeList;
    List<Ordenhas> ordeListFiltered;
    CustomItemClickListener itemClickListener;
    //DataHelper dataHelper = new DataHelper();

    public OrdeñaAdapter(Context context, List<Ordenhas> ordeList, CustomItemClickListener itemClickListener){
        this.context = context;
        this.ordeList = ordeList;
        this.ordeListFiltered = ordeList;
        this.itemClickListener = itemClickListener;
    }

    public OrdeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_orde, parent, false);

        final OrdeViewHolder mViewHolder = new OrdeViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(OrdeViewHolder holder, int position){
        holder.cantidad.setText(ordeList.get(position).getCantidad());
        holder.fecha.setText(ordeList.get(position).getFecha());
    }

    @Override
    public int getItemCount(){
        return ordeList.size();
    }

    public class OrdeViewHolder extends RecyclerView.ViewHolder{
        public TextView cantidad, fecha;

        public OrdeViewHolder(View view){
            super(view);
            cantidad = (TextView) view.findViewById(R.id.cantidadOrdeRecy);
            fecha = (TextView) view.findViewById(R.id.fechaOrdeRecy);
        }
    }

    public void update(List<Ordenhas> data) {
        ordeList.clear();
        ordeList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    ordeListFiltered = ordeList;
                }else{
                    List<Ordenhas> listFiltered = new ArrayList<>();
                    for(Ordenhas row : ordeList){
                        if(row.getFecha().toLowerCase().contains(charString.toLowerCase()) || row.getFecha().contains(constraint))
                            listFiltered.add(row);
                    }
                    ordeListFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ordeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ordeListFiltered = (List<Ordenhas>) results.values;
                ordeList.clear();
                ordeList.addAll(ordeListFiltered);
                notifyDataSetChanged();
            }
        };
    }
}
