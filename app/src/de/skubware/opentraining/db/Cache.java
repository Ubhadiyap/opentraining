package de.skubware.opentraining.db;

import java.util.List;

import android.content.Context;

import de.skubware.opentraining.basic.ExerciseType;
import de.skubware.opentraining.basic.Muscle;

/**
 * Singleton Cache for {@link ExerciseType}s.
 * 
 * This cache should be transparent to most parts of the app. Only the
 * {@link DataProvider} should use this class. For a better performance it may
 * be necessary to run updateCache() once when the app starts. Besides this the
 * activities should never use this class.
 * 
 * @author Christian Skubich
 * 
 */
public enum Cache {
	INSTANCE;
	
	private List<ExerciseType> mExerciseList = null;
	private List<Muscle> mMuscleList = null;

	
	/**
	 * Refreshes the cached data.
	 * 
	 * @param mContext
	 */
	public void updateCache(Context mContext){
		DataProvider dataProvider = new DataProvider(mContext);
		mMuscleList = dataProvider.loadMuscles();
		mExerciseList = dataProvider.loadExercises();
	}
	
	/**
	 * Returns the cached {@link ExerciseType}s.
	 * 
	 * @return The cached  {@link ExerciseType}s or null.
	 */
	public List<ExerciseType> getExercises(){
		return mExerciseList;
	}
	
	public List<Muscle> getMuscles(){
		return mMuscleList;
	}
}
