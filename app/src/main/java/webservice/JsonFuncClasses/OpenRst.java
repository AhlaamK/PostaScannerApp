package webservice.JsonFuncClasses;

/**
 * Created by ahlaam.kazi on 10/15/2017.
 */

public class OpenRst {
    private SETWC_RTNVALS d;

    public class SETWC_RTNVALS{
        public Object __type = null;
        public String ACKNO = null;
        public String ERRMSG = null;
        public String RSTNO = null;
        public String RTNO = null;

    }

    public void setd(SETWC_RTNVALS d) {
        this.d = d;
    }

    public SETWC_RTNVALS getd() {
        return this.d;
    }
}
