package utils;

import java.util.List;

import org.testng.annotations.DataProvider;

import com.demo.utils.JsonDataReader;

public class DataProviderUtil {

    private static final String DOG_BREEDS_FILE = "src/test/resources/testData/dogBreeds.json";

    @DataProvider(name = "dogBreedsData")
    public static Object[][] getDogBreeds() {
        List<String> breeds = JsonDataReader.getListFromJson(DOG_BREEDS_FILE, "breeds");
        return breeds.stream().map(breed -> new Object[]{breed}).toArray(Object[][]::new);
    }
    
}
