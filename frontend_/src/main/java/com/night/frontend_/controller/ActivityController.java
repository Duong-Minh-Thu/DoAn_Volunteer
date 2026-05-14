package com.night.frontend_.controller;

import com.night.frontend_.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public String listActivities(Model model) {
        model.addAttribute("activities", activityService.getAllActivities());
        return "activities/list";
    }

    @GetMapping("/{id}")
    public String activityDetail(@PathVariable Long id, Model model) {
        model.addAttribute("activity", activityService.getActivityById(id));
        return "activities/detail";
    }
}
