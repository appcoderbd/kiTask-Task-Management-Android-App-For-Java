package com.dscappstudio.kitask.profile;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProfileDao {

    @Insert
    void insertProfile(Profile profile);

    @Query("SELECT * FROM user_profile LIMIT 1")
    Profile getProfile();
}

