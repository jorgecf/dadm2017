package es.uam.eps.dadm.jorgecifuentes.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import es.uam.eps.dadm.jorgecifuentes.R;

/**
 * Actividad que muestra la ayuda e información de la app.
 *
 * @author Jorge Cifuentes
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_help);

        TextView textVersion = (TextView) this.findViewById(R.id.text_version);
        TextView textHelp = (TextView) this.findViewById(R.id.text_help);

        // Informacion del paquete.
        PackageInfo pkg;
        try {
            pkg = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Version de la app y demas info.
        textVersion.setText(pkg.packageName + " " + pkg.versionName);

        // Texto tipo de ayuda.
        textHelp.setText(R.string.game_rules);

    }
}