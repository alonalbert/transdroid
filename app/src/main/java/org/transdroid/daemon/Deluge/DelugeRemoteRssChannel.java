package org.transdroid.daemon.Deluge;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.transdroid.core.gui.remoterss.data.RemoteRssChannel;

/**
 * Deluge implementation of RemoteRssChannel.
 *
 * @author alonalbert
 */
class DelugeRemoteRssChannel extends RemoteRssChannel {

  DelugeRemoteRssChannel(int id, String name, String link, long lastUpdated) {
    this.id = id;
    this.name = name;
    this.link = link;
    this.lastUpdated = lastUpdated;
    items = new ArrayList<>();
  }

  public DelugeRemoteRssChannel(Parcel in) {
    id = in.readInt();
    name = in.readString();
    link = in.readString();
    lastUpdated = in.readLong();

    items = new ArrayList<>();
    in.readList(items, DelugeRemoteRssItem.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(link);
    dest.writeLong(lastUpdated);
    dest.writeList(items);
  }

  public static final Parcelable.Creator<DelugeRemoteRssChannel> CREATOR = new Parcelable.Creator<DelugeRemoteRssChannel>() {
    public DelugeRemoteRssChannel createFromParcel(Parcel in) {
      return new DelugeRemoteRssChannel(in);
    }

    public DelugeRemoteRssChannel[] newArray(int size) {
      return new DelugeRemoteRssChannel[size];
    }
  };

}