import android.app.Application;
public class Data extends Application{
    public String name;

    public void set_name(String name){
        this.name=name;
    }
    public String get_nmae(){
        return this.name;
    }
}
