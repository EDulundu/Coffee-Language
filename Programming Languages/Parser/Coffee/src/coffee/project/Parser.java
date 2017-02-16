package coffee.project;

import java.util.*;
import coffee.TokenList;
import coffee.datatypes.*;

/**
 * Created by SafaEmre on 13.12.2015.
 * SAFA EMRE DULUNDU
 * 131044044
 * SHIFT REDUCE PARSER
 */
public class Parser {

    private HashMap<String,String> productionRule [] = new HashMap[13]; // productoin rule key value ili�kisi bak�m�ndan uygun oldugundan secilmi�tir.

    private HashMap<String,String> operator = new HashMap<String, String>(); // K�saca operatorun olup olmad�g�na bakmak ve degeri kullanmak i�in kulland�m.

    private HashMap<String,String> keyword = new HashMap<String, String>();  // K�saca keywordun olup olmad�g�na bakmak ve degeri kullanmak i�in kulland�m.

    private LinkedList<String> someNonTerminal = new LinkedList<String>(); // Ge�ersiz olup olmad�g�na bakarken yada inputta derive edilmemi� kald�ysa
                                                                           // onu kontrol etmek i�in kullan�l�r.
    /* PRODUCTION RULES TANIMLANIR  */
    private void setProductionRule(){

        int i = 0;
        for( int j = 0 ; j < 13; ++j )
            productionRule[j] = new HashMap<String, String>();

        productionRule[i++].put("INPUT", "START");
        productionRule[i].put("STATEMENT","INPUT");
        productionRule[i].put("EXP","INPUT");
        productionRule[i].put("STATEMENTLIST","INPUT");
        productionRule[i++].put("EXPLIST","INPUT");

        /**
         * Production rule lar�n b�yle yaz�lmas�n�n sebebi algoritma kar���kl�g�.
         * Normalde sadece pdf teki gibi yaparsam, rule keyleri string array olarak al�p
         * string i�erisin arama yap�yordum fakat string i�erisinde arama yap�nca bakt�m �ok loop oldu azaltmak i�in
         * b�yle bir y�ntem geli�tirdi.
         */
        productionRule[i].put("( set","STATEMENT");
        productionRule[i].put("( set Id","STATEMENT");
        productionRule[i].put("( set Id EXP","STATEMENT");
        productionRule[i].put("( set Id EXP )","STATEMENT");

        productionRule[i].put("( deffun","STATEMENT");
        productionRule[i].put("( deffun Id","STATEMENT");
        productionRule[i].put("( deffun Id IDLIST","STATEMENT");
        productionRule[i].put("( deffun Id IDLIST EXPLIST","STATEMENT");
        productionRule[i].put("( deffun Id IDLIST EXPLIST )","STATEMENT");

        productionRule[i].put("( if","STATEMENT");
        productionRule[i].put("( if EXPB","STATEMENT");
        productionRule[i].put("( if EXPB then","STATEMENT");
        productionRule[i].put("( if EXPB then EXPLIST","STATEMENT");
        productionRule[i].put("( if EXPB then EXPLIST else","STATEMENT");
        productionRule[i].put("( if EXPB then EXPLIST else EXPLIST","STATEMENT");
        productionRule[i].put("( if EXPB then EXPLIST else EXPLIST )","STATEMENT");

        productionRule[i].put("( if","STATEMENT");
        productionRule[i].put("( if EXPB","STATEMENT");
        productionRule[i].put("( if EXPB EXPLIST","STATEMENT");
        productionRule[i].put("( if EXPB EXPLIST )","STATEMENT");

        productionRule[i].put("( while","STATEMENT");
        productionRule[i].put("( while (","STATEMENT");
        productionRule[i].put("( while ( EXPB","STATEMENT");
        productionRule[i].put("( while ( EXPB )","STATEMENT");
        productionRule[i].put("( while ( EXPB ) EXPLIST","STATEMENT");
        productionRule[i].put("( while ( EXPB ) EXPLIST )","STATEMENT");

        productionRule[i].put("( for","STATEMENT");
        productionRule[i].put("( for (","STATEMENT");
        productionRule[i].put("( for ( Id","STATEMENT");
        productionRule[i].put("( for ( Id EXPI","STATEMENT");
        productionRule[i].put("( for ( Id EXPI EXPI","STATEMENT");
        productionRule[i].put("( for ( Id EXPI EXPI )","STATEMENT");
        productionRule[i].put("( for ( Id EXPI EXPI ) EXPLIST","STATEMENT");
        productionRule[i].put("( for ( Id EXPI EXPI ) EXPLIST )","STATEMENT");

        productionRule[i].put("( defvar","STATEMENT");
        productionRule[i].put("( defvar Id","STATEMENT");
        productionRule[i].put("( defvar Id EXP","STATEMENT");
        productionRule[i++].put("( defvar Id EXP )","STATEMENT");

        productionRule[i].put("EXPI","EXP");
        productionRule[i].put("EXPB","EXP");
        productionRule[i].put("( if","EXP");
        productionRule[i].put("( if EXPB","EXP");
        productionRule[i].put("( if EXPB EXPLIST","EXP");
        productionRule[i].put("( if EXPB EXPLIST EXPLIST","EXP");
        productionRule[i].put("( if EXPB EXPLIST EXPLIST )","EXP");
        productionRule[i].put("( Id","EXP");
        productionRule[i].put("( Id EXPLIST","EXP");
        productionRule[i++].put("( Id EXPLIST )","EXP");

        productionRule[i].put("( +","EXPI");
        productionRule[i].put("( + EXPI","EXPI");
        productionRule[i].put("( + EXPI EXPI","EXPI");
        productionRule[i].put("( + EXPI EXPI )","EXPI");

        productionRule[i].put("( - ","EXPI");
        productionRule[i].put("( - EXPI","EXPI");
        productionRule[i].put("( - EXPI EXPI","EXPI");
        productionRule[i].put("( - EXPI EXPI )","EXPI");

        productionRule[i].put("( * ","EXPI");
        productionRule[i].put("( * EXPI","EXPI");
        productionRule[i].put("( * EXPI EXPI","EXPI");
        productionRule[i].put("( * EXPI EXPI )","EXPI");

        productionRule[i].put("( / ","EXPI");
        productionRule[i].put("( / EXPI","EXPI");
        productionRule[i].put("( / EXPI EXPI","EXPI");
        productionRule[i].put("( / EXPI EXPI )","EXPI");

        productionRule[i].put("Id","EXPI");
        productionRule[i].put("IntegerValue","EXPI");
        productionRule[i].put("( Id","EXPI");
        productionRule[i].put("( Id EXPLIST","EXPI");
        productionRule[i++].put("( Id EXPLIST )","EXPI");

        productionRule[i].put("( and","EXPB");
        productionRule[i].put("( and EXPB","EXPB");
        productionRule[i].put("( and EXPB EXPB","EXPB");
        productionRule[i].put("( and EXPB EXPB )","EXPB");

        productionRule[i].put("( or ","EXPB");
        productionRule[i].put("( or EXPB","EXPB");
        productionRule[i].put("( or EXPB EXPB","EXPB");
        productionRule[i].put("( or EXPB EXPB )","EXPB");

        productionRule[i].put("( not","EXPB");
        productionRule[i].put("( not EXPB","EXPB");
        productionRule[i].put("( not EXPB )","EXPB");

        productionRule[i].put("( equal","EXPB");
        productionRule[i].put("( equal EXPB","EXPB");
        productionRule[i].put("( equal EXPB EXPB","EXPB");
        productionRule[i].put("( equal EXPB EXPB )","EXPB");

        productionRule[i].put("( equal","EXPB");
        productionRule[i].put("( equal EXPI","EXPB");
        productionRule[i].put("( equal EXPI EXPI","EXPB");
        productionRule[i].put("( equal EXPI EXPI )","EXPB");
        productionRule[i].put("Id","EXPB");
        productionRule[i++].put("BinaryValue","EXPB");

        productionRule[i].put("( concat", "EXPLIST");
        productionRule[i].put("( concat EXPLIST","EXPLIST");
        productionRule[i].put("( concat EXPLIST EXPLIST", "EXPLIST");
        productionRule[i].put("( concat EXPLIST EXPLIST )","EXPLIST");
        productionRule[i].put("( append","EXPLIST");
        productionRule[i].put("( append EXPI","EXPLIST");
        productionRule[i].put("( append EXPI EXPLIST","EXPLIST");
        productionRule[i].put("( append EXPI EXPLIST )","EXPLIST");
        productionRule[i].put("LISTVALUE", "EXPLIST");
        productionRule[i].put("EXPLISTELEMENTS","EXPLIST");
        productionRule[i++].put("null", "EXPLIST");

        productionRule[i].put("( VALUES","LISTVALUES");
        productionRule[i].put("( VALUES )","LISTVALUES");
        productionRule[i].put("( )","LISTVALUES");
        productionRule[i++].put("null","LISTVALUES");

        productionRule[i].put("VALUES IntegerValue","VALUES");
        productionRule[i++].put("IntegerValue","VALUES");

        productionRule[i++].put("( IDLISTELEMENTS )","IDLIST");

        productionRule[i].put("null","IDLISTELEMENTS");
        productionRule[i].put("Id","IDLISTELEMENTS");
        productionRule[i++].put("IDLISTELEMENTS Id","IDLISTELEMENTS");

        productionRule[i].put("EXPI","EXPLISTELEMENTS");
        productionRule[i].put("EXPLISTELEMENTS EXPI","EXPLISTELEMENTS");
        productionRule[i].put("null","EXPLISTELEMENTS");
    }

