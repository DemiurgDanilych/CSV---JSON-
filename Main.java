import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
	
	private static final String writableFile = "src\\main\\java\\staff.json";
	
	public static void main(String[] args) {
		
		String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
		String fileName = "src\\main\\java\\data.csv";
		List<Employee> list = parseCSV(columnMapping,fileName);
		String json = listToJson(list);
		
		createFile();
		writeString(json);
	}
	
	private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
		List<Employee> staff = null;
		try (CSVReader reader = new CSVReader(new FileReader(fileName))){
			ColumnPositionMappingStrategy<Employee> strategy =
					new ColumnPositionMappingStrategy<>();
			strategy.setType(Employee.class);
			strategy.setColumnMapping(columnMapping);
			
			CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
					.withMappingStrategy(strategy)
					.build();
			staff = csv.parse();
		} catch (IOException e){
			e.printStackTrace();
		}
		return staff;
	}
	
	private static String listToJson(List<Employee> list) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Type listType = new TypeToken<List<Type>>(){}.getType();
		return gson.toJson(list,listType);
	}
	
	private static void writeString(String json){
		try (FileWriter writer = new FileWriter(writableFile)){
			writer.write(json);
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	private static void createFile(){
		File newDir = new File (writableFile);
		try {
			newDir.createNewFile();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
}
