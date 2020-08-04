package com.hms.demo.achievmentdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.common.ApiException
import com.huawei.hms.jos.JosApps
import com.huawei.hms.jos.games.AchievementsClient
import com.huawei.hms.jos.games.Games
import com.huawei.hms.jos.games.achievement.Achievement
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*
import java.text.FieldPosition


class GameActivity : AppCompatActivity(), AchievmentAdapter.MyVH.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG="GameActivity"
    lateinit var client: AchievementsClient
    lateinit var adapter: AchievmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        swipe.setOnRefreshListener(this)
        swipe.isRefreshing=true
        init()
        currentPlayerInfo()
        client = Games.getAchievementsClient(this)
        getAchievments()
        adapter= AchievmentAdapter(ArrayList(),this)
        recycler.layoutManager=LinearLayoutManager(this)
        recycler.adapter=adapter

    }

    private fun getAchievments() {
        val task =
            client.getAchievementList(true)
        task.addOnSuccessListener(OnSuccessListener { data ->
            if (data == null) {
                Log.w("Achievement", "achievement list is null")
                return@OnSuccessListener
            }

            adapter.list.clear()
            adapter.list.addAll(data)
            adapter.notifyDataSetChanged()
            swipe.isRefreshing=false
            for(entry in data){
                Log.e("Achievment",entry.id)
            }
        }).addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" +
                        (e as ApiException).statusCode
                Log.e("Achievement", result)
            }
        }
    }


    private fun init() {
        val appsClient = JosApps.getJosAppsClient(this)
        appsClient.init()
        Log.i("Application", "init success")
    }

    private fun currentPlayerInfo() {
        val playersClient = Games.getPlayersClient(this@GameActivity)
        val playerTask = playersClient.currentPlayer
        playerTask.addOnSuccessListener { player -> // The request is successful, and the player information is obtained.
            name.text=player.displayName
            id.text=player.playerId
            level.text=player.level.toString()
            Picasso.get().load(player.iconImageUri).into(avatarImg);

        }.addOnFailureListener { e -> // The player information fails to be obtained.
            if (e is ApiException) {
                Log.e(TAG, "getPlayerInfo failed, status: " + e.statusCode)
            }
        }
    }

    override fun onAchievmentClicked(position: Int) {
        client.reachWithResult(adapter.list[position].id).addOnSuccessListener{
            getAchievments()
        }
    }

    override fun onRefresh() {
        getAchievments()
    }
}

