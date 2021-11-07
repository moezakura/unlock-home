package si.mox.unlock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.quicksettings.Tile
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import si.mox.unlock.api.Client
import si.mox.unlock.components.SimpleDialogFragment
import si.mox.unlock.models.Secret
import si.mox.unlock.storage.SecretStorage
import java.net.URLEncoder
import si.mox.unlock.services.UrlService

class MainActivity : AppCompatActivity() {
    private lateinit var secretStorage: SecretStorage

    @DelicateCoroutinesApi
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

        this.updateApiKey(context)
        allUnlockButton.setOnClickListener {
            this.unlock(context, true)
        }
        entranceUnlockButton.setOnClickListener {
            this.unlock(context, false)
        }
    }

    fun updateApiKey(context: MainActivity) {
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

    @DelicateCoroutinesApi
    fun unlock(context: MainActivity, isDoor: Boolean) {
        val secret = secretStorage.load(context) ?: return

        val progressBar = ProgressBar(this)
        val linearLayout = findViewById<LinearLayout>(R.id.container)
        linearLayout.addView(progressBar)

        val client = Client(secret.apiKey)
        GlobalScope.launch {
            val res = client.unlock(isDoor)

            val dialog = SimpleDialogFragment()
            if (res.isFailure) {
                dialog.setDialogInfo("error", res.getOrNull() ?: "<NULL>")
            } else {
                dialog.setDialogInfo("success", res.getOrNull() ?: "<NULL>")
            }

            dialog.show(supportFragmentManager, "simpleDialog")
        }
        linearLayout.removeView(progressBar)
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