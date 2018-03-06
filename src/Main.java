import com.sun.tools.javadoc.Start;
import view.StartView;

import java.io.IOException;

public class Main {

    public static void main(String[] args){

        StartView s = new StartView();
        try {
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
