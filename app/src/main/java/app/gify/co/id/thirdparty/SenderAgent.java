package app.gify.co.id.thirdparty;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Spanned;
import android.widget.Toast;

import java.util.Properties;

/*import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/

public class SenderAgent /*extends AsyncTask<Void, Void, Void>*/ {

    /*private String mail;
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
        progressDialog = ProgressDialog.show(context, "Menunggu transaksi", "", false);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gify.firebase@gmail.com", "Gifyapp01");
            }
        });

        try{
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress("gify.firebase@gmail.com"));
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
        Toast.makeText(context, "Berhasil membuat Transaksi", Toast.LENGTH_LONG).show();
    }*/
}
