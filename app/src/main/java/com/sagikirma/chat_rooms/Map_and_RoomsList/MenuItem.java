package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;


/**
 * Created by sagikirma on 25/05/2015.
 */
public class MenuItem {
  private String id;
  private String name;
  private String icon;
  private boolean joined = false;

  public MenuItem(String id, String name, String icon) {
    this.id = id;
    this.name = name;
    this.icon = icon;
  }

  public String getId() {return this.id;}
  public String getName()      {return this.name;}
  public String getIcon()      {return this.icon;}
  public boolean getJoined() {return joined;}
  public void setJoined(boolean b) {joined=b;}
    

  public void join_leave() {
    joined = !joined;
  }
}
