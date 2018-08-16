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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import model.Cow;

public class CowRecyclerAdapter extends RecyclerView.Adapter<CowRecyclerAdapter.CowViewHolder> implements Filterable {

    Context context;
    List<Cow> cowList;
    List<Cow> cowListFiltered;
    CustomItemClickListener itemClickListener;

    public CowRecyclerAdapter(Context context, List<Cow> cowList, CustomItemClickListener itemClickListener){
        this.context = context;
        this.cowList = cowList;
        this.cowListFiltered = cowList;
        this.itemClickListener = itemClickListener;
    }

    public CowViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_main, parent, false);

        final CowViewHolder mViewHolder = new CowViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(CowViewHolder holder, int position){
        //holder.textViewId.setText(cowList.get(position).getId());
        holder.textViewMatricula.setText(cowList.get(position).getMatricula());
        holder.textViewNombre.setText(cowList.get(position).getNombre());
    }

    @Override
    public int getItemCount(){
        return cowList.size();
    }

    public class CowViewHolder extends RecyclerView.ViewHolder{
        //public TextView textViewId;
        public TextView textViewMatricula;
        public TextView textViewNombre;

        public CowViewHolder(View view){
            super(view);
            //textViewId = (TextView) view.findViewById(R.id.textViewId);
            textViewMatricula = (TextView) view.findViewById(R.id.textViewMatricula);
            textViewNombre = (TextView) view.findViewById(R.id.textViewNombre);
        }
    }

    public void update(List<Cow> data) {
        cowList.clear();
        cowList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    cowListFiltered = cowList;
                }else{
                    List<Cow> listFiltered = new ArrayList<>();
                    for(Cow row : cowList){
                        if(row.getNombre().toLowerCase().contains(charString.toLowerCase()) || row.getMatricula().contains(constraint))
                            listFiltered.add(row);
                    }
                    cowListFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cowListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cowListFiltered = (List<Cow>) results.values;
                cowList.clear();
                cowList.addAll(cowListFiltered);
                notifyDataSetChanged();
            }
        };
    }
}
