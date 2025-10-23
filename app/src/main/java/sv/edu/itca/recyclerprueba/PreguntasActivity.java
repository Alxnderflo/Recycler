package sv.edu.itca.recyclerprueba;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PreguntasActivity extends AppCompatActivity {
    private RecyclerView rvQuestions;
    private LinearLayout layoutVacio;
    private Button btnAdd, btnCrearPregunta;
    private PreguntasAdapter adapter;
    private List<Pregunta> listaPreguntas;
    private FirebaseFirestore db;
    private String contactoId;

    public static Intent newIntent(Context context, String contactoId) {
        Intent intent = new Intent(context, PreguntasActivity.class);
        intent.putExtra("contacto_id", contactoId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        // Obtener contactoId del intent
        contactoId = getIntent().getStringExtra("contacto_id");
        Log.d("PreguntasActivity", "ID recibido: " + contactoId);

        if (contactoId == null || contactoId.isEmpty()) {
            Toast.makeText(this, "Error: No se pudo identificar el contacto", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        listaPreguntas = new ArrayList<>();

        initViews();
        cargarPreguntas();
    }

    private void initViews() {
        rvQuestions = findViewById(R.id.rvQuestions);
        layoutVacio = findViewById(R.id.layoutVacio);
        btnAdd = findViewById(R.id.btnAdd);
        btnCrearPregunta = findViewById(R.id.btnCrearPregunta);

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PreguntasAdapter(listaPreguntas);
        rvQuestions.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> abrirCrearPregunta());
        btnCrearPregunta.setOnClickListener(v -> abrirCrearPregunta());
    }

    private void abrirCrearPregunta() {
        startActivity(CrearPreguntasActivity.newIntent(this, contactoId));
    }

    private void cargarPreguntas() {
        db.collection("contacts2").document(contactoId)
                .collection("preguntas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("PreguntasActivity", "Error al cargar preguntas: " + error.getMessage());
                            mostrarVistaVacia();
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            listaPreguntas.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Pregunta pregunta = snapshot.toObject(Pregunta.class);
                                if (pregunta != null) {
                                    listaPreguntas.add(pregunta);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            mostrarListaPreguntas();
                        } else {
                            mostrarVistaVacia();
                        }
                    }
                });
    }

    private void mostrarListaPreguntas() {
        rvQuestions.setVisibility(View.VISIBLE);
        layoutVacio.setVisibility(View.GONE);
    }

    private void mostrarVistaVacia() {
        rvQuestions.setVisibility(View.GONE);
        layoutVacio.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // El SnapshotListener ya mantiene la vista actualizada
    }
}