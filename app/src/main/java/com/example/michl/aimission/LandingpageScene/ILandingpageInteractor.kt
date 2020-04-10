package com.example.michl.aimission.LandingpageScene

import com.google.firebase.database.DataSnapshot

interface ILandingpageInteractor {
    fun getUsersMonthList(data: DataSnapshot)
}