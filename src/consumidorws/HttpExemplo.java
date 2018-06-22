/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumidorws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import modelo.Usuario;

/**
 *
 * @author marcelosiedler
 */
public class HttpExemplo {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {
        //Exemplo GET
        HttpExemplo http = new HttpExemplo();
        String chamadaWS = "http://localhost:8080/FazendaWerbService/webresources/fazenda/usuario/l.assis@eddydata";
        String json = http.sendRequest(chamadaWS, "GET");
        System.out.println(json);

        Gson g = new Gson();
        Usuario usuario = new Usuario();
        Type usuarioType = new TypeToken<Usuario>() {
        }.getType();

        usuario = g.fromJson(json, usuarioType);

        http.exibeInformacoes(usuario);

        //Exemplo POST 
        usuario.setLogin("crud.webservice@eddydata");
        usuario.setEmail("crud.webservice@eddydata");
        usuario.setSenha("596");
        usuario.setPerfil("suporte");

        json = g.toJson(usuario, usuarioType);

        chamadaWS = "http://localhost:8080/FazendaWerbService/webresources/fazenda/usuario/inserir";
        
        http.sendRequest(chamadaWS, json, "POST");

        //Eexemplo PUT
        usuario.setLogin("crud.webservice@eddydata");
        usuario.setEmail("crud.webservice.alteracao@eddydata");
        usuario.setSenha("596695");
        usuario.setPerfil("suporte");
        
        json = g.toJson(usuario, usuarioType);

        chamadaWS = "http://localhost:8080/FazendaWerbService/webresources/fazenda/usuario/alterar";
        
        http.sendRequest(chamadaWS, json, "PUT");
        
        //Exemplo DELETE
        chamadaWS = "http://localhost:8080/FazendaWerbService/webresources/fazenda/usuario/excluir/exemplo.email@eddydata";
        http.sendRequest(chamadaWS, "DELETE");
    }

    private String sendRequest(String url, String method) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod(method);

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending '" + method + "' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private void sendRequest(String url, String urlParameters, String method) throws Exception {
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending '" + method + "' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    private void exibeInformacoes(Usuario usuario) {
        System.out.println("\nDados do usuário convertido: \n");
        System.out.println("--------------------------------------");
        System.out.println("e-mail: " + usuario.getEmail());
        System.out.println("perfil: " + usuario.getPerfil());
        System.out.println("login: " + usuario.getLogin());
        System.out.println("senha: " + usuario.getSenha());
    }
}
