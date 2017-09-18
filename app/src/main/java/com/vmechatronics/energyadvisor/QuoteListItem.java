package com.vmechatronics.energyadvisor;

/**
 * Created by vmplapp on 10/8/17.
 */

public class QuoteListItem {

    private String tvqid;
    private String tvdate;

    public QuoteListItem(String tvqid, String tvdate) {
        this.tvqid = tvqid;
        this.tvdate = tvdate;
    }

    public String getTvqid() {
        return tvqid;
    }

  /*  public void setTvqid(String tvqid) {
        this.tvqid = tvqid;
    }*/

    public String getTvdate() {
        return tvdate;
    }

  /*  public void setTvdate(String tvdate) {
        this.tvdate = tvdate;
    }*/
}
