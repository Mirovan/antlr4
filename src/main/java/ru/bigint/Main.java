package ru.bigint;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import ru.bigint.model.Diagram;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {


        try {
            //Reading the DSL script
            InputStream is = ClassLoader.getSystemResourceAsStream("sample.txt");

            //Loading the DSL script into the ANTLR stream.
            CharStream cs = CharStreams.fromStream(is);

            //Passing the input to the lexer to create tokens
            ArchicodeLexer lexer = new ArchicodeLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);


            //Passing the tokens to the parser to create the parse trea.
            ArchicodeParser parser = new ArchicodeParser(tokens);

            //Semantic model to be populated
            Diagram diagram = new Diagram();

            //Adding the listener to facilitate walking through parse tree.
            parser.addParseListener(new CustomArchicodeBaseListener(diagram));

            //invoking the parser.
            parser.diagram();

            diagram.draw();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
