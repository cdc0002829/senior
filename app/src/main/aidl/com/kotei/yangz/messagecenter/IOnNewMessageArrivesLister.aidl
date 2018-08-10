// IOnNewMessageArrivesLister.aidl
package com.kotei.yangz.messagecenter;
import com.kotei.yangz.messagecenter.entity.MessageEntity;

// Declare any non-default types here with import statements

interface IOnNewMessageArrivesLister {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void onNewMessageArrives(in MessageEntity message);
}
