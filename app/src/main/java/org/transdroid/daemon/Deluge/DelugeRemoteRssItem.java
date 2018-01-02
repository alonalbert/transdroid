package org.transdroid.daemon.Deluge;

import android.os.Parcel;
import android.os.Parcelable;
import org.transdroid.core.gui.remoterss.data.RemoteRssItem;

/**
 * Deluge implementation of RemoteRssItem.
 *
 * @author alonalbert
 */
class DelugeRemoteRssItem extends RemoteRssItem {

  public DelugeRemoteRssItem(Parcel in) {

  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

  }

  public static final Parcelable.Creator<DelugeRemoteRssItem> CREATOR = new Parcelable.Creator<DelugeRemoteRssItem>() {
    public DelugeRemoteRssItem createFromParcel(Parcel in) {
      return new DelugeRemoteRssItem(in);
    }

    public DelugeRemoteRssItem[] newArray(int size) {
      return new DelugeRemoteRssItem[size];
    }
  };


}
