package br.com.hospitalfind;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import br.com.hospitalfind.Constantes.Constantes;
import br.com.hospitalfind.Dao.UsuarioDAO;
import br.com.hospitalfind.Model.Usuario;

import com.example.hospital.R;

public class MainActivity extends Activity implements Button.OnClickListener {
	
	private Button BtCadastrar;
	private Button BtTestaWS;
	private Button BtMapa;
	private Button BtLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button BtLogar = (Button) this.findViewById(R.id.BtCadastrar);
		Button BtTestaWS = (Button) this.findViewById(R.id.BtTestaWS);
		Button BtMapa = (Button) this.findViewById(R.id.BtMapa);
		Button BtLogin = (Button) this.findViewById(R.id.BtLogin);
		BtLogin.setOnClickListener(this);
		BtMapa.setOnClickListener(this);
		BtLogar.setOnClickListener(this);
		BtTestaWS.setOnClickListener(this);
				
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Constantes.RES_CADASTRO_OK) {
			Toast.makeText(this, "Cadastrado com sucesso!",
					Toast.LENGTH_SHORT).show();
		}
		if(resultCode == Constantes.RES_CADASTRO_NOK) {
			final Intent i = new Intent (this, CadastroActivity.class);
			startActivityForResult(i,Constantes.REQ_CADASTRO);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.BtLogin:
			Toast.makeText(this, "Logar", Toast.LENGTH_SHORT).show();
			final Intent l = new Intent (this, LoginActivity.class);
			startActivity(l);	
			break;
			case R.id.BtCadastrar:
				Toast.makeText(this, "Cadastrar", Toast.LENGTH_SHORT).show();
				final Intent i = new Intent (this, CadastroActivity.class);
				startActivityForResult(i,Constantes.REQ_CADASTRO);	
				break;
			case R.id.BtMapa:
				Toast.makeText(this, "Mapa", Toast.LENGTH_SHORT).show();
				final Intent m = new Intent (this, Mapactivity.class);
				startActivity(m);
				break;
			case R.id.BtTestaWS:
				//Teste de todos os metodos do Web service
				Boolean log;
				Usuario usr =new Usuario();
				UsuarioDAO dao = new UsuarioDAO();
				if(android.os.Build.VERSION.SDK_INT>9){
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				//Criar
				usr.setEmail("demolidor@marvel.com");
				usr.setNome("Matt Murdock");
				usr.setSenha("Karen");
				usr.setRaio_busca(1);
				Log.d("DAO","Usuario que vai ser inserido"+usr.toString() );
				log=dao.adiciona(usr);
				Log.d("DAO", "Adicionado="+log );
				//Read
				Usuario teste=dao.getUsuarioPorEmail(usr.getEmail());
				Log.d("DAO","Achou usuario no banco="+teste.toString());
				//Update
				teste.setRaio_busca(50);
				log=dao.altera(teste);
				Log.d("DAO","Alterou usuario="+log );
				//Remove
				log=dao.remove(teste);
				Log.d("DAO","Removeu usuario="+log );
				Toast.makeText(this, "Teste OK", Toast.LENGTH_SHORT).show();
				
			break;
		}
		
}
}