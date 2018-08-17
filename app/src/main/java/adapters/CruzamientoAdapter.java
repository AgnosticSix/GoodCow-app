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

import model.Cruzamientos;
import model.DataHelper;

public class CruzamientoAdapter extends RecyclerView.Adapter<CruzamientoAdapter.CruzaViewHolder> implements Filterable {

    Context context;
    List<Cruzamientos> cruzaList;
    List<Cruzamientos> cruzaListFiltered;
    CustomItemClickListener itemClickListener;
    DataHelper dataHelper = new DataHelper();

    public CruzamientoAdapter(Context context, List<Cruzamientos> cruzaList, CustomItemClickListener itemClickListener){
        this.context = context;
        this.cruzaList = cruzaList;
        this.cruzaListFiltered = cruzaList;
        this.itemClickListener = itemClickListener;
    }

    public CruzaViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_cruza, parent, false);

        final CruzaViewHolder mViewHolder = new CruzaViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(CruzaViewHolder holder, int position){
        holder.vaca.setText(dataHelper.getCow(cruzaList.get(position).getVacaid()));
        holder.semental.setText(dataHelper.getCow(cruzaList.get(position).getSementalid()));
        holder.fecha.setText(cruzaList.get(position).getFecha());
    }

    @Override
    public int getItemCount(){
        return cruzaList.size();
    }

    public class CruzaViewHolder extends RecyclerView.ViewHolder{
        public TextView vaca, semental, fecha;

        public CruzaViewHolder(View view){
            super(view);
            vaca = (TextView) view.findViewById(R.id.vacaCruzaRecy);
            semental = (TextView) view.findViewById(R.id.sementalCruzaRecy);
            fecha = (TextView) view.findViewById(R.id.fechaCruzaRecy);
        }
    }

    public void update(List<Cruzamientos> data) {
        cruzaList.clear();
        cruzaList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    cruzaListFiltered = cruzaList;
                }else{
                    List<Cruzamientos> listFiltered = new ArrayList<>();
                    for(Cruzamientos row : cruzaList){
                        if(row.getFecha().toLowerCase().contains(charString.toLowerCase()) || row.getFecha().contains(constraint))
                            listFiltered.add(row);
                    }
                    cruzaListFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cruzaListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cruzaListFiltered = (List<Cruzamientos>) results.values;
                cruzaList.clear();
                cruzaList.addAll(cruzaListFiltered);
                notifyDataSetChanged();
            }
        };
    }
}
