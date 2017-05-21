package fritzscanner;

import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author philipp
 */
public abstract class NetRecord implements Comparator<NetRecord> {

    protected String domain;
    protected int site;
    protected int timel;
    protected String time;

    public NetRecord() {

        domain = "";
        site = 0;
        time = "";
        timel = (int) new Date().getTime();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTimel() {
        return timel;
    }

    public void setTimel(int time) {
        this.timel = time;
    }
}
