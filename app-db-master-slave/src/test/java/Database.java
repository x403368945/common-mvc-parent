import com.utils.util.FPath;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author 谢长春 2018/12/16
 */
public class Database {
    @SneakyThrows
    public static void main(String[] args) {
        String dir = System.getProperty("user.dir"); // "D:/git-repository/common-mvc-server/";
        //        @Cleanup FileWriter writer = new FileWriter(dir + "common-mvc-server/src/main/resources/database.sql");
        @Cleanup final OutputStream writer = Files.newOutputStream(Paths.get(dir, "app-db-master-slave/src/main/resources/database.sql"));
        final List<String> pkgs = Arrays.asList(
                "app-db-master-slave"
        );
        pkgs.forEach(pkg -> {
            try {
                writer.write(FPath.of(dir, pkg, "/src/main/resources/db/db.sql").readByte());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        pkgs.forEach(pkg -> {
            try {
                writer.write(FPath.of(dir, pkg, "/src/main/resources/db/db-view.sql").readByte());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        pkgs.forEach(pkg -> {
            try {
                writer.write(FPath.of(dir, pkg, "/src/main/resources/db/db-init.sql").readByte());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        writer.flush();
    }
}
