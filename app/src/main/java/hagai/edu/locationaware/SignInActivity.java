package hagai.edu.locationaware;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

    }
    @OnTextChanged (R.id.etName)
    public void  etChanged (CharSequence text){
        Toast.makeText(this, text , Toast.LENGTH_SHORT).show();

    }

    @OnEditorAction(R.id.etName)
    public boolean  etAction(TextView tv , int actionCode , KeyEvent e){
        if (etName.getText().toString().length() < 2){
            etName.setError("Invalid name...");
        }
        FirebaseAuth.getInstance().
                signInAnonymously().
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //ssave the user name and goto maps
                    String userName = etName.getText().toString();
                    //FirebaseAuth.getInstance.getcurrentuser.getuid
                    //but hereis a nicer way
                    String uid = task.getResult().getUser().getUid();
                    task.getResult().getUser().getUid();
                    //1.ref the users table -> uid current user
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    //2.ref.setValue(username)
                    ref.setValue(userName);

                    //go to maps activity
                    Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else {
                    //get the exception
                    Exception e = task.getException();
                    //test that the exception is not null
                    if (e != null)
                        Toast.makeText(SignInActivity.this , e.getMessage(),Toast.LENGTH_SHORT).show();
                    //show error
                }

            }
        });
        return true;
    }

}
