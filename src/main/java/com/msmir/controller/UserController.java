package com.msmir.controller;

import com.msmir.entity.user.User;
import com.msmir.entity.user.UserSettings;
import com.msmir.entity.user.UserStats;
import com.msmir.service.UserService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

  private static final int AVATAR_SIZE = 256;

  @Autowired
  private UserService userService;

  @Value("${upload.path}")
  private String uploadPath;

  @GetMapping(value = "/data/currentUsername", produces = "application/json")
  public String getUser(){
    User user = userService.getCurrentUser();
    if(user != null){
      return user.getUsername();
    }else{
      return "";
    }
  }

  @GetMapping(value = "/data/currentUserStats", produces = "application/json")
  public UserStats getUserStats(){
    User user = userService.getCurrentUser();
    if(user != null && user.getStats() != null){
      return user.getStats();
    }else{
      return new UserStats();
    }
  }

  @GetMapping(value = "/data/currentUserSettings", produces = "application/json")
  public UserSettings getUserSettings() {
    User user = userService.getCurrentUser();
    if (user != null && user.getSettings() != null) {
      return user.getSettings();
    } else {
      return new UserSettings();
    }
  }

  @PutMapping(value = "/data/currentUserSettings")
  public void updateUserSettings(@RequestBody UserSettings newSettings){
    User user = userService.getCurrentUser();
    if(user != null){
      if(newSettings.isValid()) {
        user.setSettings(newSettings);
        userService.updateUser(user);
      }
    }
  }

  @GetMapping(value = "/data/currentUserAvatar", produces = "application/json")
  public String getUserAvatar(){
    return userService.getUserAvatar(userService.getCurrentUser());
  }

  @PostMapping("/profile")
  private String addAvatar(@RequestParam("file") MultipartFile file, Model model) throws IOException {
    User user = userService.getCurrentUser();
    if(user != null && file != null) {
      String avatarsPath = uploadPath + "/avatars";
      File avatarsDir = new File(avatarsPath);
      if (avatarsDir.exists() || avatarsDir.mkdir()) {
        String filename = user.getUsername() + ".jpg";
        BufferedImage image = ImageIO.read(file.getInputStream());
        image = Scalr.resize(image, Mode.FIT_EXACT, AVATAR_SIZE, AVATAR_SIZE);

        File resultFile = new File(avatarsPath + "/" + filename);
        ImageIO.write(image, "jpg", resultFile);

        user.setAvatar("avatars/" + filename);
        userService.updateUser(user);
      }
    }

    userService.addUserAvatar(model);
    return "profile";
  }
}
