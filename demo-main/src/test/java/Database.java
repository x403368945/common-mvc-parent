import com.utils.util.FPath;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author 谢长春 2018/12/16
 */
public class Database {
    @SneakyThrows
    public static void main(String[] args) {
        String dir = System.getProperty("user.dir"); // "D:/git-repository/common-mvc-parent/";
//        @Cleanup FileWriter writer = new FileWriter(dir + "demo-main/src/main/resources/database.sql");
        {
            @Cleanup final OutputStream writer = Files.newOutputStream(Paths.get(dir, "demo-main/src/main/resources/database.sql"));
            writer.write(FPath.of(dir, "demo-main/src/main/resources/db/db.sql").readByte());
            writer.write(FPath.of(dir, "demo-main/src/main/resources/db/db-init.sql").readByte());
            writer.write(FPath.of(dir, "demo-main/src/main/resources/db/db-view.sql").readByte());
            writer.write(FPath.of(dir, "demo-service/src/main/resources/db-demo-service.sql").readByte());
            writer.flush();
        }
    }
}
