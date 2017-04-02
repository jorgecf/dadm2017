package es.uam.eps.dadm.jorgecifuentes.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import es.uam.eps.dadm.jorgecifuentes.R;

/**
 * Actividad que pide permiso para mostrar la lista de contactos.
 *
 * @author Jorge Cifuentes
 */
public class ContactsActivity extends AppCompatActivity {

    // Un identificador para el permiso de lectura de contactos
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    // Un identificador único para el loader, cualquier valor sirve
    public static final int LOADER_ID = 78;
    private SimpleCursorAdapter adapter;

    // Columnas que vaos a leer.
    private static final String[] FROM = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY, ContactsContract.Contacts.HAS_PHONE_NUMBER};

    // Views donde guardar las columnas leidas (en este caso de contacts.xml).
    private static final int[] TO = {R.id.contactName, R.id.emailDirection};


    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        // Crea y devuelve el cargador del cursor
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            // Columnas de la query que queremos obtener
            String[] projectionFields = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
                    //  ContactsContract.CommonDataKinds.Email._ID, ContactsContract.CommonDataKinds.Email.ADDRESS
            };

            // Constructor del CursorLoader
            CursorLoader cursorLoader = new CursorLoader(
                    ContactsActivity.this,
                    ContactsContract.Contacts.CONTENT_URI,
                    projectionFields,
                    null, // selection
                    null, // selectionArgs
                    null // sortOrder
            );

            return cursorLoader;
        }

        // Se llama cuando el sistema termina de cargar el cursor
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            adapter.swapCursor(cursor);
        }


        // Se llama si los datos del proveedor cambian
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // on reset take any old cursor away
            adapter.swapCursor(null);
        }
    };

    private void setupCursorAdapter() {

        this.adapter = new SimpleCursorAdapter(this, R.layout.contacts, null, FROM, TO, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Cargamos el LoaderManager de los contactos.
        this.getSupportLoaderManager().initLoader(LOADER_ID, new Bundle(), contactsLoader);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts_list);

        // Comprobamos si tenemos permisos.
        if (ContextCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            // Debemos presentar una explicación?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this, Manifest.permission.READ_CONTACTS)) {
                // Mostramos una explicación al usuario y
                // solicitamos el permiso otra vez
                Snackbar.make(findViewById(R.id.contactsListView), "The contacts are required just to show them", Snackbar.LENGTH_LONG).show(); //TODO strings
                ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                // No se necesita explicación, se solicita el permiso.
                ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            showContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // el argumento grantResults está vacio si la solicitud se rechaza
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.showContacts();
                } else {
                    Snackbar.make(findViewById(R.id.contactsListView), R.string.contacts_permission_denied, Snackbar.LENGTH_INDEFINITE).show();
                }
                return;

            }
        }
    }

    /**
     * Inicializa el SimpleCursorAdapter y se lo pasa a la ListView.
     */
    private void showContacts() {
        // Instanciamos el SimpleCursorAdapter.
        this.setupCursorAdapter();

        ((ListView) findViewById(R.id.contactsListView)).setAdapter(this.adapter);
    }
}