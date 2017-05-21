/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fritzscanner;

import java.util.Comparator;

public class NetRecordComparator implements Comparator<NetRecord> {

    @Override
    public int compare(NetRecord b1, NetRecord b2) {
        if (b1.getSite() < b2.getSite()) {
            return 0;
        } else {
            return -1;
        }
    }
}