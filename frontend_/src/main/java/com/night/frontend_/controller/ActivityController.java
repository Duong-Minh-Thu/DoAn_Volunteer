package com.night.frontend_.controller;

import com.night.frontend_.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.night.frontend_.model.ActivityRequest;
import com.night.frontend_.model.Activity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("activity", new Activity());
        return "activities/form";
    }

    @PostMapping("/new")
    public String createActivity(@ModelAttribute ActivityRequest request, Model model) {
        boolean success = activityService.createActivity(request);
        if (success) {
            return "redirect:/activities";
        } else {
            model.addAttribute("error", "Lưu hoạt động thất bại, vui lòng kiểm tra lại thông tin!");
            Activity activity = new Activity();
            activity.setTitle(request.getTitle());
            activity.setDescription(request.getDescription());
            activity.setStartDate(request.getStartDate());
            activity.setEndDate(request.getEndDate());
            activity.setLocation(request.getLocation());
            activity.setPoints(request.getPoints());
            activity.setMaxParticipants(request.getMaxParticipants());
            model.addAttribute("activity", activity);
            return "activities/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Activity activity = activityService.getActivityById(id);
        if (activity == null) {
            return "redirect:/activities";
        }
        model.addAttribute("activity", activity);
        return "activities/form";
    }

    @PostMapping("/{id}/edit")
    public String updateActivity(@PathVariable Long id, @ModelAttribute ActivityRequest request, Model model) {
        boolean success = activityService.updateActivity(id, request);
        if (success) {
            return "redirect:/activities";
        } else {
            model.addAttribute("error", "Cập nhật hoạt động thất bại, vui lòng kiểm tra lại thông tin!");
            Activity activity = new Activity();
            activity.setId(id);
            activity.setTitle(request.getTitle());
            activity.setDescription(request.getDescription());
            activity.setStartDate(request.getStartDate());
            activity.setEndDate(request.getEndDate());
            activity.setLocation(request.getLocation());
            activity.setPoints(request.getPoints());
            activity.setMaxParticipants(request.getMaxParticipants());
            model.addAttribute("activity", activity);
            return "activities/form";
        }
    }
}
