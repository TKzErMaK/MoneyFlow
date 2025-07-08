package com.gb.trabalho.API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

public class IndicadoresAPI {

    public interface ResultadoListener<T> {
        void onResultado(T resultado);
        void onErro(String mensagem);
    }

    public void buscarIPCA(ResultadoListener<Double> listener) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.bcb.gov.br/dados/serie/bcdata.sgs.433/dados?formato=json");
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = in.readLine()) != null) {
                    resposta.append(linha);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(resposta.toString());
                int total = jsonArray.length();
                int inicio = Math.max(0, total - 12);

                double soma = 0.0;
                for (int i = inicio; i < total; i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String valorStr = item.getString("valor").replace(",", ".");
                    double valor = Double.parseDouble(valorStr);
                    soma += valor;
                }
                soma = new BigDecimal(soma).setScale(2, RoundingMode.HALF_UP).doubleValue();
                double resultado = soma;

                listener.onResultado(resultado);

            } catch (Exception e) {
                listener.onErro("Erro ao buscar IPCA: " + e.getMessage());
            }
        }).start();
    }

    public void buscarCDI(ResultadoListener<Double> listener) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.bcb.gov.br/dados/serie/bcdata.sgs.4391/dados?formato=json");
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = in.readLine()) != null) {
                    resposta.append(linha);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(resposta.toString());
                int total = jsonArray.length();
                int inicio = Math.max(0, total - 12);

                double soma = 0.0;
                for (int i = inicio; i < total; i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String valorStr = item.getString("valor").replace(",", ".");
                    double valor = Double.parseDouble(valorStr);
                    soma += valor;
                }
                soma = new BigDecimal(soma).setScale(2, RoundingMode.HALF_UP).doubleValue();
                listener.onResultado(soma);

            } catch (Exception e) {
                listener.onErro("Erro ao buscar CDI: " + e.getMessage());
            }
        }).start();
    }

    public void buscarSELIC(ResultadoListener<Double> listener) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.bcb.gov.br/dados/serie/bcdata.sgs.4390/dados?formato=json");
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = in.readLine()) != null) {
                    resposta.append(linha);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(resposta.toString());
                int total = jsonArray.length();
                int fim = total - 1;
                int inicio = Math.max(0, fim - 12);

                double soma = 0.0;
                for (int i = inicio; i < fim; i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String valorStr = item.getString("valor").replace(",", ".");
                    double valor = Double.parseDouble(valorStr);
                    soma += valor;
                }
                soma = new BigDecimal(soma).setScale(2, RoundingMode.HALF_UP).doubleValue();
                listener.onResultado(soma);

            } catch (Exception e) {
                listener.onErro("Erro ao buscar SELIC: " + e.getMessage());
            }
        }).start();
    }

}
