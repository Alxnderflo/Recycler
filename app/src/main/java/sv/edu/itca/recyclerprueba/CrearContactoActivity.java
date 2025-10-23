package sv.edu.itca.recyclerprueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CrearContactoActivity extends AppCompatActivity {
    private EditText etNombre, etTelefono;
    private Button btnGuardar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_contacto);

        db = FirebaseFirestore.getInstance();
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> guardarContacto());
    }

    private void guardarContacto() {
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear ID del documento ANTES de guardar para navegación inmediata
        DocumentReference nuevoDocumento = db.collection("contacts2").document();
        String nuevoId = nuevoDocumento.getId();

        Map<String, Object> contacto = new HashMap<>();
        contacto.put("name", nombre);
        contacto.put("phoneNo", telefono);
        contacto.put("fechaCreacion", FieldValue.serverTimestamp());

        // Guardar contacto - NO esperar callbacks para navegación
        nuevoDocumento.set(contacto)
                .addOnSuccessListener(aVoid -> {
                    Log.d("CrearContacto", "Contacto sincronizado con ID: " + nuevoId);
                })
                .addOnFailureListener(e -> {
                    Log.e("CrearContacto", "Error al sincronizar: " + e.getMessage());
                    // El contacto ya está en cache local, se sincronizará después
                });

        // Navegación INMEDIATA sin esperar callbacks
        Toast.makeText(this, "Contacto guardado localmente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CrearContactoActivity.this, PreguntasActivity.class);
        intent.putExtra("contacto_id", nuevoId);
        startActivity(intent);
        finish();
    }
}