import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Main {
		
		public static void main(String[] args) throws IOException {
		ReturnableBufferedReader reader = new ReturnableBufferedReader(
			    new InputStreamReader(
			        new FileInputStream(args[args.length-1]),
			        Charset.forName("UTF-8")));
			int c;
			int i=0;
			while(i<30) {
			  c=reader.readNextCharacter();
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
			  i++;
		
	}
			reader.returnLastCharacter();
			System.out.println();
			System.out.print("Siguiente tanda");
			System.out.println();

			i=0;
			while(i<18) {
				  c=reader.readNextCharacter();
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
				  i++;
			
		}
			reader.returnLastCharacter();
			System.out.println();
			System.out.print("Siguiente tanda");
			System.out.println();

			i=0;
			while(i<10) {
				  c=reader.readNextCharacter();
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
				  i++;
			
		}
			reader.close();
	}
}
