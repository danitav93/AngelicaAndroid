package com.example.danieletavernelli.angelica.firebase;

import android.os.AsyncTask;
import android.util.Log;

import com.example.danieletavernelli.angelica.Singleton.UserSingleton;
import com.example.danieletavernelli.angelica.rest.service.GitHubService;
import com.example.danieletavernelli.angelica.utility.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Firebase Service for get refreshed token
 */

public class TokensService extends FirebaseInstanceIdService {

    public static final String TAG = "FB Token Service";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        new RefreshTokenTask(refreshedToken).execute();
    }

    private static class RefreshTokenTask extends AsyncTask<Void,Void,Boolean>{

        private final String refreshToken;

        private Response<Boolean> response;

        RefreshTokenTask(String refreshedToken) {
            this.refreshToken=refreshedToken;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);

                Call<Boolean> call = service.refreshToken(refreshToken, UserSingleton.getInstance().getViewUtente().getIdUtente());

                response = call.execute();

                return (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (!success) {

               //Todo: handle success and failure of refresh token service

            }
        }
    }
}
