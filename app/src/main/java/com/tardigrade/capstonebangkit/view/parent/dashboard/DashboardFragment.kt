package com.tardigrade.capstonebangkit.view.parent.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.*
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.*
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.databinding.FragmentDashboardBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.*
import com.tardigrade.capstonebangkit.view.parent.login.preferences

class DashboardFragment : Fragment() {
    private val viewModel by viewModels<DashboardViewModel> {
        DashboardViewModel.Factory(
            ProfileRepository(ApiConfig.getApiService()),
            requireContext().preferences.getToken()
                ?: error("must have token")
        )
    }
    private var binding: FragmentDashboardBinding? = null

    private var isChildrenLoading = false
    private var isProgressLoading = false
    private var isBadgesLoading = false
    private var isAchievementsLoading = false
    private var isUsagesLoading = false
    private var hasChildren = false
    private var choosenChild: ChildProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getActionBar(activity)?.apply {
            show()
            setTitle(R.string.stat_title)
        }

        showLoading()

        viewModel.apply {
            children.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isChildrenLoading = false

                        setDropdownData(it.data)
                    }
                    is Result.Error -> {
                        isChildrenLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.getChildren()
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isChildrenLoading = true
                    }
                }

                showLoading()
            }

            progress.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isProgressLoading = false

                        setProgressData(it.data)
                    }
                    is Result.Error -> {
                        isProgressLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    choosenChild?.let { child ->
                                        viewModel.getChildData(child)
                                    }
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isProgressLoading = true
                    }
                }

                showLoading()
            }

            badges.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isBadgesLoading = false

                        setBadgesData(it.data)
                    }
                    is Result.Error -> {
                        isBadgesLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    choosenChild?.let { child ->
                                        viewModel.getChildData(child)
                                    }
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isBadgesLoading = true
                    }
                }

                showLoading()
            }

            achievements.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isAchievementsLoading = false

                        setAchievementsData(it.data)
                    }
                    is Result.Error -> {
                        isAchievementsLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    choosenChild?.let { child ->
                                        viewModel.getChildData(child)
                                    }
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isAchievementsLoading = true
                    }
                }

                showLoading()
            }

            usages.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isUsagesLoading = false

                        setUsageData(it.data)
                    }
                    is Result.Error -> {
                        isUsagesLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    choosenChild?.let { child ->
                                        viewModel.getChildData(child)
                                    }
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isUsagesLoading = true
                    }
                }

                showLoading()
            }
        }

        binding?.apply {
            showAllLessons.setOnClickListener {
                showSnackbar(binding?.root as View, getString(R.string.not_available_yet))
            }

            showAllBadgesAchievement.setOnClickListener {
                showSnackbar(binding?.root as View, getString(R.string.not_available_yet))
            }

            showAllUsages.setOnClickListener {
                showSnackbar(binding?.root as View, getString(R.string.not_available_yet))
            }

            childAutocomplete.setOnItemClickListener { adapterView, _, position, _ ->
                val child = adapterView.getItemAtPosition(position) as ChildProfile
                binding?.childAutocomplete?.apply {
                    setText(child.name)
                    choosenChild = child
                    viewModel.getChildData(child)
                    clearFocus()
                }
            }

            achievementsList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            weeklyUsageList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun setUsageData(usages: List<DailyUsage>) {
        val todayUsage = usages[0].duration.toFloat() / (60 * 60 * 1000)
        val lastWeek = usages.takeLast(7)

        binding?.apply {
            todayUsageBody.text = "${todayUsage.format(2)} Jam"
            todayUsageBody.setTextColor(Color.BLACK)
            weeklyUsageList.adapter = StatUsageAdapter(ArrayList(lastWeek))
            weeklyUsageNoData.setVisible(lastWeek.isEmpty())
        }
    }

    private fun setBadgesData(badges: List<Badge>) {
        val latestBadges = badges.sortedByDescending {
            it.acquiredDate.toDateTime()
        }.take(6)

        binding?.apply {
            badgesList.adapter = StatBadgeAdapter(ArrayList(latestBadges))
            badgesNoData.setVisible(latestBadges.isEmpty())
        }
    }

    private fun setAchievementsData(achievements: List<Achievement>) {
        val latestAchievements = achievements.sortedByDescending {
            it.acquiredDate.toDateTime()
        }.take(3)

        binding?.apply {
            achievementsList.adapter = StatAchievementAdapter(ArrayList(latestAchievements))
            achievementsNoData.setVisible(latestAchievements.isEmpty())
        }
    }

    private fun setProgressData(lessons: List<Lesson>) {
        val finished = lessons.filter {
            !it.finishedDate.isNullOrEmpty()
        }.sortedByDescending {
            it.finishedDate?.toDateTime()
        }.take(3)
        val current = lessons.filter {
            it.finishedDate.isNullOrEmpty()
        }.take(3)

        binding?.apply {
            finishedLessonsList.adapter = StatLessonAdapter(ArrayList(finished))
            finishedLessonsNoData.setVisible(finished.isEmpty())

            currentLessonsList.adapter = StatLessonAdapter(ArrayList(current))
            currentLessonsNoData.setVisible(current.isEmpty())
        }
    }

    private fun setDropdownData(children: List<ChildProfile>) {
        binding?.apply {
            if (children.isEmpty()) {
                noChildren.visibility = View.VISIBLE
                chooseChildDropdown.visibility = View.GONE
                statGroup.visibility = View.GONE
                hasChildren = false
            } else {
                noChildren.visibility = View.GONE
                chooseChildDropdown.visibility = View.VISIBLE
                statGroup.visibility = View.VISIBLE
                hasChildren = true
                childAutocomplete.setAdapter(ChooseChildAdapter(requireContext(), children))
            }
        }
    }

    private fun showLoading() {
        val loading = isChildrenLoading || isProgressLoading || isBadgesLoading
                || isAchievementsLoading || isUsagesLoading

        binding?.apply {
            statGroup.setVisible(!loading && hasChildren)
            statLoadingGroup.setVisible(loading)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}