    /* OPERATORLER TANIMLANIR       */
    private void setOperator(){

        operator.put("(","Operator");
        operator.put(")","Operator");
        operator.put("+","Operator");
        operator.put("-","Operator");
        operator.put("*","Operator");
        operator.put("/","Operator");
    }

    /* KEYWORDLER TANIMLANIR        */
    private void setKeyword(){

        keyword.put("and","Keyword");
        keyword.put("or","Keyword");
        keyword.put("not","Keyword");
        keyword.put("equal","Keyword");
        keyword.put("append","Keyword");
        keyword.put("concat","Keyword");
        keyword.put("set","Keyword");
        keyword.put("deffun","Keyword");
        keyword.put("for","Keyword");
        keyword.put("while","Keyword");
        keyword.put("if","Keyword");
        keyword.put("then","Keyword");
        keyword.put("else","Keyword");
    }

    /* BAZI NONTERMINALLER TANIMLANIR */
    private void setSomeNonTerminal(){
        someNonTerminal.add("START");
        someNonTerminal.add("INPUT");
        someNonTerminal.add("STATEMENT");
        someNonTerminal.add("EXP");
        someNonTerminal.add("EXPLIST");
        someNonTerminal.add("STATEMENTLIST");
        someNonTerminal.add("EXPI");
        someNonTerminal.add("EXPB");
        someNonTerminal.add("IDLIST");
    }

