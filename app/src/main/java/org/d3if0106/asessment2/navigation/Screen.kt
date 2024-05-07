package org.d3if0106.asessment2.navigation

import org.d3if0106.asessment2.ui.screen.KEY_ID

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID}"){
        fun withId(id: Long) = "detailScreen/$id"
    }
}