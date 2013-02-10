/**
 * 
 * This is OpenTraining, an Android application for planning your your fitness training.
 * Copyright (C) 2012-2013 Christian Skubich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package de.skubware.opentraining.activity.create_workout;

import com.actionbarsherlock.app.SherlockDialogFragment;

import de.skubware.opentraining.R;
import de.skubware.opentraining.basic.FitnessExercise;
import de.skubware.opentraining.basic.IExercise;
import de.skubware.opentraining.basic.Workout;
import de.skubware.opentraining.db.DataProvider;
import de.skubware.opentraining.db.IDataProvider;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Dialog Fragment that shows the {@link FitnessExercise}s of the current
 * {@link Workout}.
 * 
 * @author Christian Skubich
 * 
 */
public class DialogWorkoutOverviewFragment extends SherlockDialogFragment {

	/** Currently displayed {@link Workout}. */
	Workout mWorkout;

	private static String ARG_ID_WORKOUT = "workout";

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	static DialogWorkoutOverviewFragment newInstance(Workout workout) {
		DialogWorkoutOverviewFragment f = new DialogWorkoutOverviewFragment();

		Bundle args = new Bundle();
		args.putSerializable(ARG_ID_WORKOUT, workout);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWorkout = (Workout) getArguments().getSerializable(ARG_ID_WORKOUT);

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.fragment_dialog_workout_overview, null);

		// add exercises to list adapter
		IExercise[] arr = new IExercise[mWorkout.getFitnessExercises().size()];
		int i = 0;
		for (FitnessExercise ex : mWorkout.getFitnessExercises()) {
			arr[i] = ex;
			i++;
		}

		ListView listview = (ListView) v.findViewById(R.id.listview);
		listview.setAdapter(new ArrayAdapter<IExercise>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arr));

		return new AlertDialog.Builder(getActivity()).setTitle(mWorkout.getName()).setView(v).setCancelable(true)
				.setPositiveButton(getString(R.string.save_workout), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// save Workout before exiting
						IDataProvider dataProvider = new DataProvider(getActivity());
						dataProvider.saveWorkout(mWorkout);
						
						finishActivities();
					}
				}).setNeutralButton(getString(R.string.add_more_exercises), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (getActivity() instanceof ExerciseTypeDetailActivity) {
							// close activity that contained the fragment with
							// details
							// to return the result to the list activity
							getActivity().finish();
						}
					}
				}).setNegativeButton(getString(R.string.discard), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finishActivities();
					}
				}).create();
	}
	
	/**
	 * Finishes the Activities ExerciseTypeDetailActivity and ExerciseTypeListActivity .
	 */
	private void finishActivities(){
		if (getActivity() instanceof ExerciseTypeDetailActivity) {
			// finish ExerciseTypeDetailActivity AND
			// ExerciseTypeListActivity
			getActivity().finishActivityFromChild(getActivity(), ExerciseTypeListActivity.RESULT_WORKOUT);
			getActivity().finish();
		} else {
			// finish ExerciseTypeListActivity
			getActivity().finish();
		}
	}

}