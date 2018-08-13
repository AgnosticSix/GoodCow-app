package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.upc.agnosticsix.goodcow.R;

import java.util.List;

import model.ResultadosPalpamientos;

public class ResPalpaAdapter extends ArrayAdapter<ResultadosPalpamientos> {

    private final Context context;
    private final List<ResultadosPalpamientos> resultadosPalpamientosList;
    private final LayoutInflater inflater;
    private final int mRes;

    public ResPalpaAdapter(@NonNull Context context, int resource, @NonNull List<ResultadosPalpamientos> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resultadosPalpamientosList = objects;
        this.inflater = LayoutInflater.from(context);
        this.mRes = resource;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = inflater.inflate(mRes, parent, false);


        //TODO: buscar el id del textview para support_simple_spinner_dropdown_item

        TextView textView = (TextView) view.findViewById(R.id.textViewItem);

        ResultadosPalpamientos resultadosPalpamientos = resultadosPalpamientosList.get(position);

        textView.setText(resultadosPalpamientos.getNombre());

        return view;
    }
}
