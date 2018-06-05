package oop.ex6.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


import oop.ex6.parser.SyntaxException;
import oop.ex6.scope.MethodException;
import oop.ex6.parser.MainParse;
import oop.ex6.variables.VariableException;

/**
 * Sjavac is a simplified java compiler. The program receives a single path to
 * a text-based file as a parameter and will run an follows:
 * 				java oop.ex6.main.Sjavac sourceFileName
 * The program will exit in the following cases:
 * 1. If there were no compilation errors, the program will print 0
 * 2. If there was a compilation error, the program will print 1
 * 3. If there was a problem reading the file, the program will print 2
 * If any errors occur, they will be stored in the exception that is thrown.
 * 
 * @author Alex Goft/Eliav Shames
 */
public class Sjavac {
	private static final int CORRECT_LENGTH = 1;
	private static final int NOT_COMPILED_EXIT = 1;
	private static final int COMPILED_EXIT = 0;
	private static final int SOURCE_FILE = 0;
	private static final int IO_ERROR_EXIT = 2;

    /**
     * Converts the lines in a text file to an array list and sends
     * The ArrayList to the parser for inspection.
     * Any exceptions thrown by the parser are sent to the errorHandling
     * Function.
     * @param args The program arguments
     */
	public static void main(String[] args) {
		//Check legality of args.
		checkArgs(args);
		File srcFile= new File(args[SOURCE_FILE]);
		if(!srcFile.exists()){
			errorHandling(IO_ERROR_EXIT);
		}

		//Create array of the lines of the file.
		ArrayList<String> lines = new ArrayList<>();
		Scanner scanner= null;
		try {
			scanner= new Scanner(srcFile);
		}
		catch(FileNotFoundException fe) {
			errorHandling(IO_ERROR_EXIT);
		}
		while (scanner.hasNext()){
			lines.add(scanner.nextLine());
		}

		//Try to compile.
		try {
			MainParse.compile(lines);
			errorHandling(COMPILED_EXIT);
		} catch (SyntaxException | VariableException | MethodException e) {
			errorHandling(NOT_COMPILED_EXIT);
		}
	}

	//Checks the legality of the args, given an array of args.
	private static void checkArgs(String[] args){
		if ((!(args.length == CORRECT_LENGTH)) ||
                (args[SOURCE_FILE] == null)){
			errorHandling(IO_ERROR_EXIT);
		}
	}
	//Handle different types of errors.
	private static void errorHandling(int errorType){
		if (errorType == COMPILED_EXIT)
			System.out.println(0);
		if (errorType == IO_ERROR_EXIT)
			System.out.println(2);
		if (errorType == NOT_COMPILED_EXIT)
			System.out.println(1);
		//System.exit(errorType);
	}
}