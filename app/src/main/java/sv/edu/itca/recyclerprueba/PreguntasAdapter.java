package sv.edu.itca.recyclerprueba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.ViewHolder> {
    private List<Pregunta> listaPreguntas;

    public PreguntasAdapter(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pregunta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pregunta pregunta = listaPreguntas.get(position);
        holder.tvEnunciado.setText(pregunta.getEnunciado());
        holder.tvCorrecta.setText("Correcta: " + pregunta.getCorrecta());
        holder.tvIncorrecta.setText("Incorrecta: " + pregunta.getIncorrecta());
    }

    @Override
    public int getItemCount() {
        return listaPreguntas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEnunciado, tvCorrecta, tvIncorrecta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnunciado = itemView.findViewById(R.id.tvEnunciado);
            tvCorrecta = itemView.findViewById(R.id.tvCorrecta);
            tvIncorrecta = itemView.findViewById(R.id.tvIncorrecta);
        }
    }
}