package sv.edu.itca.recyclerprueba;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class CrearPreguntasActivity extends AppCompatActivity {
    private EditText etPregunta, etRespuestaCorrecta, etRespuestaIncorrecta;
    private Button btnGuardarPregunta;
    private FirebaseFirestore db;
    private String contactoId;

    public static Intent newIntent(Context context, String contactoId) {
        Intent intent = new Intent(context, CrearPreguntasActivity.class);
        intent.putExtra("contacto_id", contactoId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_preguntas);

        contactoId = getIntent().getStringExtra("contacto_id");
        if (contactoId == null) {
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        etPregunta = findViewById(R.id.etPregunta);
        etRespuestaCorrecta = findViewById(R.id.etRespuestaCorrecta);
        etRespuestaIncorrecta = findViewById(R.id.etRespuestaIncorrecta);
        btnGuardarPregunta = findViewById(R.id.btnGuardarPregunta);

        btnGuardarPregunta.setOnClickListener(v -> guardarPregunta());
    }

    private void guardarPregunta() {
        String enunciado = etPregunta.getText().toString().trim();
        String correcta = etRespuestaCorrecta.getText().toString().trim();
        String incorrecta = etRespuestaIncorrecta.getText().toString().trim();

        if (enunciado.isEmpty() || correcta.isEmpty() || incorrecta.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Pregunta pregunta = new Pregunta(enunciado, correcta, incorrecta);

        // Guardar pregunta - NO esperar callback para cerrar actividad
        db.collection("contacts2").document(contactoId)
                .collection("preguntas")
                .add(pregunta)
                .addOnSuccessListener(documentReference -> {
                    // Éxito remoto - solo log
                    Log.d("CrearPregunta", "Pregunta sincronizada con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Error remoto - solo log, la pregunta ya está en cache local
                    Log.e("CrearPregunta", "Error al sincronizar: " + e.getMessage());
                });

        // Feedback INMEDIATO y cierre de actividad
        Toast.makeText(this, "Pregunta guardada localmente", Toast.LENGTH_SHORT).show();

        // Limpiar campos
        etPregunta.setText("");
        etRespuestaCorrecta.setText("");
        etRespuestaIncorrecta.setText("");

        finish();
    }
}