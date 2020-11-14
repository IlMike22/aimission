package com.example.michl.aimission.landingpageScene

import com.google.firebase.database.DataSnapshot

interface ILandingpageInteractor {
    fun getUsersMonthList(data: DataSnapshot)
}