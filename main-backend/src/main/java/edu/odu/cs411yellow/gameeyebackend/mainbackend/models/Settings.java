package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationSettings;
import org.springframework.data.annotation.PersistenceConstructor;


public class Settings {
    private NotificationSettings notificationSettings;

    @PersistenceConstructor
    public Settings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    public Settings() {
        this.notificationSettings = new NotificationSettings();
    }

    public NotificationSettings getNotifications() {
        return this.notificationSettings;
    }

    public void setNotifications(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

}
