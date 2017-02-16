package coffee.project;

import coffee.IdentifierList;
import coffee.REPL;
import coffee.TokenList;
import coffee.datatypes.*;
import coffee.syntax.Keywords;
import coffee.syntax.Operators;
import java.util.StringTokenizer;
import java.lang.Exception;
/**
 * Safa Emre Dulundu 131044044
 *
 * Lexical Analysis
 *
 * Project1-Part1
 *
 * Created by ft on 10/14/15.
 */
public class Lexer implements REPL.LineInputCallback {
    @Override
    public String lineInput(String line) throws IllegalArgumentException {

        StringTokenizer take = new StringTokenizer(line," ");   /* Bosluklari ayirir ve analize gonderir. */
        while( take.hasMoreTokens() ){
            String temp = take.nextToken();
            Analysis(temp);
        }
        return null;
    }
    /* Inputu analiz ediyor                               */
    private void Analysis(String word) throws IllegalArgumentException {

        TokenList myList = TokenList.getInstance();
        IdentifierList idList = IdentifierList.getInstance();
        StringBuilder str = new StringBuilder();    /* Bu left veya right parantez gorene kadar append edilir.Parantez gelirse kontrol yapilir.*/

        for( int i = 0 ; i < word.length() ; ++i ){
            if( searchParenthesis(word.charAt(i)) ){    /* Left veya right parantez var mi bakar. */

                if( isKeyword(str.toString()) ){        /* append edilen strnin to string ile stringe çevirilir. */
                    myList.addToken(new Keyword(findKeyword(str.toString())));
                }else if( isOperator(str.toString())){
                    myList.addToken(new Operator(findOperator(str.toString())));
                }else if( isValue(str.toString()) ){
                    myList.addToken(new ValueInt(Integer.parseInt(str.toString())));
                }else if( isBool(str.toString()) ){
                    myList.addToken(new ValueBool(Boolean.parseBoolean(str.toString().toLowerCase())));
                }else if( isIdentifier(str.toString()) ) {
                        idList.addIdentifier(str.toString());
                        myList.addToken(new Identifier(str.toString()));
                }else{
                    if( str.toString().length() > 0 ) {
                        System.out.println("Tokens:");
                        TokenList tokens = TokenList.getInstance();
                        for (Token token : tokens.getAllTokens()) {
                            System.out.println(token);
                        }
                        System.out.println("Identifiers:");
                        IdentifierList identifiers = IdentifierList.getInstance();
                        for (String identifier : identifiers.getIdentifiers()) {
                            System.out.println(identifier);
                        }
                        throw new IllegalArgumentException("Illegal Input Exception (" + str.toString() + ") is not Keyword, Operator, Value, Boolean and Identifier !!!\n");
                    }
                }
                myList.addToken(new Operator(findOperator(Character.toString(word.charAt(i))))); /*  Parantez eklenir */

                str = new StringBuilder();      /* Parantez goruldu eklemeler yapildi ve str yeni append islemi icin yenilendi. */
            }else
                str.append(word.charAt(i));
        }
        /* Yukarida Parantez disindakiler icin bir eksik Kontrol yaptigi icin burada son bir kontrol yapilir */
        if( isKeyword(str.toString()) ){
            myList.addToken(new Keyword(findKeyword(str.toString())));
        }else if( isOperator(str.toString())){
            myList.addToken(new Operator(findOperator(str.toString())));
        }else if( isValue(str.toString()) ){
            myList.addToken(new ValueInt(Integer.parseInt(str.toString())));
        }else if( isBool(str.toString()) ){
            myList.addToken(new ValueBool(Boolean.parseBoolean(str.toString().toLowerCase())));
        }else if( isIdentifier(str.toString()) ) {
            idList.addIdentifier(str.toString());
            myList.addToken(new Identifier(str.toString()));
        }else{
            if( str.toString().length() > 0 ) {
                System.out.println("Tokens:");
                TokenList tokens = TokenList.getInstance();
                for (Token token : tokens.getAllTokens()) {
                    System.out.println(token);
                }
                System.out.println("Identifiers:");
                IdentifierList identifiers = IdentifierList.getInstance();
                for (String identifier : identifiers.getIdentifiers()) {
                    System.out.println(identifier);
                }
                throw new IllegalArgumentException("Illegal Input Exception (" + str.toString() + ") is not Keyword, Operator, Value, Boolean and Identifier !!!\n");
            }
        }

        return;
    }
    /* Gelen keyin keyword mu diye bakar                  */
    private boolean isKeyword(String key){
        /* Case sensitive icin key lower case yapilir */
        key = key.toLowerCase();

        if( Keywords.AND.equals(key) || Keywords.APPEND.equals(key) || Keywords.CONCAT.equals(key) ||
                Keywords.DEFFUN.equals(key) || Keywords.ELSE.equals(key) || Keywords.EQUAL.equals(key) ||
                Keywords.FOR.equals(key) || Keywords.IF.equals(key)  || Keywords.NOT.equals(key)   ||
                Keywords.OR.equals(key)  || Keywords.SET.equals(key) || Keywords.THEN.equals(key)  ||
                Keywords.WHILE.equals(key) )
            return true;
        else
            return false;
    }
    /* Gelen string'in operator olup olmadigina bakar     */
    private boolean isOperator(String operator){

        if( Operators.PLUS.equals(operator) || Operators.MINUS.equals(operator) ||
                Operators.SLASH.equals(operator) || Operators.ASTERISK.equals(operator) ||
                Operators.LEFT_PARENTHESIS.equals(operator) || Operators.RIGHT_PARENTHESIS.equals(operator))
            return true;
        else
            return false;
    }
    /* DFA for integer value and negative value           */
    private boolean isValue(String number){

        if( number.length() == 0 )
            return false;

        if( number.length() == 1 && number.charAt(0) == '0' )
            return true;

        if( number.charAt(0) == '-' ) {
            if (number.charAt(1) == '0')
                return false;
            else
                for (int i = 1; i < number.length(); ++i)
                    if (!Character.isDigit(number.charAt(i)))
                        return false;
        }else{
            if( number.charAt(0) == '0' )
                return false;
            else
                for( int i = 0 ; i < number.length(); ++i )
                    if(!Character.isDigit(number.charAt(i)) )
                        return false;
        }

        return true;
    }
    /* DFA for true and false boolean value               */
    private boolean isBool(String key){

        if( key.length() == 0 )
            return false;

        key = key.toLowerCase();

        if( key.equals("true") || key.equals("false") )
            return true;
        else
            return false;
    }
    /* DFA for identifier                                 */
    private boolean isIdentifier(String word){

        if( word.length() == 0 )
            return false;

        for( int i = 0 ; i < word.length(); ++i )
            if( !Character.isLetter(word.charAt(i)) )
                return false;

        return true;
    }
    /* Left ve right parantez arar                        */
    private boolean searchParenthesis(char ch){

        if( ch == '(' || ch == ')' )
            return true;
        else
            return false;
    }
    /* Gelen keyin hangi keyword oldugunu dondurur.       */
    private String findKeyword(String key){

        key = key.toLowerCase();
        String ans = null;

        if( Keywords.AND.equals(key) )
            ans = Keywords.AND;
        else if( Keywords.APPEND.equals(key) )
            ans = Keywords.APPEND;
        else if( Keywords.CONCAT.equals(key) )
            ans = Keywords.CONCAT;
        else if( Keywords.DEFFUN.equals(key) )
            ans = Keywords.DEFFUN;
        else if( Keywords.ELSE.equals(key) )
            ans = Keywords.ELSE;
        else if( Keywords.EQUAL.equals(key) )
            ans = Keywords.EQUAL;
        else if( Keywords.FOR.equals(key) )
            ans = Keywords.FOR;
        else if( Keywords.IF.equals(key) )
            ans = Keywords.IF;
        else if( Keywords.NOT.equals(key) )
            ans = Keywords.NOT;
        else if( Keywords.OR.equals(key) )
            ans = Keywords.OR;
        else if( Keywords.SET.equals(key) )
            ans = Keywords.SET;
        else if( Keywords.THEN.equals(key) )
            ans = Keywords.THEN;
        else if( Keywords.WHILE.equals(key) )
            ans = Keywords.WHILE;

        return ans;
    }
    /* Gelen operatorun hangi operator oldugunu dondurur. */
    private String findOperator(String operator){

        String ans = null;

        if( Operators.PLUS.equals(operator) )
            ans = "PLUS";
        else if( Operators.MINUS.equals(operator) )
            ans = "MINUS";
        else if( Operators.SLASH.equals(operator) )
            ans = "SLASH";
        else if( Operators.ASTERISK.equals(operator) )
            ans = "ASTERISK";
        else if( Operators.LEFT_PARENTHESIS.equals(operator) )
            ans = "LEFT_PARENTHESIS";
        else if( Operators.RIGHT_PARENTHESIS.equals(operator) )
            ans = "RIGHT_PARENTHESIS";

        return ans;
    }
}