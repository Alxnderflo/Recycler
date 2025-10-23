package sv.edu.itca.recyclerprueba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolderContacto> {

    public interface OnContactoClickListener {
        void onContactoClick(Contacts contacto, String contactoId);
    }

    private List<Contacts> listaContactos;
    private List<String> listaContactosIds;
    private Context contexto;
    private OnContactoClickListener listener;

    public ContactsAdapter(List<Contacts> listaContactos, Context context, OnContactoClickListener listener) {
        this.listaContactos = listaContactos;
        this.contexto = context;
        this.listener = listener;
        this.listaContactosIds = new ArrayList<>();
    }

    public void actualizarIds(List<String> ids) {
        this.listaContactosIds.clear();
        this.listaContactosIds.addAll(ids);
    }

    @NonNull
    @Override
    public ViewHolderContacto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.items_recycler, parent, false);
        return new ViewHolderContacto(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContacto holder, int position) {
        Contacts contacto = listaContactos.get(position);
        String contactoId = position < listaContactosIds.size() ? listaContactosIds.get(position) : null;

        holder.txtName.setText(contacto.getName());
        holder.txtPhoneNo.setText(contacto.getPhoneNo());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && contactoId != null) {
                listener.onContactoClick(contacto, contactoId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public static class ViewHolderContacto extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtPhoneNo;

        public ViewHolderContacto(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhoneNo = itemView.findViewById(R.id.txtPhoneNo);
        }
    }
}