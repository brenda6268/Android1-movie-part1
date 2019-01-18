package com.br.gridviewmovie;

import org.json.JSONException;
import org.json.JSONObject;

public class GridItem {
    private String image;
    private String title;
    private String averrate;
    private String overview;
    private String releasedate;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {        return title;    }

    public void setTitle(String title) {        this.title = title;    }

    public String getAverrate() {        return averrate;    }

    public void setAverrate(String averrate) {        this.averrate = averrate;    }

    public String getOverview() {        return overview;    }

    public void setOverview(String overview) {        this.overview = overview;    }

    public String getReleasedate() {        return releasedate;    }

    public void setReleasedate(String releasedate) {        this.releasedate = releasedate;    }

    //want to write another Json to store data of one movie. But haven't done.
    public String toJSONString()
    {   JSONObject obj=new JSONObject();
        try
        {  // obj.put("id",id);
            obj.put("title",title);
            obj.put("banner",image);
           // obj.put("releasedate",releasedate);
           // obj.put("overview",overview);
           // obj.put("averrate",averrate);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