    /* Tokenlistten gelen operatorlerin hangisi oldugunu return eder */
    private String findSymbol(String symbol){

        String temp = null;
        if( symbol.equals("LEFT_PARENTHESIS") )
            temp = "(";
        else if( symbol.equals("RIGHT_PARENTHESIS") )
            temp = ")";
        else if( symbol.equals("ASTERISK") )
            temp = "*";
        else if( symbol.equals("MINUS") )
            temp = "-";
        else if( symbol.equals("PLUS") )
            temp = "+";
        else if( symbol.equals("SLASH") )
            temp = "/";
        return temp;
    }

    /* Tokenlistteki tokenlar� alarak queue ya �evirir. */
    private void fillQueue(Queue<String> unscanned){

        int i = 0;
        TokenList myToken = TokenList.getInstance();
        List<Token> myList = myToken.getAllTokens();

        while( i < myList.size() ){
            String type = myList.get(i).getTokenName();

            if( type.equals("Keyword") ){
                Keyword key = (Keyword) myList.get(i);
                unscanned.offer(key.getKeyword());
            }else if( type.equals("Operator") ){
                Operator op = (Operator) myList.get(i);
                unscanned.offer(findSymbol(op.getOperator()));
            }else if( type.equals("VALUE_BOOL") ){
                unscanned.offer("BinaryValue");
            }else if( type.equals("VALUE_INT") ){
                unscanned.offer("IntegerValue");
            }else if( type.equals("IDENTIFIER") ){
                unscanned.offer("Id");
            }
            ++i;
        }
        return;
    }

    /* production ruleda direkt e�le�me olup olmad�g�na bakar */
    private int[] DirectMatch(String head){

        ArrayList<Integer> arr = new ArrayList<Integer>(0);

        for( int i = 0; i < productionRule.length; ++i ) {
            if (productionRule[i].containsKey(head)) {
                arr.add(i);
            }
        }
        int[] position = new int[arr.size()];

        for( int i = 0; i < arr.size(); ++i )
            position[i] = arr.get(i);

        return position;
    }

    /* derive yapar iken parsestacktekileri hepsini al�p string yapar. */
    private String combine(Stack<String> parseStack){

        String link = parseStack.get(0);
        for( int i = 1; i < parseStack.size(); ++i )
            link = link + " " + parseStack.get(i);

        return link;
    }

    /* Eger $ sembol�ne gelmi�sse sona gelmi�tir starta t�retmeye �al���r */
    private String reduceReduce(String all, Stack<String> parseTree){

        String head;
        String reduce;

        for(int i = 0; i < productionRule.length; ++i ){
            if( productionRule[i].containsKey(all) ){

                head = productionRule[i].get(all);
                if( !head.equals("START"))
                    parseTree.push(head);
                reduce = reduceReduce(head, parseTree);
                if( reduce == null )
                    return head;
                else
                    return reduce;
            }
        }

        return null;
    }

