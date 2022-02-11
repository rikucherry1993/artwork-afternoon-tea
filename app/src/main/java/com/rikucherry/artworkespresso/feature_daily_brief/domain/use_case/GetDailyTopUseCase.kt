package com.rikucherry.artworkespresso.feature_daily_brief.domain.use_case

import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import javax.inject.Inject

class GetDailyTopUseCase@Inject constructor(
    private val dailyBriefRepository: DailyBriefRepository
) {

}