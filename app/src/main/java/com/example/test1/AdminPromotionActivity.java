package com.example.test1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminPromotionActivity extends AppCompatActivity {

    private EditText editTextUserId;
    private Button buttonPromote;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        editTextUserId = findViewById(R.id.editText_user_id);
        buttonPromote = findViewById(R.id.button_promote);
        db = FirebaseFirestore.getInstance();

        buttonPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = editTextUserId.getText().toString();
                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(AdminPromotionActivity.this, "Please enter the user ID", Toast.LENGTH_SHORT).show();
                    editTextUserId.setError("User ID is required");
                    editTextUserId.requestFocus();
                } else {
                    promoteToAdmin(userId);
                }
            }
        });
    }

    private void promoteToAdmin(String userId) {
        DocumentReference userRef = db.collection("Users").document(userId);
        userRef.update("isAdmin", true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminPromotionActivity.this, "User promoted to admin successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminPromotionActivity.this, "Failed to promote user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
