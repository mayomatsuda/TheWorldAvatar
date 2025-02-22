package uk.ac.cam.cares.jps.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import uk.ac.cam.cares.jps.todo.databinding.TodoSecondFragmentBinding;

public class TodoSecondFragment extends Fragment {

    private TodoSecondFragmentBinding binding;

    // Inflate the XML layout for this fragment into a collection of View objects that can be manipulated in code.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TodoSecondFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    // Actions to be taken after view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.topAppbar.setNavigationOnClickListener(view1 -> NavHostFragment.findNavController(this).navigateUp());

    }
}
