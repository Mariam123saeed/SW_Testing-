package end_to_end_testing;
import org.testng.annotations.DataProvider;


public class DataProviders {


    @DataProvider(name = "users")
    public Object[][] users(){
        return new Object[][] {
                {"problem_user", "secret_sauce"},
                {"standard_user", "secret_sauce"},
        };
    }
}
