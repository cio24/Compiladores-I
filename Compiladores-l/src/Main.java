import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Main {
		
		public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(
			    new InputStreamReader(
			        new FileInputStream(args[args.length-1]),
			        Charset.forName("UTF-8")));
			int c;
			while((c = reader.read()) != -1) {
			  char character = (char) c;
			  
			  if (character==' ')
			  System.out.print("\\e->");
			  else
			  if (character=='\t')
				  System.out.print("\\t->");
			  else
			  if (character=='\n')
				System.out.print("\\n->");
			  else System.out.print(character);
			  
	}
	}
}
