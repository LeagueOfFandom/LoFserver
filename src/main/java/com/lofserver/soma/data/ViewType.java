package com.lofserver.soma.data;

import com.lofserver.soma.controller.v1.response.community.CommunityViewObject;
import com.lofserver.soma.controller.v1.response.community.HighLightViewObject;
import com.lofserver.soma.controller.v1.response.match.sub.MatchViewObject;
import lombok.Getter;
import org.json.simple.JSONObject;

@Getter
public class ViewType {
    private final String liveViewType = "LIVE_VIEW";
    private final String textArrowView = "TEXT_ARROW_VIEW";
    private final String matchResultView = "MATCH_RESULT_VIEW";
    private final String matchScheduleView = "MATCH_SCHEDULE_VIEW";
    private final String userTeamView = "USER_TEAM_VIEW";
    private final String communityView = "COMMUNITY_VIEW";
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
        } else if(object instanceof JSONObject){
            return textArrowView;
        } else if(object instanceof HighLightViewObject){
            return highlightView;
        } else if(object instanceof CommunityViewObject){
            return communityView;
        } else {
            return errorView;
        }
    }
}
