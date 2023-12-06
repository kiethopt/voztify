package nhom2.voztify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Class.History;

public class HistoryFragment extends Fragment {



    private RecyclerView rvRecentlyPlayed;
    private RecentlyPlayedAdapter recentlyPlayedAdapter;
    private List<History> recentlyPlayedList;


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();



    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvRecentlyPlayed = view.findViewById(R.id.rvRecentlyPlayed);



        // Initialize the RecyclerView, data list, and adapter for Recently Played
        rvRecentlyPlayed = view.findViewById(R.id.rvRecentlyPlayed);
        LinearLayoutManager layoutManagerRV = new LinearLayoutManager(getActivity());
        rvRecentlyPlayed.setLayoutManager(layoutManagerRV);

        // Initialize the data list and the adapter
        recentlyPlayedList = new ArrayList<>();
        recentlyPlayedAdapter = new RecentlyPlayedAdapter(getActivity(), recentlyPlayedList);
        rvRecentlyPlayed.setAdapter(recentlyPlayedAdapter);



        // Fetch and display the user's recently played tracks
        fetchRecentlyPlayedTracks();


        return view;
    }







    //Lịch sử nghe nhạc
    private void fetchRecentlyPlayedTracks() {
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("user_history");

        // Listen for changes in the user's listening history
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentlyPlayedList.clear(); // Clear existing data

                // Iterate through the history items and add them to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    History history = snapshot.getValue(History.class);
                    recentlyPlayedList.add(history);
                }

                // Notify the adapter that the data has changed
                recentlyPlayedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors during data retrieval
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();

        // Fetch and display the user's recently played tracks
        fetchRecentlyPlayedTracks();

    }



}
