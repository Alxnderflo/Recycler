package sv.edu.itca.recyclerprueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvContactos;
    private ContactsAdapter adapterContactos;
    private List<Contacts> listaContactos;
    private List<String> listaContactosIds;
    private FirebaseFirestore bd;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = FirebaseFirestore.getInstance();
        listaContactos = new ArrayList<>();
        listaContactosIds = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvContactos = view.findViewById(R.id.rvContacts);
        rvContactos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar FAB
        FloatingActionButton fab = view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CrearContactoActivity.class));
        });

        // Configurar adapter
        adapterContactos = new ContactsAdapter(listaContactos, getContext(), new ContactsAdapter.OnContactoClickListener() {
            @Override
            public void onContactoClick(Contacts contacto, String contactoId) {
                Log.d("HomeFragment", "Click en contacto - ID: " + contactoId);
                if (contactoId != null && !contactoId.isEmpty()) {
                    Intent intent = new Intent(getActivity(), PreguntasActivity.class);
                    intent.putExtra("contacto_id", contactoId);
                    startActivity(intent);
                } else {
                    Log.e("HomeFragment", "contactoId es null o vacío");
                }

            }


        });
        rvContactos.setAdapter(adapterContactos);

        obtenerContactos();
    }

    private void obtenerContactos() {
        bd.collection("contacts2")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("HomeFragment", "Error: " + error.getMessage());
                            return;
                        }
                        if (value != null) {
                            listaContactos.clear();
                            listaContactosIds.clear();

                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Contacts contacto = snapshot.toObject(Contacts.class);
                                if (contacto != null) {
                                    listaContactos.add(contacto);
                                    listaContactosIds.add(snapshot.getId());
                                    Log.d("HomeFragment", "Contacto cargado - ID: " + snapshot.getId());
                                }
                            }

                            adapterContactos.actualizarIds(listaContactosIds);
                            adapterContactos.notifyDataSetChanged();
                        }
                    }
                });
    }
}