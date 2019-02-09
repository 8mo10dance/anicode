import java.text.SimpleDateFormat;
import java.util.Date;

public class History {
    private final int ep;
    private final Date createdAt;

    public History(int ep, Date createdAt) {
        this.ep = ep;
        this.createdAt = createdAt;
    }

    public int getEp() {
        return this.ep;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(createdAt) + ":" + ep;
    }

    public static final Date defaultCreatedAt() {
        var d = new Date();
        d.setTime(0);

        return d;
    }
}
