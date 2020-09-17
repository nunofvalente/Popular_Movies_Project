package com.example.android.popularmovies.model;

import java.util.List;

@SuppressWarnings("ALL")
public class TrailerModel {

    /**
     * id : 297761
     * results : [{"id":"58f2193c9251412ff40098ae","iso_639_1":"en","iso_3166_1":"US","key":"PLLQK9la6Go","name":"Suicide Squad - Comic-Con First Look [HD]","site":"YouTube","size":1080,"type":"Trailer"},{"id":"58f218d69251412fa90094e1","iso_639_1":"en","iso_3166_1":"US","key":"CmRih_VtVAs","name":"Suicide Squad - Official Trailer 1 [HD]","site":"YouTube","size":1080,"type":"Trailer"},{"id":"58f21965c3a3682eb6009a6a","iso_639_1":"en","iso_3166_1":"US","key":"5AwUdTIbA8I","name":"Suicide Squad - Blitz Trailer [HD]","site":"YouTube","size":1080,"type":"Trailer"},{"id":"5f163d571d78f2003236ed6c","iso_639_1":"en","iso_3166_1":"US","key":"5okgENDeCfM","name":"Suicide Squad - TV Spot 3 [HD]","site":"YouTube","size":1080,"type":"Teaser"},{"id":"58403fcb9251417f7900b892","iso_639_1":"en","iso_3166_1":"US","key":"JsbG97hO6lA","name":"SUICIDE SQUAD Promo Trailer - Harley Quinn Therapy (Margot Robbie - 2016)","site":"YouTube","size":720,"type":"Featurette"},{"id":"58f219dc9251412fa900959c","iso_639_1":"en","iso_3166_1":"US","key":"m0Xb9BhfVjY","name":"Suicide Squad - Official Comic-Con Soundtrack Remix [HD]","site":"YouTube","size":1080,"type":"Trailer"}]
     */

    private int id;
    private List<ResultsBean> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * id : 58f2193c9251412ff40098ae
         * iso_639_1 : en
         * iso_3166_1 : US
         * key : PLLQK9la6Go
         * name : Suicide Squad - Comic-Con First Look [HD]
         * site : YouTube
         * size : 1080
         * type : Trailer
         */

        private String id;
        private String iso_639_1;
        private String iso_3166_1;
        private String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
