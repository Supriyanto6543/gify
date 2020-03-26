package app.gify.co.id.thirdparty;

import javax.mail.Authenticator;
import javax.mail.Message;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Spanned;
import android.widget.Toast;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import app.gify.co.id.activity.CheckoutActivity;
import app.gify.co.id.activity.MainActivity;

public class SenderAgent extends AsyncTask<Void, Void, Void> {

    private String mail;
    private String subject;
    private Spanned message;

    private Context context;
    private Session session;

    private ProgressDialog progressDialog;

    public SenderAgent(String mail, String subject, Spanned message, Context context) {
        this.mail = mail;
        this.subject = subject;
        this.message = message;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please wait. . .", "", false);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "kitasinau.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin@kitasinau.com", "kitasinau123");
            }
        });

        try{
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress("admin@kitasinau.com"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("supriyanto150@gmail.com"));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(String.valueOf(message));
            Transport.send(mimeMessage);
        }catch (MessagingException m){
            m.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        context.startActivity(new Intent(context, MainActivity.class));
        //new CheckoutActivity().sendCart(context);
        new CheckoutActivity().pushNotify(context);
    }
}