    /* Normal gelen lookAheadi alarak t�rerse t�reyeni return eder t�remezsse null return eder */
    private String recursiveReduce(String all,String lookAhead){

        String result, temp, link;
        int[] position = DirectMatch(lookAhead);    // direk e�le�melerin ge�ti�i yer bulunur di�er tabiriyle ambiguity yerleri bulunur.

        /**         AMBIGUITY   AMBIGUITY   AMBIGUITY   AMBIGUITY
         * Ambiguity ile kar��la�t�g�nda mesela ( equal x y ) mesela bu durumda x y bir integer value olsun yada binary value
         * olsun her t�rl� EXPI olmaktad�r. Ambiguity burada ��yle yok ettim s�ras� ile �d lerin oldugu rule lar� bulup reduce
         * ettim ve reduce haliyle �nceki inputlar� string olarak birle�tirerek kontrol ettim eger bir rule 'a gidiyorsa
         * do�ru kabul ettim ve b�yle devam ettim.
         *
         * S�ras� ile denerim ilk hangisi do�ru ise onla devam ederim.
         * (+ x y) mesela bu �rnekte ( , + bunlar direk push edilir. x' gelindi�inde x in reducelar� yani direk e�le�meleri
         * bulunur. EXPI EXPB IDLIST s�ras� ile denerim. "( + EXPI" bakt�m b�yle bir �ey var direk devam et gibi.
         *
         * PROBLEMI bu �ekilde ��zd�m...
         */
        for( int i = 0; i < position.length; ++i ){

            for( int j = 0; j < productionRule.length; ++j ){
                if( productionRule[j].containsKey(lookAhead) ){

                    link = productionRule[position[i]].get(lookAhead);
                    temp = recursiveReduce(all, link);

                    // return edilen null ise production rule dan gelen konulur.
                    // null de�ilse ise recursive den gelen mevcut rule gelir.
                    if( temp == null )
                        result = link;
                    else
                        result = temp;

                    // Her defas�nda reduce haliyle �ncekiler birle�tirilerek kontrol edilir rule da var ise
                    // direk return edilir yok ise aramaya devam eder en genelde yok ise null return edilir.
                    for( int k = 0; k < productionRule.length; ++k )
                        if( productionRule[k].containsKey(all + " " + result) )
                            return result;
                }
            }
        }

        // Eger reduce suz hali �nceki inputlarla birle�tirildi�inde var ise onu kabul eder.
        // ( Id EXPLIST ) bu rule gibiler icin yap�lm��t�r.
        for( int k = 0 ; k < productionRule.length; ++k )
            if( productionRule[k].containsKey(all + " " + lookAhead) )
                return lookAhead;

        return null;
    }

