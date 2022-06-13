package com.tardigrade.capstonebangkit.data.repository

import android.util.Log
import com.tardigrade.capstonebangkit.data.api.AddChild
import com.tardigrade.capstonebangkit.data.api.ApiService
import com.tardigrade.capstonebangkit.data.api.UpdateChildrenData
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.model.DailyUsage
import com.tardigrade.capstonebangkit.utils.getMidnight
import com.tardigrade.capstonebangkit.utils.isSameDay
import com.tardigrade.capstonebangkit.utils.toDateTime
import java.util.*

class ProfileRepository(private val apiService: ApiService) {
    suspend fun getAvatars() = apiService.getAvatars().avatars

    suspend fun getChildren(token: String) = apiService.getChildren(token).children

    suspend fun getSelf(token: String) = apiService.getSelf(token).user

    suspend fun addChildren(token: String, child: AddChild) =
        apiService.addChildren(token, child).new_child

    suspend fun getChildrenProgress(token: String, child: ChildProfile) =
        apiService.getChildrenLesson(token, child.id).lessons

    suspend fun getChildrenBadges(token: String, child: ChildProfile) =
        apiService.getChildrenBadges(token, child.id).badges

    suspend fun getChildrenAchievements(token: String, child: ChildProfile) =
        apiService.getChildrenAchievements(token, child.id).achievements

    suspend fun getChildrenDailyUsages(token: String, child: ChildProfile): List<DailyUsage> {
        val dailyUsages = mutableListOf<DailyUsage>().apply {
            for (i in 0 downTo -7) {
                val cal = Calendar.getInstance().apply {
                    add(Calendar.DATE, i)
                }

                add(
                    DailyUsage(
                        date = cal.time
                    )
                )

                Log.d(
                    "TAG", "getChildrenDailyUsages: ${
                        DailyUsage(
                            date = cal.time
                        )
                    }"
                )
            }
        }

        val sevenDaysAgo = Calendar.getInstance().apply {
            add(Calendar.DATE, -7)
        }.time
        val usages = apiService.getChildrenUsages(token, child.id).usages.sortedWith(
            compareBy(nullsLast<Date>()) { it.timeEnd?.toDateTime() }
        ).filter {
            it.timeStart.toDateTime()?.after(sevenDaysAgo) ?: false
        }

        for (usage in usages) {
            val timeStart = usage.timeStart.toDateTime() as Date
            val timeEnd = usage.timeEnd?.toDateTime() ?: timeStart

            val sameDay = isSameDay(timeStart, timeEnd)

            if (sameDay) {
                val thatDay = dailyUsages.find {
                    isSameDay(it.date, timeStart)
                } ?: continue

                thatDay.duration += timeEnd.time - timeStart.time
            } else {
                val midnight = getMidnight(timeStart, 1)
                val thatDay = dailyUsages.find {
                    isSameDay(it.date, timeStart)
                } ?: continue

                thatDay.duration += midnight - timeStart.time

                val theOtherDay = dailyUsages.find {
                    isSameDay(it.date, timeEnd)
                } ?: continue

                theOtherDay.duration += timeEnd.time - midnight
            }
        }

        return dailyUsages
    }

    suspend fun updateChild(token: String, child: ChildProfile) {
        apiService.updateChild(token, child.id, UpdateChildrenData(child.level))
    }
}