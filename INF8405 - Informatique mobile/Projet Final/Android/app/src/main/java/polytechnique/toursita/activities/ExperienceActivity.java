package polytechnique.toursita.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import polytechnique.toursita.R;
import polytechnique.toursita.manager.SharedPreferenceManager;
import polytechnique.toursita.webService.AddCommentResponse;
import polytechnique.toursita.webService.Comment;
import polytechnique.toursita.webService.ImageData;
import polytechnique.toursita.webService.ImageUploadResult;
import polytechnique.toursita.webService.LocationRequestResponse;
import polytechnique.toursita.webService.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExperienceActivity extends AppCompatActivity {

    private ViewGroup commentView;
    private ViewGroup imageView;
    private TextView placeName, address;
    private ImageView backArrow;
    private Button addComment, addImage, okComment;
    private EditText commentBox;
    private RelativeLayout commentGroup;
    private WebService webService;
    private String locationId;
    private RelativeLayout loadingView;
    private boolean picPermission = true;

    public final int RESULT_LOAD_IMG = 400;

    Callback<ImageUploadResult> uploadImageCallback = new Callback<ImageUploadResult>() {
        @Override
        public void onResponse(Call<ImageUploadResult> call, Response<ImageUploadResult> response) {
            if (response.isSuccessful() && response.body().success){
                getLocation();
            }else{
                hideLoadingScreen();
                Toast.makeText(getApplicationContext(), "Une erreur c\'est produite lors de l\'ajout de l\'image", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ImageUploadResult> call, Throwable t) {
            hideLoadingScreen();
            Toast.makeText(getApplicationContext(), "Une erreur c\'est produite lors de l\'ajout de l\'image", Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener pictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (locationId ==  null){
                Toast.makeText(getApplicationContext(), "Attendre que la page load s\'il vous plait.", Toast.LENGTH_LONG).show();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions();
            }
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }
    };

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri selectedImageURI = data.getData();
            File imageFile = new File(getRealPathFromURI(selectedImageURI));

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imageToSend = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            showLoadingScreen();
            webService.addImage(locationId, imageToSend).enqueue(uploadImageCallback);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String toReturn = cursor.getString(idx);
            cursor.close();
            return toReturn;
        }
    }


    private View.OnClickListener okCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (commentBox.getText().toString().equals(""))
                return;
            showLoadingScreen();
            webService.addComment(locationId, SharedPreferenceManager.getUserId(getApplicationContext()), commentBox.getText().toString()).enqueue(new Callback<AddCommentResponse>() {
                @Override
                public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {
                    if (response.isSuccessful()){
                        closeKeyboard();
                        commentGroup.setVisibility(View.GONE);
                        getLocation();
                    }
                }

                @Override
                public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Une erreur c\'est produite lors de l\'ajout du commentaire", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private View.OnClickListener addCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            commentBox.setText("");
            commentGroup.setVisibility(View.VISIBLE);
            commentBox.requestFocus();
        }
    };

    private View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_layout);
        initializeView();
        webService = new WebService();
        showLoadingScreen();
        getLocation();
    }

    private void initializeView(){
        commentView = findViewById(R.id.comments);
        imageView = findViewById(R.id.image);
        placeName = findViewById(R.id.name);
        address = findViewById(R.id.address);
        backArrow = findViewById(R.id.backArrow);
        addComment = findViewById(R.id.add_comment);
        addImage = findViewById(R.id.add_picture);
        commentBox = findViewById(R.id.commentBox);
        okComment = findViewById(R.id.ok_comment);
        commentGroup = findViewById(R.id.commentGroup);
        loadingView = findViewById(R.id.loadingScreen);

        backArrow.setOnClickListener(backArrowListener);
        addComment.setOnClickListener(addCommentListener);
        okComment.setOnClickListener(okCommentListener);
        addImage.setOnClickListener(pictureListener);
    }

    private void populateView(LocationRequestResponse data){
        commentView.removeAllViews();
        placeName.setText(data.description);
        address.setText(data.address);

        for (Comment com : data.comments){
            View thisComment = LayoutInflater.from(this).inflate(R.layout.comment_row, commentView, false);

            TextView postedBy = thisComment.findViewById(R.id.posterId);
            TextView actualComment = thisComment.findViewById(R.id.coreComment);

            String name = com.postedBy.FirstName + " " + com.postedBy.LastName;
            postedBy.setText(name);
            actualComment.setText(com.text);

            commentView.addView(thisComment);
        }
        if (data.comments.length == 0){
            View thisComment = LayoutInflater.from(this).inflate(R.layout.comment_row, commentView, false);

            TextView postedBy = thisComment.findViewById(R.id.posterId);
            TextView actualComment = thisComment.findViewById(R.id.coreComment);
            TextView ecrtiPar = thisComment.findViewById(R.id.ecritPar);

            postedBy.setVisibility(View.GONE);
            actualComment.setText("Il n\'y a aucun commentaire");
            ecrtiPar.setVisibility(View.GONE);

            commentView.addView(thisComment);
        }
    }

    private void addImages(ImageData[] data){
        imageView.removeAllViews();
        for (ImageData im : data){
            String picture = im.data;
            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            View thisImage = LayoutInflater.from(this).inflate(R.layout.image_layout, imageView, false);

            ImageView imgView = thisImage.findViewById(R.id.imageContainer);
            imgView.setImageBitmap(decodedByte);

            imageView.addView(thisImage);
        }
        if(data.length == 0){
            View thisImage = LayoutInflater.from(this).inflate(R.layout.image_layout, imageView, false);

            ImageView imgView = thisImage.findViewById(R.id.imageContainer);
            imgView.setImageResource(R.drawable.aucune_image);

            imageView.addView(thisImage);
        }
    }

    private void getLocation(){
        String placeId = getIntent().getStringExtra("id");
        webService.getLocationId(placeId).enqueue(new Callback<LocationRequestResponse>() {
            @Override
            public void onResponse(Call<LocationRequestResponse> call, Response<LocationRequestResponse> response) {
                if (response.isSuccessful()){
                    locationId = response.body()._id;
                    populateView(response.body());
                    addImages(response.body().images);
                }
                hideLoadingScreen();
            }

            @Override
            public void onFailure(Call<LocationRequestResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erreur lors de la recherche d'infomation sur l\'endroit.", Toast.LENGTH_LONG).show();
                hideLoadingScreen();
            }
        });
    }

    protected void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1052);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    picPermission = true;
                } else {
                    picPermission = false;
                    Toast.makeText(getApplicationContext(), "Pour ajouter une photo, il faut avoir la permissions", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void showLoadingScreen(){
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingScreen(){
        loadingView.setVisibility(View.GONE);
    }
}