    /*  gelen lookahead direkt e�le�me yok ise direk parse stacke push eder : (shift eder).
        eger direkt e�le�me var ise recursive bir �ekilde reduce yapar.
        (reduce) : yapar iken �ncekiler ile reduce yapilm�i haline birle�tirerek kontrol eder uygun olan� return eder.
        Eger $ sembolune ula�t�ysak en genel bir reduce yapar onuda parseTree stackine kaydeder.
        Input bitti�inde parseStackte null var ise rejected,  null de�ilse Accepted kabul edilmi�tir.
     */
    private String shiftReduceParser(Queue<String> unscanned, ArrayList<String> output){

        Stack<String> parseTree = new Stack<String>();           /* For output       */
        Stack<String> parseStack  = new Stack<String>();         /* Parse Stack      */

        unscanned.offer("$");
        while( unscanned.size() > 0 ){

            String lookAhead = unscanned.poll();
            parseStack.push(lookAhead);
            if( lookAhead.equals("$") ){        /* $ bitti�ini anlamak i�in sonuna konulmu�tur */
                parseStack.pop();
                parseTree.push(combine(parseStack));
                String reduce = reduceReduce(combine(parseStack),parseTree);
                parseStack.clear();
                parseStack.push(reduce);
            }else if( (DirectMatch(lookAhead)).length > 0 ) {   // direk e�le�me lengthi 0 dan kucukse devam eder yani push etmi�tir.
                String[] x = unscanned.toArray(new String[unscanned.size()]); // length 0 dan buyuk ise reduce yap�lcakt�r
                String y = x[0];
                for( int i = 1; i < x.length - 1; ++i)  // Buradaki loop parseTree icin yani outputu pdf te istenen �ekilde g�stermek i�in kullan�lm��t�r
                    y = y + " " + x[i];
                parseTree.push(combine(parseStack) + " " + y);
                lookAhead = parseStack.pop();   // direk e�le�me var ise en son eklenen pop edilir.
                parseStack.push(recursiveReduce(combine(parseStack), lookAhead)); // lookAhead reduce edilebildi�i kadar edilir ve push edilir.
            }
        }

        /* Kabul edildiyse ekrana derivationi basar, kabul edilmediyse sadece REJECTED basar */
        if( parseStack.pop() == null )
            return "FALSE";
        else{
            output.add("START -> " + parseTree.get(parseTree.size() - 1));
            for( int i = parseTree.size() - 2; i >= 0; --i)
                output.add("      -> " + parseTree.get(i));
            output.add("**** ACCEPTED ****");
        }

        /*  Bunlar�n yap�lma sebebi eger inputta parentez i�inde ba�ka parentezli
            ifadeler var ise onun i�inde parse yapabilmek icin return degeri yap�l�r.
         */
        if( !parseTree.get(parseTree.size()-1).equals("INPUT"))
            return "IDLIST";
        String rule = parseTree.get(parseTree.size() - 2);
        if( rule.equals("EXP") ){
            if( parseTree.get(parseTree.size() - 3).equals("( Id EXPLIST )"))
                return "EXPI";
            return parseTree.get(parseTree.size() - 3);
        }
        else if( rule.equals("EXPLIST") )
            return "EXPLIST";
        else if( rule.equals("STATEMENT"))
            return "STATEMENT";
        else
            return "START";
    }

    /* Bu method icerisinde shift reduce parseri �a��rmaktad�r.
    *  Ilk olarak TokenListteki elemanlar� alarak input queueya donusturur.
    *  sonra ")" kapatma parentezi g�rene kadar elemanlar� push eder. ")" bunu gorunce "(" a�may� g�rene kadar pop eder.
    *  b�ylece shift reduce parser i�in inputu olu�turur.
    *  Bu methodun genel yap�lma amac� (+ (+ 2 3) 3 ) gibi parentez i�in parentez oldugu durumlar� sa�lamak i�in yap�ld�.
    * */
    public ArrayList<String> SRParser(){

        Stack<String> tokenStack = new Stack<String>();
        Queue<String> input = new LinkedList<String>();
        ArrayList<String> tokenArray = new ArrayList<String>();
        Queue<String> unscanned = new LinkedList<String>();
        ArrayList<String> output = new ArrayList<String>();

        fillQueue(input);

        while( input.size() > 0 ){

            String head = input.poll();
            if( head.equals(")") ){
                tokenStack.push(")");
                // inputlar� bir stacke basar.
                String temp = tokenStack.pop();
                while( !temp.equals("(") && tokenStack.size() > 0 ){
                    tokenArray.add(temp);
                    temp = tokenStack.pop();
                }
                // stacki al�r tersten arrayliste g�nderir ve d�zg�n input elimize ge�er.
                tokenArray.add("(");
                for( int i = tokenArray.size()-1; i >= 0; --i)
                    unscanned.offer(tokenArray.get(i));
                tokenArray.clear();

                // sonuc FALSE ise semantic hata, FALSE de�ilse bir t�r rule gelir ve stacke push edilir.
                String result = shiftReduceParser(unscanned, output);
                if( result.equals("FALSE") ) {
                    for( int i = 0; i < output.size(); ++i)
                        System.out.println(output.get(i));
                    throw new NoSuchElementException("Boyle bir semantic olamaz");
                }
                else
                    tokenStack.push(result);
            }else
                tokenStack.push(head);

        }
        // Eger parentezler d���nda bir eleman var ise yada kalm��sa son olarak onu bulur ve exception f�rlat�r.
        // Eger bir productionRule daki nonterminal var ise hata vermez
        String search = null;
        boolean find = false;
        for( int i = 0; i < tokenStack.size() && !find; ++i){
            search = tokenStack.get(i);
            if( !someNonTerminal.contains(search) )
                find = true;
        }
        if( find ){
            for( int i = 0; i < output.size(); ++i)
                System.out.println(output.get(i));
            throw new NoSuchElementException("Semantic Fault Fazladan Bir Eleman Kaldi" + search);
        }


        return output;
    }

    /* Constructor set methodlar� �a��r�r initial eder */
    public Parser(){
        setProductionRule();
        setOperator();
        setKeyword();
        setSomeNonTerminal();
    }
}
