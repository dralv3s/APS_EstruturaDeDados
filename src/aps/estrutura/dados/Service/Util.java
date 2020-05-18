package aps.estrutura.dados.Service;
import aps.estrutura.dados.Model.Tarefa;

import java.io.*;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Util {

    public static Scanner scanInput = new Scanner(System.in);

    public void CriarTarefa(List<Tarefa> tarefas){

        try{
            String verify, putData;

            File file = new File("FileDB.txt");

            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Tarefa tarefa:tarefas) {
                bw.append(tarefa.Index + ";" + tarefa.NomeTarefa + ";" + tarefa.DataEntrega + ";" + tarefa.Status);
                bw.newLine();
            }

            bw.flush();
            bw.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /* Rotina auxiliar para gerar barra de loading */
    public static void Loading(int intervaloMS, String OutputMsg)  {

        try {
            WriteNewLineConsole("\n"+ OutputMsg);
            System.out.print("|==                   |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|====                 |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|======               |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|========             |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|==========           |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|============         |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|==============       |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|================     |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|==================   |\r");
            Thread.sleep(intervaloMS);
            System.out.print("|=====================|\n");
            Thread.sleep(intervaloMS);

            System.out.print("\n");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Rotina auxiliar para escrever e pular uma linha */
    public static void WriteNewLineConsole(String msg){
        System.out.print(msg + "\n");
    }

    /* Rotina auxiliar para escrever novas linhas no console */
    public static void WriteConsole(String msg){
        System.out.print(msg);
    }

    public void ShowMenu(){

        WriteNewLineConsole("Bem-Vindo!");
        WriteNewLineConsole("Selecione uma opção para prosseguir.");
        WriteNewLineConsole("1 - Ver lista de tarefas.");
        WriteNewLineConsole("2 - Incluir nova tarefa.");
        WriteNewLineConsole("3 - Atualizar status da tarefa.");
        WriteNewLineConsole("4 - Excluir tarefa");
        WriteNewLineConsole("5 - Finalizar Aplicação");

        FluxoHandler(ReadInteger());

    }

    /* Rotina para finalizar aplicação sem erros */
    public void ExitAplication(){
        WriteNewLineConsole("\nAplicação Encerrada...\nPressione enter para sair...");
        scanInput.nextLine();
        Loading(500, "Saindo...");
    }

    public int ReadInteger(){

        int tryCountLimit = 5;
        int tryCount = 1;

        String userInput = scanInput.nextLine();

        while ((!isInteger(userInput)) && (tryCount < tryCountLimit) ){
            WriteNewLineConsole("\nFormato incorreto de entrada, digite apenas numeros inteiros.\nTente Novamente!");
            userInput = scanInput.nextLine();
            tryCount ++;
        }

        if (tryCount >= tryCountLimit){
            WriteNewLineConsole("\nNumero maximo de tentativas atingido, saindo...");
            ExitAplication();
            WriteNewLineConsole("\r");
            return 0;
        }
        return Integer.parseInt(userInput);
    }

    /* Rotina auxiliar para checar se entrada de usuario é valida e pode ser convertida para integer */
    public static boolean isInteger(String input){
        try
        {
            Integer.parseInt(input);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public void VisualisarTarefa(){
        try{
            String checkLine;

            File file = new File("FileDB.txt");

            if (!file.exists()){
                WriteNewLineConsole("Não existem tarefas cadastradas!");
            }else{
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                while((checkLine = br.readLine()) != null){

                    String[] splitLine = checkLine.split(";", 3);

                    WriteNewLineConsole("Tarefa: " + splitLine[0]);
                    WriteNewLineConsole("Data de entrega: " + splitLine[1]);
                    WriteNewLineConsole("Status: " + GetStatus(splitLine[2]));
                    WriteNewLineConsole("\n");
                }
                br.close();
            }

            WriteNewLineConsole("Pressione enter para retornar ao menu principal.");
            scanInput.nextLine();
            ShowMenu();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void IncluiTarefa() {

        try {

            List<Tarefa> list = new ArrayList<Tarefa>();

            Boolean done = false;
            String sDataTarefa = "";

            Integer inclurNova = 0;

            Integer lastIndex = GetLastIndex();

            while (!done) {
                Tarefa tarefa = new Tarefa();

                WriteNewLineConsole("Digite o titulo da tarefa.");
                tarefa.NomeTarefa = scanInput.nextLine();

                WriteNewLineConsole("Digite a data de termino da tarefa no formato DD/MM/YYYY HH:MM:SS");
                sDataTarefa = scanInput.nextLine();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

                LocalDate dateParsed = LocalDate.parse(sDataTarefa, formatter);

                tarefa.DataEntrega = dateParsed;

                tarefa.Status = 0;

                lastIndex = lastIndex +=1;

                tarefa.Index = lastIndex;

                list.add(tarefa);

                WriteNewLineConsole("Digite 1 para incluir outra tarefa");
                WriteNewLineConsole("Digite 2 para finalizar");
                inclurNova = ReadInteger();

                if (inclurNova != 1){
                    done = true;
                }
            }

            CriarTarefa(list);
            ShowMenu();

        } catch (Exception e) {
            WriteNewLineConsole("Data digitada esta em formato incorreto");
            IncluiTarefa();
        }
    }

    public void AtualizarTarefa(){

        try{

            ClearConsole();

            File file = new File("FileDB.txt");

            if (!file.exists()){
                WriteNewLineConsole("Não existem tarefas cadastradas!");
            }else{

                WriteNewLineConsole("Digite o numero da tarefa que deseja atualizar.");

                Integer indiceTarefa = ReadInteger();

                WriteNewLineConsole("Digite o status para atualizar");
                WriteNewLineConsole("0 - Pendente");
                WriteNewLineConsole("1 - Concluido");
                WriteNewLineConsole("2 - Atrasado");

                Integer stsUpdate = ReadInteger();

                String checkLine;

                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                List<Tarefa> list = new ArrayList<Tarefa>();


                while((checkLine = br.readLine()) != null){

                    Tarefa tarefa = new Tarefa();
                    String[] splitLine = checkLine.split(";", 4);

                    tarefa.Index = Integer.parseInt(splitLine[0]);
                    tarefa.NomeTarefa = splitLine[1];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/mm/dd");

                    LocalDate dateParsed = LocalDate.parse(splitLine[2]);

                    tarefa.DataEntrega = dateParsed;

                    if (Integer.parseInt(splitLine[0]) == indiceTarefa){
                        tarefa.Status = stsUpdate;
                    }else{
                        tarefa.Status = Integer.parseInt(splitLine[3]);
                    }


                    list.add(tarefa);
                }

                br.close();


                file.delete();

                CriarTarefa(list);
                ShowMenu();
            }

        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ExcluirTarefa(){

        try{

            ClearConsole();

            File file = new File("FileDB.txt");

            if (!file.exists()){
                WriteNewLineConsole("Não existem tarefas cadastradas!");
            }else{

                WriteNewLineConsole("Digite o numero da tarefa que deseja excluir.");

                Integer indiceTarefa = ReadInteger();

                String checkLine;

                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                List<Tarefa> list = new ArrayList<Tarefa>();

                while((checkLine = br.readLine()) != null){

                    String[] splitLine = checkLine.split(";", 4);

                    if (Integer.parseInt(splitLine[0]) != indiceTarefa){

                        Tarefa tarefa = new Tarefa();

                        tarefa.Index = Integer.parseInt(splitLine[0]);
                        tarefa.NomeTarefa = splitLine[1];

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/mm/dd");
                        LocalDate dateParsed = LocalDate.parse(splitLine[2]);
                        tarefa.DataEntrega = dateParsed;

                        tarefa.Status = Integer.parseInt(splitLine[3]);

                        list.add(tarefa);
                    }
                }

                br.close();

                file.delete();

                CriarTarefa(list);

                ShowMenu();
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FluxoHandler(Integer escolha) {

        ClearConsole();

        switch (escolha) {
            case 1:
                VisualisarTarefa();
                break;
            case 2:
                IncluiTarefa();
                break;
            case 3:
                AtualizarTarefa();
                break;
            case 4:
                ExcluirTarefa();
                break;
            case 5:
                ExitAplication();
                break;

        }
    }

    private String GetStatus(String Sts){

        String result = "";

        switch (Sts) {

            case "0":
                result = "Pendente";
                break;
            case "1":
                result = "Concluido";
                break;
            case "2":
                result = "Atrasado";
                break;
        }

        return result;
    }

    private void ClearConsole(){

        for (int i = 0; i < 500; i++) {
            WriteNewLineConsole("");
        }
    }

    private Integer GetLastIndex(){
        try{
            String checkLine;

            File file = new File("FileDB.txt");

            String lastIndex = "0";

            Integer result = 0;



            if (file.exists()){
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                while((checkLine = br.readLine()) != null){

                    String[] splitLine = checkLine.split(";", 4);

                    lastIndex = splitLine[0];
                }
                br.close();
            }

            try{
                result = Integer.parseInt(lastIndex);
            }catch (Exception e){
                result = 0;
            }

            return result;

        }catch(IOException e){
            e.printStackTrace();
            return 0;
        }


    }




}
