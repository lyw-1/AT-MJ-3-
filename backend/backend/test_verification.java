import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestVerification {
    public static void main(String[] args) throws Exception {
        // 检查已修复的测试文件
        String[] testFiles = {
            "src/test/java/com/mold/digitalization/controller/AuthControllerTest.java",
            "src/test/java/com/mold/digitalization/controller/AdminAuthControllerTest.java", 
            "src/test/java/com/mold/digitalization/controller/UserRoleControllerTest.java",
            "src/test/java/com/mold/digitalization/controller/UserControllerTest.java"
        };
        
        boolean allFixed = true;
        
        for (String testFile : testFiles) {
            File file = new File(testFile);
            if (!file.exists()) {
                System.out.println("❌ 文件不存在: " + testFile);
                allFixed = false;
            }    continue;
            }
            
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            boolean hasComponentScan = false;
            boolean hasFilterType = false; 
            boolean hasOperationLogAspect = false;
            boolean hasExcludeFilters = false;
            
            for (String line : lines) {
                if (line.contains("import org.springframework.context.annotation.ComponentScan")) {
                    hasComponentScan = true;
                }
                if (line.contains("import org.springframework.context.annotation.FilterType")) {
                    hasFilterType = true;
                }
                if (line.contains("import com.mold.digitalization.aspect.OperationLogAspect")) {
                    hasOperationLogAspect = true;
                }
                if (line.contains("excludeFilters = @ComponentScan.Filter")) {
                    hasExcludeFilters = true;
                }
            }
            
            if (hasComponentScan && hasFilterType && hasOperationLogAspect && hasExcludeFilters) {
                System.out.println("✅ " + testFile + " - 修复完成");
            } else {
                System.out.println("❌ " + testFile + " - 修复不完整");
                System.out.println("   ComponentScan: " + hasComponentScan);
                System.out.println("   FilterType: " + hasFilterType);
                System.out.println("   OperationLogAspect: " + hasOperationLogAspect);
                System.out.println("   ExcludeFilters: " + hasExcludeFilters);
                allFixed = false;
            }
        }
        
        if (allFixed) {
            System.out.println("\n🎉 所有测试类修复完成！");
        } else {
            System.out.println("\n⚠️ 部分测试类需要进一步修复！");
        }
    }
}