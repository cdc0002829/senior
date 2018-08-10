// ProcessService.aidl
package com.kotei.yangz.messagecenter;
import com.kotei.yangz.messagecenter.entity.MessageEntity;
import com.kotei.yangz.messagecenter.IOnNewMessageArrivesLister;
import com.kotei.yangz.messagecenter.entity.MessageEntity;
// Declare any non-default types here with import statements

interface ProcessService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void registerListener(IOnNewMessageArrivesLister listener);
   void unregisterListener(IOnNewMessageArrivesLister listener);
}
