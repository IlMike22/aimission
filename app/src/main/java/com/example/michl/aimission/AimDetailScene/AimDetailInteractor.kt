package com.example.michl.aimission.AimDetailScene

import com.example.michl.aimission.Models.AimItem


interface AimDetailInteractorInput {
    fun createNewAim(userId: String, item: AimItem)
    fun deleteSingleAim(userId: String)
    fun updateAim(userId:String, item:AimItem)

}

class AimDetailInteractor : AimDetailInteractorInput {
    override fun updateAim(userId: String, item: AimItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSingleAim(userId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNewAim(userId: String, item: AimItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

