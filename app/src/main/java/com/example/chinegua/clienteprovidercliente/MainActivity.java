package com.example.chinegua.clienteprovidercliente;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private static final String CONTENT_URI = "content://es.upm.miw.clientedb.provider/clientes";

    // Proyección: columnas de la tabla a recuperar
    private static String[] PROJECTION = new String[] {
            "_id",
            "nombre",
            "email",
            "telefono"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filtrarDatos(null);
    }

    @SuppressWarnings("UnusedParameters")
    void filtrarDatos(View v) {
        TextView txtResultados = (TextView) findViewById(R.id.txtRespuesta);
        TextView etFiltro = (EditText) findViewById(R.id.etFiltroNombre);
        String textoFiltro = etFiltro.getText().toString();

        txtResultados.setText("");

        String recurso = CONTENT_URI + ((textoFiltro.isEmpty()) ? "" : '/' + textoFiltro );
        Uri uriContenido =  Uri.parse(recurso);

        // obtenemos el ContentResolver
        ContentResolver contentResolver = getContentResolver();

        // Se ejecuta la consulta
        Cursor cursor = contentResolver.query(
                uriContenido,   // uri del recurso
                PROJECTION,     // Columnas a devolver
                null,           // Condición de la query
                null,           // Argumentos variables de la query
                null            // Orden de los resultados
        );

        // if (cursor.getCount() != 0)
        if (cursor != null && cursor.getCount() != 0) {
            int id;
            String nombre;
            String email;
            int telefono;

            // índices de las columnas
            int colId       = cursor.getColumnIndex(PROJECTION[0]);
            int colNombre   = cursor.getColumnIndex(PROJECTION[1]);
            int colEmail    = cursor.getColumnIndex(PROJECTION[2]);
            int colTelefono = cursor.getColumnIndex(PROJECTION[3]);

            // se recuperan y muestran los resultados recuperados en el cursor
            while (cursor.moveToNext()) {
                id       = cursor.getInt(colId);
                nombre   = cursor.getString(colNombre);
                telefono = cursor.getInt(colTelefono);
                email    = cursor.getString(colEmail);

                txtResultados.append(
                        String.format("%3d: ", id) + String.format("%20s - ", nombre) +
                                String.format("%20s - ", email) + String.format("%12d \n", telefono));
            }
            cursor.close();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "No se han obtenido datos",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}