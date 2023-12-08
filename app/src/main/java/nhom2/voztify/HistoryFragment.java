package nhom2.voztify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nhom2.voztify.Class.SongForU;
import nhom2.voztify.Class.Track;
import nhom2.voztify.RecentlyPlayedAdapter;

public class HistoryFragment extends Fragment {

    private RecyclerView rvRecentlyPlayed;
    private RecentlyPlayedAdapter recentlyPlayedAdapter;
    private List<SongForU> recentlyPlayedList;
    private List<Track> trackList;

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = currentUser.getUid();

    private ChipGroup chipGroup;
    private Chip chipDay, chipWeek, chipMonth, chipYear;

    private enum TimeFilter {
        DAY, WEEK, MONTH, YEAR
    }

    private TimeFilter selectedTimeFilter = TimeFilter.WEEK;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvRecentlyPlayed = view.findViewById(R.id.rvRecentlyPlayed);

        // Initialize the RecyclerView, data list, and adapter for Recently Played
        LinearLayoutManager layoutManagerRV = new LinearLayoutManager(getActivity());
        rvRecentlyPlayed.setLayoutManager(layoutManagerRV);

        // Initialize the data list and the adapter
        recentlyPlayedList = new ArrayList<>();
        recentlyPlayedAdapter = new RecentlyPlayedAdapter(getActivity(), recentlyPlayedList, new RecentlyPlayedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


            }
        });
        rvRecentlyPlayed.setAdapter(recentlyPlayedAdapter);

        // Initialize the ChipGroup and Chips
        chipGroup = view.findViewById(R.id.chipGroup);
        chipWeek = view.findViewById(R.id.chipWeek);
        chipDay = view.findViewById(R.id.chipDay);
        chipMonth = view.findViewById(R.id.chipMonth);
        chipYear = view.findViewById(R.id.chipYear);

        // Set the default chip selection (Week)
        chipWeek.setChecked(true);

        // Set listeners for chip clicks
        chipDay.setOnClickListener(v -> onChipClicked(TimeFilter.DAY));
        chipWeek.setOnClickListener(v -> onChipClicked(TimeFilter.WEEK));
        chipMonth.setOnClickListener(v -> onChipClicked(TimeFilter.MONTH));
        chipYear.setOnClickListener(v -> onChipClicked(TimeFilter.YEAR));

        // Fetch and display the user's recently played tracks
        fetchRecentlyPlayedTracks();

        return view;
    }

    private void onChipClicked(TimeFilter timeFilter) {
        selectedTimeFilter = timeFilter;
        fetchRecentlyPlayedTracks(); // Update the displayed data based on the selected time filter
    }

    private void fetchRecentlyPlayedTracks() {
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                .child("user_history");

        // Listen for changes in the user's listening history
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentlyPlayedList.clear(); // Clear existing data

                // Iterate through the history items and add them to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SongForU songForU = snapshot.getValue(SongForU.class);

                    // Check the time filter before adding to the list
                    if (isWithinTimeFilter(songForU)) {
                        recentlyPlayedList.add(songForU);
                    }
                }

                // Set the sorted songForU list to the adapter
                recentlyPlayedAdapter.setHistoryList(recentlyPlayedList);

                // Notify the adapter that the data has changed
                recentlyPlayedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors during data retrieval
            }
        });
    }

    private boolean isWithinTimeFilter(SongForU songForU) {
        if (songForU == null || songForU.getTimestamp() == null) {
            return false; // Handle null cases
        }

        // Modify the calculation based on your SongForU timestamp field
        long currentTimeMillis = System.currentTimeMillis();
        long filterStartTimeMillis;

        switch (selectedTimeFilter) {
            case DAY:
                filterStartTimeMillis =  getTodayStartMillis();
                break;
            case WEEK:
                filterStartTimeMillis = currentTimeMillis - (7 * 24 * 60 * 60 * 1000); // 7 days
                break;
            case MONTH:
                // Assuming a month is approximately 30 days
                filterStartTimeMillis = currentTimeMillis - (30 * 24 * 60 * 60 * 1000);
                break;
            default:
                filterStartTimeMillis = 0;
        }

        long songTimestamp = (Long) songForU.getTimestamp();
        return songTimestamp >= filterStartTimeMillis;
    }

    private long getTodayStartMillis() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis - (currentTimeMillis % (24 * 60 * 60 * 1000));
    }



}
