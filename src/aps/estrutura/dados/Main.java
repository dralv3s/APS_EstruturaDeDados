package aps.estrutura.dados;

import aps.estrutura.dados.Model.Tarefa;
import aps.estrutura.dados.Service.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        try {

            Util util = new Util();
            util.ShowMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
