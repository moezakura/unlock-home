package si.mox.unlock.services

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import si.mox.unlock.api.Client
import si.mox.unlock.storage.SecretStorage

@RequiresApi(api = Build.VERSION_CODES.N)
class UnlockTileService : TileService() {

    // Quick SettingsにTileが追加されるときに呼び出されます
    override fun onTileAdded() {
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    // Quick SettingsにTileが削除されるときに呼び出されます
    override fun onTileRemoved() {
    }

    // Tileの変化がある場合に呼び出されます。 onClick()の前に呼び出されます
    override fun onStartListening() {
    }

    // Tileの変化がある場合に呼び出されます。 onClick()の後に呼び出されます
    override fun onStopListening() {
    }

    // Tileをクリックしたときに呼び出されます
    override fun onClick() {
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()

        val secretStorage = SecretStorage()
        val secret = secretStorage.load(this)
        if (secret == null) {
            return
        }

        val client = Client(secret.apiKey)
        GlobalScope.launch {
            client.unlock()

            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
        }
    }

    companion object {
        val TAG = UnlockTileService::class.java.simpleName
    }
}
