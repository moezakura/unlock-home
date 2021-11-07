package si.mox.unlock.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SimpleDialogFragment: DialogFragment() {
    private var title: String = ""
    private var message: String = ""

    fun setDialogInfo(title: String, message: String){
        this.title = title
        this.message = message
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(this.title)
            .setMessage(this.message)
            .setPositiveButton("done") { dialog, id ->
                println("dialog:$dialog which:$id")
            }
            .setNegativeButton("cancel") { dialog, id ->
                println("dialog:$dialog which:$id")
            }

        return builder.create()
    }

}

