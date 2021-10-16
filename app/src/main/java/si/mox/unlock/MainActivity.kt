package si.mox.unlock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*
import si.mox.unlock.models.Secret
import si.mox.unlock.storage.SecretStorage
import java.net.URLEncoder
import si.mox.unlock.services.UrlService

class MainActivity : AppCompatActivity() {
    private lateinit var secretStorage: SecretStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this

        this.secretStorage = SecretStorage()
        val secret = secretStorage.load(context)

        if (secret != null) {
            apiKeyTextArea.setText(secret.apiKey)
            updateAccessURL(secret.apiKey)
        } else {
            updateAccessURL(null)
        }

        apiKeyTextArea.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }


            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val text: String = apiKeyTextArea.text.toString()
                updateAccessURL(text)
                secretStorage.save(context, Secret(text))
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun updateAccessURL(text: String?) {
        if (text == null) {
            accessURL.setText("<<invalid api key>>")
            return
        }
        val token = URLEncoder.encode(text, "UTF-8")
        accessURL.setText(UrlService.accessURL(token))
    }
}