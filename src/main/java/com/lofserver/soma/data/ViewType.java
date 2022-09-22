package com.lofserver.soma.data;

import lombok.Getter;

@Getter
public class ViewType {
    private final String liveViewType = "LIVE_VIEW";
    private final String textArrowView = "TEXT_ARROW_VIEW";
    private final String matchResultView = "MATCH_RESULT_VIEW";
    private final String matchScheduleView = "MATCH_SCHEDULE_VIEW";
    private final String userTeamView = "USER_TEAM_VIEW";
    private final String CommunityView = "COMMUNITY_VIEW";
    private final String highlightView = "HIGHLIGHT_VIEW";
    private final String errorView = "ERROR_VIEW";
}
