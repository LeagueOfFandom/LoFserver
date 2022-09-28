package com.lofserver.soma.data;

import com.lofserver.soma.controller.v1.response.match.MatchViewObject;
import com.lofserver.soma.entity.MatchEntity;
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

    public String getViewType(Object object) {
        if (object instanceof MatchViewObject){
            if(((MatchViewObject) object).getStatus().equals("not_started"))
                return matchScheduleView;
            else if (((MatchViewObject) object).getStatus().equals("finished"))
                return matchResultView;
            else
                return liveViewType;
        } else {
            return errorView;
        }
    }
}
