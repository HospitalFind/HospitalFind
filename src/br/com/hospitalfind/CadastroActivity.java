package br.com.hospitalfind;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import br.com.hospitalfind.Dao.UsuarioDAO;
import br.com.hospitalfind.Model.Usuario;

import com.example.hospital.R;

public class CadastroActivity extends Activity implements LoaderCallbacks<Cursor> {

	private UserCadastroTask mAuthTask = null;
			
	private EditText etNome;
	private AutoCompleteTextView etEmail;
	private EditText etSenha;
	private EditText etConf;
	private View mProgressView;
	private View mCadastroFormView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);
		
		etNome = (EditText) findViewById(R.id.etNome);
		etEmail = (AutoCompleteTextView) findViewById(R.id.etEmail);
		populateAutoComplete();
		etSenha = (EditText) findViewById(R.id.etSenha);
		etConf = (EditText) findViewById(R.id.etConf);
		
		Button btCadastrar = (Button) findViewById(R.id.btCadastrar);
		btCadastrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptCadastro();
			}
		});
		
		mCadastroFormView = findViewById(R.id.cadastro_form);
		mProgressView = findViewById(R.id.cadastro_progress);
	}
	
	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}
	
	public void attemptCadastro() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		etNome.setError(null);
		etEmail.setError(null);
		etSenha.setError(null);
		etConf.setError(null);

		// Store values at the time of the login attempt.
		String nome = etNome.getText().toString();
		String email = etEmail.getText().toString();
		String password = etSenha.getText().toString();
		String conf = etConf.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			etSenha.setError(getString(R.string.error_invalid_password));
			focusView = etSenha;
			cancel = true;
		}
		// Check password confirmation.
		if (!TextUtils.isEmpty(conf) && !(conf.equals(password))){
			etConf.setError(getString(R.string.error_invalid_password));
			focusView = etConf;
			cancel = true;
		}
		// Check name.
		if (TextUtils.isEmpty(nome)) {
			etNome.setError(getString(R.string.error_field_required));
			focusView = etNome;
			cancel = true;
		}
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			etEmail.setError(getString(R.string.error_field_required));
			focusView = etEmail;
			cancel = true;
		} else if (!isEmailValid(email)) {
			etEmail.setError(getString(R.string.error_invalid_email));
			focusView = etEmail;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserCadastroTask(nome, email, password,this);
			mAuthTask.execute((Void) null);
			
		}
	}
	
	
	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mCadastroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mCadastroFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mCadastroFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mCadastroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
				
	}
	
	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}
	
	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				CadastroActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		etEmail.setAdapter(adapter);
	}
	
	public class UserCadastroTask extends AsyncTask<Void, Void, Boolean> {
		
		private final String mEmail;
		private final String mNome;
		private final String mPassword;
		private Context context;
		private Usuario usr = new Usuario();
		

		UserCadastroTask(String nome, String email, String password, Context ctx) {
			mNome = nome;
			mEmail = email;
			mPassword = password;
			context=ctx;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			
			UsuarioDAO dao = new UsuarioDAO();
			
			try{
				usr.setNome(mNome);
				usr.setEmail(mEmail);
				usr.setSenha(mPassword);
				usr.setRaio_busca(5);
				return dao.adiciona(usr);
			}
			catch(Exception e){
				Log.e("DAO", e+"");
				
				return false;
			}
				
			
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				final Intent m = new Intent (context, Mapactivity.class);
				startActivity(m);
			} else{
				etEmail.setError(getString(R.string.error_invalid_email));
				etEmail.requestFocus();
			}
		}
		

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

}
