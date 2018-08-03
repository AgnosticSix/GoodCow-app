package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upc.agnosticsix.goodcow.CustomItemClickListener;
import com.upc.agnosticsix.goodcow.R;

import org.w3c.dom.Text;

import java.util.List;

import model.Cow;

public class CowRecyclerAdapter extends RecyclerView.Adapter<CowRecyclerAdapter.CowViewHolder> {

    Context context;
    List<Cow> cowList;
    CustomItemClickListener itemClickListener;

    public CowRecyclerAdapter(Context context, List<Cow> cowList, CustomItemClickListener itemClickListener){
        this.context = context;
        this.cowList = cowList;
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

}
